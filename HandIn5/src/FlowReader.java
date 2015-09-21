import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class FlowReader {

	public static DirectedGraph<Integer, DirectedEdge> parseFile(File f) throws Exception {
		DirectedGraph<Integer, DirectedEdge> g;

		int vertices, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			vertices = Integer.parseInt( s.next() );
			g = new DirectedMultigraph<>( DirectedEdge.class );
			int i = 0;
			while( s.hasNext() && vertices > 0) {
				s.next();
				vertices--;
				g.addVertex( i++ );
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				int from = s.nextInt();
				int to = s.nextInt();
				int value = s.nextInt();
				g.addEdge( from, to, new DirectedEdge( from, to, value ) );
				edges--;
			}
		}
		return g;
	}

	public static int NF(DirectedGraph<Integer, DirectedEdge> g, Integer source, Integer sink)
	{
		int totalFlow = 0;
		int maxEdges = 1;

		DirectedGraph<Integer, DirectedEdge> residualGraph = new DefaultDirectedGraph( DirectedEdge.class );
		for (Integer v : g.vertexSet())
		{
			residualGraph.addVertex( v );
		}
		for (DirectedEdge edge : g.edgeSet())
		{
			residualGraph.addEdge( edge.from(), edge.to(), edge );
		}


		main:do
		{
			BellmanFordShortestPath pathFinder = new BellmanFordShortestPath<>( residualGraph, source, maxEdges );
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
										.filter( e -> e.getCapacity() > 0 )
										.min( (o1, o2) -> Double.compare( o1.weight(), o2.weight() ) )
										.get()
									 	.weight();
			totalFlow += min;

			for (DirectedEdge edge : pathEdgeList)
            {
				edge.setFlow( min );
				if (edge.weight() == 0)
				{
					residualGraph.removeEdge( edge );
					residualGraph.addEdge( edge.to(), edge.from(), edge.getFlippedVersion() );
				}
            }
		} while (true);

		return totalFlow;
	}

	public static void main(String[] args) throws Exception {
		DirectedGraph<Integer, DirectedEdge> g = parseFile( new File( "flow-data/rail.txt" ) );
//		final DirectedGraph<Integer, DirectedEdge> g = new DirectedMultigraph<>( DirectedEdge.class );
//		g.addVertex( 0 );
//		g.addVertex( 1 );
//		g.addVertex( 2 );
//		g.addVertex( 3 );
//		g.addVertex( 4 );
//		g.addVertex( 5 );
//		g.addEdge( 0, 1, new DirectedEdge( 0,1,-1 ) );
//		g.addEdge( 0, 2, new DirectedEdge( 0,2,-1 ) );
//		g.addEdge( 1, 2, new DirectedEdge( 1,2,2 ) );
//		g.addEdge( 1, 3, new DirectedEdge( 1,3,4 ) );
//		g.addEdge( 1, 4, new DirectedEdge( 1,4,8 ) );
//		g.addEdge( 2, 4, new DirectedEdge( 2,4,9 ) );
//		g.addEdge( 3, 5, new DirectedEdge( 3,5,10 ) );
//		g.addEdge( 4, 3, new DirectedEdge( 4,3,6 ) );
//		g.addEdge( 4, 5, new DirectedEdge( 4,5,10 ) );

		System.out.println( NF( g, 0, 54 ) );
	}
}
