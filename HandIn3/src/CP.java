import javafx.util.Pair;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Søren Palmund on 07-09-2015.
 */
public class CP
{
    public static void main(String[] args) throws FileNotFoundException
    {
        List<Point> points = CPParser.readPoints( args[0] );
//        new ArrayList<>(  );
//        points.add( new Point( 0, 0 ) );
////        points.add( new Point( 1, 2 ) );
//        points.add( new Point( 2, -11 ) );
//        points.add( new Point( 3, -10 ) );
////        points.add( new Point( 4, 90 ) );
//        points.add( new Point( 5, 100 ) );
        Collections.sort( points, (o1, o2) -> Integer.compare( o1.x, o2.x ) );
        final EuclideanPair pointPointPair = ClosestPair( points );
        System.out.printf( "Pair 1: (%d, %d)\n", pointPointPair.left.x, pointPointPair.left.y );
        System.out.printf( "Pair 2: (%d, %d)\n", pointPointPair.right.x, pointPointPair.right.y );
    }
    public static EuclideanPair ClosestPair(List<Point> points)
    {
        switch (points.size())
        {
            case 3: // Only 3 points - Base Case
                return getClosestPairOf3( points );
            case 2:
                return new EuclideanPair( points.get( 0 ), points.get( 1 ) );
            case 1: // We can't handle this shit!
                System.exit( -2 );
                break;
            default:
                final int median = points.size() / 2;
                final List<Point> left = points.subList( 0, median );
                final List<Point> right = points.subList( median, points.size() );
                final EuclideanPair closestLeftPair = ClosestPair( left );
                final EuclideanPair closestRightPair = ClosestPair( right );

                final double delta = Math.min( closestLeftPair.distance, closestRightPair.distance );

                final int lowerBound = (int) (median - delta);
                final int upperBound = (int) (median + delta);

                final Iterator<Point> iterator = points.iterator();
                while (iterator.hasNext()) {
                    final Point next = iterator.next();
                    if (next.x < lowerBound || next.x > upperBound) {
                        iterator.remove();
                    }
                }

                Collections.sort( points, (o1, o2) -> Integer.compare( o1.y, o2.y ) );
                return ClosestPair( points );
        }
        return null;
    }

    private static EuclideanPair getClosestPairOf3(List<Point> points)
    {
        final Point A = points.get( 0 );
        final Point B = points.get( 1 );
        final Point C = points.get( 2 );
        final double ab = A.distance( B );
        final double bc = B.distance( C );
        final double ca = C.distance( A );
        Point left, right;
        if (ab < bc)
        {
            left = A;
            right = B;
        } else
        {
            left = B;
            right = C;
        }
        if (ca < ab)
        {
            left = A;
            right = C;
        }

        return new EuclideanPair( left, right );
    }

    static class EuclideanPair
    {
        public final Point left;
        public final Point right;

        public final double distance;

        public EuclideanPair(Point left, Point right)
        {
            this.left = left;
            this.right = right;
            distance = left.distance( right );
        }
    }
}
