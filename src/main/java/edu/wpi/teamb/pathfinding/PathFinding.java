package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public enum PathFinding {
    ASTAR(new AStarAlgorithmI()),
    DEPTH_FIRST(new DepthFirstSearchAlgorithmI()),
    BREADTH_FIRST(new BreadthFirstSearchAlgorithmI()),
    ELEVATOR_BIAS(new AStarAlgorithmElevatorBiasI()),
    STAIR_BIAS(new AStarAlgorithmStairsBiasI()),
    DIJKSTRA(new DijkstraAlgorithmI());


    private IPathFindingAlgorithm algorithm;
    //private static PathFinding algoType = ASTAR;

    PathFinding(IPathFindingAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    public ArrayList<Integer> findPath(int start, int goal) throws SQLException {
        return algorithm.findPath(start, goal);
    }

    public void init_pathfinder() throws SQLException {this.algorithm.init_pathfinder();}

    public void force_init() throws SQLException {this.algorithm.force_init();}

    public void setAlgorithm(IPathFindingAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public HashMap<Integer, Node> get_node_map() {return this.algorithm.get_node_map();}

    public String[] getPathAsStrings(ArrayList<Integer> shortestPath) {return this.algorithm.getPathAsStrings(shortestPath);};

    public ArrayList<FullNode> getFullNodes() {return this.algorithm.getFullNodes();}

//    private static class SingletonHolder {
//        private static final PathFinding INSTANCE = new PathFinding();
//    }
//
//    public static PathFinding getInstance() {
//        return SingletonHolder.INSTANCE;
//    }

//    public static void setAlgoType(PathFinding context) {
//        algoType = context;
//    }

//    public static PathFindingAlgorithm getAlgoType(){
//        return algorithm;
//    }

//    public static PathFinding getInstance() {
//        return algoType;
//    }

}
