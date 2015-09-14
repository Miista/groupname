
public class Matcher {

	final static int delta = 1;
	final int[][] costs = new int[0][0];
	
	
	static int[][] compare(String a, String b) {
		int[][] vals = new int[a.length()][b.length()];

		for (int m = 0; m < vals.length; m++) {
			vals[m][0] = m*delta;
		}
		for (int n = 0; n < vals[0].length; n++) {
			vals[0][n] = n*delta;
		}
		
		for (int i = 0; i < vals.length; i++) {
			for (int j = 0; j < vals[i].length; j++) {				
				vals[i][j] = optimal(i,j);
			}
		}

		return vals;
	}

	private static int optimal(int i, int j) {
		
		return Math.min( Math.min( alfa(i,j) + optimal(i-1, j-1), delta +  optimal(i-1,j ) ), delta + optimal(i, j-1) );
	}

	private static int alfa(int i, int j) {
		//lookup
		return 42;
	}
	
}
