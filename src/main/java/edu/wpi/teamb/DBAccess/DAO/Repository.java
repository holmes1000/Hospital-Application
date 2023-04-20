package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.ORMs.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import java.time.LocalDate;

public class Repository {
    private Repository() {
        try {
            edgeDAO = new EdgeDAOImpl();
            conferencerequestDAO = new ConferenceRequestDAOImpl();
            flowerrequestDAO = new FlowerRequestDAOImpl();
            mealrequestDAO = new MealRequestDAOImpl();
            requestDAO = RequestDAOImpl.getRequestDaoImpl();
            nodeDAO = new NodeDAOImpl();
            moveDAO = new MoveDAOImpl();
            locationNameDAO = new LocationNameDAOImpl();
            userDAO = new UserDAOImpl();
            nodeDAOImpl = new NodeDAOImpl();
            locationNameDaoImpl = new LocationNameDAOImpl();
            furniturerequestDAO = new FurnitureRequestDAOImpl();
            officerequestDAO = new OfficeRequestDAOImpl();
            moveDAOImpl = new MoveDAOImpl();
            db = DB.getDB();
        } catch (SQLException e) {
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
    private final IDAO edgeDAO;
    private final IDAO conferencerequestDAO;
    private final IDAO flowerrequestDAO;
    private final IDAO mealrequestDAO;
    private final IDAO requestDAO;
    private final IDAO nodeDAO;
    private final IDAO moveDAO;
    private final IDAO locationNameDAO;
    private final IDAO userDAO;
    private final IDAO furniturerequestDAO;
    private final NodeDAOImpl nodeDAOImpl;
    private final LocationNameDAOImpl locationNameDaoImpl;
    private final IDAO officerequestDAO;
    private final MoveDAOImpl moveDAOImpl;
    private final DB db;

    public void addEdge(Edge e) {
        edgeDAO.add(e);
    }

    public void deleteEdge(Edge e) {
        edgeDAO.delete(e);
    }

    public Edge getEdge(String id) {
        return (Edge) edgeDAO.get(id);
    }

    public ArrayList<Edge> getAllEdges() {
        return edgeDAO.getAll();
    }
    public void updateEdge(Edge e) {
        edgeDAO.update(e);
    }

    public void addConferenceRequest(String[] cr) {
        conferencerequestDAO.add(cr);
    }
    public void deleteConferenceRequest(ConferenceRequest cr) {
        conferencerequestDAO.delete(cr);
    }

    public FullConferenceRequest getConferenceRequest(int id) {
        return (FullConferenceRequest) conferencerequestDAO.get(id);
    }

    public ArrayList<FullConferenceRequest> getAllConferenceRequests() {
        return conferencerequestDAO.getAll();
    }

    public void updateConferenceRequest(ConferenceRequest cr) {
        conferencerequestDAO.update(cr);
    }

    public void addMealRequest(String[] mr) {
        mealrequestDAO.add(mr);
    }

    public void deleteMealRequest(MealRequest mr) {
        mealrequestDAO.delete(mr);
    }

    public FullMealRequest getMealRequest(int id) {
        return (FullMealRequest) mealrequestDAO.get(id);
    }

    public ArrayList<FullOfficeRequest> getAllMealRequests() {
        return mealrequestDAO.getAll();
    }

    public void updateMealRequest(MealRequest mr) {
        mealrequestDAO.update(mr);
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

    public Node getNode(int id) {
        return (Node) nodeDAO.get(id);
    }

    public ArrayList<Node> getAllNodes() {
        return nodeDAO.getAll();
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
        flowerrequestDAO.add(fr);
    }

    public void deleteFlowerRequest(FlowerRequest fr) {
        flowerrequestDAO.delete(fr);
    }

    public FullFlowerRequest getFlowerRequest(int id) {
        return (FullFlowerRequest) flowerrequestDAO.get(id);
    }

    public ArrayList<FlowerRequest> getAllFlowerRequests() {
        return flowerrequestDAO.getAll();
    }

    public void updateFlowerRequest(FlowerRequest fr) {
        flowerrequestDAO.update(fr);
    }

    public ArrayList<Integer> getNeighbors(int nodeID) {
        return nodeDAOImpl.getNeighbors1(nodeID);
    }

    public ArrayList<String> getAllLongNames() {
        return locationNameDaoImpl.getLongNamesAlphebeticalOrder();
    }

    public void resetNodesFromBackup() { nodeDAOImpl.resetNodesFromBackup(); }

    public ArrayList<String> getAllShortNames() {
        return locationNameDaoImpl.getShortNamesAlphebeticalOrder();
    }

    public String getShortName(int nodeID) { return nodeDAOImpl.getShortName(nodeID);}
    public ArrayList<FullNode> getFullNodes() {
        return nodeDAOImpl.getFullNodes();
    }
    public FullNode getFullNode(int nodeID) {
        return nodeDAOImpl.getFullNode(nodeID);
    }
    public ArrayList<ArrayList<Integer>> nodeNeighborIDs(ArrayList<Node> floorNodes) {
        return nodeDAOImpl.nodeNeighborIDs(floorNodes);
    }

    public ArrayList<Node> getNodesByFloor(String floor){
        return nodeDAOImpl.getNodesFromFloor(floor);
    }

    public void addFurnitureRequest(String[] fr) {
        furniturerequestDAO.add(fr);
    }

    public void deleteFurnitureRequest(FurnitureRequest fr) {
        furniturerequestDAO.delete(fr);
    }

    public FullFurnitureRequest getFurnitureRequest(int id) {
        return (FullFurnitureRequest) furniturerequestDAO.get(id);
    }

    public ArrayList<FullFurnitureRequest> getAllFurnitureRequests() {
        return furniturerequestDAO.getAll();
    }

    public void updateFurnitureRequest(FurnitureRequest fr) {
        furniturerequestDAO.update(fr);
    }

    public void addOfficeRequest(String[] or) {
        officerequestDAO.add(or);
    }

    public void deleteOfficeRequest(OfficeRequest or) {
        officerequestDAO.delete(or);
    }

    public FullOfficeRequest getOfficeRequest(int id) {
        return (FullOfficeRequest) officerequestDAO.get(id);
    }

    public ArrayList<FullOfficeRequest> getAllOfficeRequests() {
        return officerequestDAO.getAll();
    }

    public Connection getConnection() {
        return db.getConnection();
    }

    public void connectToDB() {
        db.connectToDB();
    }

    public void addFullNode (Object n) {
        Date current = Date.valueOf(LocalDate.now());
        FullNode fullNode = (FullNode) n;
        Node node = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding());
        nodeDAOImpl.add(node);
        locationNameDaoImpl.add(new LocationName(fullNode.getLongName(), fullNode.getShortName(), fullNode.getNodeType()));
        moveDAOImpl.add(new Move(fullNode.getNodeID(), fullNode.getLongName(), current));
    }

    public ArrayList<String> getNodeTypesUniqueAlphabetical () {
        return locationNameDaoImpl.getNodeTypesUniqueAlphabetical();
    }
    public void deleteFullNode(Object n) {
        Date current = Date.valueOf(LocalDate.now());
        FullNode fullNode = (FullNode) n;
        Node node = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding());
        nodeDAOImpl.delete(node);
        locationNameDaoImpl.delete(new LocationName(fullNode.getLongName(), fullNode.getShortName(), fullNode.getNodeType()));
        moveDAOImpl.delete(new Move(fullNode.getNodeID(), fullNode.getLongName(), current));
    }

    public void updateFullNode(Object n) {
        FullNode fn = (FullNode) n;
        Node node = new Node(fn.getNodeID(), fn.getxCoord(), fn.getyCoord(), fn.getFloor(), fn.getBuilding());
        nodeDAO.update(node);
        LocationName ln = new LocationName(fn.getLongName(), fn.getShortName(), fn.getNodeType());
        locationNameDAO.update(ln);
        Move m = new Move(fn.getNodeID(), fn.getLongName(), Date.valueOf("2023-04-18"));
        moveDAO.update(m);
    }

}
