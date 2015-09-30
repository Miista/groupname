import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowReader {

	public static final ArrayList<Vertex> vertices = new ArrayList<>();

	public static DirectedGraph<Vertex, DirectedEdge> parseFile(File f) throws Exception {
		DirectedGraph<Vertex, DirectedEdge> g = new DirectedMultigraph<>( DirectedEdge.class );

		int vertices, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			vertices = Integer.parseInt( s.next() );
			while( s.hasNext() && vertices > 0) {
				String name = s.next();
				final Vertex vertex = new Vertex( name );
				FlowReader.vertices.add( vertex );
				vertices--;
				g.addVertex( vertex );
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				int from = s.nextInt();
				int to = s.nextInt();
				int value = s.nextInt();
				final Vertex fromVertex = FlowReader.vertices.get( from );
				final Vertex toVertex = FlowReader.vertices.get( to );
				g.addEdge( fromVertex, toVertex, new DirectedEdge( fromVertex, from, toVertex, to, value ) );
				g.addEdge( toVertex, fromVertex, new DirectedEdge( toVertex, to, fromVertex, from, value ) );
				edges--;
			}
		}
		return g;
	}

	public static int NF(DirectedGraph<Vertex, DirectedEdge> g, Vertex source, Vertex sink)
	{
		int totalFlow = 0;
		int maxEdges = 1;

		DirectedGraph<Vertex, DirectedEdge> residualGraph = new DirectedMultigraph<>( DirectedEdge.class );
		g.vertexSet()
		 .forEach( residualGraph::addVertex );
		for (DirectedEdge edge : g.edgeSet())
		{
			residualGraph.addEdge( edge.getFromVertex(), edge.getToVertex(), edge );
		}

		main:do
		{
			BellmanFordShortestPath<Vertex, DirectedEdge> pathFinder = new BellmanFordShortestPath<>( residualGraph, source, maxEdges );
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
			for (DirectedEdge forwardEdge : pathEdgeList)
            {
				final DirectedEdge backwardsEdge = getBackwardsEdge( residualGraph, forwardEdge );
				forwardEdge.setFlow( min );
				if (forwardEdge.weight() == 0)
				{
					residualGraph.removeEdge( forwardEdge );
				}
				backwardsEdge.setFlow( -min );
			}
		} while (true);

		return totalFlow;
	}

	private static DirectedEdge getBackwardsEdge(DirectedGraph<Vertex, DirectedEdge> residualGraph, DirectedEdge edge) {
		if (residualGraph.containsEdge( edge.getToVertex(), edge.getFromVertex() ))
		{
			return residualGraph.getEdge( edge.getToVertex(), edge.getFromVertex() );
		}
		final DirectedEdge backwardsEdge = new DirectedEdge( edge.getToVertex(), edge.getFromVertex(), edge.getCapacity() );
		backwardsEdge.setFlow( edge.getCapacity() );
		residualGraph.addEdge( edge.getToVertex(), edge.getFromVertex(), backwardsEdge );
		return backwardsEdge;
	}

	public static void main(String[] args) throws Exception {
		DirectedGraph<Vertex, DirectedEdge> g = parseFile( new File( "flow-data/rail.txt" ) );
		System.out.println( NF( g, vertices.get( 0 ), vertices.get( 54 ) ) );
	}

	public static class Vertex
	{
		private final String name;

		public Vertex(String name) {this.name = name;}

		public String getName()
		{
			return name;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
