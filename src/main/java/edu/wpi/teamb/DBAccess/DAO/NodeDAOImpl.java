package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.ORMs.Edge;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NodeDAOImpl implements IDAO {
    private ArrayList<Node> nodes;
    private ArrayList<FullNode> fullNodes;
    ArrayList<Node> getNodes() {
        return nodes;
    }
    ArrayList<FullNode> getFullNodes() {
        return fullNodes;
    }
    void setNodes(ArrayList<Node> newNodes) {
        nodes = newNodes;
    }

    public NodeDAOImpl() throws SQLException {
        nodes = getAllHelper();
        fullNodes = getFullNodesHelper();
    }

    /**
     * Gets a node by its ID
     *
     * @param id The ID of the node
     * @return The node with the given ID
     */
    public Node get(Object id) {
        int nodeID = (int) id;
        ResultSet rs = DButils.getRowCond("Nodes", "*", "nodeID = " + nodeID + "");
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                Node returnNode = new Node(rs);
                rs.close();
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

    /**
     * Gets all nodes
     *
     * @return A list of all nodes
     */
    @Override
    public ArrayList<Node> getAll() {
        return nodes;
    }

    /**
     * Sets all nodes
     */
    @Override
    public void setAll () { nodes = getAllHelper(); }

    /**
     * Adds a node
     *
     * @param n The node to add
     */
    @Override
    public void add(Object n) {
        Node node = (Node) n;
        insertDBNode(node);
        nodes.add(node);
    }

    /**
     * Deletes a node
     *
     * @param n The node to delete
     */
    @Override
    public void delete(Object n) {
        Node node = (Node) n;
        deleteDBNode(0, node);
        nodes.remove(node);
    }

    /**
     * Updates a node
     *
     * @param n The node to update
     */
    @Override
    public void update(Object n) {
        Node node = (Node) n;
        String[] values = {node.getNodeID() + "", node.getxCoord() + "", node.getyCoord() + "", node.getFloor() + "", node.getBuilding()};
        updateRow(values);
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).getNodeID() == node.getNodeID()) {
                nodes.set(i, node);
            }
        }
    }

    /**
     * Gets all nodes
     *
     * @return A list of all nodes
     */
    public ArrayList<Node> getAllHelper() {
        ArrayList<Node> nds = new ArrayList<Node>();
        try {
            ResultSet rs = DButils.getCol("nodes", "*");
            while (rs.next()) {
                nds.add(new Node(rs));
            }
            return nds;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.getAllHelper': " + e.getMessage());
        }
        return nds;
    }

    /**
     * Searches through the database for the row(s) that matches the given column
     * and value
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowFromCol(String col, String value) {
        return DButils.getRowCond("Nodes", "*", col + " = " + value);
    }

    /**
     * Gets all nodes
     *
     * @return A list of all nodes
     */
    public ResultSet getDBRowAllNodes() {
        return DButils.getRowCond("Nodes", "*", "TRUE");
    }

    /**
     * Gets the row from the database that matches the given nodeID
     *
     * @param nodeID the nodeID to search for
     * @return the result set of the row that matches the given nodeID
     */
    public ResultSet getBDRowNodeID(int nodeID) {
        return getDBRowFromCol("nodeID", "" + nodeID + "");
    }

    /**
     * Gets the row(s) from the database that matches the given x coordinate
     *
     * @param xCoord the x coordinate to search for
     * @return the result set of the row that matches the given x coordinate
     */
    public ResultSet getBDRowXCoord(int xCoord) {
        return getDBRowFromCol("xCoord", "" + xCoord + "");
    }

    /**
     * Gets the row(s) from the database that matches the given y coordinate
     *
     * @param yCoord the y coordinate to search for
     * @return the result set of the row that matches the given y coordinate
     */
    public ResultSet getBDRowYCoord(int yCoord) {
        return getDBRowFromCol("yCoord", "" + yCoord + "");
    }

    /**
     * Gets the row(s) from the database that matches the given floor
     *
     * @param floor the floor to search for
     * @return the result set of the row that matches the given floor
     */
    public ResultSet getBDRowFloor(String floor) {
        return getDBRowFromCol("floor", "'" + floor + "'");
    }

    /**
     * Gets the row(s) from the database that matches the given floor
     *
     * @param floor the floor to search for
     * @return the result set of the row that matches the given floor
     */
    public ArrayList<Node> getNodesFromFloor(String floor) {
        ResultSet rs = getBDRowFloor(floor);
        ArrayList<Node> nds = new ArrayList<Node>();
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                nds.add(new Node(rs));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return nds;
    }

    /**
     * Gets the row(s) from the database that matches the given building
     *
     * @param building the building to search for
     * @return the result set of the row that matches the given building
     */
    public ResultSet getBDRowBuilding(String building) {
        return getDBRowFromCol("building", "'" + building + "'");
    }


    /**
     * Updates the database with the information in this node object
     *
     * @param value the values to update
     */
    private void updateRow(String value[]) {
        String col[] = {"xCoord", "yCoord", "floor", "building"};
        String val[] = {value[1], value[2], value[3], value[4]};
        if (col == null || value == null)
            throw new IllegalArgumentException("The column and value arrays must be the same length");
        DButils.updateRow("Nodes", col, val, "nodeID = '" + value[0] + "'");
    }

    /**
     * Deletes the row in the database that matches the nodeID of this node object
     *
     * @param confirm 0 to confirm, anything else to cancel
     */
    public void deleteDBNode(int confirm, Node n) {
        if (confirm == 0)
            DButils.deleteRow("Nodes", "nodeID = '" + n.getNodeID() + "'");
        else {
            System.out.println("Delete cancelled");
        }
    }

    /**
     * Returns a string of all the information about the node
     *
     * @return String of all the information about the node
     */
    public String toString(Node node) {
        return node.getNodeID() + ", " + node.getxCoord() + ", " + node.getyCoord() + ", " + node.getFloor() + ", "
                + node.getBuilding();
    }

    /**
     * Inserts a node into the database
     *
     * @param n the node to insert
     */
    public void insertDBNode(Node n) {
        String col[] = {"nodeID", "xCoord", "yCoord", "floor", "building"};
        String value[] = {Integer.toString(n.getNodeID()), Integer.toString(n.getxCoord()), Integer.toString(n.getyCoord()),
                n.getFloor(), n.getBuilding()};
        DButils.insertRow("Nodes", col, value);
    }

    /**
     * Resets the node, location names, and moves tables using the backup tables
     */
    public void resetNodesFromBackup() {

        String dropMove = "DROP TABLE IF EXISTS Moves";
        String dropLocationNames = "DROP TABLE IF EXISTS LocationNames";
        String dropNodes = "DROP TABLE IF EXISTS Nodes";

        Statement dropStatement = null;

        try {

            dropStatement = DBConnection.getDBconnection().getConnection().createStatement();
            dropStatement.executeUpdate(dropMove);
            dropStatement.executeUpdate(dropLocationNames);
            dropStatement.executeUpdate(dropNodes);
            dropStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String recreateAll =
                """
                CREATE TABLE nodes(nodeID INT, xcoord INT, ycoord INT, floor VARCHAR(255), building varchar(255), primary key (nodeID));

                CREATE TABLE locationnames(longName VARCHAR(255), shortName VARCHAR(255), nodetype VARCHAR(255), primary key (longName));
                
                CREATE TABLE moves (nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date),
                constraint fk_nodeID foreign key(nodeID) references nodes(nodeID),
                constraint fk_longName foreign key(longName) references locationnames(longName));
                """;

        String recreateNodes = "INSERT INTO Nodes SELECT * FROM NodeBackup";
        String recreateLocationNames = "INSERT INTO LocationNames SELECT * FROM LocationNameBackup";
        String recreateMove = "INSERT INTO Moves SELECT * FROM MoveBackup";

        Statement recreateStatement = null;

        try {

            recreateStatement = DBConnection.getDBconnection().getConnection().createStatement();
            recreateStatement.executeUpdate(recreateAll);
            recreateStatement.executeUpdate(recreateNodes);
            recreateStatement.executeUpdate(recreateLocationNames);
            recreateStatement.executeUpdate(recreateMove);
            recreateStatement.close();

        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.resetNodesFromBackup': " + e.getMessage());
        }
    }

    /**
     * Gets all the neighbors of a given node
     *
     * @return an array list of all the neighbors of the given node
     */
    public ArrayList<Node> getNeighbors(int nodeID) throws SQLException {
        // set up an empty list
        ArrayList<Node> neighbors = new ArrayList<Node>();

        for (Edge e : EdgeDAOImpl.getEdges()) {
            if (e.getStartNodeID() == nodeID) {
                for (Node m : getNodes()) {
                    if (m.getNodeID() == e.getEndNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
            if (e.getEndNodeID() == nodeID) {
                for (Node m : getNodes()) {
                    if (m.getNodeID() == e.getStartNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
        }
        return neighbors;
    }

    public ArrayList<Integer> getNeighbors1(int nodeID) {
        // set up an empty list
        ArrayList<Node> neighbors = new ArrayList<Node>();

        for (Edge e : EdgeDAOImpl.getEdges()) {
            if (e.getStartNodeID() == nodeID) {
                for (Node m : getNodes()) {
                    if (m.getNodeID() == e.getEndNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
            if (e.getEndNodeID() == nodeID) {
                for (Node m : getNodes()) {
                    if (m.getNodeID() == e.getStartNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
        }
        return nodeToIDs(neighbors);
    }

    /**
     * Returns a list of nodeIDs given a list of nodes
     *
     * @return an integer array list of nodeIDs
     */
    public ArrayList<Integer> nodeToIDs(ArrayList<Node> nodes) {
        ArrayList<Integer> nodeIDs = new ArrayList<Integer>();
        for (Node n : nodes) {
            nodeIDs.add(n.getNodeID());
        }
        return nodeIDs;
    }

    /**
     * Instantiates all local nodes using the database
     */
    public ArrayList<Node> instantiateNodes() {
        ArrayList<Node> nodes = new ArrayList<Node>();

        try {
            String query = "SELECT * FROM Nodes";
            Statement stmt = DBConnection.getDBconnection().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Node n = new Node(rs.getInt("nodeID"), rs.getInt("xCoord"), rs.getInt("yCoord"), rs.getString("floor"), rs.getString("building"));
                nodes.add(n);
            }
            rs.close();
            stmt.close();
            return nodes;
        } catch (SQLException e) {
            System.out.println("ERROR Query Failed in method 'NodeDAOImpl.instantiateNodes': " + e.getMessage());
        }
        return nodes;
    }

    /**
     * Updates a certain node in the database
     */
    public void updateEditedNode(Node node, int newNodeID, int newXCoord, int newYCoord, String newFloor, String newBuilding) {
        node.setNodeID(newNodeID);
        node.setxCoord(newXCoord);
        node.setyCoord(newYCoord);
        node.setFloor(newFloor);
        node.setBuilding(newBuilding);

        String[] col = { "nodeID", "xCoord", "yCoord", "floor", "building" };
        String[] value = { Integer.toString(node.getNodeID()), Integer.toString(node.getxCoord()), Integer.toString(node.getyCoord()),
                "'" + node.getFloor() + "'", "'" + node.getBuilding() + "'" };
        DButils.updateRow("Nodes", col, value, "nodeID = " + Integer.toString(node.getNodeID()));
    }

    /**
     * Gets the short name of a node given its nodeID
     *
     * @param nodeID the nodeID of the node
     * @return the short name of the node
     */
    public String getShortName(int nodeID) {
        return getShortNameFromNodeID(nodeID);
    }

    /**
     * A helper function for getting a list of full nodes
     *
     * @return the long name of the node
     */
    public ArrayList<FullNode> getFullNodesHelper() {
        ArrayList<FullNode> fns = new ArrayList<>();
        try {
            ResultSet rs = joinFullNodes();
            while (rs.next()) {
                fns.add(new FullNode(rs));
            }
            return fns;
        } catch (SQLException e) {
            System.out.println("ERROR Query Failed in method 'NodeDAOImpl.getFullNodesHelper': " + e.getMessage());
        }
        return fns;
    }

    /**
     * A helper function for getting a FullNode object given its nodeID
     *
     * @param nodeID the nodeID of the node
     * @return a FullNode object
     */
    public FullNode getFullNode(int nodeID) {
        for (int i = 0; i < fullNodes.size(); i++) {
            if (fullNodes.get(i).getNodeID() == nodeID) {
                return fullNodes.get(i);
            }
        }
        return null;
    }

    /**
     * A helper function for getting a list of node neighbors given a list of nodes
     *
     * @param floorNodes the list of nodes on a certain floor
     * @return the long name of the node
     */
    public ArrayList<ArrayList<Integer>> nodeNeighborIDs(ArrayList<Node> floorNodes) {
        ArrayList<ArrayList<Integer>> nodesNeighborids = new ArrayList<>();
        for (Node n : floorNodes) {
            ArrayList<Integer> neighborIDs = getNeighbors1(n.getNodeID());
            nodesNeighborids.add(neighborIDs);
        }
        return nodesNeighborids;
    }

    /**
     * Gets the node type of a node given its nodeID
     *
     * @param n the Node object
     * @return the node type of the node
     */
    public String getNodeType (Node n) {
        String nodeType = null;
        String longName = null;
        for (Move move : MoveDAOImpl.moves) {
            if (n.getNodeID() == move.getNodeID()) {
                longName = move.getLongName();
            }
        }
        for (LocationName locationName : LocationNameDAOImpl.locationNames) {
            if (longName.equals(locationName.getLongName())) {
                nodeType = locationName.getNodeType();
            }
        }
        return nodeType;
    }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @return a ResultSet containing the full nodes table joined with the moves table and the locationnames table
     */
    public static ResultSet joinFullNodes() {
        try{
            Statement stmt = DBConnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT * FROM nodes, moves, locationnames WHERE nodes.nodeid = moves.nodeid AND moves.longname = locationnames.longname";
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.joinFullNode's: " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @param nodeID the nodeID to get the longName from
     * @return the longName of the node
     */
    public static String getLongNameFromNodeID(int nodeID) {
        try {
            Statement stmt = DBConnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT * from nodes join moves m on nodes.nodeid = m.nodeid where nodes.nodeid = " + nodeID;
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String set = rs.getString("longName");
            rs.close();
            return set;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.getLongNameFromNodeID': " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @param nodeID NodeID to get shortname from
     * @return a String with the shortname associated with the given NodeID
     */
    public static String getShortNameFromNodeID(int nodeID) {
        try {
            String longName = getLongNameFromNodeID(nodeID);
            Statement stmt = DBConnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT shortname from locationnames join moves m on locationnames.longname = m.longname";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            String set = rs.getString("shortname");
            rs.close();
            return set;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.getShortNameFromNodeID': " + e.getMessage());
            return null;
        }
    }

    /**
     * Makes a node from the given nodeID
     *
     * @param nodeID the nodeID to make the node from
     * @return the node with the given nodeID. Returns null if the nodeID is not
     * found
     */
    public static Node getNode(int nodeID) {
        ResultSet rs = DButils.getRowCond("Nodes", "*", "nodeID = " + nodeID + "");
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
            System.err.println("ERROR Query Failed in method : " + e.getMessage());
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                System.err.println("ERROR Query Failed in method : " + e.getMessage());
            }
        }
    }

    /**
     * 'Fills' a node with its neighbors
     *
     * @param nodeID the nodeID to get the neighbors from
     * @return a 'filled' Node object with the given nodeID
     */
    public static Node nodeFill(int nodeID) {
        Node node = getNode(nodeID);;
        node.setNeighborIds(node.getNeighborIds());
        return node;
    }
}


