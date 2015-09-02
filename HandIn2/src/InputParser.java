import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InputParser {

	private static Scanner s;
	
	public static Job[] readFromFile(String name) throws FileNotFoundException {
		s = new Scanner( new File(name) );
		
		return readJobs();
	}
	
	
	private static Job[] readJobs() {
		int i = 0;
		int n = Integer.parseInt( s.nextLine() );
		System.out.println("n is "+n);
		Job[] jobs = new Job[n];
		s.nextLine();
		String[] job;
		while( s.hasNextLine() ) {
			job = s.nextLine().split(" ");
			jobs[i] = new Job( Integer.parseInt( job[0] ), Integer.parseInt( job[1] ) );
			i++;
		}
		return jobs;
	}
	
	public static void main(String[] args) {
		Job[] jobs = null;
		try {
			jobs = InputParser.readFromFile( args[0] );
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(jobs != null)
		for (Job job : jobs) {
			System.out.println(job.start+" "+job.finish);
		}
	}
}
