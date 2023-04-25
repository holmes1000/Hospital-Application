package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.util.ArrayList;
import java.util.*;
import java.sql.*;

public class Pathfinder {

    HashMap<Integer,Node> node_map = new HashMap<Integer,Node>();

    public void init_pathfinder() throws SQLException{
        if (node_map.isEmpty()){create_all_nodes();}
    }

    public HashMap<Integer, Node> get_node_map() {
        return node_map;
    }

    public ArrayList<Integer> bfSearch(Integer startNodeID, Integer endNodeID) throws SQLException {
        // que for frontier
        LinkedList<Integer> frontier = new LinkedList<Integer>();
        // que for visited
        ArrayList<Integer> visited = new ArrayList<Integer>();

        Integer location = null;
        // add start node to frontier
        frontier.addFirst(startNodeID);
        while (frontier.size() != 0){
            location = frontier.poll();
            visited.add(location);
            if (location.equals(endNodeID)){
                return visited;
            }
            frontier.addAll(newFrontiers(location,visited));
        }
        return null;
    }

    public ArrayList<Integer> dfSearch(Integer start, Integer end) throws SQLException {

        Stack<Integer> frontier = new Stack<Integer>();
        // list for visited nodes
        ArrayList<Integer> visited = new ArrayList<Integer>();

        frontier.push(start);

        while (!frontier.isEmpty()) {
            Integer location = frontier.pop();

            // if location has not been visited
            if (!visited.contains(location)) {
                visited.add(location);

                // if location is the end node, return visited list
                if (location.equals(end)) {
                    return visited;
                }

                // add all adjacent nodes to frontier
                frontier.addAll(newFrontiers(location, visited));
            }
        }

        // end node was not found
        return null;
    }

    public LinkedList<Integer> newFrontiers(Integer location, ArrayList<Integer> visited) throws SQLException {
        LinkedList<Integer> newFrontiers = new LinkedList<Integer>();
        for (int nodeID : Repository.getRepository().getNodeNeighborsAsNodeIDs(location)) { // this is a junk function for edge data
            if (!visited.contains(nodeID)){
                newFrontiers.add(nodeID);
            }
        }
        return newFrontiers;
    }

    public ArrayList<Integer> Astar(int start, int goal) throws SQLException {
        init_pathfinder();
        System.out.println(node_map.size());
        if (!node_map.containsKey(start)) {System.out.println("Invalid Start");}
        if (!node_map.containsKey(goal)) {System.out.println("Invalid Goal");}
        if (!node_map.containsKey(goal) || !node_map.containsKey(start)){return new ArrayList<Integer>();}
        HashMap<Integer,Node> node_map = this.node_map;
        Node startNode = node_map.get(start);
        startNode.setCost(0.0);
        Node goalNode = node_map.get(goal);

        PriorityQueue<Node> frontier = new PriorityQueue<Node>(new PriorityComparatorAstar());
        frontier.add(startNode);
        Node current;
        double newCost;

        HashMap<Integer, Integer> cameFrom = new HashMap<Integer, Integer>();
        HashMap<Integer, Double> costSoFar = new HashMap<Integer, Double>();
        ArrayList<Integer> visited = new ArrayList<Integer>();
        cameFrom.put(start, null);
        costSoFar.put(start, 0.0);
        ArrayList<Integer> path = new ArrayList<Integer>();

        while (!frontier.isEmpty()) {
//          System.out.println("frontier: " + frontier);

            current = frontier.poll();
//          System.out.println(current.getNodeID());
            visited.add(current.getNodeID());

            if (current.getNeighborIds().contains(goal)){ // needs to implement Alisha's new function

                cameFrom.put(goal,current.getNodeID());
//              System.out.println(cameFrom);
                current = node_map.get(goal);
                visited.add(current.getNodeID());
            }

            if (visited.contains(goal)) { //Early termination
                // make path
                System.out.println(current.getNodeID());
                System.out.println();
                Integer currentInt = goal;
//              path.add(current.getNodeID());
                while (currentInt != start) { //Probably an issue here
                    path.add(currentInt);
                    currentInt = cameFrom.get(currentInt);
                    if (currentInt == null) {break;}
                }
                path.add(start);
                Collections.reverse(path);
                System.out.println(path);
                return path; //returns the path from start to end
            }
//          System.out.println("didn't terminate");
//          System.out.println("current" + current);
//          System.out.println("current node id: " + current.getNodeID());
//          System.out.println("get neighbors: " + getNeighbors(current.getNodeID()).length);

            // getNeighbors gets a list of nodes
            for (int next : node_map.get(current.getNodeID()).getNeighborIds()) {
//              System.out.println("next id: " + next)
                Node nextNode = node_map.get(next); // Implemented this to continue to use int as the node ID format
//              System.out.print("Next Node: ");
//              System.out.println(nextNode);
                //newCost = costSoFar.get(current.getNodeID()) + manhattanDistance(current, nextNode) + manhattanDistance(current, goalNode);
                newCost = costSoFar.get(current.getNodeID()) + manhattanDistance(current, nextNode);
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    nextNode.setCost(newCost);
                    costSoFar.put(next,newCost);
                    if (cameFrom.containsKey(next)) {cameFrom.replace(next, current.getNodeID());}
                    else {cameFrom.put(next,current.getNodeID());}
//                  System.out.println(cameFrom);
                    if (!visited.contains(nextNode.getNodeID())) {
                        frontier.add(nextNode);
                    }
                }
            }
        }
//      return path;
        return null;

    }


    public static boolean primitiveContains(int nodeID, int[] list){
        for (int i = 0; i < list.length; i++){
            if (list[i] == nodeID) return true;
        }
        return false;
    }

    public void create_all_nodes() throws SQLException {
        HashMap<Integer,Node> node_map = new HashMap<Integer,Node>();
        ArrayList<Node> node_list = Repository.getRepository().getAllNodes();
        for (int i = 0; i < node_list.size(); i++) {
//            System.out.println(node_list.get(i).getNodeID());
//            System.out.println(node_list.get(i).toString());
            node_list.get(i).setNeighborIds(Repository.getRepository().getNeighborsAsNodeIDs(node_list.get(i).getNodeID()));
            node_map.put(node_list.get(i).getNodeID(),node_list.get(i));
        }
        this.node_map = node_map;
        System.out.println("Initialized all nodes. Ready for pathfinding");
    }

