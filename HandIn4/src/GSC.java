/**
 * Created by Sren Palmund on 14-09-2015.
 */
import javafx.util.Pair;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

public class GSC
{

    private static ArrayList<Pair<Point, Point>> pairs = new ArrayList<>(  );
    private static Point[][] path;

    public static void main(String[] args) throws FileNotFoundException {
		final String A = "-KQRK";
        final String B = "-KQRIKAAKABK";
        final int m = A.length();
        final int n = B.length();

        final char[] ns = B.toCharArray();
        final char[] ms = A.toCharArray();

        final long start = System.currentTimeMillis();
        doIt( ns, ms, -4, Matcher.costs );
        final long stop = System.currentTimeMillis();
        System.out.println( "O: "+(stop-start) );

        System.out.println("Score: "+M[n-1][m-1]);

        final Point[] _path = new Point[Math.max( m, n )];
        Point p;
        int i = n-1, j = m-1, z = _path.length-1;
        _path[z--] = new Point( i, j );

        do { // Fill up the array from the end
            p = path[i][j];
            _path[z--] = p;
            i = p.x;
            j = p.y;
        } while ( z >= 0 );

        Point last = _path[0];
        for (Point point : _path)
        {
            if (Math.abs( point.y - last.y ) == 1){
                System.out.print( ms[ point.y ] );
            }else{
                System.out.print( "-" );
            }
            last = point;
        }
        System.out.println();
        last = _path[0];
        for (Point point : _path)
        {
            if (Math.abs( point.x - last.x ) == 1){
                System.out.print( ns[ point.x ] );
            }else{
                System.out.print( "-" );
            }
            last = point;
        }
        System.out.println();
    }

    static int[][] M;
	private static void doIt(char[] x, char[] y, int delta, Map<String, Integer> alpha)
    {
        path = new Point[x.length][y.length];
        M = new int[x.length][y.length];
        for (int[] ints : M)
        {
            Arrays.fill(ints, -999);
        }
        for (int i = 0; i < x.length; i++)
        {
            M[i][0] = i*delta;
            path[i][0] = new Point( i-1, 0 );
        }

        for (int j = 0; j < y.length; j++)
        {
            M[0][j] = j*delta;
            path[0][j] = new Point( 0, j );
        }

        for (int i = 1; i < x.length; i++)
        {
            for (int j = 1; j < y.length; j++)
            {
                M[i][j] = OPT(x, y, i, j, alpha, delta );
                printThem( x, y );
            }
        }
//        printThem( x, y, M );
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
            path[i][j] = parent;
        }
        else if (_alphaPlus > _deltaIMinus && _alphaPlus < _deltaJMinus)
        {
            final Point parent = new Point( i, j-1 );
            path[i][j] = parent;
        }
        else if (_deltaIMinus > _deltaJMinus)
        {
            final Point parent = new Point( i-1, j );
            path[i][j] = parent;
        } else {
            final Point parent = new Point( i, j-1 );
            path[i][j] = parent;
        }

        return Math.max( _alphaPlus,
                Math.max( _deltaIMinus,
                        _deltaJMinus ) );
    }

    private static void printThem(char[] x, char[] y)
    {
        System.out.println( "=================================" );

        System.out.printf( "%-6c", ' ' );
        for (char c : y)
        {
            System.out.printf( "%-6c", c );
        }
        System.out.println();
        for (int i = 0; i < M.length; i++)
        {
            int[] ints = M[ i ];
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
