package hacking;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * written to conform to the given data sets for GayleSharpley in.txt files
 * @author Ivan Naumovski
 *
 */
public class InputParser {

	private static Scanner s;
	
	private static int readHeaders() {
		String line = s.nextLine();
		//skip the comment lines
		while( line.contains("#") ) {
			line = s.nextLine();
		}
		//reach the size
		String[] sizeResult = line.split("=");
		return Integer.parseInt( sizeResult[1] );
	}
	
	private static Person[] initPeople(int peopleSize) {
		//id's start at 1 and not 0 - alternative could be to subtract 1 from every id 
		Person[] people = new Person[peopleSize + 1];

		String[] personData;
		for (int i = 0; i < peopleSize; i++) {
			personData = s.nextLine().split(" "); //Format: ID Name ie. 1 Al
			people[ Integer.parseInt( personData[0] ) ] = new Person( personData[1] );
		}
		return people;
	}
	
	private static Person[] readPreferences(Person[] people) {
		Person[] updated = people;
		
		//preference parse
		String pref;
		int id;
		Integer[] order;
		while ( s.hasNext() ) {
			pref = s.nextLine();
			System.out.println(pref+", ");
			String[] tmp = pref.split(":"); //FORMAT-> 1: 6 8 4 2
			id = Integer.parseInt( tmp[0] );

			order = new Integer[people.length/2]; //only half the pairs and hence length/2 to rank
			String[] vals = (tmp[1].substring(1)).split(" ");
			for (int j = 0; j < vals.length; j++) {
				order[j] = Integer.parseInt( vals[j] );
			}
			updated[id].preference( order );
		}
		return updated;
	}
	
	public static Person[] parseInput(String filename) {
		try {
			s = new Scanner( new File(filename), "UTF-8" );
			Person[] tmp, result;
			
			int size = readHeaders();
			System.out.println("n = "+size);

			//parse people
			tmp = initPeople(2*size); //there is n pairs - hence twice the people
			
			s.nextLine(); //skip the empty line

			result = readPreferences(tmp);
				
			//avoid loitering
			s.close();

			return result;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("ERROR file not found: "+e);
		}
		return null;


	}

}
