import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class InputParser {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		if(! (args.length > 0) ) {
			System.out.println("Filename not found - please provide proper name");
			return;
		}
		
		Map<String, String> result = readFromFile(args[0]);
		
		Map<String, Integer> costs = readCosts("gorilla_data/BLOSUM62.txt");
		
		System.out.println("Sz = "+costs.size());
		
//		int[][] res = Matcher.compare( result.get("sphinx"), result.get("snark") );
		
		for (Entry<String, Integer> e : costs.entrySet() ) {
			System.out.println(  "["+e.getKey()+"]: "+e.getValue() );
		}
		
//		for (int i = 0; i < res.length; i++) {
//			System.out.print("R"+i+" ");
//			for (int j = 0; j < res[i].length; j++) {
//				System.out.print( res[i][j]+" ");
//			}
//			System.out.println("");
//		}
	}
	
	private static Scanner s;
	
	public static Map<String, String> readFromFile(String name) throws FileNotFoundException {
		s = new Scanner( new File(name) );
		
		return readSequences();
	}
	
	public static Map<String, Integer> readCosts(String filename) {
		Map<String, Integer> result = new HashMap<String, Integer>();
		String line;
		String[] vertHd, horiHd;
		try(Scanner s = new Scanner( new File(filename), "UTF-8" ) ) {
			line = s.nextLine();
			String tmp[];
			while(line.contains("#") ) line = s.nextLine();
			
			//consume first line
			tmp = line.split("[ ]+");
			horiHd = tmp;
			vertHd = tmp;
			int i = 1;
			line = s.nextLine();				
			while( s.hasNextLine() ) {
				tmp = line.split("[ ]+");
				for (int j = 1; j < vertHd.length; j++) {
					result.put(	""+vertHd[i].toLowerCase()+horiHd[j].toLowerCase(),
								Integer.parseInt( tmp[j] )
								);
				}
				line = s.nextLine();
				i++;
			}
			
			
		} catch(FileNotFoundException e) {}

		return result;
	}
	
	private static Map<String,String> readSequences() {
		Map<String, String> sequences = new HashMap<>();
		String line = s.nextLine();
		String[] name;
		StringBuilder seqBuilder = new StringBuilder();

		if( line.startsWith(">") ) {
			name = line.split(" ");
		}
		else {
			//wrong format
			return null;
		}
		
		while( s.hasNextLine() ) {
			line = s.nextLine();
			if( line.startsWith(">") ) {
				sequences.put( (name[0]).substring(1).toLowerCase(), seqBuilder.toString() );
				name = line.split(" ");
				seqBuilder = new StringBuilder();
			}
			else {
				seqBuilder.append( line );
			}

		}
		sequences.put( (name[0]).substring(1).toLowerCase(), seqBuilder.toString() );

		return sequences;
	}

}
