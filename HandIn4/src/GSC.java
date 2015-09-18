/**
 * Created by Sren Palmund on 14-09-2015.
 */

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

public class GSC
{

    private static Point[][] path;
    private static Map<String, Integer> alpha = Matcher.costs;
    private static final int delta = -4;
    private static char[] x, y;

    public static void main(String[] args) throws FileNotFoundException {
        String a = "KQRK";
        String b = "KAK";
        final long start = System.currentTimeMillis();
        final Tuple<Integer, Point[]> value = sequenceAlignment( a, b );
        final long stop = System.currentTimeMillis();
        System.out.println( "O: "+(stop-start) );

        System.out.println( value.item1 );

        final char[] as = a.toCharArray();
        final char[] bs = b.toCharArray();
        final Point[] _path = value.item2;
        Point last = new Point( 0, 0 );
        for (Point point : _path)
        {
            if (Math.abs( point.y - last.y ) == 1)
            {
                System.out.print( as[ point.y-1 ] );
            } else
            {
                System.out.print( "-" );
            }
            last = point;
        }
        System.out.println();
        last = new Point( 0, 0 );
        for (Point point : _path)
        {
            if (Math.abs( point.x - last.x ) == 1)
            {
                System.out.print( bs[ point.x-1 ] );
            } else
            {
                System.out.print( "-" );
            }
            last = point;
        }
        System.out.println();
    }

    /**
     * The Point array that is returned contains the path that is the optimal alignment.
     * The y-values of each point corresponds to the index in a, while x-values are the
     * index into b.
     *
     * @param a
     * @param b
     * @return
     */
    private static Tuple<Integer, Point[]> sequenceAlignment(String a, String b) {
        final String A = "-"+a;
        final String B = "-"+b;
        final int m = A.length();
        final int n = B.length();

        final char[] ns = B.toCharArray();
        final char[] ms = A.toCharArray();

        x = ns;
        y = ms;

        doIt( x, y );

        final Point[] _path = new Point[Math.max( m, n )-1];
        Point p;
        int i = n-1, j = m-1, z = _path.length-1;
        _path[z--] = new Point( i, j );

        do { // Fill up the array from the end
            p = path[i][j];
            if (p.x == 0 && p.y == 0)
            {
                break;
            }
            _path[z--] = p;
            i = p.x;
            j = p.y;
        } while ( z >= 0 );

        return new Tuple<>( M[n-1][m-1], _path );
    }

    static int[][] M;
	private static void doIt(char[] x, char[] y)
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
                M[i][j] = OPT( i, j );
            }
        }
    }

    private static int OPT(int i, int j)
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
        final int _alphaPlus = _alpha + OPT( i-1, j-1 );
        final int _deltaIMinus = delta + OPT( i-1, j );
        final int _deltaJMinus = delta + OPT( i, j-1 );

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

    public static class Tuple<T1, T2> {
        final public T1 item1;
        final public T2 item2;

        public Tuple(T1 x, T2 y) {
            this.item1 = x;
            this.item2 = y;
        }
    }
}
