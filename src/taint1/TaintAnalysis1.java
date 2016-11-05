package taint1;

/*
 *  @author Srishti Sengupta, 2013108.
 *  @author Naman Gupta, 2013064.
 */
import java.util.List;

import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.tagkit.LineNumberTag;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

public class TaintAnalysis1 extends ForwardFlowAnalysis {

	FlowSet inval, outval;

	public TaintAnalysis1(UnitGraph g) {
		super(g);
		doAnalysis();
	}

	@Override
	protected void copy(Object source, Object dest) {
		FlowSet srcSet = (FlowSet) source;
		FlowSet destSet = (FlowSet) dest;
		srcSet.copy(destSet);
	}

	@Override
	protected Object entryInitialFlow() {
		return new ArraySparseSet();
	}

	@Override
	protected void merge(Object in1, Object in2, Object out) {
		FlowSet inval1 = (FlowSet) in1;
		FlowSet inval2 = (FlowSet) in2;
		FlowSet outSet = (FlowSet) out;

		// May analysis
		inval1.union(inval2, outSet);
	}

	@Override
	protected Object newInitialFlow() {
		return new ArraySparseSet();
	}

	@Override
	protected void flowThrough(Object in, Object unit, Object out) {
		inval = (FlowSet) in;
		outval = (FlowSet) out;
		Stmt u = (Stmt) unit;

		LineNumberTag tag = (LineNumberTag) u.getTag("LineNumberTag");
		int lineNumber = tag.getLineNumber();

		inval.copy(outval);

		List<ValueBox> defBoxes = u.getDefBoxes();
		List<ValueBox> useBoxes = u.getUseBoxes();

		/*
		 * Kill operation
		 * 
		 * when the variable is assigned a constant or untainted expression
		 * containing untainted variables.
		 */
		if (u instanceof AssignStmt) {
			Value variable = defBoxes.get(0).getValue();

			boolean isUntainted = true;
			for (ValueBox valueBox : useBoxes) {
				if (inval.contains(valueBox.getValue().toString())) {
					isUntainted = false;
					break;
				}
			}

			if (isUntainted) {
				outval.remove(variable.toString());
			}
		}

		/*
		 * Gen operation
		 * 
		 * function parameters when the variable is assigned = expression
		 * containing a tainted variable.
		 */
		if (u instanceof IdentityStmt) {
			if (u.toString().contains("parameter")) { // or if
														// (!param.toString().equals("this"))
				Value param = defBoxes.get(0).getValue();
				if (!outval.contains(param.toString())) {
					outval.add(param.toString());
				}
			}
		}

		// assignment statement
		if (u instanceof AssignStmt) {
			Value variable = defBoxes.get(0).getValue();

			boolean isUntainted = true;
			for (ValueBox valueBox : useBoxes) {
				if (inval.contains(valueBox.getValue().toString())) {
					isUntainted = false;
					break;
				}
			}

			if (!isUntainted) { // is tainted
				if (!outval.contains(variable.toString())) {
					outval.add(variable.toString());
				}
			}
		}

		// function call
		if (u.toString().contains("invoke")
				&& !u.toString().contains("println")) {

			// System.out.println(useBoxes);
			boolean isTainted = false;
			for (ValueBox vb : useBoxes) {
				Value value = vb.getValue();

				if (vb.toString().contains("Immediate")) { // arg

					if (outval.contains(value.toString())) {
						isTainted = true;
						try {
							Value lhs_variable = defBoxes.get(0).getValue();

							if (!outval.contains(lhs_variable)) {
								outval.add(lhs_variable.toString());
							}
						} catch (Exception e) {
						}
					}

				} else if (vb.toString().contains("LinkedRValueBox")
						&& vb.toString().contains("invoke")) { // function

					System.out.println("Function:" + value);
				}
			}

			if (isTainted) {
				System.out.println("Summary" + " at line " + lineNumber
						+ " returns a tainted value.");
			} else {
				System.out.println("Summary" + " at line " + lineNumber
						+ " returns an untainted value.");

			}
		}

		if (u.toString().contains("println")) {

			System.out.print("Sink present at " + lineNumber);

			boolean isTainted = false;
			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (outval.contains(value.toString())) {
					isTainted = true;
				}
			}

			if (isTainted) {
				System.out.println(" tainted values printed");
			} else {
				System.out.println(" untainted values printed");
			}

			System.out.println("Variables which are tainted: " + outval);
		}

		if (u instanceof ReturnStmt) {

			System.out.print("Sink present at " + lineNumber);

			boolean isTainted = false;
			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (outval.contains(value.toString())) {
					isTainted = true;
				}

			}

			if (isTainted) {
				System.out.println(" tainted values returned");
			} else {
				System.out.println(" untainted values returned");
			}

			System.out.println("Variables which are tainted: " + outval);

		}
	}
}
