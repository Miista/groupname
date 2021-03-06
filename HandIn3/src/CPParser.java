import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by S�ren Palmund on 07-09-2015.
 */
public class CPParser
{
	

	
    public static ArrayList<CP.EPoint> readPoints(String filename) throws FileNotFoundException
    {
        Scanner s = new Scanner( new File( filename ) );
        ArrayList<CP.EPoint> points = new ArrayList<>();
        String[] job;
        while( s.hasNextLine() ) {
            final String line = s.nextLine();
            if ("".equals( line ) || line.contains( ":" ) || line.contains( "_" ) || line.contains( "EOF" ))
            {
                continue;
            }
            job = line.split( "[\\s]+" );
            points.add( new CP.EPoint( Double.parseDouble( job[ 1 ] ), Double.parseDouble( job[ 2 ] ) ) );
        }

        s.close();
        return points;
    }
}
