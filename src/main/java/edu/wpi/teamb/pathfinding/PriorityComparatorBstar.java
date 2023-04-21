package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.util.Comparator;

// Compares node costs, used in A star for priority queue sorting
public class PriorityComparatorBstar implements Comparator<Node>{
    public int compare(Node node1, Node node2){
        if (node1.getCost_and_heuristic() < node2.getCost_and_heuristic()){
            return 1;
        }
        else if (node1.getCost_and_heuristic() > node2.getCost_and_heuristic()){
            return -1;
        }
        return 0;
    }
}
