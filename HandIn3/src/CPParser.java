import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by S�ren Palmund on 07-09-2015.
 */
public class CPParser
{


    public static ArrayList<CP.EPoint> readPoints(String filename)
    {return readPoints( new File( filename ) );}

    public static ArrayList<CP.EPoint> readPoints(File filename)
    {
        try
        {
            Scanner s = new Scanner( filename );
            ArrayList<CP.EPoint> points = new ArrayList<>();
            String[] job;
            boolean canRead = !filename.getName().contains( "tsp" );
            while( s.hasNextLine() ) {
                final String line = s.nextLine();
                if (filename.getName().contains( "tsp" ) && !canRead) {
                    if ("NODE_COORD_SECTION".equals( line )) {
                        canRead = true;
                    }
                    continue;
                }
                if ("".equals( line ) || line.contains( ":" ) || line.contains( "_" ) || line.contains( "EOF" ) || " ".equals( line ))
                {
                    continue;
                }
                job = line.split( "[\\s]+" );
                points.add( new CP.EPoint( Double.parseDouble( job[ 1 ] ), Double.parseDouble( job[ 2 ] ) ) );
            }

            s.close();
            return points;
        }
        catch (Exception e)
        {
            throw new RuntimeException( e );
        }
    }
}
