import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowReader {

	private static ArrayList<String> names = new ArrayList<>(  );
	public static final ArrayList<Vertex> vertices = new ArrayList<>();

	public static DirectedGraph<Vertex, DirectedEdge> parseFile(File f) throws Exception {
		DirectedGraph<Vertex, DirectedEdge> g;

		int vertices, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			vertices = Integer.parseInt( s.next() );
			g = new DirectedMultigraph<>( DirectedEdge.class );
			int i = 0;
			while( s.hasNext() && vertices > 0) {
				String name = s.next();
				final Vertex vertex = new Vertex( name );
				FlowReader.vertices.add( vertex );
				names.add( name );
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
				edges--;
			}
		}
		return g;
	}

	public static int NF(DirectedGraph<Vertex, DirectedEdge> g, Vertex source, Vertex sink)
	{
		int totalFlow = 0;
		int maxEdges = 1;

		DirectedGraph<Vertex, DirectedEdge> residualGraph = new DirectedMultigraph<Vertex, DirectedEdge>( DirectedEdge.class );
		for (Vertex v : g.vertexSet())
		{
			residualGraph.addVertex( v );
		}
		for (DirectedEdge edge : g.edgeSet())
		{
			residualGraph.addEdge( edge.getFromVertex(), edge.getToVertex(), edge );
//			final DirectedEdge backwardsEdge = new DirectedEdge( edge.getToVertex(), edge.getFromVertex(), edge.getCapacity() );
//			backwardsEdge.setFlow( edge.getCapacity() );
//			residualGraph.addEdge( edge.getToVertex(), edge.getFromVertex(), backwardsEdge );
		}


		main:do
		{
			BellmanFordShortestPath<Vertex, DirectedEdge> pathFinder = new BellmanFordShortestPath<Vertex, DirectedEdge>( residualGraph, source, maxEdges );
			int n = 0;
			while (pathFinder.getPathEdgeList( sink ) == null)
            {
				if (n > 1000) {
					break main;
				}
                pathFinder = new BellmanFordShortestPath<Vertex, DirectedEdge>( residualGraph, source, ++maxEdges );
				n++;
            }

			final List<DirectedEdge> pathEdgeList = pathFinder.getPathEdgeList( sink );

			// Some edges can be infinite
			final int min = pathEdgeList.stream()
										.filter( e -> e.getCapacity() > 0 )
										.min( (o1, o2) -> Double.compare( o1.weight(), o2.weight() ) )
										.get()
									 	.weight();
			if (pathEdgeList.stream().anyMatch( directedEdge -> directedEdge.weight() == -1 ))
			{
				System.out.println();
			}
			totalFlow += min;
//			System.out.println("Min: "+min);
//			System.out.println("Total: "+totalFlow);
			for (DirectedEdge forwardEdge : pathEdgeList)
            {
				final DirectedEdge backwardsEdge = getBackwardsEdge( residualGraph, forwardEdge );
				forwardEdge.setFlow( min );
				if (forwardEdge.weight() == 0)
				{
					residualGraph.removeEdge( forwardEdge );
				}
				backwardsEdge.setFlow( -min );
				int a = 2;
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
//		final DirectedGraph<Vertex, DirectedEdge> g = new DirectedMultigraph<>( DirectedEdge.class );
//		final Vertex v0 = new Vertex( "0" );
//		final Vertex v1 = new Vertex( "1" );
//		final Vertex v2 = new Vertex( "2" );
//		final Vertex v3 = new Vertex( "3" );
//		g.addVertex( v0 );
//		g.addVertex( v1 );
//		g.addVertex( v2 );
//		g.addVertex( v3 );
//		vertices.add( v0 );
//		vertices.add( v1 );
//		vertices.add( v2 );
//		vertices.add( v3 );
////		g.addVertex( 4 );
////		g.addVertex( 5 );
//		g.addEdge( v0, v1, new DirectedEdge( v0, 0, v1, 1,20 ) );
//		g.addEdge( v0, v3, new DirectedEdge( v0, 0, v3, 3,10 ) );
//		g.addEdge( v1, v2, new DirectedEdge( v1, 1, v2, 2,10 ) );
//		g.addEdge( v1, v3, new DirectedEdge( v1, 1, v3,3,30 ) );
//		g.addEdge( v3, v2, new DirectedEdge( v3,3,v2,2,20 ) );
//		g.addEdge( v1, v0, new DirectedEdge( v1, 1, v0, 0, -1 ) );
////		g.addEdge( 0, 1, new DirectedEdge( 0,1,10 ) );
////		g.addEdge( 0, 2, new DirectedEdge( 0,2,10 ) );
////		g.addEdge( 1, 2, new DirectedEdge( 1,2,2 ) );
////		g.addEdge( 1, 3, new DirectedEdge( 1,3,4 ) );
////		g.addEdge( 1, 4, new DirectedEdge( 1,4,8 ) );
////		g.addEdge( 2, 4, new DirectedEdge( 2,4,9 ) );
////		g.addEdge( 3, 5, new DirectedEdge( 3,5,10 ) );
////		g.addEdge( 4, 3, new DirectedEdge( 4,3,6 ) );
////		g.addEdge( 4, 5, new DirectedEdge( 4,5,10 ) );

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
