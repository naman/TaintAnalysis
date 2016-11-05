package pa;

public class Test8 {

	public static void happy(int n) {
		int status = 1;
		int num = 3;
		n = 10;
		if (n >= 1) {
			System.out.println("First " + n + " happy numbers are :-");
			System.out.println(2);
		}
		int count = 2;
		while (count <= n) {
			int j = 2;
			while (j <= Math.sqrt(num)) {
				if (num % j == 0) {
					status = 0;
					break;
				}
				j++;
			}
			if (status != 0) {
				System.out.println(num);
				count++;
			}
			status = 1;
			num++;
		}
	}
}
