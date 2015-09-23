/**
 * Created by Sren Palmund on 14-09-2015.
 */

import java.awt.Point;
import java.io.FileNotFoundException;
import java.util.*;

public class GSC
{
    private static final int delta = -4;
    public static final int UNSET_VALUE = Integer.MIN_VALUE;

    private static Point[][] path;
    private static Map<String, Integer> alpha = Matcher.costs;
    private static int[][] M;
    private static char[] x, y;

    public static void main(String[] args) throws FileNotFoundException {
        x = args[0].toCharArray();
        y = args[1].toCharArray();
        final int score = calculateMatchScore();

        StringBuilder s1 = new StringBuilder(  );
        StringBuilder s2 = new StringBuilder(  );
        Point current = new Point( x.length, y.length );
        Point last = new Point( -1, -1 );
        while (current.x != 0 && current.y != 0)
        {
            final char c = current.x == last.x ? '-' : x[ current.x - 1 ];
            final char c1 = current.y == last.y ? '-' : y[ current.y - 1 ];
            s1.append( c );
            s2.append( c1 );
            last = current;
            current = path[current.x][current.y];
        }

        System.out.printf( "Score: %d\nString1: %s\nString2: %s\n", score, s1.reverse(), s2.reverse() );
    }

    static int calculateMatchScore()
    {
        M = new int[x.length+1][y.length+1];
        path = new Point[x.length+1][y.length+1];

        // Initialize all cells
        for (int[] ints : M)
        {
            Arrays.fill(ints, UNSET_VALUE );
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
        if ( M[i][j] != UNSET_VALUE)
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