//    getLongFromInt() takes Int from the nodeID and returns the long name
//    String printPath(ArrayList<Integer> shortestPath) {
//        String path = "";
//
//        for (int i = 0; i < shortestPath.size(); i++) {
//            String nodeName = DB.getLongNameFromNodeID(shortestPath.get(i).intValue());
//            path = path + nodeName + ", ";
//        }
//        String finalPath = path.substring(0, path.length() - 2);
//        return finalPath;
//
//    }

    //Code is optimized a bit. Assumes shortestPath is an ArrayList of Integer Node Ids.
    public static String printPath(ArrayList<Integer> shortestPath){
        StringBuilder path = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            if (i > 0) {
                path.append(", ");
            }
            path.append(Repository.getRepository().getLongNameFromNodeID(shortestPath.get(i)));
        }
        return path.toString();
    }

    public String[] getPathAsStrings(ArrayList<Integer> shortestPath){
        String[] longNames = new String[shortestPath.size()];
        for (int i = 0; i < shortestPath.size(); i++) {
            longNames[i] = Repository.getRepository().getLongNameFromNodeID(shortestPath.get(i));
        }
        return longNames;
    }

    // frontend needs to call FindShortestPath.getPathAsStrings(FindShortestPath.Astar(start, end))

    int manhattanDistance(int x_1, int y_1, int x_2, int y_2) {
        return Math.abs(x_1 - x_2) + Math.abs(y_1 - y_2);
    }

    static int manhattanDistance(Node node1, Node node2) {
        return (Math.abs(node2.getxCoord() - node1.getxCoord()) + Math.abs(node2.getyCoord() - node1.getyCoord())
                + Math.abs(node2.getFloorNum() - node1.getFloorNum()));
    }

    static int euclideanDistance(Node node1, Node node2) {
        return (int) Math.sqrt(Math.pow(node2.getxCoord() - node1.getxCoord(), 2) + Math.pow(node2.getyCoord() - node1.getyCoord(), 2)
                + Math.pow(node2.getFloorNum() - node1.getFloorNum(), 2));
    }

    ArrayList<Node> createNeighbors(ArrayList<Integer> neighbor_list){

        return null;
    }

    void printPath(int parent[], int j) {
        if (parent[j] == 0) {
            System.out.print(j + " ");
            return;
        }
        printPath(parent, parent[j]);
        System.out.print(j + " ");
    }
}

