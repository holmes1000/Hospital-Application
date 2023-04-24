package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.DBinput;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import java.util.Map;

import static java.lang.Integer.parseInt;

public class Repository {
    private Repository() {
        try {
            dbConnection = DBconnection.getDBconnection();
            nodeDAO = new NodeDAOImpl();
            edgeDAO = new EdgeDAOImpl();
            locationNameDAO = new LocationNameDAOImpl();
            moveDAO = new MoveDAOImpl();
            userDAO = new UserDAOImpl();
            requestDAO = RequestDAOImpl.getRequestDaoImpl();
            conferenceRequestDAO = new ConferenceRequestDAOImpl();
            flowerRequestDAO = new FlowerRequestDAOImpl();
            mealRequestDAO = new MealRequestDAOImpl();
            furnitureRequestDAO = new FurnitureRequestDAOImpl();
            officeRequestDAO = new OfficeRequestDAOImpl();
            alertDAO = new AlertDAOImpl();
        } catch (SQLException e) {
            System.out.println("ERROR: Repository failed to initialize");
            throw new RuntimeException(e);
        }
    }

    private static class SingletonHelper {
        //Nested class is referenced after getRepository() is called
        private static final Repository repository = new Repository();
    }

    public static Repository getRepository() {
        return SingletonHelper.repository;
    }

    private final NodeDAOImpl nodeDAO;
    private final EdgeDAOImpl edgeDAO;
    private final LocationNameDAOImpl locationNameDAO;
    private final MoveDAOImpl moveDAO;
    private final UserDAOImpl userDAO;
    private final RequestDAOImpl requestDAO;
    private final ConferenceRequestDAOImpl conferenceRequestDAO;
    private final FlowerRequestDAOImpl flowerRequestDAO;
    private final MealRequestDAOImpl mealRequestDAO;
    private final FurnitureRequestDAOImpl furnitureRequestDAO;
    private final OfficeRequestDAOImpl officeRequestDAO;
    private final AlertDAOImpl alertDAO;
    private final DBconnection dbConnection;

    //TODO Node methods

    /**
     * Gets a node by its ID
     *
     * @param id The ID of the node
     * @return The node with the given ID
     */
    public Node get(Object id) {
        return nodeDAO.get(id);
    }

    /**
     * Gets all local Node objects
     *
     * @return an ArrayList of all local Node objects
     */
    public ArrayList<Node> getAllNodes() {
        return nodeDAO.getAll();
    }

    /**
     * Gets all local FullNode objects
     *
     * @return an ArrayList of all local FullNode objects
     */
    public ArrayList<FullNode> getAllFullNodes() {
        return nodeDAO.getAllFullNodes();
    }

    /**
     * Sets all Node objects using the database
     */
    public void setAllNodes () { nodeDAO.setAll(); }

    /**
     * Adds a Node object to the both the database and local list
     *
     * @param n the Node object to be added
     */
    public void addNode(Object n) { nodeDAO.add(n); }

    /**
     * Removes a Node from the both the database and the local list
     *
     * @param n the Node object to be removed
     */
    public void deleteNode(Object n) { nodeDAO.delete(n); }

    /**
     * Updates a Node object in both the database and local list
     *
     * @param n the Node object to be updated
     */
    public void updateNode(Object n) { nodeDAO.update(n); }

    /**
     * Gets all nodes from the database
     *
     * @return a list of all nodes
     */
    public ArrayList<Node> getAllNodesFromDB() { return nodeDAO.getAllHelper(); }

    /**
     * Searches through the database for the row(s) that matches the col and value in the Nodes table
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return A ResultSet of the row(s) that match the col and value
     */
    private ResultSet getDBRowFromCol(String col, String value) { return nodeDAO.getDBRowFromCol(col, value); }

    /**
     * Gets a ResultSet of all rows from the Nodes table
     *
     * @return a ResultSet of all rows from the Nodes table
     */
    public ResultSet getDBRowAllNodes() { return nodeDAO.getDBRowAllNodes(); }

