import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.FileNotFoundException;


public class Matcher {

	final static int delta = 2;
	static Map<String, Integer> costs = InputParser.readCosts("gorilla_data/BLOSUM62.txt");
	static String a, b;
	static int[][] vals;

	
	static int[][] compare(String a, String b) {
		Matcher.a = a;
		Matcher.b = b;
		vals = new int[a.length()][b.length()];
		for (int m = 0; m < vals.length; m++) {
			vals[m][0] = m*delta;
		}
		for (int n = 0; n < vals[0].length; n++) {
			vals[0][n] = n*delta;
		}
		
		for (int i = 1; i < a.length(); i++) {
			for (int j = 1; j < b.length(); j++) {
				vals[i][j] = optimal(i,j);
			}
		}

		return vals;
	}

	private static int optimal(int i, int j) {
		if (i==0) {
			return j*delta;
		}
		if (j==0) {
			return i*delta;
		}

		final int C = delta + optimal( i, j - 1 );
		final int B = delta + optimal( i - 1, j );
		final int A = alfa( i, j ) + optimal( i - 1, j - 1 );
		return Math.min( Math.min( A, B ), C );
	}

	private static int alfa(int i, int j) {
		System.out.println(" alpha : "+ i +" "+ j );
		System.out.println( costs.get( "" + a.charAt( i ) + b.charAt( j ) ) );
		final ArrayList<Character> vowels = new ArrayList<Character>()
		{{
				add( 'a' );
				add( 'e' );
				add( 'i' );
				add( 'o' );
				add( 'u' );
			}};
		return ((vowels.contains( a.charAt( i ) ) && vowels.contains( b.charAt( j )))
				|| (!vowels.contains( a.charAt( i ) ) && !vowels.contains( b.charAt( j ))))
				? 1
				: 3;
//		return costs.get("" + a.charAt(i) + b.charAt(j));
	}
	
}
