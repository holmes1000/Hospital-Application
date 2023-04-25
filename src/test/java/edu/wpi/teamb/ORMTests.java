package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;

public class ORMTests {
    // JUnit Tests for All ORM classes

    // Test Alert

    @Test
    public void testAlert() {
        Alert alert = new Alert();
        alert.setId(1);
        alert.setTitle("Test Title");
        alert.setDescription("Test Description");
        alert.setCreated_at(Timestamp.valueOf("2020-01-01 00:00:00"));
        assert (alert.getId() == 1);
        assert (alert.getTitle().equals("Test Title"));
        assert (alert.getDescription().equals("Test Description"));
        assert (alert.getCreatedAt().equals(Timestamp.valueOf("2020-01-01 00:00:00")));
    }

    // Test Edge
    @Test
    public void testEdgeCreation() {
        Edge edge = new Edge();
        assertNotNull(edge);
        assertEquals(edge.getEndNodeID(), 0);
        assertEquals(edge.getStartNodeID(), 0);

    }

    @Test
    public void testEdgeCreation2() {
        Edge edge = new Edge(1, 2);
        assertNotNull(edge);
        assertEquals(edge.getEndNodeID(), 2);
        assertEquals(edge.getStartNodeID(), 1);
    }

    @Test
    public void testEdgeCreation3() {
        Node node1 = new Node(1, 1, 1, "Test2", "Test2");
        Node node2 = new Node(2, 2, 2, "Test2", "Test2");
        Edge edge = new Edge();
        edge.setStartNode(node1);
        edge.setEndNode(node2);
        assertNotNull(edge);
        assertEquals(edge.getEndNodeID(), 2);
        assertEquals(edge.getStartNodeID(), 1);

    }

    // Test Node
    @Test
    public void testEmptyNodeCreation() {
        Node node = new Node();
        assertNotNull(node);
        assertEquals(node.getNodeID(), 0);
        assertEquals(node.getxCoord(), 0);
        assertEquals(node.getyCoord(), 0);
        assertEquals(node.getFloor(), "");
        assertEquals(node.getFloorNum(), -1);
        assertEquals(node.getBuilding(), "");
        assertEquals(node.getCost(), 10000);
        assertEquals(node.getConnectedEdges().size(), 0);

    }

    @Test
    public void testNodeCreation() {
        Node node = new Node(1, 1, 1, "L", "Test");
        assertNotNull(node);
        assertEquals(node.getNodeID(), 1);
        assertEquals(node.getxCoord(), 1);
        assertEquals(node.getyCoord(), 1);
        assertEquals(node.getFloor(), "L");
        assertEquals(node.getFloorNum(), 300);
        assertEquals(node.getBuilding(), "Test");
        assertEquals(node.getCost(), 10000);
        assertEquals(node.getConnectedEdges().size(), 0);

    }

    @Test
    public void testConnected() {
        Node node1 = new Node(1, 1, 1, "L", "Test");
        Node node2 = new Node(2, 2, 2, "L", "Test");
        Edge edge = new Edge(1, 2);
        node1.getConnectedEdges().add(edge);
        node2.getConnectedEdges().add(edge);
        assertEquals(node1.getConnectedEdges().size(), 1);
        assertEquals(node2.getConnectedEdges().size(), 1);
        assertEquals(node1.getConnectedEdges().contains(edge), true);
        assertEquals(node2.getConnectedEdges().contains(edge), true);

    }

    @Test
    public void testLongerConnection() {
        Node node1 = new Node(1, 1, 1, "L", "Test");
        Node node2 = new Node(2, 2, 2, "L", "Test");
        Node node3 = new Node(3, 3, 3, "L", "Test");
        Edge edge1 = new Edge(1, 2);
        edge1.setEndNode(node2);
        edge1.setStartNode(node1);
        Edge edge2 = new Edge(2, 3);
        edge2.setEndNode(node3);
        edge2.setStartNode(node2);

        assertEquals(edge1.getEndNodeID(), 2);
        assertEquals(edge1.getStartNodeID(), 1);
        assertEquals(edge2.getEndNodeID(), 3);
        assertEquals(edge2.getStartNodeID(), 2);

        node1.getConnectedEdges().add(edge1);
        node2.getConnectedEdges().add(edge1);
        node2.getConnectedEdges().add(edge2);
        node3.getConnectedEdges().add(edge2);

        assertEquals(node1.getConnectedEdges().size(), 1);
        assertEquals(node2.getConnectedEdges().size(), 2);
        assertEquals(node3.getConnectedEdges().size(), 1);

    }

    // Test LocationName
    @Test
    public void testLocationNameCreation() {
        LocationName locationName = new LocationName();
        assertNotNull(locationName);
        assertEquals(locationName.getNodeType(), "");
        assertEquals(locationName.getLongName(), "");
        assertEquals(locationName.getShortName(), "");
    }

    @Test
    public void testLocationNameCreation2() {
        LocationName locationName = new LocationName("Test", "Test2", "Test3");
        assertNotNull(locationName);
        assertEquals(locationName.getNodeType(), "Test");
        assertEquals(locationName.getLongName(), "Test2");
        assertEquals(locationName.getShortName(), "Test3");
    }

    @Test
    public void testLocationNameCreation3() {
        LocationName m = Repository.getRepository().getLocationName("15 Francis Security Desk Floor 2");
        LocationName locationName = new LocationName("Test", "Test2", "Test3");
        locationName.setNodeType("Test4");
        locationName.setLongName("Test5");
        locationName.setShortName("Test6");
        assertNotNull(locationName);
        assertEquals(locationName.getNodeType(), "Test4");
        assertEquals(locationName.getLongName(), "Test5");
        assertEquals(locationName.getShortName(), "Test6");
    }

    // Test User

    // Test Request

    // Test Move

    // Test FlowerRequest

    // Test ConferenceRequest

    // Test OfficeRequest

    // Test FurnitureRequest

    // Test MealRequest
}