    /**
     * Gets a ResultSet of rows from the Nodes table that match the given nodeID
     *
     * @param nodeID the nodeID to look for to get Node data
     * @return a ResultSet of the row(s) that match the nodeID
     */
    public ResultSet getDBRowNodeIDFromNodes(int nodeID) {
        return nodeDAO.getDBRowNodeID(nodeID);
    }

    /**
     * Gets a ResultSet of rows from the Nodes table that match the given xCoord
     *
     * @param xCoord the longName to look for to get Node data
     * @return a ResultSet of the row(s) that match the xCoord
     */
    public ResultSet getDBRowXCoordFromNodes(int xCoord) { return nodeDAO.getDBRowXCoord(xCoord); }

    /**
     * Gets a ResultSet of rows from the Nodes table that match the given yCoord
     *
     * @param yCoord the longName to look for to get Node data
     * @return a ResultSet of the row(s) that match the yCoord
     */
    public ResultSet getDBRowYCoordFromNodes(int yCoord) { return nodeDAO.getDBRowYCoord(yCoord); }

    /**
     * Gets a ResultSet of rows from the Nodes table that match the given floor
     *
     * @param floor the floor to look for to get Node data
     * @return a ResultSet of the row(s) that match the floor
     */
    public ResultSet getDBRowFloorFromNodes(String floor) { return nodeDAO.getDBRowFloor(floor); }

    /**
     * Gets an ArrayList of Nodes from the Nodes table that are on the given floor
     *
     * @param floor the floor to look for to get Node data
     * @return an ArrayList of the Nodes that are on the given floor
     */
    public ArrayList<Node> getNodesFromFloor(String floor) { return nodeDAO.getNodesFromFloor(floor); }

    /**
     * Gets a ResultSet of rows from the Nodes table that match the given building
     *
     * @param building the building to look for to get Node data
     * @return a ResultSet of the row(s) that match the building
     */
    public ResultSet getDBRowBuilding(String building) { return nodeDAO.getDBRowBuilding(building); }

    /**
     * Updates the database with the information in this Node object
     *
     * @param value a String array containing values to update (nodeID, xCoord, yCoord, floor, building)
     */
    private void updateRowNode(String value[]) { nodeDAO.updateRow(value); }

    /**
     * Deletes the row in the database that matches the nodeID of this Node object
     *
     * @param confirm 0 to confirm, anything else to cancel
     */
    public void deleteDBNode(int confirm, Node n) { nodeDAO.deleteDBNode(confirm, n); }

    /**
     * Returns a String of all the information about the node
     *
     * @return a String of all the information about the node
     */
    public String nodeToString(Node node) { return nodeDAO.toString(node); }

    /**
     * Inserts a Node object into the database
     *
     * @param n the Node to insert
     */
    public void insertDBNode(Node n) { nodeDAO.insertDBNode(n); }

    /**
     * Resets the node, location names, and moves tables using the backup tables
     */
    public void resetNodesFromBackup() {
        nodeDAO.resetNodesFromBackup();
        nodeDAO.setAll();
        locationNameDAO.setAll();
        moveDAO.setAll();
    }

    /**
     * Gets all the neighbors of a given Node using its nodeID
     *
     * @return an ArrayList of all the neighbors of the given node using its nodeID
     */
    public ArrayList<Node> getNodeNeighbors(int nodeID) { return nodeDAO.getNeighbors(nodeID); }

    /**
     * Gets all the neighbors of a given Node using its nodeID
     *
     * @return an ArrayList of all the nodeIDs of the neighbors of the given node using its nodeID
     */
    public ArrayList<Integer> getNodeNeighborsAsNodeIDs(int nodeID) { return nodeDAO.getNeighborsAsNodeIDs(nodeID); }

    /**
     * Returns a list of nodeIDs given a list of nodes
     *
     * @return an integer ArrayList of nodeIDs
     */
    public ArrayList<Integer> nodeToIDs(ArrayList<Node> nodes) { return nodeDAO.nodeToIDs(nodes); }

