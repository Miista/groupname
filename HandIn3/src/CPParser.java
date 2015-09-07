import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Søren Palmund on 07-09-2015.
 */
public class CPParser
{
    public static List<Point> readPoints(String filename) throws FileNotFoundException
    {
        int i = 0;
        Scanner s = new Scanner( new File( filename ) );
        List<Point> points = new ArrayList<>();
        String[] job;
        while( s.hasNextLine() ) {
            job = s.nextLine().split(" ");
            points.add( new Point( Integer.parseInt( job[ 1 ] ), Integer.parseInt( job[ 2 ] ) ) );
            i++;
        }
        return points;
    }
}
