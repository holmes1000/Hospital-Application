package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Edge;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NodeDAOImpl implements IDAO {
    private static ArrayList<Node> nodes;
    private static ArrayList<FullNode> fullNodes;

    static ArrayList<Node> getNodes () {
        return nodes;
    }
    static ArrayList<FullNode> getFullNodes() {
        return fullNodes;
    }

    static void setNodes (ArrayList<Node> nodes) {
        NodeDAOImpl.nodes = nodes;
    }

    public NodeDAOImpl() throws SQLException {
        nodes = getAllHelper();
        fullNodes = getFullNodesHelper();
    }

    public Node get(Object id) {
        int nodeID = (int) id;
        ResultSet rs = DB.getRowCond("Nodes", "*", "nodeID = " + nodeID + "");
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
            ResultSet rs = DB.getCol("nodes", "*");
            while (rs.next()) {
                nds.add(new Node(rs));
            }
            return nds;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.getAllHelper': " + e.getMessage());
        }
        return nds;
    }
//    /**
//     * Sets the list of connected edges to the given map if already initialized
//     *
//     * @param connectedEdges Map of connected edges
//     */
//    public void setConnectedEdges(Set<Edge> connectedEdges, Node n) {
//        n.getConnectedEdges = connectedEdges;
//    }

//    /**
//     * Adds an edge to the list of connected edges
//     *
//     * @param edge Edge to be added
//     */
//    void addEdge(Edge edge) {
//        if (connectedEdges.contains(edge))
//            return;
//        connectedEdges.add(edge);
//    }

    // Access from Database Methods

    // Methods to get information about the node from the database

    /**
     * Searches through the database for the row(s) that matches the given column
     * and value
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("Nodes", "*", col + " = " + value);
    }

    public ResultSet getDBRowAllNodes() {
        return DB.getRowCond("Nodes", "*", "TRUE");
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


    // Method to Update the Database

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
        DB.updateRow("Nodes", col, val, "nodeID = '" + value[0] + "'");
    }

    // Method to Delete the Database

    /**
     * Deletes the row in the database that matches the nodeID of this node object
     *
     * @param confirm 0 to confirm, anything else to cancel
     */
    public void deleteDBNode(int confirm, Node n) {
        if (confirm == 0)
            DB.deleteRow("Nodes", "nodeID = '" + n.getNodeID() + "'");
        else {
            System.out.println("Delete cancelled");
        }
    }

    // list information about this node object

    /**
     * Returns a string of all the information about the node
     *
     * @return String of all the information about the node
     */
    public String toString(Node node) {
        return node.getNodeID() + ", " + node.getxCoord() + ", " + node.getyCoord() + ", " + node.getFloor() + ", "
                + node.getBuilding();
    }

    public void insertDBNode(Node n) {
        String col[] = { "nodeID", "xCoord", "yCoord", "floor", "building" };
        String value[] = { Integer.toString(n.getNodeID()), Integer.toString(n.getxCoord()), Integer.toString(n.getyCoord()),
                n.getFloor() , n.getBuilding() };
        DB.insertRow("Nodes", col, value);
    }

    public void resetNodesFromBackup() {

        DB.connectToDB();

        String dropMove = "DROP TABLE IF EXISTS Moves";
        String dropLocationNames = "DROP TABLE IF EXISTS LocationNames";
        String dropNodes = "DROP TABLE IF EXISTS Nodes";

        Statement dropStatement = null;

        try {

            dropStatement = DB.c.createStatement();
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

            recreateStatement = DB.c.createStatement();
            recreateStatement.executeUpdate(recreateAll);
            recreateStatement.executeUpdate(recreateNodes);
            recreateStatement.executeUpdate(recreateLocationNames);
            recreateStatement.executeUpdate(recreateMove);
            recreateStatement.close();

        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'NodeDAOImpl.resetNodesFromBackup': " + e.getMessage());
        }

        nodes = getAllHelper();
        Repository.getRepository().getAllLongNames();
        Repository.getRepository().getAllMoves();
    }

