package pa;

public class T3 {
	int test3(int x) {
		int a = 0;
		while (x > 10) {
			a = a * x;
			x = x - 1;
		}
		return a;
	}
}