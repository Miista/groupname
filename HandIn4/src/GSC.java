/**
 * Created by Sren Palmund on 14-09-2015.
 */
import javafx.util.Pair;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class GSC
{

    private static ArrayList<Pair<Point, Point>> pairs = new ArrayList<>(  );

    public static void main(String[] args) throws FileNotFoundException {
		final String A = "-KQRK";
        final String B = "-KAK";
        final int m = A.length();
        final int n = B.length();
        doIt( A.toCharArray(), B.toCharArray(), -4, Matcher.costs );

        ArrayList<Pair<Point, Point>> route = new ArrayList<>(  );
        Collections.reverse( pairs );
        Point lookupValue = new Point( m-1, n-1 );
        for (Pair<Point, Point> pair : pairs)
        {
            if (pair.getValue().x == lookupValue.x && pair.getValue().y == lookupValue.y)
            {
                route.add( pair );
                if (pair.getKey().x == 0 && pair.getKey().y == 0)
                {
                    break;
                }
                lookupValue = pair.getKey();
            }
        }
        Collections.reverse( route );
        route.stream().mapToInt( value -> value.getValue().x ).forEach( value1 -> System.out.print( A.charAt( value1 ) ) );
        System.out.println();
        final IntStream intStream = route.stream()
                                         .mapToInt( value -> value.getValue().y );
        final PrimitiveIterator.OfInt iterator = intStream.iterator();
        int last = 0;
        while (iterator.hasNext()){
            final Integer next = iterator.next();
            if (next == last) {
                System.out.printf( "-" );
            }else{
                System.out.print( B.charAt( next ) );
            }
            last = next;
        }
    }

    static int[][] M;
	private static void doIt(char[] x, char[] y, int delta, Map<String, Integer> alpha)
    {
        M = new int[x.length][y.length];
        for (int[] ints : M)
        {
            Arrays.fill(ints, -999);
        }
        for (int i = 0; i < x.length; i++)
        {
            M[i][0] = i*delta;
            pairs.add( new Pair<>( new Point( i-1, 0 ), new Point( i, 0 ) ) );
        }

        for (int j = 0; j < y.length; j++)
        {
            M[0][j] = j*delta;
            pairs.add( new Pair<>( new Point( 0, j-1 ), new Point( 0, j ) ) );
        }
//        printThem( x, y, M );

        for (int i = 1; i < x.length; i++)
        {
            for (int j = 1; j < y.length; j++)
            {
                M[i][j] = OPT(x, y, i, j, alpha, delta );
                printThem( x, y, M );
            }
        }
        printThem( x, y, M );
    }

    private static int OPT(char[] x, char[] y, int i, int j, Map<String, Integer> alpha, int delta)
    {
        if (M[i][j] != -999)
        {
            return M[i][j];
        }

        if (i == 0)
        {
            return j*delta;
        }

        if (j == 0)
        {
            return i*delta;
        }

        char c1 = x[i];
        char c2 = y[j];
        String s = ("" + c1 + c2).toLowerCase();
        final Integer _alpha = alpha.getOrDefault( s, 0 );
        final int _alphaPlus = _alpha + OPT( x, y, i-1, j-1, alpha, delta );
        final int _deltaIMinus = delta + OPT( x, y, i-1, j, alpha, delta );
        final int _deltaJMinus = delta + OPT( x, y, i, j-1, alpha, delta );

        if (_alphaPlus > _deltaIMinus && _alphaPlus > _deltaJMinus)
        {
            final Point parent = new Point( i - 1, j - 1 );
            final Point us = new Point( i, j );
            pairs.add( new Pair<>( parent, us ) );
        }
        else if (_alphaPlus > _deltaIMinus && _alphaPlus < _deltaJMinus)
        {
            final Point parent = new Point( i, j-1 );
            final Point us = new Point( i, j );
            pairs.add( new Pair<>( parent, us ) );
        }
        else if (_deltaIMinus > _deltaJMinus)
        {
            final Point parent = new Point( i-1, j );
            final Point us = new Point( i, j );
            pairs.add( new Pair<>( parent, us ) );
        } else {
            final Point parent = new Point( i, j-1 );
            final Point us = new Point( i, j );
            pairs.add( new Pair<>( parent, us ) );
        }

        return Math.max( _alphaPlus,
                Math.max( _deltaIMinus,
                        _deltaJMinus ) );
    }

    private static void printThem(char[] x, char[] y, int[][] m)
    {
        System.out.println( "=================================" );

        System.out.printf( "%-6c", ' ' );
        for (char c : y)
        {
            System.out.printf( "%-6c", c );
        }
        System.out.println();
        for (int i = 0; i < m.length; i++)
        {
            int[] ints = m[ i ];
            System.out.printf( "%-6c", x[ i ] );
            for (int anInt : ints)
            {
                System.out.printf( "%-5d ", anInt );
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("=================================");
    }
}
