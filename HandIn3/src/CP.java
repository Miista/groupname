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
//        new ArrayList<>(  );
//        EPoints.add( new EPoint( 0, 0 ) );
////        EPoints.add( new EPoint( 1, 2 ) );
//        EPoints.add( new EPoint( 2, -11 ) );
//        EPoints.add( new EPoint( 3, -10 ) );
////        EPoints.add( new EPoint( 4, 90 ) );
//        EPoints.add( new EPoint( 5, 100 ) );
        Collections.shuffle( EPoints );
        Collections.sort( EPoints, (o1, o2) -> Double.compare( o1.x, o2.x ) );
        final ArrayList<EPoint> sortedByY = EPoints;
        Collections.sort( sortedByY, (o1, o2) -> Double.compare( o1.y, o2.y ) );
        final long before = System.currentTimeMillis();
        final EuclideanPair EPointEPointPair = closestPairDAQ( EPoints, sortedByY );
        final long after = System.currentTimeMillis();
        System.out.println( (after - before) );
        System.out.printf( "Pair 1: (%f, %f)\n", EPointEPointPair.left.x, EPointEPointPair.left.y );
        System.out.printf( "Pair 2: (%f, %f)\n", EPointEPointPair.right.x, EPointEPointPair.right.y );
        final long ourBefore = System.currentTimeMillis();
        final EuclideanPair pair = ClosestPair( EPoints );
        final long ourAfter = System.currentTimeMillis();
        System.out.println( ourAfter - ourBefore );
        System.out.printf( "Pair 1: (%f, %f)\n", pair.left.x, pair.left.y );
        System.out.printf( "Pair 2: (%f, %f)\n", pair.right.x, pair.right.y );
    }

    public static EuclideanPair closestPairDAQ(List<EPoint> xs, List<EPoint> ys)
    {
        if (xs.size() == 2)
        {
            return new EuclideanPair( xs.get( 0 ), xs.get( 1 ) );
        }
        if (xs.size() <= 3)
        {
            return getClosestPairOf3( xs );
        }

        final int median = xs.size() / 2;
        final List<EPoint> leftOfCenter = xs.subList( 0, median );
        final List<EPoint> rightOfCenter = xs.subList( median, xs.size() );

        final ArrayList<EPoint> tempList = new ArrayList<>( leftOfCenter );
        Collections.sort( tempList, (o1, o2) -> Double.compare( o1.y, o2.y ) );

        final EuclideanPair leftPair = closestPairDAQ( leftOfCenter, tempList );

        tempList.clear();
        tempList.addAll( rightOfCenter );
        Collections.sort( tempList, (o1, o2) -> Double.compare( o1.y, o2.y ) );
        final EuclideanPair rightPair = closestPairDAQ( rightOfCenter, tempList );

        EuclideanPair closestPair;
        if (leftPair.distance < rightPair.distance)
        {
            closestPair = leftPair;
        }
        else {
            closestPair = rightPair;
        }

        double delta = Math.min( leftPair.distance, rightPair.distance );

        final int lowerBound = (int) (median - delta);
        final int upperBound = (int) (median + delta);

        tempList.clear();
        for (EPoint py : ys)
        {
            if (py.x < upperBound && lowerBound < py.x )
            {
                tempList.add( py );
            }
        }

        Collections.sort( tempList, (o1, o2) -> Double.compare( o1.y, o2.y ) );

        for (int i = 0; i < tempList.size(); i++)
        {
            final EPoint p = tempList.get( i );
            for (int j = i+1; j < delta; j++)
            {
                if (j >= tempList.size()) {
                    break;
                }
                if (tempList.get( j ).distance( p ) < delta)
                {
                    closestPair = new EuclideanPair( p, tempList.get( j ) );
                    delta = tempList.get( j ).distance( p );
                }
            }
        }

        return closestPair;
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

                double delta = Math.min( closestLeftPair.distance, closestRightPair.distance );

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
                if (closestLeftPair.distance < closestRightPair.distance)
                {
                    pair = closestLeftPair;
                }
                else {
                    pair = closestRightPair;
                }
                for (int i = 0; i < points.size(); i++)
                {
                    final EPoint p = points.get( i );
                    for (int j = i+1; j < delta; j++)
                    {
                        if (j >= points.size()) {
                            break;
                        }
                        if (points.get( j ).distance( p ) < delta)
                        {
                            pair = new EuclideanPair( p, points.get( j ) );
                            delta = points.get( j ).distance( p );
                        }
                    }
                }

                return pair;
        }
        return null;
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

    static class EuclideanPair
    {
        public final EPoint left;
        public final EPoint right;

        public final double distance;

        public EuclideanPair(EPoint left, EPoint right)
        {
            this.left = left;
            this.right = right;
            distance = distance();
        }

        private double distance()
        {
            final double y = Math.abs( left.y - right.y );
            final double x = Math.abs( left.x - right.x );
            return Math.sqrt( y * y + x * x );
        }


    }
}
