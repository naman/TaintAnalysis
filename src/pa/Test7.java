package pa;

/**
 * @author naman
 * 
 */
public class Test7 {
	int bar(int x) {
		int a = 0;
		while (x > 10) {
			a = a * x;
			x = x - 1;
		}
		return a;
	}
}