    /**
     * Instantiates all local nodes using the database
     */
    public ArrayList<Node> instantiateNodes() { return nodeDAO.instantiateNodes(); }

    /**
     * Updates a certain node in the database
     *
     * @param node the node to be updated
     * @param newNodeID the new nodeID
     * @param newXCoord the new xCoord
     * @param newYCoord the new yCoord
     * @param newFloor the new floor
     * @param newBuilding the new building
     */
    public void updateEditedNode(Node node, int newNodeID, int newXCoord, int newYCoord, String newFloor, String newBuilding) {
        nodeDAO.updateEditedNode(node, newNodeID, newXCoord, newYCoord, newFloor, newBuilding);
    }

    /**
     * Gets the short name of a node given its nodeID
     *
     * @param nodeID the nodeID of the node
     * @return the short name of the node
     */
    public String getShortName(int nodeID) { return nodeDAO.getShortName(nodeID); }

    /**
     * A helper function for getting a list of full nodes
     *
     * @return the long name of the node
     */
    public ArrayList<FullNode> getFullNodesHelper() { return nodeDAO.getFullNodesHelper(); }

    /**
     * A helper function for getting a FullNode object given its nodeID
     *
     * @param nodeID the nodeID of the node
     * @return a FullNode object
     */
    public FullNode getFullNode(int nodeID) { return nodeDAO.getFullNode(nodeID); }

    /**
     * A helper function for getting a list of node neighbors given a list of nodes
     *
     * @param floorNodes the list of nodes on a certain floor
     * @return the long name of the node
     */
    public ArrayList<ArrayList<Integer>> nodeNeighborIDs(ArrayList<Node> floorNodes) { return nodeDAO.nodeNeighborIDs(floorNodes); }

    /**
     * Gets the node type of a node given its nodeID
     *
     * @param n the Node object
     * @return the node type of the node
     */
    public String getNodeType (Node n) { return nodeDAO.getNodeType(n); }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @return a ResultSet containing the full nodes table joined with the moves table and the locationnames table
     */
    public ResultSet joinFullNodes() { return nodeDAO.joinFullNodes(); }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @param nodeID the nodeID to get the longName from
     * @return the longName of the node
     */
    public String getLongNameFromNodeID(int nodeID) { return nodeDAO.getLongNameFromNodeID(nodeID); }

    /**
     * Gets the value of the column from the table that matches the condition
     *
     * @param nodeID NodeID to get shortname from
     * @return a String with the shortname associated with the given NodeID
     */
    public String getShortNameFromNodeID(int nodeID) { return nodeDAO.getShortNameFromNodeID(nodeID); }

    /**
     * Gets a Node object given its nodeID
     *
     * @param nodeID the nodeID to get the node from
     * @return a Node object
     */
    public Node getNode(int nodeID) { return nodeDAO.getNode(nodeID); }

    /**
     * 'Fills' a node with its neighbors
     *
     * @param nodeID the nodeID to get the neighbors from
     * @return a 'filled' Node object with the given nodeID
     */
    public Node nodeFill(int nodeID) { return nodeDAO.nodeFill(nodeID); }


    //TODO Edge methods

    /**
     * Gets an edge by its endpoints
     *
     * @param id The endpoints of the edge
     * @return The edge with the given endpoints
     */
    public Edge getEdge(Object id) { return (Edge) edgeDAO.get(id); }

    /**
     * Gets all local Edge objects
     *
     * @return an ArrayList of all local Edge objects
     */

    public ArrayList<Edge> getAllEdges() { return edgeDAO.getAll(); }

    /**
     * Sets all Edge objects using the database
     */
    public void setAllEdges() { edgeDAO.setAll(); }

    /**
     * Gets all edges from the database
     *
     * @return a list of all edges
     */
    public ArrayList<Edge> getAllHelper() { return edgeDAO.getAllHelper(); }

