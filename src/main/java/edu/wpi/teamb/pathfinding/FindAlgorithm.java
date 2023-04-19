package edu.wpi.teamb.pathfinding;

import java.sql.SQLException;
import java.util.ArrayList;

public class FindAlgorithm {

    private PathFindingAlgorithm algorithm;

    public void FindAlgorithm(PathFindingAlgorithm algorithm){
        this.algorithm = algorithm;
    }

    public ArrayList<Integer> findPath(int start, int goal) throws SQLException {
        return algorithm.findPath(start,goal);
    }

}
