package pa;

public class T5 {
	int test5(int x) {
		int a = 10;
		if (x > 10) {
			x = x + a;
			a = x - a;
			while (a != 10) {
				if (a % 2 == 0) {
					return a;
				} else {
					a = a + 1;
					x = x + 1;
				}
			}
			x = 30;
		} else {
			x = x + a;
			a = x - a;
			x = x - a;
			x = 10;
		}
		return x;
	}
}