import javafx.util.Pair;

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
        ArrayList<EPoint> EPoints = CPParser.readPoints( args[ 0 ] );
        Collections.sort( EPoints, (o1, o2) -> Double.compare( o1.x, o2.x ) );
        final long ourBefore = System.currentTimeMillis();
        points = EPoints;
        final EuclideanPair pair = ClosestPairRico( 0, points.size()-1 );
        final long ourAfter = System.currentTimeMillis();
        System.out.println( ourAfter - ourBefore );
        System.out.println(pair.distance());
        System.out.printf( "Pair 1: (%f, %f)\n", pair.left.x, pair.left.y );
        System.out.printf( "Pair 2: (%f, %f)\n", pair.right.x, pair.right.y );
    }

    private static ArrayList<EPoint> points;

    public static EuclideanPair ClosestPairRico(int start, int end)
    {
        switch (end - start + 1)
        {
            case 3: // Only 3 EPoints - Base Case
                return getClosestPairOf3Rico(start, end);
            case 2:
                return new EuclideanPair( points.get( start ), points.get( end ) );
            case 1: // We can't handle this shit!
                System.exit( -2 );
                break;
            default:
                final int medianIndex = start + ((end - start) / 2);
                final EuclideanPair closestLeftPair = ClosestPairRico( start, medianIndex );
                final EuclideanPair closestRightPair = ClosestPairRico( medianIndex, end );

                double delta = Math.min( closestLeftPair.distance(), closestRightPair.distance() );



                EuclideanPair pair;
                if (closestLeftPair.distance() < closestRightPair.distance())
                {
                    pair = closestLeftPair;
                }
                else {
                    pair = closestRightPair;
                }

//                final ArrayList<EPoint> pointsBeingStitched = new ArrayList<>();
//                for (int i = start; i < end; i++)
//                {
//                    final EPoint next = points.get( i );
//                    if (lowerBound < next.x && next.x < upperBound)
//                    {
//                        pointsBeingStitched.add( next );
//                    }
//                }

                final double medianX = points.get( medianIndex ).x;
                final int lowerBound = (int) (medianX - delta);
                final int upperBound = (int) (medianX + delta);

                int p1 = -1, p2 = -1;
                for (int i = start; i < end; i++)
                {
                    final EPoint p = points.get( i );
                    if (p.x <= lowerBound || p.x >= upperBound)
                    {
                        continue;
                    }
                    for (int j = i+1; j <= i+16; j++)
                    {
                        if (j >= end) {
                            break;
                        }
                        final double distance = points.get( j ).distance( p );
                        if (distance < delta)
                        {
                            p1 = i;
                            p2 = j;
                            delta = distance;
                        }
                    }
                }

                return p1 == -1
                        ? pair
                        : new EuclideanPair( points.get( p1 ), points.get( p2 ) );
        }
        return null;
    }

    public static EuclideanPair ClosestPair(ArrayList<EPoint> points)
    {
        switch (points.size())
        {
            case 3: // Only 3 EPoints - Base Case
                return getClosestPairOf3( points );
            case 2:
                return new EuclideanPair( points.get( 0 ), points.get( 1 ) );
            case 1: // We can't handle this shit!
                System.exit( -2 );
                break;
            default:
                final int median = points.size() / 2;
                final ArrayList<EPoint> left = new ArrayList<>( median );
                for (int i = 0; i < median; i++)
                {
                    left.add( points.get( i ) );
                }
                final ArrayList<EPoint> right = new ArrayList<>( median );
                for (int i = median; i < points.size(); i++)
                {
                    right.add( points.get( i ) );
                }
                final EuclideanPair closestLeftPair = ClosestPair( left );
                final EuclideanPair closestRightPair = ClosestPair( right );

                double delta = Math.min( closestLeftPair.distance(), closestRightPair.distance() );

                final int lowerBound = (int) (median - delta);
                final int upperBound = (int) (median + delta);

                final ArrayList<EPoint> pointsBeingStitched = new ArrayList<>();
                final Iterator<EPoint> iterator = points.iterator();
                while (iterator.hasNext()) {
                    final EPoint next = iterator.next();
                    if (lowerBound < next.x && next.x < upperBound)
                    {
                        pointsBeingStitched.add( next );
                    }
                }

                EuclideanPair pair;
                if (closestLeftPair.distance() < closestRightPair.distance())
                {
                    pair = closestLeftPair;
                }
                else {
                    pair = closestRightPair;
                }

                int p1 = -1, p2 = -1;
                for (int i = 0; i < points.size(); i++)
                {
                    final EPoint p = points.get( i );
                    for (int j = i+1; j <= delta; j++)
                    {
                        if (j >= points.size()) {
                            break;
                        }
                        final double distance = points.get( j ).distance( p );
                        if (distance < delta)
                        {
                            p1 = i;
                            p2 = j;
                            delta = distance;
                        }
                    }
                }

                return p1 == -1
                        ? pair
                        : new EuclideanPair( points.get( p1 ), points.get( p2 ) );
        }
        return null;
    }

    private static EuclideanPair getClosestPairOf3Rico(int start, int end)
    {
        final EPoint A = points.get( start );
        final EPoint B = points.get( start+1 );
        final EPoint C = points.get( end );
        final double ab = A.distance( B );
        final double bc = B.distance( C );
        final double ca = C.distance( A );
        EPoint left, right;
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

    private static EuclideanPair getClosestPairOf3(List<EPoint> EPoints)
    {
        final EPoint A = EPoints.get( 0 );
        final EPoint B = EPoints.get( 1 );
        final EPoint C = EPoints.get( 2 );
        final double ab = A.distance( B );
        final double bc = B.distance( C );
        final double ca = C.distance( A );
        EPoint left, right;
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

    public static class EPoint
    {
        public final double x, y;

        public EPoint(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        public double distance(EPoint other)
        {
            final double y = Math.abs( this.y - other.y );
            final double x = Math.abs( this.x - other.x );
            return Math.sqrt( y * y + x * x );
        }
    }

    public static class EuclideanPair
    {
        public EPoint left;
        public EPoint right;

        public EuclideanPair(EPoint left, EPoint right)
        {
            this.left = left;
            this.right = right;
        }

        public double distance()
        {
            final double y = Math.abs( left.y - right.y );
            final double x = Math.abs( left.x - right.x );
            return Math.sqrt( y * y + x * x );
        }
    }
}
