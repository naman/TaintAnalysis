package pa;

/**
 * @author naman
 * 
 */
public class Test4 {
	int fun(int x, int y) {
		int a = x;
		if (a == 10) {
			y = 10;
		} else {
			y = 20;
		}
		a = y;
		return a;
	}
}