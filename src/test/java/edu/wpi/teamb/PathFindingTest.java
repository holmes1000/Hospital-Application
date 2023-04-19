package edu.wpi.teamb;
import edu.wpi.teamb.pathfinding.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static edu.wpi.teamb.pathfinding.Pathfinder.printPath;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class PathFindingTest {

    @Test
    public void A_Star1 () throws SQLException {
        ArrayList<Integer> shortestPath = PathFinding.ASTAR.findPath(700,150);

        //PathFindingAlgorithm pathfinder = new PathFindingAlgorithm(algo);

        //These rooms share an edge, so path should be between them both.
        //ArrayList<Integer> shortestPath = graph.Astar(1135, 1140);
        System.out.println(shortestPath);
        System.out.println(printPath(shortestPath));
        assertEquals("Elevator S 01, Elevator S 02", printPath(shortestPath));
    }
    @Test
    public void BREADTH_FIRST () throws SQLException {
        ArrayList<Integer> shortestPath = PathFinding.BREADTH_FIRST.findPath(700,150);

        //PathFindingAlgorithm pathfinder = new PathFindingAlgorithm(algo);

        //These rooms share an edge, so path should be between them both.
        //ArrayList<Integer> shortestPath = graph.Astar(1135, 1140);
        System.out.println(shortestPath);
        System.out.println(printPath(shortestPath));
        assertEquals("Elevator S 01, Elevator S 02", printPath(shortestPath));
    }
    @Test
    public void DEPTH_FIRST1 () throws SQLException {
        ArrayList<Integer> shortestPath = PathFinding.DEPTH_FIRST.findPath(700,150);

        //PathFindingAlgorithm pathfinder = new PathFindingAlgorithm(algo);

        //These rooms share an edge, so path should be between them both.
        //ArrayList<Integer> shortestPath = graph.Astar(1135, 1140);
        System.out.println(shortestPath);
        System.out.println(printPath(shortestPath));
        assertEquals("Elevator S 01, Elevator S 02", printPath(shortestPath));
    }

    @Test
    public void A_Star2 () throws SQLException {
        //These rooms share an edge, so path should be between them both.
        Pathfinder graph = new Pathfinder();
        ArrayList<Integer> shortestPath = graph.Astar(1135, 1145);
        System.out.println(shortestPath);
        if (shortestPath != null) { System.out.println(printPath(shortestPath));
            assertEquals("Elevator S 01, Elevator S Floor 3", printPath(shortestPath));
        }

    }

    @Test
    public void A_Star0 () throws SQLException {
        //These rooms share an edge, so path should be between them both.
        Pathfinder graph = new Pathfinder();
        ArrayList<Integer> shortestPath = graph.Astar(700, 150);
        System.out.println(shortestPath);
        if (shortestPath != null) { System.out.println(printPath(shortestPath));
            assertEquals("Elevator S 01, Elevator S Floor 3", printPath(shortestPath));
        }

    }

    @Test
    public void A_Star00 () throws SQLException {
        //These rooms share an edge, so path should be between them both.
        Pathfinder graph = new Pathfinder();
        ArrayList<Integer> shortestPath = graph.bfSearch(700, 150);
        System.out.println(shortestPath);
        if (shortestPath != null) { System.out.println(printPath(shortestPath));
            assertEquals("Elevator S 01, Elevator S Floor 3", printPath(shortestPath));
        }

    }

    @Test
    public void A_Star3 () throws SQLException {
        //These rooms share an edge, so path should be between them both.
        Pathfinder graph = new Pathfinder();
        ArrayList<Integer> shortestPath = graph.Astar(1755, 155);
        System.out.println(shortestPath);
        if (shortestPath != null) { System.out.println(printPath(shortestPath));}
        assertEquals("Hallway 5 Floor L1, Elevator M Floor L1, Outpatient Fluoroscopy Floor L1, Restroom M Elevator Floor L1, Hallway 6 Floor L1, Nuclear Medicine Floor L1", printPath(shortestPath));
    }

    @Test
    public void testPrintPath() {
        ArrayList<Integer> myShortestPath = new ArrayList<Integer>();
        myShortestPath.add(2315);
        myShortestPath.add(2030);
        myShortestPath.add(2275);
        myShortestPath.add(1875);

        System.out.println(printPath(myShortestPath));
        assertEquals("Elevator Q MapNode 6 Floor L2, Elevator Q Node 31a Floor 2, Elevator Q MapNode 7 Floor L1, Elevator Q MapNode 18 Floor 1", printPath(myShortestPath));
    }
}
