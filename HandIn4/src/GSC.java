/**
 * Created by Sren Palmund on 14-09-2015.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class GSC
{
	public static void main(String[] args) throws FileNotFoundException {
//		Map<String, String> map = InputParser.readFromFile("gorilla_data/Toy_FASTAs-in.txt");
//		System.out.println("snark:  " + map.get("snark"));
		final String A = "mean";
		final String B = "name";
		int[][] comp = Matcher.compare( A, B );


		for (String s : A.split( "" ))
		{
			System.out.print( s + " " );
		}
		for (int i = 0; i < comp.length; i++) {

			System.out.println();
			System.out.print( B.split( "" )[ i ] + " ");
			for (int j = 0; j < comp[i].length; j++) {
				System.out.print( comp[i][j] + " ");
			}
			System.out.println();
		}
	}
}
