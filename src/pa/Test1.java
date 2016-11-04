/**
 * 
 */
package pa;

/**
 * @author naman
 * 
 */
public class Test1 {

	int compute(int x, int y) {
		int res = x * 2;
		int a = 0;
		while (y != 0) {
			if (x >= 0) {
				a = 2 * res + (y % 2);
				y = y / 2;
				x--;
			} else {
				res = a * 2;
			}
		}
		res = 9;
		if (x > 0) {
			return a;
		}

		return res;
	}
}