import javafx.util.Pair;

import java.awt.Point;
import java.util.List;

/**
 * Created by Søren Palmund on 07-09-2015.
 */
public class CP
{
    public static Pair<Point, Point> ClosestPair(List<Point> points)
    {
        if (points.size() <= 3)
        {
            // Find closest pair
        }
        else
        {
            final int median = points.size() / 2;
            final List<Point> left = points.subList( 0, median );
            final List<Point> right = points.subList( median, points.size() );
        }



        return null;
    }
}
