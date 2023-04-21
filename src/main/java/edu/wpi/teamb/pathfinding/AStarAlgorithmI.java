package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class AStarAlgorithmI implements IPathFindingAlgorithm {


    HashMap<Integer,Node> node_map = new HashMap<Integer,Node>();
    ArrayList<FullNode> fullNodes = new ArrayList<FullNode>();

    public void init_pathfinder() throws SQLException {
        if (node_map.isEmpty()){create_all_nodes();}
    }
    public void force_init() throws SQLException {create_all_nodes();}

    public HashMap<Integer, Node> get_node_map() {
        return node_map;
    }

    public ArrayList<FullNode> getFullNodes() {
        return fullNodes;
    }

    public void create_all_nodes() {
        HashMap<Integer,Node> node_map = new HashMap<Integer,Node>();
        Repository.getRepository().setAllNodes();
        ArrayList<Node> node_list = Repository.getRepository().getAllNodes();
        for (int i = 0; i < node_list.size(); i++) {
//            System.out.println(node_list.get(i).getNodeID());
//            System.out.println(node_list.get(i).toString());
            node_list.get(i).setNeighborIds(Repository.getRepository().getNeighbors(node_list.get(i).getNodeID()));
            node_map.put(node_list.get(i).getNodeID(),node_list.get(i));
        }
        this.node_map = node_map;
        this.fullNodes = Repository.getRepository().getAllFullNodes();
        System.out.println("Initialized all nodes. Ready for pathfinding");
    }


    @Override
    public ArrayList<Integer> findPath(int start, int goal) throws SQLException{
        init_pathfinder();
//        System.out.println(node_map.size());
        if (!node_map.containsKey(start)) {System.out.println("Invalid Start");}
        if (!node_map.containsKey(goal)) {System.out.println("Invalid Goal");}
        if (!node_map.containsKey(goal) || !node_map.containsKey(start)){return new ArrayList<Integer>();}
        HashMap<Integer, Node> node_map = this.node_map;
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
//                System.out.println(current.getNodeID());
//                System.out.println();
                Integer currentInt = goal;
//              path.add(current.getNodeID());
                while (currentInt != start) {
                    path.add(currentInt);
                    currentInt = cameFrom.get(currentInt);
                    if (currentInt == null) {break;}
                }
                path.add(start);
                Collections.reverse(path);
//                System.out.println(path);
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
                Double cost_with_heuristic = newCost + manhattanDistance(current, goalNode);
                if (!costSoFar.containsKey(next) || newCost < costSoFar.get(next)) {
                    nextNode.setCost(newCost);
                    nextNode.setCost_and_heuristic(cost_with_heuristic);
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

    static int manhattanDistance(Node node1, Node node2) {
        return (Math.abs(node2.getxCoord() - node1.getxCoord()) + Math.abs(node2.getyCoord() - node1.getyCoord())
                + Math.abs(node2.getFloorNum() - node1.getFloorNum()));
    }

    void printPath(int parent[], int j) {
        if (parent[j] == 0) {
            System.out.print(j + " ");
            return;
        }
        printPath(parent, parent[j]);
        System.out.print(j + " ");
    }

    public String[] getPathAsStrings(ArrayList<Integer> shortestPath){
        String[] longNames = new String[shortestPath.size()];
        for (int i = 0; i < shortestPath.size(); i++) {
            longNames[i] = Repository.getRepository().getLongNameFromNodeID(shortestPath.get(i));
        }
        return longNames;
    }


}
