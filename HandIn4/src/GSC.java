/**
 * Created by Sren Palmund on 14-09-2015.
 */

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

public class GSC
{
    private static final int delta = -4;

    private static Point[][] path;
    private static Map<String, Integer> alpha = Matcher.costs;
    private static int[][] M;
    private static char[] x, y;

    public static void main(String[] args) throws FileNotFoundException {
        x = args[0].toCharArray();
        y = args[1].toCharArray();
        final long start = System.currentTimeMillis();
        final int score = calculateMatchScore();
        final long stop = System.currentTimeMillis();
        System.out.println( "O: "+(stop-start) );

        StringBuilder s1 = new StringBuilder(  );
        StringBuilder s2 = new StringBuilder(  );
        Point current = new Point( x.length, y.length );
        Point last = new Point( -1, -1 );
        while (current.x != 0 && current.y != 0)
        {
            if (current.x == last.x)
            {
                s1.append( '-' );
            }
            else
            {
                s1.append( x[current.x-1] );
            }

            if (current.y == last.y)
            {
                s2.append( '-' );
            }
            else
            {
                s2.append( y[current.y-1] );
            }
            last = current;
            current = path[current.x][current.y];
        }

        System.out.println(s1.reverse().toString());
        System.out.println(s2.reverse().toString());

        System.out.println( score );
//        System.out.println( value.item1 );

//        final char[] as = a.toCharArray();
//        final char[] bs = b.toCharArray();
//        final Point[] _path =
//        Point last = new Point( 0, 0 );
//        for (Point point : _path)
//        {
//            if (Math.abs( point.y - last.y ) == 1)
//            {
//                System.out.print( as[ point.y-1 ] );
//            } else
//            {
//                System.out.print( "-" );
//            }
//            last = point;
//        }
//        System.out.println();
//        last = new Point( 0, 0 );
//        for (Point point : _path)
//        {
//            if (Math.abs( point.x - last.x ) == 1)
//            {
//                System.out.print( bs[ point.x-1 ] );
//            } else
//            {
//                System.out.print( "-" );
//            }
//            last = point;
//        }
//        System.out.println();
    }

    static int calculateMatchScore()
    {
        M = new int[x.length+1][y.length+1];
        path = new Point[x.length+1][y.length+1];

        // Initialize all cells
        for (int[] ints : M)
        {
            Arrays.fill(ints, -999);
        }

        // Initialize first row
        for (int i = 0; i < x.length; i++)
        {
            M[i][0] = i*delta;
            path[i][0] = new Point( i-1, 0 );
        }

        // Initialize first column
        for (int j = 0; j < y.length; j++)
        {
            M[0][j] = j*delta;
            path[0][j] = new Point( 0, j );
        }

        for (int i = 1; i <= x.length; i++)
        {
            for (int j = 1; j <= y.length; j++)
            {
                int opt = optimal( i, j );
                M[i][j] = opt;
            }
        }

        return M[x.length][y.length];
    }

    private static int optimal(int i, int j)
    {
        if ( M[i][j] != -999 )
        {
            return M[i][j];
        }

        if ( i == 0 )
        {
            return j * delta;
        }

        if ( j == 0 )
        {
            return i * delta;
        }

        String key = (x[i-1] + "" + y[j-1]).toLowerCase();
        final int _alpha = alpha.get( key );
        final int _alphaPlus = _alpha + optimal( i - 1, j - 1 );
        final int _deltaIMinus = delta + optimal( i - 1, j );
        final int _deltaJMinus = delta + optimal( i, j - 1 );

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
}
