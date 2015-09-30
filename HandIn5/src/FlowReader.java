import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlowReader {

	public final ArrayList<Vertex> vertices = new ArrayList<>();

	private final File f;
	private final DirectedGraph<Vertex, DirectedEdge> graph;

	public FlowReader(File f) throws Exception
	{
		this.f = f;
		this.graph = parseFile( f );
	}

	public DirectedGraph<Vertex, DirectedEdge> parseFile(File f) throws Exception {
		DirectedGraph<Vertex, DirectedEdge> g = new DirectedMultigraph<>( DirectedEdge.class );

		int verticeCount, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			verticeCount = Integer.parseInt( s.next() );
			while( s.hasNext() && verticeCount > 0) {
				String name = s.next();
				final Vertex vertex = new Vertex( name );
				vertices.add( vertex );
				verticeCount--;
				g.addVertex( vertex );
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				int from = s.nextInt();
				int to = s.nextInt();
				int value = s.nextInt();
				final Vertex fromVertex = vertices.get( from );
				final Vertex toVertex = vertices.get( to );
				g.addEdge( fromVertex, toVertex, new DirectedEdge( fromVertex, toVertex, value ) );
				g.addEdge( toVertex, fromVertex, new DirectedEdge( toVertex, fromVertex, value ) );
				edges--;
			}
		}
		return g;
	}

	public int maxFlow(int sourceIndex, int sinkIndex)
	{
		Vertex source = vertices.get( sourceIndex );
		Vertex sink = vertices.get( sinkIndex );
		int totalFlow = 0;
		int maxEdges = 1;

		DirectedGraph<Vertex, DirectedEdge> residualGraph = new DirectedMultigraph<>( DirectedEdge.class );
		graph.vertexSet()
		 	 .forEach( residualGraph::addVertex );
		for (DirectedEdge edge : graph.edgeSet())
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
		FlowReader f1 = new FlowReader( new File( "flow-data/rail.txt" ) );
//		FlowReader f2 = new FlowReader( new File( "flow-data/rail_both-lowered-10.txt" ) );
//		FlowReader f3 = new FlowReader( new File( "flow-data/rail_both-lowered-20.txt" ) );
		f1.setSpecificCapacityOnEdge( 20, 21, 10 );
		f1.setSpecificCapacityOnEdge( 20, 23, 10 );
		System.out.println( "Both lowered -> 10: "+f1.maxFlow( 0, 54 ));

		f1.setSpecificCapacityOnEdge( 20, 21, 20 ); // 4W-48
		f1.setSpecificCapacityOnEdge( 20, 23, 20 ); // 4W-49
		System.out.println( "Both lowered -> 20: "+f1.maxFlow( 0, 54 ));

		f1.setSpecificCapacityOnEdge( 20, 21, 20 );
		f1.setSpecificCapacityOnEdge( 20, 23, 30 );
		System.out.println( "4W-48 -> 20: "+f1.maxFlow( 0, 54 ));

		f1.setSpecificCapacityOnEdge( 20, 21, 10 );
		f1.setSpecificCapacityOnEdge( 20, 23, 30 );
		System.out.println( "4W-48 -> 10: "+f1.maxFlow( 0, 54 ));

		f1.setSpecificCapacityOnEdge( 20, 21, 30 );
		f1.setSpecificCapacityOnEdge( 20, 23, 20 );
		System.out.println( "4W-49 -> 20: "+f1.maxFlow( 0, 54 ));

		f1.setSpecificCapacityOnEdge( 20, 21, 30 );
		f1.setSpecificCapacityOnEdge( 20, 23, 10 );
		System.out.println( "4W-49 -> 10: "+f1.maxFlow( 0, 54 ));
//		System.out.println( f2.maxFlow( 0, 54 ) );
//		System.out.println( f3.maxFlow( 0, 54 ) );
//		System.out.println( maxFlow( g1, vertices.get( 0 ), vertices.get( 54 ) ) );
//		System.out.println( maxFlow( g2, vertices.get( 0 ), vertices.get( 54 ) ) );
//		System.out.println( maxFlow( g3, vertices.get( 0 ), vertices.get( 54 ) ) );
	}

	public void setSpecificCapacityOnEdge(int sourceIndex, int targetIndex, int newCapacity)
	{
		final Vertex source = vertices.get( sourceIndex );
		final Vertex target = vertices.get( targetIndex );
		final DirectedEdge forwardEdge = graph.getEdge( source, target );
		final DirectedEdge backwardsEdge = graph.getEdge( target, source );

		final DirectedEdge newForward = new DirectedEdge( forwardEdge.getFromVertex(), forwardEdge.getToVertex(), newCapacity );
		final DirectedEdge newBackwards = new DirectedEdge( backwardsEdge.getFromVertex(), backwardsEdge.getToVertex(), newCapacity );
		graph.removeEdge( forwardEdge );
		graph.removeEdge( backwardsEdge );
		graph.addEdge( newForward.getFromVertex(), newForward.getToVertex(), newForward );
		graph.addEdge( newBackwards.getFromVertex(), newBackwards.getToVertex(), newBackwards );
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
