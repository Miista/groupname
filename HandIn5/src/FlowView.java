import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.layout.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DirectedMultigraph;

public class FlowView extends JFrame {

	JScrollPane ph;
	JGraphModelAdapter<FlowReader.Vertex, DirectedEdge> jgma;
	JGraph jgraph;	

	public FlowView() throws Exception {
		setLayout( new BorderLayout() );

		DirectedGraph<FlowReader.Vertex, DirectedEdge> gg;
		// create a JGraphT graph
//		gg = FlowReader.parseFile( new File( "flow-data/rail.txt" ) );

		gg = new DirectedMultigraph<>( DirectedEdge.class );
		final FlowReader.Vertex v0 = new FlowReader.Vertex( "0" );
		final FlowReader.Vertex v1 = new FlowReader.Vertex( "1" );
		final FlowReader.Vertex v2 = new FlowReader.Vertex( "2" );
		final FlowReader.Vertex v3 = new FlowReader.Vertex( "3" );
		gg.addVertex( v0 );
		gg.addVertex( v1 );
		gg.addVertex( v2 );
		gg.addVertex( v3 );
		gg.addEdge( v0, v1, new DirectedEdge( v0, 0, v1, 1,20 ) );
		gg.addEdge( v0, v3, new DirectedEdge( v0, 0, v3, 3,10 ) );
		gg.addEdge( v1, v2, new DirectedEdge( v1, 1, v2, 2,10 ) );
		gg.addEdge( v1, v3, new DirectedEdge( v1, 1, v3,3,30 ) );
		gg.addEdge( v3, v2, new DirectedEdge( v3,3,v2,2,20 ) );
		gg.addEdge( v1, v0, new DirectedEdge( v1, 1, v0, 0, -1 ) );

		// create a visualization using JGraph, via the adapter

		jgma = new JGraphModelAdapter<FlowReader.Vertex, DirectedEdge>( gg );
		JGraph jgraph = new JGraph( jgma );
		ph = new JScrollPane(jgraph);

		this.add(ph, BorderLayout.CENTER);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setVisible(true);

	     List roots = new ArrayList(); 
	     Iterator vertexIter = gg.vertexSet().iterator(); 
	     while (vertexIter.hasNext()) { 
	         Object vertex = vertexIter.next(); 
	         if (gg.inDegreeOf( (FlowReader.Vertex) vertex ) == 0) {
	             roots.add(jgma.getVertexCell(vertex)); 
	         } 
	     } 
		JGraphLayoutAlgorithm layout = new SpringEmbeddedLayoutAlgorithm();
		layout.applyLayout(jgraph, roots.toArray(), layout); 

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		FlowReader.NF( gg , v0, v2 );

	}

	public static void main(String[] args) throws Exception {
		new FlowView();
	}

}
