/******************************************************************************
 *  Compilation:  javac DirectedEdge.java
 *  Execution:    java DirectedEdge
 *  Dependencies: StdOut.java
 *
 *  Immutable weighted directed edge.
 *
 ******************************************************************************/

/**
 *  The <tt>DirectedEdge</tt> class represents a weighted edge in an 
 *  {@link EdgeWeightedDigraph}. Each edge consists of two integers
 *  (naming the two vertices) and a real-value weight. The data type
 *  provides methods for accessing the two endpoints of the directed edge and
 *  the weight.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class DirectedEdge { 
    private final int from;
    private final int to;
    private final int capacity;
    private final FlowReader.Vertex vn, wn;
    private int flow;

    /**
     * Initializes a directed edge from vertex <tt>from</tt> to vertex <tt>to</tt> with
     * the given <tt>weight</tt>.
     * @param v the tail vertex
     * @param w the head vertex
     * @param capacity the weight of the directed edge
     * @throws IndexOutOfBoundsException if either <tt>from</tt> or <tt>to</tt>
     *    is a negative integer
     * @throws IllegalArgumentException if <tt>weight</tt> is <tt>NaN</tt>
     */
    public DirectedEdge(int v, int w, int capacity) {
        this(new FlowReader.Vertex( "" ), v, new FlowReader.Vertex( "" ), w, capacity);
    }

    public DirectedEdge(FlowReader.Vertex from, FlowReader.Vertex to, int capacity) {
        this(from, 0, to, 0, capacity);
    }

    public DirectedEdge(FlowReader.Vertex vn, int v, FlowReader.Vertex wn, int w, int capacity) {
        if (v < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");
        if (w < 0) throw new IndexOutOfBoundsException("Vertex names must be nonnegative integers");

        this.vn = vn;
        this.wn = wn;
        if (Double.isNaN(capacity)) throw new IllegalArgumentException("Weight is NaN");
        this.from = v;
        this.to = w;
        this.capacity = capacity;
        this.flow = 0;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return from;
    }

    /**
     * Returns the head vertex of the directed edge.
     * @return the head vertex of the directed edge
     */
    public int to() {
        return to;
    }

    /**
     * Returns the weight of the directed edge.
     * @return the weight of the directed edge
     */
    public int weight() {
        return capacity == -1 ? -1 : capacity-flow;
    }

    /**
     * Returns a string representation of the directed edge.
     * @return a string representation of the directed edge
     */
    public String toString() {
        return String.format( "%s (%s) -> %s (%s) [ %d / %d ]", from, vn.getName(), to, wn.getName(), flow, capacity);
    }

    public int getCapacity()
    {
        return capacity;
    }

    public void setFlow(int flow)
    {
        this.flow += flow;
    }

    public int getFlow()
    {
        return flow;
    }

    public DirectedEdge getFlippedVersion()
    {
        final DirectedEdge directedEdge = new DirectedEdge( vn, to(), wn, from(), capacity );
        directedEdge.flow = 0;
        return directedEdge;
    }

    public FlowReader.Vertex getFromVertex()
    {
        return vn;
    }

    public FlowReader.Vertex getToVertex()
    {
        return wn;
    }
}

/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/