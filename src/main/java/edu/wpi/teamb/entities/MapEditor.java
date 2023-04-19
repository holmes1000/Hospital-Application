package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Edge;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.SQLException;
import java.util.ArrayList;

public class MapEditor {
  Node node;
  Move move;
  Edge edge;
  LocationName locationName;

  ArrayList<Node> nodeList = new ArrayList<>();
  ArrayList<Move> moveList = new ArrayList<>();
  ArrayList<Edge> edgeList = new ArrayList<>();
  ArrayList<LocationName> locationNameList = new ArrayList<>();

  public MapEditor() throws SQLException {
    this.node = new Node();
    this.move = new Move();
    this.edge = new Edge();
    this.locationName = new LocationName();
  }

  public ArrayList<Node> getNodeList() {
   return Repository.getRepository().getAllNodes();
  }

  public ArrayList<Edge> getEdgeList() {
    return Repository.getRepository().getAllEdges();
  }

  public ArrayList<Move> getMoveList() {
    return Repository.getRepository().getAllMoves();
  }

  public ArrayList<LocationName> getLocationNameList() {
    return Repository.getRepository().getAllLocationNames();
  }

//  public Node getNodeFromLongName(String longName) {
//    this.move = new Move(this.move.getDBRowLongName(longName));
//    try{
//      this.node = new Node(this.move.getDBRowNodeID(this.move.getNodeID()));
//      return this.node;
//    } catch(java.sql.SQLException e) {
//      System.out.println("MapEditor Entity: Error getting node from long name");
//      e.printStackTrace();
//    }
//    return null;
//  }
}
