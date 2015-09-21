import java.util.ArrayList;

/**
 * Created by Søren Palmund on 21-09-2015.
 */
public class ResidualGraph extends EdgeWeightedDigraph
{
    public ResidualGraph(EdgeWeightedDigraph G)
    {
        super( G );
    }

    public Path getPath(int start, int end)
    {

        return null;
    }

    public void actOnPath(Path path)
    {

    }

    public boolean pathExistsFromTo(int start, int end)
    {
        if (start == end) {
            return true;
        }

        final Iterable<DirectedEdge> edgesGoingOut = this.adj( start );
        for (DirectedEdge edge : edgesGoingOut)
        {
            return pathExistsFromTo( edge.to(), end );
        }
        return false;
    }

    public int[][] buildPaths(int start, int end)
    {
        final Iterable<DirectedEdge> edges = this.adj( start );

        int[][] paths = new int[10][10];
        int index = 0;
        for (DirectedEdge edge : edges)
        {
            if (edge.to() == end)
            {
                final int[][] ints = new int[ 0 ][ 0 ];
                ints[0][0] = start;
                return ints;
            }
            paths[index] = new int[10];
            buildPaths( edge.to(), end );
        }

        return new int[0][0];
    }
}
