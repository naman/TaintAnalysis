package pa;

public class T1 {

	int test1(int x, int y) {
		int a = x;
		if (a == 10) {
			y = 10;
			System.out.println(y);
		} else {
			y = 20;
			System.out.println(y);
		}
		a = y;
		return a;
	}
}