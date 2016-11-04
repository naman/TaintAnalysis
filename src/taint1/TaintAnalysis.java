package taint1;

import java.util.List;

import soot.SootMethod;
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
import taint2.TaintWrapper2;

public class TaintAnalysis extends ForwardFlowAnalysis {

	public FlowSet inval;
	public FlowSet outval;

	public FlowSet returnVar;

	public TaintAnalysis(UnitGraph g) {
		super(g);
		returnVar = new ArraySparseSet();
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

		inval.copy(outval);

		LineNumberTag tag = (LineNumberTag) u.getTag("LineNumberTag");
		int lineNumber = tag.getLineNumber();

		List<ValueBox> defBoxes = u.getDefBoxes();
		List<ValueBox> useBoxes = u.getUseBoxes();

		// function name retrieve
		String functionName = "";
		boolean islibraryMethod = false;
		boolean isMethod = false;

		if (u.toString().contains("invoke")) {
			isMethod = true;

			for (ValueBox valueBox : useBoxes) {

				Value value = valueBox.getValue();

				if (valueBox.toString().contains("Linked")) {

					String[] s = value.toString().split(">")[0].split(" ");
					functionName = s[s.length - 1].split("\\(")[0];

					System.out.println("Function: " + functionName);
					if (!TaintWrapper2.map.containsKey(functionName)) {
						islibraryMethod = true;
					}

				}
			}
		}

		/*
		 * Kill operation
		 * 
		 * when the variable is assigned a constant or untainted expression
		 * containing untainted variables.
		 */
		if (u instanceof AssignStmt) {

			Value lhs_variable = defBoxes.get(0).getValue();

			boolean isTainted = false;
			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (inval.contains(value.toString())) { // rhs me koi bhi
														// variable is tainted
					isTainted = true;
				}

				if (isMethod && !islibraryMethod
						&& value.toString().contains("invoke")) { // our
																	// method
					System.out.println("hi");
					FlowSet tmp = TaintWrapper2.map.get(functionName);
					if (tmp.size() != 0) { // summary contains a tainted
											// return variable
						isTainted = true;
						System.out.println("Summary" + " at line " + lineNumber
								+ " returns a tainted value.");
					} else {
						System.out.println("Summary" + " at line " + lineNumber
								+ " returns an untainted value.");
					}
				}
			}

			if (!isTainted) {
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
				Value param = defBoxes.get(0).getValue();
				if (!outval.contains(param.toString())) {
					outval.add(param.toString());
				}
			}
		}

		// assignment statement
		if (u instanceof AssignStmt) {
			Value lhs_variable = defBoxes.get(0).getValue();
			boolean isTainted = false;

			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (inval.contains(value.toString())) { // rhs me koi bhi
														// variable is tainted
					isTainted = true;
				}

				if (isMethod && !islibraryMethod
						&& value.toString().contains("invoke")) { // our
																	// method
					System.out.println("hi");
					FlowSet tmp = TaintWrapper2.map.get(functionName);
					if (tmp.size() != 0) { // summary contains a tainted
											// return variable
						isTainted = true;
						System.out.println("Summary" + " at line " + lineNumber
								+ " returns a tainted value.");
					} else {
						System.out.println("Summary" + " at line " + lineNumber
								+ " returns an untainted value.");
					}
				}
			}

			if (isTainted) { // is tainted
				System.out.println(lhs_variable);
				if (!outval.contains(lhs_variable.toString())) {
					outval.add(lhs_variable.toString());
				}
			}
		}

		if (u instanceof ReturnStmt || u.toString().contains("println")) {

			System.out.print("[" + lineNumber + "] ");

			System.out.print("Sink present at line : " + lineNumber);

			if (outval.contains("Immediate")) {
				System.out.println(" tainted values printed");
			} else {
				System.out.println(" untainted values printed");
			}

			System.out.println("Variables which are tainted: " + out);
		}

		System.out.print("[" + lineNumber + "] ");
		System.out.println("Variables which are tainted: " + out);

		if (u instanceof ReturnStmt) {
			for (ValueBox valueBox : useBoxes) {
				Value value = valueBox.getValue();

				if (!value.toString().contains("$")
						&& outval.contains(value.toString())) {
					returnVar.add(value.toString());
				}
			}
		}

	}
}
