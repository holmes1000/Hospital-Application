package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

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

    @Test
    public void testAlertGettersAndSetters() {
        Alert alert = new Alert();
        alert.setId(1);
        alert.setTitle("Test Title");
        alert.setDescription("Test Description");
        alert.setCreated_at(Timestamp.valueOf("2020-01-01 00:00:00"));
        assertEquals(alert.getId(), 1);
        assertEquals(alert.getTitle(), "Test Title");
        assertEquals(alert.getDescription(), "Test Description");
        assertEquals(alert.getCreatedAt(), Timestamp.valueOf("2020-01-01 00:00:00"));
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

    @Test
    public void testEdgeGettersAndSetter() {
        Node n1 = new Node(1, 1, 1, "Test2", "Test2");
        Node n2 = new Node(2, 2, 2, "Test2", "Test2");
        Edge edge = new Edge();
        edge.setStartNode(n1);
        edge.setEndNode(n2);
        assertEquals(edge.getStartNodeID(), 1);
        assertEquals(edge.getEndNodeID(), 2);
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

    @Test
    public void testNodeGettersAndSetters() {
        Node node = new Node();
        node.setNodeID(1);
        node.setxCoord(1);
        node.setyCoord(1);
        node.setFloor("L");
        node.setBuilding("Test");
        node.setCost(1000.0);
        assertEquals(node.getNodeID(), 1);
        assertEquals(node.getxCoord(), 1);
        assertEquals(node.getyCoord(), 1);
        assertEquals(node.getFloor(), "L");
        assertEquals(node.getFloorNum(), -1);
        assertEquals(node.getBuilding(), "Test");
        ;
        assertEquals(node.getCost(), 1000);
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
        assertEquals(locationName.getNodeType(), "Test3");
        assertEquals(locationName.getLongName(), "Test");
        assertEquals(locationName.getShortName(), "Test2");
    }

    @Test
    public void testLocationNameCreation3() {
        LocationName locationName = LocationName.getLocationNameFromLongName("15 Francis Security Desk Floor 2");
        assertNotNull(locationName);
        assertEquals(locationName.getNodeType(), "INFO");
        assertEquals(locationName.getLongName(), "15 Francis Security Desk Floor 2");
        assertEquals(locationName.getShortName(), "Information Desk");
    }

    @Test
    public void testLocationNameGettersAndSetters() {
        LocationName locationName = new LocationName();
        locationName.setNodeType("TEST");
        locationName.setLongName("TEST2");
        locationName.setShortName("TEST3");
        assertEquals(locationName.getNodeType(), "TEST");
        assertEquals(locationName.getLongName(), "TEST2");
        assertEquals(locationName.getShortName(), "TEST3");
    }
    // Test User

    @Test
    public void testEmptyUserCreation() {
        User user = new User();
        assertNotNull(user);
        assertEquals(user.getUsername(), "");
        assertEquals(user.getPassword(), "");
        assertEquals(user.getEmail(), "");
        assertEquals(user.getPassword(), "");
        assertEquals(user.getPermissionLevel(), 0);
    }

    @Test
    public void testUserCreation() {
        User user = new User("Test", "Test2", "Test3", "Test4", 1);
        assertNotNull(user);
        assertEquals(user.getUsername(), "Test2");
        assertEquals(user.getPassword(), "Test3");
        assertEquals(user.getEmail(), "Test4");
        assertEquals(user.getName(), "Test");
        assertEquals(user.getPermissionLevel(), 1);
    }

    @Test
    public void testUserCreation2() {
        User u = Repository.getRepository().getUser("admin");
        assertNotNull(u);
        assertEquals(u.getUsername(), "admin");
        assertEquals(u.getPassword(), "admin");
        assertEquals(u.getEmail(), "admin@wpi.edu");
        assertEquals(u.getName(), "Admin");
    }

    @Test
    public void testUserGettersAndSetters() {
        User u = new User("Test", "Test2", "Test3", "Test4", 1);
        assertNotNull(u);
        assertEquals(u.getUsername(), "Test2");
        assertEquals(u.getPassword(), "Test3");
        assertEquals(u.getEmail(), "Test4");
        assertEquals(u.getName(), "Test");
        assertEquals(u.getPermissionLevel(), 1);

        u.setUsername("Test5");
        u.setPassword("Test6");
        u.setEmail("Test7");
        u.setName("Test8");
        u.setPermissionLevel(2);

        assertEquals(u.getUsername(), "Test5");
        assertEquals(u.getPassword(), "Test6");
        assertEquals(u.getEmail(), "Test7");
        assertEquals(u.getName(), "Test8");
        assertEquals(u.getPermissionLevel(), 2);

    }

    // Test Request

    @Test
    public void testRequestCreation() {
        Request request = new Request();
        assertNotNull(request);
        assertNull(request.getDateSubmitted());
        assertEquals(request.getId(), 0);
        assertEquals(request.getEmployee(), "");
        assertEquals(request.getLocationName(), "");
        assertEquals(request.getNotes(), "");
        assertEquals(request.getRequestStatus(), "");
        assertEquals(request.getRequestType(), "");
    }

    @Test
    public void testRequestCreation2() {
        Request r = new Request(3, "t", Timestamp.valueOf("2019-04-17 00:00:00.0"), "m", "e", "l", "k");
        assertNotNull(r);
        assertEquals(r.getId(), 3);
        assertEquals(r.getEmployee(), "t");
        assertEquals(r.getLocationName(), "l");
        assertEquals(r.getNotes(), "k");
        assertEquals(r.getRequestStatus(), "m");
        assertEquals(r.getRequestType(), "e");
        assertEquals(r.getDateSubmitted(), Timestamp.valueOf("2019-04-17 00:00:00.0"));
    }

    @Test
    public void testRequestGettersAndSetters() {
        Request r = new Request(3, "t", Timestamp.valueOf("2019-04-17 00:00:00.0"), "m", "e", "l", "k");
        assertNotNull(r);
        assertEquals(r.getId(), 3);
        assertEquals(r.getEmployee(), "t");
        assertEquals(r.getLocationName(), "l");
        assertEquals(r.getNotes(), "k");
        assertEquals(r.getRequestStatus(), "m");
        assertEquals(r.getRequestType(), "e");
        assertEquals(r.getDateSubmitted(), Timestamp.valueOf("2019-04-17 00:00:00.0"));

        r.setId(4);
        r.setEmployee("t2");
        r.setLocationName("l2");
        r.setNotes("k2");
        r.setRequestStatus("m2");
        r.setRequestType("e2");
        r.setDateSubmitted(Timestamp.valueOf("2019-04-18 00:00:00.0"));

        assertEquals(r.getId(), 4);
        assertEquals(r.getEmployee(), "t2");
        assertEquals(r.getLocationName(), "l2");
        assertEquals(r.getNotes(), "k2");
        assertEquals(r.getRequestStatus(), "m2");
        assertEquals(r.getRequestType(), "e2");
        assertEquals(r.getDateSubmitted(), Timestamp.valueOf("2019-04-18 00:00:00.0"));

    }

    // Test Move
    @Test
    public void testMoveCreation() {
        Move move = new Move();
        assertNotNull(move);
        assertNull(move.getDate());
        assertEquals(move.getLongName(), "");
        assertEquals(move.getNodeID(), 0);

    }

    @Test
    public void testMoveCreation2() {
        Move move = new Move(1, "Test", Date.valueOf("2019-04-17"));
        assertNotNull(move);
        assertEquals(move.getDate(), Date.valueOf("2019-04-17"));
        assertEquals(move.getLongName(), "Test");
        assertEquals(move.getNodeID(), 1);

    }

    @Test
    public void testMoveGettersAndSetters() {
        Move move = new Move(1, "Test", Date.valueOf("2019-04-17"));
        assertNotNull(move);
        assertEquals(move.getDate(), Date.valueOf("2019-04-17"));
        assertEquals(move.getLongName(), "Test");
        assertEquals(move.getNodeID(), 1);

        move.setDate(Date.valueOf("2019-04-18"));
        move.setLongName("Test2");
        move.setNodeID(2);

        assertEquals(move.getDate(), Date.valueOf("2019-04-18"));
        assertEquals(move.getLongName(), "Test2");
        assertEquals(move.getNodeID(), 2);
    }

    // Test FlowerRequest
    @Test
    public void testEmptyFlowerRequestCreation() {
        FlowerRequest flowerRequest = new FlowerRequest();
        assertNotNull(flowerRequest);
        assertEquals(flowerRequest.getId(), 0);
        assertEquals(flowerRequest.getFlowerType(), "");
        assertEquals(flowerRequest.getColor(), "");
        assertEquals(flowerRequest.getSize(), "");
        assertEquals(flowerRequest.getMessage(), "");

    }

    @Test
    public void testFlowerRequestCreation() {
        FlowerRequest flowerRequest = new FlowerRequest(17, "Test", "Test2", "Test3", "Test4");
        assertNotNull(flowerRequest);
        assertEquals(flowerRequest.getId(), 17);
        assertEquals(flowerRequest.getFlowerType(), "Test");
        assertEquals(flowerRequest.getColor(), "Test2");
        assertEquals(flowerRequest.getSize(), "Test3");
        assertEquals(flowerRequest.getMessage(), "Test4");

    }

    @Test
    public void testFlowerRequestGettersAndSetters() {
        FlowerRequest flowerRequest = new FlowerRequest(17, "Test", "Test2", "Test3", "Test4");
        assertNotNull(flowerRequest);
        assertEquals(flowerRequest.getId(), 17);
        assertEquals(flowerRequest.getFlowerType(), "Test");
        assertEquals(flowerRequest.getColor(), "Test2");
        assertEquals(flowerRequest.getSize(), "Test3");
        assertEquals(flowerRequest.getMessage(), "Test4");

        flowerRequest.setId(18);
        flowerRequest.setFlowerType("Test5");
        flowerRequest.setColor("Test6");
        flowerRequest.setSize("Test7");
        flowerRequest.setMessage("Test8");

        assertEquals(flowerRequest.getId(), 18);
        assertEquals(flowerRequest.getFlowerType(), "Test5");
        assertEquals(flowerRequest.getColor(), "Test6");
        assertEquals(flowerRequest.getSize(), "Test7");
        assertEquals(flowerRequest.getMessage(), "Test8");

    }

    // Test ConferenceRequest

    @Test
    public void testEmptyConferenceRequestCreation() {
        ConferenceRequest conferenceRequest = new ConferenceRequest();
        assertNotNull(conferenceRequest);
        assertEquals(conferenceRequest.getId(), 0);
        assertEquals(conferenceRequest.getEventName(), "");
        assertEquals(conferenceRequest.getBookingReason(), "");
        assertEquals(conferenceRequest.getDuration(), 0);
        assertNull(conferenceRequest.getDateRequested());
    }

    @Test
    public void testConferenceRequestCreation() {
        ConferenceRequest conferenceRequest = new ConferenceRequest(17, Timestamp.valueOf("2019-04-17 00:00:00.0"),
                "Test", "Test2", 1);
        assertNotNull(conferenceRequest);
        assertEquals(conferenceRequest.getId(), 17);
        assertEquals(conferenceRequest.getEventName(), "Test");
        assertEquals(conferenceRequest.getBookingReason(), "Test2");
        assertEquals(conferenceRequest.getDuration(), 1);
        assertEquals(conferenceRequest.getDateRequested(), Timestamp.valueOf("2019-04-17 00:00:00.0"));
    }

    @Test
    public void testConferenceRequestCreation2() {
        Repository.getRepository().addConferenceRequest(new String[] { "TestConf", "Pending", "BTM Conference Center",
                "alphabet", "9999-09-09 00:00:00.0", "TestConfEvent", "TestConfReason", "-1", "1" });
        ArrayList<FullConferenceRequest> a = Repository.getRepository().getAllConferenceRequests();
        int id = -1;
        FullConferenceRequest fcR= null;
        for (FullConferenceRequest i : a) {
            if (i.getNotes() == "alphabet") {
                id = i.getId();
                fcR = i;
                break;
            }

        }
        assertNotEquals(id, -1);
        ConferenceRequest cR = ConferenceRequest.getConfRequest(id);
        assertNotNull(cR);
        assertEquals(cR.getId(), id);
        assertEquals(cR.getEventName(), "TestConfEvent");
        assertEquals(cR.getBookingReason(), "TestConfReason");
        assertEquals(cR.getDuration(), -1);
        assertEquals(cR.getDateRequested(), Timestamp.valueOf("9999-09-09 00:00:00.0"));
        Repository.getRepository().deleteConferenceRequest(fcR);

    }

    @Test
    public void testConferenceRequestGettersAndSetters() {
        ConferenceRequest conferenceRequest = new ConferenceRequest(17, Timestamp.valueOf("2019-04-17 00:00:00.0"),
                "Test", "Test2", 1);
        assertNotNull(conferenceRequest);
        assertEquals(conferenceRequest.getId(), 17);
        assertEquals(conferenceRequest.getEventName(), "Test");
        assertEquals(conferenceRequest.getBookingReason(), "Test2");
        assertEquals(conferenceRequest.getDuration(), 1);
        assertEquals(conferenceRequest.getDateRequested(), Timestamp.valueOf("2019-04-17 00:00:00.0"));

        conferenceRequest.setId(18);
        conferenceRequest.setEventName("Test3");
        conferenceRequest.setBookingReason("Test4");
        conferenceRequest.setDuration(2);
        conferenceRequest.setDateRequested(Timestamp.valueOf("2019-04-18 00:00:00.0"));

        assertEquals(conferenceRequest.getId(), 18);
        assertEquals(conferenceRequest.getEventName(), "Test3");
        assertEquals(conferenceRequest.getBookingReason(), "Test4");
        assertEquals(conferenceRequest.getDuration(), 2);
        assertEquals(conferenceRequest.getDateRequested(), Timestamp.valueOf("2019-04-18 00:00:00.0"));

    }

    // Test OfficeRequest

    @Test
    public void testOfficeRequestCreation() {
        OfficeRequest officeRequest = new OfficeRequest(1, "Test", "Test2", 1);
        assertNotNull(officeRequest);
        assertEquals(officeRequest.getId(), 1);
        assertEquals(officeRequest.getType(), "Test");
        assertEquals(officeRequest.getItem(), "Test2");
        assertEquals(officeRequest.getQuantity(), 1);
    }

    @Test
    public void testOfficeRequestGettersAndSetters() {
        OfficeRequest officeRequest = new OfficeRequest(1, "Test", "Test2", 1);
        assertNotNull(officeRequest);
        assertEquals(officeRequest.getId(), 1);
        assertEquals(officeRequest.getType(), "Test");
        assertEquals(officeRequest.getItem(), "Test2");
        assertEquals(officeRequest.getQuantity(), 1);

        officeRequest.setId(2);
        officeRequest.setType("Test3");
        officeRequest.setItem("Test4");
        officeRequest.setQuantity(2);

        assertEquals(officeRequest.getId(), 2);
        assertEquals(officeRequest.getType(), "Test3");
        assertEquals(officeRequest.getItem(), "Test4");
        assertEquals(officeRequest.getQuantity(), 2);
    }

    // Test FurnitureRequest

    @Test
    public void testEmptyFurnitureRequestCreation() {
        FurnitureRequest fr = new FurnitureRequest();

        assertNotNull(fr);
        assertEquals(fr.getId(), 0);
        assertEquals(fr.getType(), "");
        assertEquals(fr.getModel(), "");
        assertEquals(fr.isAssembly(), false);

    }

    @Test
    public void testFurnitureRequestCreation() {
        FurnitureRequest fr = new FurnitureRequest(1, "Test", "Test2", true);

        assertNotNull(fr);
        assertEquals(fr.getId(), 1);
        assertEquals(fr.getType(), "Test");
        assertEquals(fr.getModel(), "Test2");
        assertEquals(fr.isAssembly(), true);

    }

    @Test

    public void testFurnitureRequestGettersAndSetters() {
        FurnitureRequest fr = new FurnitureRequest(1, "Test", "Test2", true);

        assertNotNull(fr);
        assertEquals(fr.getId(), 1);
        assertEquals(fr.getType(), "Test");
        assertEquals(fr.getModel(), "Test2");
        assertEquals(fr.isAssembly(), true);

        fr.setId(2);
        fr.setType("Test3");
        fr.setModel("Test4");
        fr.setAssembly(false);

        assertEquals(fr.getId(), 2);
        assertEquals(fr.getType(), "Test3");
        assertEquals(fr.getModel(), "Test4");
        assertEquals(fr.isAssembly(), false);

    }

    // Test MealRequest

    @Test
    public void testEmptyMealRequestCreation() {
        MealRequest mr = new MealRequest();
        assertNotNull(mr);
        assertEquals(mr.getId(), 0);
        assertEquals(mr.getOrderFrom(), "");
        assertEquals(mr.getFood(), "");
        assertEquals(mr.getDrink(), "");
        assertEquals(mr.getSnack(), "");
    }

    @Test
    public void testMealRequestCreation() {
        MealRequest mr = new MealRequest(1, "Test", "Test2", "Test3", "Test4");
        assertNotNull(mr);
        assertEquals(mr.getId(), 1);
        assertEquals(mr.getOrderFrom(), "Test");
        assertEquals(mr.getFood(), "Test2");
        assertEquals(mr.getDrink(), "Test3");
        assertEquals(mr.getSnack(), "Test4");
    }

    @Test
    public void testMealRequestGettersAndSetters() {
        MealRequest mr = new MealRequest(1, "Test", "Test2", "Test3", "Test4");
        assertNotNull(mr);
        assertEquals(mr.getId(), 1);
        assertEquals(mr.getOrderFrom(), "Test");
        assertEquals(mr.getFood(), "Test2");
        assertEquals(mr.getDrink(), "Test3");
        assertEquals(mr.getSnack(), "Test4");

        mr.setId(2);
        mr.setOrderFrom("Test5");
        mr.setFood("Test6");
        mr.setDrink("Test7");
        mr.setSnack("Test8");

        assertEquals(mr.getId(), 2);
        assertEquals(mr.getOrderFrom(), "Test5");
        assertEquals(mr.getFood(), "Test6");
        assertEquals(mr.getDrink(), "Test7");
        assertEquals(mr.getSnack(), "Test8");
    }
}