//    /**
//     * Returns a string of all the edges connected to this node
//     *
//     * @return String of all the edges connected to this node
//     */
//    public String listEdges() {
//        String str = "";
//        for (Edge edge : connectedEdges) {
//            str += edge.getStartNodeID() + " "+edge.getEndNodeID()+" ";
//        }
//        return str;
//    }

    public ArrayList<Node> getNeighbors(int nodeID) throws SQLException {
        // set up an empty list
        ArrayList<Node> neighbors = new ArrayList<Node>();

        for (Edge e : EdgeDAOImpl.getEdges()) {
            if (e.getStartNodeID() == nodeID) {
                for (Node m : NodeDAOImpl.getNodes()) {
                    if (m.getNodeID() == e.getEndNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
            if (e.getEndNodeID() == nodeID) {
                for (Node m : NodeDAOImpl.getNodes()) {
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
                for (Node m : NodeDAOImpl.getNodes()) {
                    if (m.getNodeID() == e.getEndNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
            if (e.getEndNodeID() == nodeID) {
                for (Node m : NodeDAOImpl.getNodes()) {
                    if (m.getNodeID() == e.getStartNodeID()) {
                        neighbors.add(m);
                    }
                }
            }
        }
        return nodeToIDs(neighbors);
    }

    public ArrayList<Integer> nodeToIDs(ArrayList<Node> nodes) {
        ArrayList<Integer> nodeIDs = new ArrayList<Integer>();
        for (Node n : nodes) {
            nodeIDs.add(n.getNodeID());
        }
        return nodeIDs;
    }

    public ArrayList<Node> instantiateNodes() throws SQLException {
        DB.connectToDB();
        String query = "SELECT * FROM Nodes";
        Statement stmt = DB.c.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<Node> nodes = new ArrayList<Node>();
        while (rs.next()) {
            Node n = new Node(rs.getInt("nodeID"), rs.getInt("xCoord"), rs.getInt("yCoord"), rs.getString("floor"), rs.getString("building"));
            nodes.add(n);
        }
        rs.close();
        stmt.close();
        return nodes;
    }

    public void updateEditedNode(Node node, int newNodeID, int newXCoord, int newYCoord, String newFloor, String newBuilding) {
        node.setNodeID(newNodeID);
        node.setxCoord(newXCoord);
        node.setyCoord(newYCoord);
        node.setFloor(newFloor);
        node.setBuilding(newBuilding);

        String[] col = { "nodeID", "xCoord", "yCoord", "floor", "building" };
        String[] value = { Integer.toString(node.getNodeID()), Integer.toString(node.getxCoord()), Integer.toString(node.getyCoord()),
                "'" + node.getFloor() + "'", "'" + node.getBuilding() + "'" };
        DB.updateRow("Nodes", col, value, "nodeID = " + Integer.toString(node.getNodeID()));
    }

    public String getShortName(int nodeID) {
        return DB.getShortNameFromNodeID(nodeID);
    }

    public ArrayList<FullNode> getFullNodesHelper() throws SQLException {
        ResultSet rs = DB.joinFullNodes();
        ArrayList<FullNode> fns = new ArrayList<>();
        while (rs.next()) {
            fns.add(new FullNode(rs));
        }
        return fns;
    }

    public FullNode getFullNode(int nodeID) {
        for (int i = 0; i < fullNodes.size(); i++) {
            if (fullNodes.get(i).getNodeID() == nodeID) {
                return fullNodes.get(i);
            }
        }
        return null;
    }
    public ArrayList<ArrayList<Integer>> nodeNeighborIDs(ArrayList<Node> floorNodes) {
        ArrayList<ArrayList<Integer>> nodesNeighborids = new ArrayList<>();
        for (Node n : floorNodes) {
            ArrayList<Integer> neighborIDs = getNeighbors1(n.getNodeID());
            nodesNeighborids.add(neighborIDs);
        }
        return nodesNeighborids;
    }

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
        Repository.getRepository().connectToDB();
        try{
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String query = "SELECT * FROM nodes, moves, locationnames WHERE nodes.nodeid = moves.nodeid AND moves.longname = locationnames.longname";
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.joinFullNode's: " + e.getMessage());
            return null;
        }
    }

//    public void addFullNode (Object n) {
//        Date current = Date.valueOf(LocalDate.now());
//        FullNode fullNode = (FullNode) n;
//        Node node = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding());
//        LocationNameDAOImpl.add(new LocationName(fullNode.getLongName(), fullNode.getShortName(), fullNode.getNodeType()));
//        MoveDAOImpl.add(new Move(fullNode.getNodeID(), fullNode.getLongName(), current));
//    }

}

// /**
//  * Gets the row(s) from the database that matches the given node type
//  *
//  * @param nodeType the node type to search for
//  * @return the result set of the row that matches the given node type
//  */
// public ResultSet getBDRowNodeType(String nodeType) {
//     return getDBRowFromCol("nodeType", "'" + nodeType + "'");
// }

// /**
//  * Gets the row(s) from the database that matches the given long name
//  *
//  * @param longName the long name to search for
//  * @return the result set of the row that matches the given long name
//  */
// public ResultSet getBDRowLongName(String longName) {
//     return getDBRowFromCol("longName", "'" + longName + "'");
// }

// /**
//  * Gets the row(s) from the database that matches the given short name
//  *
//  * @param shortName the short name to search for
//  * @return the result set of the row that matches the given short name
//  */
// public ResultSet getBDRowShortName(String shortName) {
//     return getDBRowFromCol("shortName", "'" + shortName + "'");
// }

//    /**
//     * Update the node type in the database
//     *
//     * @param nodeType the new node type
//     *
//     */
// public void updateNodeType(String nodeType) {
//     if (nodeType == null || nodeType.equals(""))
//         throw new IllegalArgumentException("The node type is null or empty");
//     String col[] = { "nodeType" };
//     String value[] = { "'" + nodeType + "'" };
//     updateRow(col, value);
// }

// /**
//  * Update the long name in the database
//  *
//  * @param longName the new long name
//  *
//  */
// public void updateLongName(String longName) {
//     if (longName == null || longName.equals(""))
//         throw new IllegalArgumentException("The long name is null or empty");
//     String col[] = { "longName" };
//     String value[] = { "'" + longName + "'" };
//     updateRow(col, value);
// }

// /**
//  * Update the short name in the database
//  *
//  * @param shortName the new short name
//  *
//  */
// public void updateShortName(String shortName) {
//     if (shortName == null || shortName.equals(""))
//         throw new IllegalArgumentException("The short name is null or empty");
//     String col[] = { "shortName" };
//     String value[] = { "'" + shortName + "'" };
//     updateRow(col, value);
// }


