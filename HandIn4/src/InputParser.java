import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class InputParser {
	
	public static void main(String[] args) throws FileNotFoundException {
		
		if(! (args.length > 0) ) {
			System.out.println("Filename not found - please provide proper name");
			return;
		}
		
		Map<String, String> result = readFromFile(args[0]);
		
		System.out.println("Sz = "+result.size());
		
		for (String k : result.keySet()) {
			System.out.println(  "["+k+"]: "+result.get(k) );
		}
	}
	
	private static Scanner s;
	
	public static Map<String, String> readFromFile(String name) throws FileNotFoundException {
		s = new Scanner( new File(name) );
		
		return readSequences();
	}
	
	
	private static Map<String,String> readSequences() {
		Map<String, String> sequences = new HashMap<>();
		String regx = "[>][A-Za-z]+\\s+[\\d]+[\\s]+[A-Z]+\\s+[\\d]?[A-Z]+", line = s.nextLine();
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
			if( line.startsWith(">") ) {
				sequences.put( (name[0]).substring(1), seqBuilder.toString() );
				name = line.split(" ");
				seqBuilder = new StringBuilder();
			}
			else {
				seqBuilder.append( line );
			}
			line = s.nextLine();

		}

		return sequences;
	}

}
