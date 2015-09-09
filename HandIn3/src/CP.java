import javafx.util.Pair;

import java.io.FileNotFoundException;
import java.util.*;

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
        final int[] cp = ClosestPairRico( 0, points.size() - 1 );
        final EuclideanPair pair = new EuclideanPair( points.get( cp[0]), points.get( cp[1] ) );
        final long ourAfter = System.currentTimeMillis();
        System.out.println( ourAfter - ourBefore );
        System.out.println(pair.distance());
        System.out.printf( "Pair 1: (%f, %f)\n", pair.left.x, pair.left.y );
        System.out.printf( "Pair 2: (%f, %f)\n", pair.right.x, pair.right.y );
    }

    private static ArrayList<EPoint> points;

    public static double distance(EPoint p1, EPoint p2)
    {
        final double y = Math.abs( p1.y - p2.y );
        final double x = Math.abs( p1.x - p2.x );
        return Math.sqrt( y * y + x * x );
    }

    public static int[] ClosestPairRico(int start, int end)
    {
        switch (end - start + 1)
        {
            case 3: // Only 3 EPoints - Base Case
                return getClosestPairOf3Rico(start, end);
            case 2:
                return new int[] { start, end };
            case 1: // We can't handle this shit!
                System.exit( -2 );
                break;
            default:
                final int medianIndex = start + ((end - start) / 2);
                final int[] closestLeftPair = ClosestPairRico( start, medianIndex );
                final int[] closestRightPair = ClosestPairRico( medianIndex, end );

                EPoint leftPoint = points.get( closestLeftPair[ 0 ] );
                EPoint rightPoint = points.get( closestLeftPair[1] );
                final double d1 = distance( leftPoint, rightPoint );

                EPoint leftPoint1 = points.get( closestRightPair[ 0 ] );
                EPoint rightPoint1 = points.get( closestRightPair[1] );
                final double d2 = distance( leftPoint1, rightPoint1 );

                double delta; // = Math.min( closestLeftPair.distance(), closestRightPair.distance() );
                int p1 = -1, p2 = -1;
                if (d1 < d2)
                {
                    p1 = closestLeftPair[0];
                    p2 = closestLeftPair[1];
                    delta = d1;
                }
                else {
                    p1 = closestRightPair[0];
                    p2 = closestRightPair[1];
                    delta = d2;
                }

                final double medianX = points.get( medianIndex ).x;
                final int lowerBound = (int) (medianX - delta);
                final int upperBound = (int) (medianX + delta);

                for (int i = start; i < end; i++)
                {
                    final EPoint p = points.get( i );
                    if (p.x <= lowerBound || p.x >= upperBound)
                    {
                        continue;
                    }
                    for (int j = i+1; j < end; j++)
                    {
                        final double distance = points.get( j ).distance( p );
                        if (distance < delta)
                        {
                            p1 = i;
                            p2 = j;
                            delta = distance;
                        }
                    }
                }

                return new int[] { p1, p2 };
        }
        return null;
    }

    private static int[] getClosestPairOf3Rico(int start, int end)
    {
        final EPoint A = points.get( start );
        final EPoint B = points.get( start+1 );
        final EPoint C = points.get( end );
        final double ab = A.distance( B );
        final double bc = B.distance( C );
        final double ca = C.distance( A );

        int left, right;
        if (ab < bc)
        {
            left = start;
            right = start+1;
        } else
        {
            left = start+1;
            right = end;
        }
        if (ca < ab)
        {
            left = start;
            right = end;
        }

        return new int[] { left, right };
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
