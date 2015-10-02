import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BellmanFordShortestPath;
import org.jgrapht.graph.*;

import java.io.File;
import java.util.*;

public class FlowReader {
	public final ArrayList<Integer> vertices = new ArrayList<>();

	private final Set<Integer> setA = new HashSet<>(  );
	private final SimpleWeightedGraph<Integer, FlowEdge> graph;

	private int totalFlow = 0;
	private int maxEdges = 1;

	public FlowReader(File f) throws Exception
	{
		this.graph = parseFile( f );
	}

	public SimpleWeightedGraph<Integer, FlowEdge> parseFile(File f) throws Exception {
		SimpleWeightedGraph<Integer, FlowEdge> g = new SimpleWeightedGraph<>( FlowEdge.class );

		int verticeCount, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			verticeCount = Integer.parseInt( s.next() );
			int i = 0;
			while( s.hasNext() && verticeCount > 0) {
				vertices.add( i );
				verticeCount--;
				g.addVertex( i );
				i++;
				s.next();
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				int from = s.nextInt();
				int to = s.nextInt();
				int value = s.nextInt();
				final Integer fromVertex = vertices.get(from);
				final Integer toVertex = vertices.get(to);
				g.addEdge( fromVertex, toVertex, new FlowEdge( fromVertex, toVertex, value ) );
				edges--;
			}
		}
		return g;
	}

	public int maxFlow(int sourceIndex, int sinkIndex)
	{
		final int numberOfVerticesInGraph = graph.vertexSet()
											  .size();

		Integer source = vertices.get( sourceIndex );
		Integer sink = vertices.get( sinkIndex );

		DirectedGraph<Integer, FlowEdge> residualGraph = new DirectedMultigraph<>( FlowEdge.class );
		graph.vertexSet()
		 	 .forEach( residualGraph::addVertex );
		for (FlowEdge edge : graph.edgeSet())
		{
			residualGraph.addEdge( edge.from, edge.to, edge );
			residualGraph.addEdge( edge.to, edge.from, new FlowEdge( edge.to, edge.from, edge.capacity ) );
		}

		main:do
		{
			BellmanFordShortestPath<Integer, FlowEdge> pathFinder = new BellmanFordShortestPath<>( residualGraph, source, maxEdges );
			while (pathFinder.getPathEdgeList( sink ) == null)
            {
				if (maxEdges > numberOfVerticesInGraph) {
					break main; // We can't visit the same node twice
				}
                pathFinder = new BellmanFordShortestPath<>( residualGraph, source, ++maxEdges );
			}

			final List<FlowEdge> shortestPath = pathFinder.getPathEdgeList( sink );

			// Some edges can be infinite
			final int bottleneck = shortestPath.stream()
											   .filter( e -> !e.isInfinite )
											   .min( (o1, o2) -> Double.compare( o1.weight(), o2.weight() ) )
											   .get()
											   .weight();
			totalFlow += bottleneck;
			for (FlowEdge forwardEdge : shortestPath)
            {
				final FlowEdge backwardsEdge = getBackwardsEdge( residualGraph, forwardEdge );
				forwardEdge.flow += bottleneck;
				if (forwardEdge.weight() == 0)
				{
					residualGraph.removeEdge( forwardEdge );
				}
				int flow = -bottleneck;
				backwardsEdge.flow += flow;
			}
		} while (true);

		findMinimumCut( residualGraph, source );
		setA.forEach( i -> graph.edgesOf( i )
								.stream()
								.filter( e -> !(setA.contains( e.from ) && setA.contains( e.to )) ) // Where both end are NOT in A*
								.forEach( e -> System.out.printf( "%d %d %d\n", e.from, e.to, e.capacity ) ) );

		return totalFlow;
	}

	private FlowEdge getBackwardsEdge(DirectedGraph<Integer, FlowEdge> graph, FlowEdge edge)
	{
		FlowEdge backwardsEdge = graph.getEdge( edge.to, edge.from );
		if (backwardsEdge == null)
		{
			backwardsEdge = new FlowEdge( edge.from, edge.to, edge.capacity );
			graph.addEdge( edge.to, edge.from, backwardsEdge );
		}
		return backwardsEdge;
	}

	private void findMinimumCut(DirectedGraph<Integer, FlowEdge> graph, Integer source)
	{
		if (setA.contains( source ))
		{
			return;
		}

		setA.add( source );
		graph.outgoingEdgesOf( source )
			 .stream()
			 .filter( e -> (e.weight() >= 0 || e.isInfinite) && !setA.contains( e.to ) )
			 .map( e -> e.to )
			 .forEach( i -> findMinimumCut( graph, i ) );
	}

	public static void main(String[] args) throws Exception {
		FlowReader f1 = new FlowReader( new File( "flow_data/rail.txt" ) );
		System.out.println( f1.maxFlow( 0, 54 ) );
	}

    private static class FlowEdge
	{
		public final int capacity;
		public final int from, to;
		public int flow;
		public final boolean isInfinite;

		public FlowEdge(int from, int to, int capacity) {
			this.from = from;
			this.to = to;
			this.capacity = capacity;
			this.flow = 0;
			this.isInfinite = capacity == -1;
		}

		public int weight() {
			return capacity-flow;
		}
	}
}
