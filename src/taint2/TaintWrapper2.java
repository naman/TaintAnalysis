package taint2;

/*
 *  @author Srishti Sengupta, 2013108.
 *  @author Naman Gupta, 2013064.
 */
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soot.MethodOrMethodContext;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Transformer;
import soot.jimple.toolkits.annotation.purity.DirectedCallGraph;
import soot.jimple.toolkits.annotation.purity.SootMethodFilter;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class TaintWrapper2 extends SceneTransformer {

	// hashmap to store the results

	private static TaintWrapper2 wrap = new TaintWrapper2();

	@Override
	protected void internalTransform(String arg0, Map arg1) {
		String className = "pa.Fact";

		SootClass c = Scene.v().loadClassAndSupport(className);
		c.setApplicationClass();

		CallGraph cg = Scene.v().getCallGraph();

		SootMethodFilter filter = new SootMethodFilter() {

			@Override
			public boolean want(SootMethod method) {
				if (method.isJavaLibraryMethod()
					|| method.getName().equals("<init>") 
				    	|| method.getName().equals("<clinit>") {
					return false;
				}
				return true;
			}
		};

		Iterator<MethodOrMethodContext> heads = cg.sourceMethods();

		DirectedCallGraph dcg = new DirectedCallGraph(cg, filter, heads, true);

		FlowSet tmp = new ArraySparseSet();

		for (Object object : dcg) {
			SootMethod method = (SootMethod) object;
			map.put(method.getName(), tmp);
		}

		SootMethod tail = (SootMethod) dcg.getTails().get(0);
		SootMethod head = (SootMethod) dcg.getHeads().get(0);

		System.out.println("head " + head.getName());
		System.out.println("tail " + tail.getName());

		work(tail);

		while (true) {
			SootMethod method = (SootMethod) dcg.getPredsOf(tail).get(0);
			if (method != null) {

				// work here
				work(method);

				if (method.equals(head))
					break;

				tail = method;
			} else
				break;
		}

		System.out.println("map:" + map);

	}

	public static HashMap<String, FlowSet> map = new HashMap<>();

	private void work(SootMethod method) {
		String name = method.getName();

		System.out.println("\nAnalysing " + name);
		BriefUnitGraph g = new BriefUnitGraph(method.getActiveBody());
		TaintAnalysis2 reach = new TaintAnalysis2(g);

		map.put(name, reach.returnVar);
		System.out.println("Summary of " + name + ":" + reach.returnVar);

		System.out.println();

	}

	public static Transformer v() {
		return wrap;
	}

}
