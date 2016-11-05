package taint1;

/*
 *  @author Srishti Sengupta, 2013108.
 *  @author Naman Gupta, 2013064.
 */
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.SootMethod;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class TaintWrapper1 extends BodyTransformer {

	@Override
	protected void internalTransform(Body body, String phase, Map options) {
		SootMethod sootMethod = body.getMethod();
		UnitGraph g = new BriefUnitGraph(sootMethod.getActiveBody());
		TaintAnalysis1 reach = new TaintAnalysis1(g);
	}

}
