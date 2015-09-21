import java.io.File;
import java.util.Scanner;

public class FlowReader {

	public static EdgeWeightedDigraph parseFile(File f) throws Exception {
		EdgeWeightedDigraph g;

		int vertices, edges;
		try( Scanner s = new Scanner(f, "UTF-8") ) {
			vertices = Integer.parseInt( s.next() );
			g = new EdgeWeightedDigraph( vertices );
			while( s.hasNext() && vertices > 0) {
				s.next();
				vertices--;
			}
			edges = Integer.parseInt( s.next() );
			while( s.hasNext() && edges > 0) {
				g.addEdge( new DirectedEdge( s.nextInt() , s.nextInt() , s.nextInt() ) );
				edges--;
			}
		}
		return g;
	}

	public static int NF(EdgeWeightedDigraph g)
	{
		int totalFlow = 0;

		ResidualGraph residualGraph = new ResidualGraph( g );
		while ( residualGraph.pathExistsFromTo( 0, 5 ) )
		{
			residualGraph.buildPaths( 0, 5 );
			final Path bestPath = residualGraph.getPath( 0, 5 );
			totalFlow += bestPath.bottleneckCapacity;
			residualGraph.actOnPath( bestPath );
		}

		return totalFlow;
	}

	public static void main(String[] args) throws Exception {
//		EdgeWeightedDigraph g = parseFile( new File("flow-data/rail.txt") );
		EdgeWeightedDigraph g = new EdgeWeightedDigraph( 6 );
		g.addEdge( new DirectedEdge( 0, 1, 10.0 ) );
		g.addEdge( new DirectedEdge( 0, 2, 10.0 ) );
		g.addEdge( new DirectedEdge( 1, 2, 2.0 ) );
		g.addEdge( new DirectedEdge( 1, 3, 4.0 ) );
		g.addEdge( new DirectedEdge( 1, 4, 8.0 ) );
		g.addEdge( new DirectedEdge( 2, 4, 9.0 ) );
		g.addEdge( new DirectedEdge( 3, 5, 10.0 ) );
		g.addEdge( new DirectedEdge( 4, 3, 6.0 ) );
		g.addEdge( new DirectedEdge( 4, 5, 10.0 ) );
		System.out.println( NF( g ) );
//		System.out.println("Read getVerticeCount:" + g.getVerticeCount() + " getEdgeCount: " + g.getEdgeCount() );
	}

}
