package pa;

public class Test11 {

	int fact(int n) {
		int res = 1;
		if (n == 1) {
			return res;
		} else {
			while (n > 1) {
				res = res * n;
				n = n - 1;
			}
			return res;
		}
	}
}
