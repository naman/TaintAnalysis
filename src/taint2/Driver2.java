package taint2;

import soot.Pack;
import soot.PackManager;
import soot.Transform;
import soot.options.Options;

public class Driver2 {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println("Usage: java Driver [options] classname");
			System.exit(0);
		}

		Pack wjtp = PackManager.v().getPack("wjtp");
		wjtp.add(new Transform("wjtp.profiler", TaintWrapper2.v()));
		Options.v().setPhaseOption("jb", "use-original-names:true");
		Options.v().set_keep_line_number(true);
		Options.v().set_whole_program(true);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_no_bodies_for_excluded(true);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_main_class(args[0]);
		soot.Main.main(args);

	}
}
