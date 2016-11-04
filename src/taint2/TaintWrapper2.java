package taint2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import taint1.TaintAnalysis;

public class TaintWrapper2 extends SceneTransformer {

	// hashmap to store the results

	private static TaintWrapper2 wrap = new TaintWrapper2();

	@Override
	protected void internalTransform(String arg0, Map arg1) {
		String className = "pa.T6";

		SootClass c = Scene.v().loadClassAndSupport(className);
		c.setApplicationClass();

		CallGraph cg = Scene.v().getCallGraph();

		SootMethodFilter filter = new SootMethodFilter() {

			@Override
			public boolean want(SootMethod method) {
				if (method.isJavaLibraryMethod()) {
					return false;
				}
				return true;
			}
		};

		Iterator<MethodOrMethodContext> heads = cg.sourceMethods();

		DirectedCallGraph dcg = new DirectedCallGraph(cg, filter, heads, false);

		FlowSet tmp = new ArraySparseSet();

		map.put("empty", tmp);
		for (Object object : dcg) {
			SootMethod method = (SootMethod) object;
			map.put(method.getName(), tmp);
		}

		SootMethod tail = (SootMethod) dcg.getTails().get(0);
		SootMethod head = (SootMethod) dcg.getHeads().get(0);

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

		BriefUnitGraph g = new BriefUnitGraph(method.getActiveBody());
		TaintAnalysis reach = new TaintAnalysis(g);

		map.put(name, reach.returnVar);
		System.out.println("Summary of " + name + ":" + reach.returnVar);

		System.out.println();

	}

	public static Transformer v() {
		return wrap;
	}

}
