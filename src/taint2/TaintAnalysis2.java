package taint2;

/*
 *  @author Srishti Sengupta, 2013108.
 *  @author Naman Gupta, 2013064.
 */
import java.util.HashMap;
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

public class TaintAnalysis2 extends ForwardFlowAnalysis {

	public FlowSet inval;
	public FlowSet outval;

	public FlowSet returnVar;

	HashMap<Integer, Boolean> taintedness = new HashMap<>();

	public TaintAnalysis2(UnitGraph g) {
		super(g);
		returnVar = new ArraySparseSet();

		initTainted();

		doAnalysis();
	}

	private void initTainted() {
		for (int i = 1; i < 100; i++) {
			taintedness.put(i, false);
		}
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

		inval.copy(outval);

		LineNumberTag tag = (LineNumberTag) u.getTag("LineNumberTag");
		int lineNumber = tag.getLineNumber();

		List<ValueBox> defBoxes = u.getDefBoxes();
		List<ValueBox> useBoxes = u.getUseBoxes();

		// FOR EACH UNIT
		// function name retrieve
		String functionName = "";
		boolean islibraryMethod = false;
		boolean isMethod = false;

		if (u.toString().contains("invoke")) {
			isMethod = true;

			for (ValueBox valueBox : useBoxes) {
				if (valueBox.toString().contains("Linked")) {
					Value value = valueBox.getValue();

					String[] s = value.toString().split(">")[0].split(" ");
					functionName = s[s.length - 1].split("\\(")[0];

					System.out.println("\nFunction: " + functionName);
					if (!TaintWrapper2.map.containsKey(functionName)) {
						islibraryMethod = true;
					}

				}
			}
		}

		boolean taintedArguments = false;
		if (useBoxes.size() != 0) {

			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				// System.out.println(valueBox);

				// not an argument to the function
				if (!isMethod) {
					if (inval.contains(value.toString())) { // rhs me koi bhi
						// variable is tainted
						taintedness.put(lineNumber, true);
					}
				} else { // arg variable
					if (!islibraryMethod) {
						if (inval.contains(value.toString())) { // rhs me koi
																// bhi
							// variable is tainted
							taintedArguments = true;
						}
					}
				}
				if (isMethod && !islibraryMethod
						&& value.toString().contains("invoke")
						&& taintedArguments) { // our
					// method

					if (TaintWrapper2.map.containsKey(functionName)) {
						FlowSet tmp = TaintWrapper2.map.get(functionName);
						if (tmp.size() != 0) { // summary contains a tainted
												// return variable
							taintedness.put(lineNumber, true);
							System.out.println("Summary of " + functionName
									+ " at " + lineNumber
									+ " returns a tainted value.");
						} else {
							System.out.println("Summary of " + functionName
									+ " at " + lineNumber
									+ " returns an untainted value.");
						}
					}
				}
			} // end for

		}
		if (!taintedArguments && functionName != "") {
			System.out.println("Summary of " + functionName + " at "
					+ lineNumber + " returns an untainted value.");
		}

		/*
		 * Kill operation
		 * 
		 * when the variable is assigned a constant or untainted expression
		 * containing untainted variables.
		 */
		if (u instanceof AssignStmt) {
			Value lhs_variable = defBoxes.get(0).getValue();

			if (!taintedness.get(lineNumber)) {
				System.out.println("Kill: " + lhs_variable);
				outval.remove(lhs_variable.toString());
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
				Value lhs_variable = defBoxes.get(0).getValue();

				System.out.println("Gen: " + lhs_variable);
				if (!outval.contains(lhs_variable.toString())) {
					outval.add(lhs_variable.toString());
				}
			}
		}

		// assignment statement
		if (u instanceof AssignStmt) {
			Value lhs_variable = defBoxes.get(0).getValue();

			if (taintedness.get(lineNumber)) { // is tainted
				System.out.println("Gen: " + lhs_variable);

				if (!outval.contains(lhs_variable.toString())) {
					outval.add(lhs_variable.toString());
				}
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

			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (outval.contains(value.toString())) {
					returnVar.add(value.toString());
				}
			}
		}

	}
}
