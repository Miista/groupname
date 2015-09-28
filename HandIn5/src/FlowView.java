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
import org.jgraph.layout.JGraphLayoutAlgorithm;
import org.jgraph.layout.SugiyamaLayoutAlgorithm;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.JGraphModelAdapter;

public class FlowView extends JFrame {

	JScrollPane ph;
	JGraphModelAdapter<Integer, DirectedEdge> jgma;
	JGraph jgraph;	

	public FlowView() throws Exception {
		setLayout( new BorderLayout() );

		DirectedGraph<Integer, DirectedEdge> gg;
		// create a JGraphT graph
		gg = FlowReader.parseFile( new File( "flow-data/rail.txt" ) );

		// create a visualization using JGraph, via the adapter

		jgma = new JGraphModelAdapter<Integer, DirectedEdge>( gg );
		JGraph jgraph = new JGraph( jgma );
		ph = new JScrollPane(jgraph);

		this.add(ph, BorderLayout.CENTER);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setVisible(true);

	     List roots = new ArrayList(); 
	     Iterator vertexIter = gg.vertexSet().iterator(); 
	     while (vertexIter.hasNext()) { 
	         Object vertex = vertexIter.next(); 
	         if (gg.inDegreeOf((Integer) vertex) == 0) { 
	             roots.add(jgma.getVertexCell(vertex)); 
	         } 
	     } 
		JGraphLayoutAlgorithm layout = new SugiyamaLayoutAlgorithm(); 
		layout.applyLayout(jgraph, roots.toArray(), layout); 

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		FlowReader.NF( gg , 0, 54 );

	}

	public static void main(String[] args) throws Exception {
		new FlowView();
	}

}
