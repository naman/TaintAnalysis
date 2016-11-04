/**
 * 
 */
package pa;

/**
 * @author naman
 * 
 */
public class Test12 {
	static String field1;
	static int field2;
	static boolean field3;

	public static void main(String[] args) {
		bun2();
		int x = 10;
		int sum = 0;
		for (int i = 0; i < x; i++) {
			sum += 1;
		}
	}

	public static void bun2() {
		int xyz = 42;
		bar(field3);
		foo(3);
	}

	private static int bar(boolean field32) throws NullPointerException {
		new Test12().bun();
		int b = 13;
		int a = 2 * b;
		return a;
	}

	private static String foo(int f) {
		if (f > 2) {
			int x = 1;
		}
		String tummy = "bhook";
		return tummy;
	}

	void bun() {
		int xyz = 42;
		bar(field3);
		foo(xyz);
	}

}