    /**
     * Adds an Edge object to the both the database and local list
     *
     * @param edge the Edge object to be added
     */

    public void addEdge(Object edge) { edgeDAO.add(edge); }

    /**
     * Removes an Edge from the both the database and the local list
     *
     * @param edge the Edge object to be removed
     */

    public void deleteEdge(Object edge) { edgeDAO.delete(edge); }

    /**
     * Updates an Edge object in both the database and local list
     *
     * @param edge the Edge object to be updated
     */

    public void updateEdge(Object edge) { edgeDAO.update(edge); }

    /**
     * Adds this edge to its start and end nodes. If the list of nodes does not
     * contain the start or end node, it will make them
     *
     * @param nodes the list of nodes to look through add the edge to the start and
     *              end nodes
     */
    public void addToNode(Map<Integer, Node> nodes, Edge e) { edgeDAO.addToNode(nodes, e); }

    /**
     * Gets the row(s) from the database that matches the startNode
     *
     * @param startNode the startNode to search for, make sure there are NO single
     *                  quotes surrounding the startNode
     * @return the row(s) that have a matching the startNode
     */
    public static ResultSet getDBRowStartNodeFromEdges(String startNode) { return EdgeDAOImpl.getDBRowStartNode(startNode); }

    /**
     * Gets the row(s) from the database that matches the endNode
     *
     * @param endNode the endNode to search for, make sure there are NO single
     *                quotes surrounding the endNode
     * @return the row(s) that have a matching the endNode
     */
    public static ResultSet getDBRowEndNodeFromEdges(String endNode) { return EdgeDAOImpl.getDBRowEndNode(endNode); }

    /**
     * Searches through the database for the row(s) that matches the col and value in the Edges table
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return A ResultSet of the row(s) that match the col and value
     */
    private static ResultSet getRowFromCol(String col, String value) { return EdgeDAOImpl.getRowFromCol(col, value); }

    /**
     * Gets all the rows from the Edge table
     */
    public ResultSet getDBRowAllEdges() { return edgeDAO.getDBRowAllEdges(); }

    /**
     * Inserts an edge into the database
     *
     * @param startNode the startNode of the edge
     * @param endNode the endNode of the edge
     */
    public void insertEdge(int startNode, int endNode) { edgeDAO.insertEdge(startNode, endNode); }

    /**
     * Updates the row in the database that matches the edgeID
     *
     * @param cols   the columns to update
     * @param values the values to update the columns to
     */
    private void updateRow(String[] cols, String[] values, Edge e) { edgeDAO.updateRow(cols, values, e); }

    /**
     * Updates the startNode in the database
     *
     * @param startNode the new startNode
     */
    public void updateDBStartNodeInEdges(String startNode, Edge e) { edgeDAO.updateDBStartNode(startNode, e);  }

    /**
     * Updates the endNode in the database
     *
     * @param endNode the new endNode
     * @param e the Edge to update
     */
    public void updateDBEndNodeInEdges(String endNode, Edge e) { edgeDAO.updateDBEndNode(endNode, e);  }

    /**
     * Updates all attributes in the database to match the edge
     */
    public void updateDBEdge(Edge e) { edgeDAO.updateDBEdge(e); }

    /**
     * Deletes the row in the database that matches the edgeID
     * only use this if you are sure you want to delete the row
     *
     * @param confirm 0 to confirm delete, anything else to cancel
     * @param e the edge to delete
     */
    public void deleteDBEdge(int confirm, Edge e) { edgeDAO.deleteDBEdge(confirm, e); }

    /**
     * Gets the edge from the database that matches the edgeID
     *
     * @param endpoints a String of endpoints in the format "startNode_endNode"
     * @return the edge that matches the endpoints
     */
    public Edge getEdge(String endpoints) { return edgeDAO.getEdge(endpoints); }

    /**
     * Returns the edge in a string
     *
     * @return the edge in a string with the format "[edgeID] [startNode] [endNode]"
     */
    public String edgeToString(Edge e) { return edgeDAO.toString(e); }

