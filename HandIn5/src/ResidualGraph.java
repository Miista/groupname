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

//    public ArrayList<Integer[]> buildPaths(int start, int end)
//    {
//        final Iterable<DirectedEdge> edges = this.adj( start );
//
//        final ArrayList<Integer[]> paths = new ArrayList<>();
//
//        int index = 0;
//        for (DirectedEdge edge : edges)
//        {
//            if (edge.to() == end)
//            {
//                paths.add( new Integer[] { start, end } );
//                return paths;
//            }
//
//            final ArrayList<Integer[]> childPaths = buildPaths( edge.to(), end );
//        }
//
//        return new int[0][0];
//    }
}
