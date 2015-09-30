import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowReader {
	public final ArrayList<Integer> vertices = new ArrayList<>();

	private final DirectedGraph<Integer, DirectedEdge<Integer>> graph;

	public FlowReader(File f) throws Exception
	{
		this.graph = parseFile( f );
	}

	public DirectedGraph<Integer, DirectedEdge<Integer>> parseFile(File f) throws Exception {
		DirectedGraph<Integer, DirectedEdge<Integer>> g = new DirectedMultigraph<>( DirectedEdge.class );

		int verticeCount, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			verticeCount = Integer.parseInt( s.next() );
			int i = 0;
			while( s.hasNext() && verticeCount > 0) {
				vertices.add( i );
				verticeCount--;
				g.addVertex( i );
				i++;
				s.next(); //added this for correct reading
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				int from = s.nextInt();
				int to = s.nextInt();
				int value = s.nextInt();
				final Integer fromVertex = vertices.get( from );
				final Integer toVertex = vertices.get( to );
				g.addEdge( fromVertex, toVertex, new DirectedEdge<>( fromVertex, toVertex, value ) );
				g.addEdge( toVertex, fromVertex, new DirectedEdge<>( toVertex, fromVertex, value ) );
				edges--;
			}
		}
		return g;
	}

	public int maxFlow(int sourceIndex, int sinkIndex)
	{
		Integer source = vertices.get( sourceIndex );
		Integer sink = vertices.get( sinkIndex );
		int totalFlow = 0;
		int maxEdges = 1;

		DirectedGraph<Integer, DirectedEdge> residualGraph = new DirectedMultigraph<>( DirectedEdge.class );
		graph.vertexSet()
		 	 .forEach( residualGraph::addVertex );
		for (DirectedEdge<Integer> edge : graph.edgeSet())
		{
			residualGraph.addEdge( edge.from, edge.to, edge );
		}

		main:do
		{
			BellmanFordShortestPath<Integer, DirectedEdge> pathFinder = new BellmanFordShortestPath<>( residualGraph, source, maxEdges );
			int n = 0;
			while (pathFinder.getPathEdgeList( sink ) == null)
            {
				if (n > 1000) {
					break main;
				}
                pathFinder = new BellmanFordShortestPath<>( residualGraph, source, ++maxEdges );
				n++;
            }

			final List<DirectedEdge> pathEdgeList = pathFinder.getPathEdgeList( sink );

			// Some edges can be infinite
			final int min = pathEdgeList.stream()
										.filter( e -> e.capacity > 0 )
										.min( (o1, o2) -> Double.compare( o1.weight(), o2.weight() ) )
										.get()
									 	.weight();
			totalFlow += min;
			for (@SuppressWarnings("unchecked") DirectedEdge<Integer> forwardEdge : pathEdgeList)
            {
				final DirectedEdge backwardsEdge = residualGraph.getEdge( forwardEdge.to, forwardEdge.from );
				forwardEdge.flow += min;
				if (forwardEdge.weight() == 0)
				{
					residualGraph.removeEdge( forwardEdge );
				}
				int flow = -min;
				backwardsEdge.flow += flow;
			}
		} while (true);

		return totalFlow;
	}

	public static void main(String[] args) throws Exception {
		FlowReader f1 = new FlowReader( new File( "flow-data/rail.txt" ) );
		System.out.println( f1.maxFlow( 0, 54 ) );
	}

    private static class DirectedEdge<V> {
        public final int capacity;
        public final V from, to;
        public int flow;

        public DirectedEdge(V from, V to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.flow = 0;
        }

        public int weight() {
            return capacity-flow;
        }
    }
}
