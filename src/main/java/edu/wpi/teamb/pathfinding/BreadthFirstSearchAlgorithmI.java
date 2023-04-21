package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchAlgorithmI implements IPathFindingAlgorithm {


    HashMap<Integer,Node> node_map = PathFinding.ASTAR.get_node_map();

    @Override
    public ArrayList<Integer> findPath(int start, int goal) throws SQLException {
        PathFinding.ASTAR.init_pathfinder();
        node_map = PathFinding.ASTAR.get_node_map();
        // queue for frontier
        Queue<Integer> frontier = new LinkedList<Integer>();
        // list for visited nodes
        ArrayList<Integer> visited = new ArrayList<Integer>();
        // map for parent nodes
        HashMap<Integer, Integer> parentMap = new HashMap<Integer, Integer>();

        Integer location = null;
        // add start node to frontier and mark as visited
        frontier.add(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!frontier.isEmpty()) {
            location = frontier.poll();

            // loop through neighboring nodes
            for (Integer neighbor : newFrontiers(location, visited)) {
                // check for cycles
                if (visited.contains(neighbor) && !parentMap.get(location).equals(neighbor)) {
                    continue;
                }
                // add new node to frontier and mark as visited
                if (!visited.contains(neighbor)) {
                    frontier.add(neighbor);
                    visited.add(neighbor);
                    parentMap.put(neighbor, location);
                }
            }

            // check if end node has been reached
            if (location.equals(goal)) {
                ArrayList<Integer> path = new ArrayList<Integer>();
                path.add(location);
                Integer parent = parentMap.get(location);
                while (parent != null) {
                    path.add(0, parent);
                    parent = parentMap.get(parent);
                }
                return path;
            }
        }

        return null; // end node was not found
    }

    public LinkedList<Integer> newFrontiers(int location, ArrayList<Integer> visited) throws SQLException {
        LinkedList<Integer> newFrontiers = new LinkedList<Integer>();
        for (int nodeID : node_map.get(location).getNeighborIds()) { // this is a junk function for edge data
            if (!visited.contains(nodeID)){
                newFrontiers.add(nodeID);
            }
        }
        return newFrontiers;
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

    public String[] getPathAsStrings(ArrayList<Integer> shortestPath){
        String[] longNames = new String[shortestPath.size()];
        for (int i = 0; i < shortestPath.size(); i++) {
            longNames[i] = Repository.getRepository().getLongNameFromNodeID(shortestPath.get(i));
        }
        return longNames;
    }

    void printPath(int parent[], int j) {
        if (parent[j] == 0) {
            System.out.print(j + " ");
            return;
        }
        printPath(parent, parent[j]);
        System.out.print(j + " ");
    }
    public void init_pathfinder() throws SQLException {}
    public HashMap<Integer, Node> get_node_map() {return null;}

    @Override
    public void force_init() throws SQLException {}



}
