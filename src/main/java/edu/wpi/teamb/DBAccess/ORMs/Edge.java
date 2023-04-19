package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class Edge {
    private int startNodeID;
    private Node startNode;
    private int endNodeID;
    private Node endNode;

    public String endpoints = startNodeID + "_" + endNodeID;

    /**
     * Creates a new edge object with empty values
     */
    public Edge() {
        this.startNodeID = 0;
        this.endNodeID = 0;
        endpoints = startNodeID + "_" + endNodeID;
    }

    /**
     * Creates a new edge object from the given parameters
     *
     * @param startNode the start node
     * @param endNode   the end node
     */

    public Edge(int startNode, int endNode) {
        this.startNodeID = startNode;
        this.endNodeID = endNode;
        endpoints = startNodeID + "_" + endNodeID;
    }

    /**
     * Creates a new edge object from a result set
     *
     * @param rs the result set to create the edge from
     */
    public Edge(ResultSet rs) {
        try {
            this.startNodeID = rs.getInt("startNode");
            this.endNodeID = rs.getInt("endNode");
            this.endpoints = startNodeID + "_" + endNodeID;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
        }
    }


    // Getters and Setters

    /**
     * Gets the start node id of the edge
     *
     * @return the start node id of the edge
     */
    public int getStartNodeID() {
        return startNodeID;
    }

    /**
     * Gets the end node id of the edge
     *
     * @return the end node id of the edge
     */
    public int getEndNodeID() {
        return endNodeID;
    }

    /**
     * Gets the start node of the edge
     *
     * @return the start node of the edge
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Sets the start node and the start node id and removes the edge from the old
     * start and adds it to the new start
     *
     * @param startNode the start node to set
     */
    public void setStartNode(Node startNode) {
        this.startNodeID = startNode.getNodeID();
        if (this.startNode != null)
            //this.startNode.getConnectedEdges().remove(this);
            this.startNode = startNode;
        //startNode.addEdge(this);
    }

    /**
     * Gets the end node of the edge
     *
     * @return the end node of the edge
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Sets the end node and the end node id and removes the edge from the old end
     * and adds it to the new end
     *
     * @param endNode the end node to set
     */
    public void setEndNode(Node endNode) {
        this.endNodeID = endNode.getNodeID();
        if (this.endNode != null)
            //this.endNode.getConnectedEdges().remove(this);
            this.endNode = endNode;
        //endNode.addEdge(this);
    }

    public Edge getEdge(String endpoints) {
        String[] endpointsArray = endpoints.split("_");
        ResultSet rs = DB.getRowCond("Edges", "*", "startnode = " + endpointsArray[0] + " AND endnode = " + endpointsArray[1]);
        try {
            if (rs.isBeforeFirst()) { // if there is something it found
                rs.next();
                return new Edge(rs); // make the edge
            } else
                throw new SQLException("No rows found");
        } catch (SQLException e) {
            // handle error

            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
    }
    @Override
    public String toString() {
        return "StartNodeID: " + endpoints + " EndNodeID: " + endNodeID;
    }
}

// Access from Database Methods

// /**
// * Gets the row from the database that matches he edgeid
// *
// * @param edgeid the edge id to search for, make sure there are NO single
// quotes
// * surrounding the edgeid
// * @return the row that matches the edgeid
// */
// public ResultSet getDBRowEdgeID(String edgeid) {
// return getRowFromCol("edgeid", "'" + edgeid + "'");
// }