    //TODO LocationName methods



    //TODO Move methods



    //TODO User methods



    //TODO Request methods



    //TODO ConferenceRequest methods



    //TODO FlowerRequest methods



    //TODO MealRequest methods



    //TODO FurnitureRequest methods



    //TODO OfficeRequest methods

    /**
     * Updates an OfficeRequest in the database
     * @param fullOfficeRequest
     */
    public void updateOfficeRequest(FullOfficeRequest fullOfficeRequest) { officeRequestDAO.update(fullOfficeRequest); }



    //TODO DBinput methods

    /**
     * This method imports the Nodes table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importNodesFromCSV(String filename, int location) { DBinput.importNodesFromCSV(filename, location); }

    /**
     * This method imports the Edges table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importEdgesFromCSV(String filename, int location) { DBinput.importEdgesFromCSV(filename, location); }

    /**
     * This method imports the LocationNames table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importLocationNamesFromCSV(String filename, int location) { DBinput.importLocationNamesFromCSV(filename, location); }

    /**
     * This method imports the Moves table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importMovesFromCSV(String filename, int location) { DBinput.importMovesFromCSV(filename, location); }

    /**
     * This method imports the Users table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importUsersFromCSV(String filename, int location) { DBinput.importUsersFromCSV(filename, location); }

    /**
     * This method imports the Requests table from a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importRequestsFromCSV(String filename, int location) { DBinput.importRequestsFromCSV(filename, location); }

    /**
     * Imports the conference requests from a CSV file into the database
     *
     * @param filename The name of the CSV file to be imported as a String
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importConferenceRequestsFromCSV(String filename, int location) { DBinput.importConferenceRequestsFromCSV(filename, location); }

    /**
     * Imports the flower requests from a CSV file into the database
     *
     * @param filename The name of the CSV file to be imported as a String
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importFlowerRequestsFromCSV(String filename, int location) { DBinput.importFlowerRequestsFromCSV(filename, location); }

    /**
     * Imports the furniture requests from a CSV file into the database
     *
     * @param filename The name of the CSV file to be imported as a String
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importFurnitureRequestsFromCSV(String filename, int location) { DBinput.importFurnitureRequestsFromCSV(filename, location); }

    /**
     * Imports the meal requests from a CSV file into the database
     *
     * @param filename The name of the CSV file to be imported as a String
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importMealRequestsFromCSV(String filename, int location) { DBinput.importMealRequestsFromCSV(filename, location); }

    /**
     * Imports the office requests from a CSV file into the database
     *
     * @param filename The name of the CSV file to be imported as a String
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void importOfficeRequestsFromCSV(String filename, int location) { DBinput.importOfficeRequestsFromCSV(filename, location); }

    //TODO DBoutput methods

    /**
     * This method exports the Nodes table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportNodesToCSV(String filename, int location) { DBoutput.exportNodesToCSV(filename, location); }

    /**
     * This method exports the Edges table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportEdgesToCSV(String filename, int location) { DBoutput.exportEdgesToCSV(filename, location); }

    /**
     * This method exports the LocationNames table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportLocationNamesToCSV(String filename, int location) { DBoutput.exportLocationNamesToCSV(filename, location); }

    /**
     * This method exports the Moves table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportMovesToCSV(String filename, int location) { DBoutput.exportMovesToCSV(filename, location); }

    /**
     * This method exports the Users table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportUsersToCSV(String filename, int location) { DBoutput.exportUsersToCSV(filename, location); }

    /**
     * This method exports the Requests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportRequestsToCSV(String filename, int location) { DBoutput.exportRequestsToCSV(filename, location); }

    /**
     * This method exports the ConferenceRequests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportConferenceRequestsToCSV(String filename, int location) { DBoutput.exportConferenceRequestsToCSV(filename, location); }

    /**
     * This method exports the FlowerRequests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportFlowerRequestsToCSV(String filename, int location) { DBoutput.exportFlowerRequestsToCSV(filename, location); }

    /**
     * This method exports the FurnitureRequests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportFurnitureRequestsToCSV(String filename, int location) { DBoutput.exportFurnitureRequestsToCSV(filename, location); }

    /**
     * This method exports the MealRequests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportMealRequestsToCSV(String filename, int location) { DBoutput.exportMealRequestsToCSV(filename, location); }

    /**
     * This method exports the OfficeRequests table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public void exportOfficeRequestsToCSV(String filename, int location) { DBoutput.exportOfficeRequestsToCSV(filename, location); }

    //TODO Unorganized stuff below

    public void addEdge(Edge e) {
        edgeDAO.add(e);
    }

    public void deleteEdge(Edge e) {
        edgeDAO.delete(e);
    }

    public void updateEdge(Edge e) {
        edgeDAO.update(e);
    }

    public void addConferenceRequest(String[] cr) {
        conferenceRequestDAO.add(cr);
    }

    public void deleteConferenceRequest(ConferenceRequest cr) {
        conferenceRequestDAO.delete(cr);
    }

    public FullConferenceRequest getConferenceRequest(int id) { return (FullConferenceRequest) conferenceRequestDAO.get(id); }

    public ArrayList<FullConferenceRequest> getAllConferenceRequests() {
        return conferenceRequestDAO.getAll();
    }

    public void updateConferenceRequest(ConferenceRequest cr) {
        conferenceRequestDAO.update(cr);
    }

    public void updateConferenceRequest(FullConferenceRequest cr) {
        conferenceRequestDAO.update(cr);
    }

    public void addMealRequest(String[] mr) {
        mealRequestDAO.add(mr);
    }

    public void deleteMealRequest(MealRequest mr) {
        mealRequestDAO.delete(mr);
    }

    public FullMealRequest getMealRequest(int id) {
        return (FullMealRequest) mealRequestDAO.get(id);
    }

    public ArrayList<FullMealRequest> getAllMealRequests() {
        return mealRequestDAO.getAll();
    }

    public void updateMealRequest(MealRequest mr) {
        mealRequestDAO.update(mr);
    }

    public void updateMealRequest(FullMealRequest mr) {
        mealRequestDAO.update(mr);
    }

    public Object getRequest(int id) {
        return requestDAO.get(id);
    }

    public ArrayList<Request> getAllRequests() {
        return requestDAO.getAll();
    }

    public void addLocationName(LocationName ln) {
        locationNameDAO.add(ln);
    }

    public void deleteLocationName(LocationName ln) {
        locationNameDAO.delete(ln);
    }

    public LocationName getLocationName(int id) {
        return (LocationName) locationNameDAO.get(id);
    }

    public ArrayList<LocationName> getAllLocationNames() {
        return locationNameDAO.getAll();
    }

    public void updateLocationName(LocationName ln) {
        locationNameDAO.update(ln);
    }

    public void addUser(User u) {
        userDAO.add(u);
    }

    public void deleteUser(User u) {
        userDAO.delete(u);
        requestDAO.updateRequestDeleteUser(u.getUsername());
    }

    public User getUser(String id) {
        return (User) userDAO.get(id);
    }

    public ArrayList<User> getAllUsers() {
        return userDAO.getAll();
    }

    public void updateUser(User u) {
        userDAO.update(u);
    }

    public void addNode(Node n) {
        nodeDAO.add(n);
    }

    public void deleteNode(Node n) {
        nodeDAO.delete(n);
    }

    public void updateNode(Node n) {
        nodeDAO.update(n);
    }

    public void addMove(Move m) {
        moveDAO.add(m);
    }

    public void deleteMove(Move m) {
        moveDAO.delete(m);
    }

    public Move getMove(int id) {
        return (Move) moveDAO.get(id);
    }

    public ArrayList<Move> getAllMoves() {
        return moveDAO.getAll();
    }

    public void updateMove(Move m) {
        moveDAO.update(m);
    }

    public void addFlowerRequest(String[] fr) {
        flowerRequestDAO.add(fr);
    }

    public void deleteFlowerRequest(FlowerRequest fr) {
        flowerRequestDAO.delete(fr);
    }

    public FullFlowerRequest getFlowerRequest(int id) {
        return (FullFlowerRequest) flowerRequestDAO.get(id);
    }

    public ArrayList<FullFlowerRequest> getAllFlowerRequests() {
        return flowerRequestDAO.getAll();
    }

    public void updateFlowerRequest(FlowerRequest fr) {
        flowerRequestDAO.update(fr);
    }

    public void updateFlowerRequest(FullFlowerRequest fr) {
        flowerRequestDAO.update(fr);
    }

    public ArrayList<Integer> getNeighbors(int nodeID) {
        return nodeDAO.getNeighborsAsNodeIDs(nodeID);
    }

    public ArrayList<String> getAllLongNames() {
        return locationNameDAO.getLongNamesAlphebeticalOrder();
    }

    public ArrayList<String> getAllShortNames() {
        return locationNameDAO.getShortNamesAlphebeticalOrder();
    }

    public ArrayList<Node> getNodesByFloor(String floor){
        return nodeDAO.getNodesFromFloor(floor);
    }

    public void addFurnitureRequest(String[] fr) {
        furnitureRequestDAO.add(fr);
    }

    public void deleteFurnitureRequest(FurnitureRequest fr) {
        furnitureRequestDAO.delete(fr);
    }

    public FullFurnitureRequest getFurnitureRequest(int id) {
        return (FullFurnitureRequest) furnitureRequestDAO.get(id);
    }

    public ArrayList<FullFurnitureRequest> getAllFurnitureRequests() {
        return furnitureRequestDAO.getAll();
    }

    public void updateFurnitureRequest(FurnitureRequest fr) {
        furnitureRequestDAO.update(fr);
    }

    public void updateFurnitureRequest(FullFurnitureRequest fr) {
        furnitureRequestDAO.update(fr);
    }

    public void addOfficeRequest(String[] or) {
        officeRequestDAO.add(or);
    }

    public void deleteOfficeRequest(OfficeRequest or) {
        officeRequestDAO.delete(or);
    }

    public FullOfficeRequest getOfficeRequest(int id) {
        return (FullOfficeRequest) officeRequestDAO.get(id);
    }

    public ArrayList<FullOfficeRequest> getAllOfficeRequests() {
        return officeRequestDAO.getAll();
    }

    public Connection getConnection() {
        return dbConnection.getConnection();
    }

    public ArrayList<String> getNodeTypesUniqueAlphabetical () {
        return locationNameDAO.getNodeTypesUniqueAlphabetical();
    }

    public void addFullNode (Object n) {
        FullNode.addFullNode(n);
    }

    public void deleteFullNode(Object n) {
        FullNode.deleteFullNode(n);
    }

    public void updateFullNode(Object n) {
        FullNode.updateFullNode(n);
    }

    public ArrayList<IFull> getAllFullRequests() {
        return requestDAO.getAllHelper1();
    }

    public ArrayList<IFull> getAllFullRequestsByUser(String username) {
        return requestDAO.getFullRequestsbyEmployee(username);
    }

    public ArrayList<IFull> getAllFullRequestsByStatus(String status) {
        return requestDAO.getFullRequestsbyStatus(status);
    }

    public ArrayList<String> getLongNameByType(String type) {
        return locationNameDAO.getLongNameByType(type);
    }

    public ArrayList<String> getPracticalLongNames() {
        return locationNameDAO.getLongNamePractical();
    }

    public Alert getAlert(int id) {
        return alertDAO.get(id);
    }

    public ArrayList<Alert> getAllAlerts() {
        return alertDAO.getAll();
    }

    public void addAlert(Alert a) {
        alertDAO.add(a);
    }

    public void deleteAlert(Alert a) {
        alertDAO.delete(a);
    }

    public void updateAlert(Alert a) {
        alertDAO.update(a);
    }
}
