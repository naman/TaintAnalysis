package taint1;

/*
 *  @author Srishti Sengupta, 2013108.
 *  @author Naman Gupta, 2013064.
 */
import soot.Pack;
import soot.PackManager;
import soot.SootClass;
import soot.SootResolver;
import soot.Transform;
import soot.options.Options;

public class Driver1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length == 0) {
			System.err.println("Usage: java Driver1 [options] classname");
			System.exit(0);
		}

		// SootClass
		// abc=Scene.v().loadClassAndSupport("ReachingDefinition.Tester");
		Options.v().setPhaseOption("jb", "use-original-names:true");
		Options.v().set_keep_line_number(true);

		Pack jtp = PackManager.v().getPack("jtp");
		jtp.add(new Transform("jtp.instrumenter", new TaintWrapper1()));
		SootResolver.v().resolveClass("java.lang.CloneNotSupportedException",
				SootClass.SIGNATURES);
		Options.v().set_output_format(Options.output_format_jimple);
		soot.Main.main(args);
	}
}
