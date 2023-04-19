package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DAO.NodeDAOImpl;
import edu.wpi.teamb.DBAccess.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

public class Node {
    private int nodeID;
    private int oldNodeID;
    private int xCoord;
    private int yCoord;
    private String floor;
    private int floorNum;
    private String building;
    private Set<Edge> connectedEdges;
    private Double cost; // Alex added a cost value we can mess with to the nodes for A* pathfinding, we may change this implementation later
    private ArrayList<Integer> neighborIds;
    //private Map<String, Edge> connectedEdges;

    private String nodeType = null; // implemented here for elevator vs. stair weighting

    /**
     * Creates a node with empty values
     */
    public Node() {
        this.nodeID = 0;
        this.oldNodeID = 0;
        this.xCoord = 0;
        this.yCoord = 0;
        this.floor = "";
        this.setFloorNum();
        this.building = "";
        this.connectedEdges = new HashSet<>();
        this.cost = 10000.0; // Here's that pesky cost thing again
        //this.connectedEdges = new HashMap<>();
    }

    /**
     * Creates a node from the given parameters
     *
     * @param ID       the node id
     * @param xCoord   the x coordinate
     * @param yCoord   the y coordinate
     * @param floor    the floor
     * @param building the building
     */
    public Node(int ID, int xCoord, int yCoord, String floor, String building) {
        this.nodeID = ID;
        this.oldNodeID = ID;//here to keep track of the old nodeID before we update the db
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.floor = floor;
        this.setFloorNum();
        this.building = building;
        this.cost = 10000.0;
        //this.connectedEdges = new HashMap<>();
        this.connectedEdges = new HashSet<>();
    }

    /**
     * Creates a node from a result set
     *
     * @param rs the result set to create the node from
     * @throws java.sql.SQLException if the result set is empty
     */
    public Node(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getInt("nodeid"),
                rs.getInt("xcoord"),
                rs.getInt("ycoord"),
                rs.getString("floor"),
                rs.getString("building"));
    }

    /**
     * Makes a node from the given nodeID
     *
     * @param nodeID the nodeID to make the node from
     * @return the node with the given nodeID. Returns null if the nodeID is not
     * found
     */

    public static Node getNode(int nodeID) {
        ResultSet rs = DB.getRowCond("Nodes", "*", "nodeID = " + nodeID + "");
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                Node returnNode = new Node(rs);
                rs.close();
                returnNode.setFloorNum();
                return returnNode;
            } else
                rs.close();
            throw new SQLException("No rows found");
        } catch (SQLException e) {
            // handel error

            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("ERROR Query Failed: " + e.getMessage());
            }
        }

    }

    public static Node nodeFill(int nodeID) throws SQLException {
        Node node = Node.getNode(nodeID);
        node.setNeighborIds(node.getNeighborIds());
        return node;
    }

    public void setFloorNum(){
        // turns the floor string into a number for 3D algo cost finding
        int weight = 150;
        if(Objects.equals("",this.floor)){
            this.floorNum = -1;
        }
        else if(Objects.equals("L2",this.floor)){
            this.floorNum = 0*weight;
        }
        else if(Objects.equals("L1",this.floor)){
            this.floorNum = 1*weight;
        }
        else if(Objects.equals("L",this.floor)){
            this.floorNum = 2*weight;
        }
        else if(Objects.equals("1",this.floor)){
            this.floorNum = 3*weight;
        }
        else if(Objects.equals("2",this.floor)){
            this.floorNum = 4*weight;
        }
        else if(Objects.equals("3",this.floor)) {
            this.floorNum = 5* weight;
        }
    }

    // Getters and Setters

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public String getFloor() {
        return floor;
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public double getCost() {
        return cost;
    } // alex wrote these functions I just merged them badly

    public void setCost(Double cost) {this.cost  = cost;} // alex wrote these functions I just merged them badly
    public ArrayList<Integer> getNeighborIds(){return this.neighborIds;}
    public void setNeighborIds(ArrayList<Integer> neighborIds) {this.neighborIds  = neighborIds;}

    public String getNodeType() {
        return nodeType;
    }

    // this function kinda sucks I also only want to run it when we're doing shitty pathfinding
    // so it needs to be called on every node.
    public static String getNodeType(int nodeID){
        String name = DB.getShortNameFromNodeID(nodeID);
        if(name.contains("Stair")){
            return "STAI";
        }
        else if(name.contains("Elevator")){
            return "ELAV";
        }
        else if(name.contains("Hallway")){ // this is also trash
            return "HALL";
        }
        else {
            return "OTHE"; // this is trash
        }
    }

    public Set<Edge> getConnectedEdges() {
        return connectedEdges;
    }
    @Override
    public String toString(){
        return "NodeID: " + this.nodeID + " X: " + this.xCoord + " Y: " + this.yCoord + " Floor: " + this.floor + " Building: " + this.building;
    }
}