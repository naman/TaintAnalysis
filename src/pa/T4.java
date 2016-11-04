package pa;

public class T4 {
	int test4(int x, int y) {
		int a = 0;
		int res = 10;
		if (res > 10) {
			a = x + y;
		} else {
			a = 20;
		}
		if (a == 20) {
			res = 0;
			return a;
		} else {
			res = a;
		}
		return res;
	}
}
