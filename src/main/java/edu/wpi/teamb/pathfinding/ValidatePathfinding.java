package edu.wpi.teamb.pathfinding;

import edu.wpi.teamb.DBAccess.DButils;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is used to validate the pathfinding algorithms
 */
public class ValidatePathfinding {
    public static void main(String[] args) throws SQLException {
        DButils.connectToDB();
        ArrayList<Integer> path = new ArrayList<Integer>();
        Pathfinder pathfinder = new Pathfinder();
        path = pathfinder.Astar(1135,1145);
        System.out.println(path);

        path = pathfinder.Astar(1135,1630);
        System.out.println(path);
        path = pathfinder.Astar(1145,1135);
        System.out.println(path);
    }
}
