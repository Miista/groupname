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
	
	public static void main(String[] args) throws Exception {
		EdgeWeightedDigraph g = parseFile( new File("flow_data/rail.txt") );
		System.out.println("Read V:" + g.V() + " E: " + g.E() );
	}

}
