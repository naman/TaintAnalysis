package pa;

public class T6 {
	static int f1(int play, int t) {
		int y = play;
		play = 1;
		return play;
	}

	public static void main(String[] args) {
		int play = Integer.parseInt(args[0]);

		int z = play;
		int t = 1;
		int a = f1(play, t);

		System.out.println(a + z + t);
	}

	// int func (int x)
	// {
	// int temp = 100;
	// return (x + temp);
	// }
	//
	// int swappingAgain(int x)
	// {
	// if(x > 0)
	// return 2;
	// int y = func (x);
	// return 10;
	// }
	//
	// int foo (int play)
	// {
	// int beta = 0;
	// int alpha = beta + func(1);
	// beta = swappingAgain(alpha);
	// int gamma = 0;
	// while(beta > 3)
	// {
	// gamma = func(play);
	// beta = beta - swappingAgain(gamma);
	// }
	// return (beta+gamma);
	// }
	//
	// public static void main (String[] args)
	// {
	// int play = Integer.parseInt(args[0]);
	// T6 s = new T6();
	// int x = 0;
	// int y;
	// if(play > 0)
	// x = s.foo(play);
	// else
	// {
	// y = s.swappingAgain(play);
	// System.out.println(y);
	// }
	// System.out.println(x);
	// }
	// int func (int x)
	// {
	// int temp = x;
	// temp = 100;
	// return temp;
	// }
	//
	// int foo (int play)
	// {
	// int beta = play;
	// int alpha = beta + func(1);
	// int gamma = 3;
	// if(alpha == 2)
	// {
	// return gamma;
	// }
	// else
	// {
	// gamma = beta + func(beta);
	// }
	// return gamma;
	// }
	//
	// public static void main (String[] args)
	// {
	// int play = Integer.parseInt(args[0]);
	// T6 s = new T6();
	// int x = 0;
	// int y = 0;
	// if(play > 0)
	// x = s.foo(play);
	// else
	// {
	// y = s.foo(4);
	// System.out.println(y);
	// }
	// System.out.println(x);
	// }
}

// public class T6 {
//
// public static int f1(int x) {
// int a = x;
// int b = f2(x);
// int c = 1;
// int d = f2(c);
//
// System.out.println(b + c + d + a + x);
// return a;
// }
//
// public static void main(String[] args) {
// int p = Integer.parseInt(args[0]);
// int t = f1(p);
// }
//
// private static int f2(int e) {
// return e;
// }
//
/*
 * public static int func (int x, int y) { int temp = 100; return (x + temp +
 * y); }
 * 
 * public static int swappingAgain (int x, int y, int q) { return (x + y + q); }
 * 
 * int foo (int play) { return play*play; }
 * 
 * public static void main (String[] args) { int play =
 * Integer.parseInt(args[0]); T6 s = new T6(); int x; int beta = 0; if(play > 0)
 * x = s.foo(play); else { x = s.foo(4); System.out.println(x); }
 * System.out.println(x);
 * 
 * int alpha = beta + func(1,2); beta = swappingAgain(beta,alpha,beta); int
 * gamma = 0; while(beta > 3) { System.out.println(gamma); gamma =
 * swappingAgain(1,2,play); beta = beta - func(beta,gamma); }
 * System.out.println(beta); }
 */

