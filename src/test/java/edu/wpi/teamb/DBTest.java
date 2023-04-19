//package edu.wpi.teamb;
//
//import edu.wpi.teamb.DBAccess.*;
//import edu.wpi.teamb.DBAccess.DAO.Repository;
//import edu.wpi.teamb.DBAccess.ORMs.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.sql.*;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//
//import static org.assertj.core.api.AssertionsForClassTypes.not;
////import static org.junit.Assert.assertEquals;
////import static org.junit.Assert.fail;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.testfx.assertions.api.Assertions.assertThat;
//
//
//public class DBTest {
//    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//
//    @Test
//    public void testgetRowtrue() throws SQLException {
//        //DB.connectToDB();
//        ResultSet query = DB.getRowCond("Users", "*", "username like 'testEmployee'");
//        String username = null;
//        String password = null;
//        Integer permissionlevel = null;
//        String position = null;
//        while (query.next()) {
//            username = query.getString("username");
//            password = query.getString("password");
//            permissionlevel = query.getInt("permissionlevel");
//            position = query.getString("position");
//        }
//        Assertions.assertEquals(username, "testEmployee");
//        Assertions.assertEquals(password, "testEmployee");
//        Assertions.assertEquals(permissionlevel, 1);
//        Assertions.assertEquals(position, "Nurse");
//    }
//
//    @Test
//    public void testgetRowfalse() throws SQLException {
//        //DB.connectToDB();
//        ResultSet rs = DB.getRowCond("Users", "*", "username = 'testEmployee'");
//        String username = null;
//        String password = null;
//        Integer permissionlevel = null;
//        String position = null;
//        while (rs.next()) {
//            username = rs.getString("username");
//            password = rs.getString("password");
//            permissionlevel = rs.getInt("permissionlevel");
//            position = rs.getString("position");
//        }
//        Assertions.assertNotEquals("testEmployee1", username);
//        Assertions.assertNotEquals("testEmployee1", password);
//        Assertions.assertNotEquals(2, permissionlevel);
//        Assertions.assertNotEquals("position", position);
//    }
//    @Test
//    public void testgetDBRowUsernameTrue() throws SQLException {
//        //DB.connectToDB();
//        ResultSet query = Repository.getDBRowUsername("testAdmin");
//        String username = null;
//        String password = null;
//        Integer permissionlevel = null;
//        String position = null;
//        while (query.next()) {
//            username = query.getString("username");
//            password = query.getString("password");
//            permissionlevel = query.getInt("permissionlevel");
//            position = query.getString("position");
//        }
//        Assertions.assertEquals(username, "testAdmin");
//        Assertions.assertEquals(password, "password");
//        Assertions.assertEquals(permissionlevel, 0);
//        Assertions.assertEquals(position, "Doctor");
//    }
//
//    @Test
//    public void testgetDBRowUsernameFalse() throws SQLException {
//        //DB.connectToDB();
//        ResultSet query;
//        query = Repository.getDBRowUsername("testAdmin");
//        String username = null;
//        String password = null;
//        Integer permissionlevel = null;
//        String position = null;
//        while (query.next()) {
//            username = query.getString("username");
//            password = query.getString("password");
//            permissionlevel = query.getInt("permissionlevel");
//            position = query.getString("position");
//        }
//        Assertions.assertNotEquals("testEmployee1", username);
//        Assertions.assertNotEquals("testEmployee1", password);
//        Assertions.assertNotEquals(2, permissionlevel);
//        Assertions.assertNotEquals("position", position);
//    }
//    //test update row
//    @Test
//    public void updatePasswordTrue() {
//        //DB.connectToDB();
//        User testAdmin = Repository.getUser("testAdmin");
//        Assertions.assertEquals("password", testAdmin.getPassword());
//        Repository.updatePassword(testAdmin, "password1");
//        Assertions.assertEquals("password1", testAdmin.getPassword());
//        Repository.updatePassword(testAdmin,"password");
//    }
//
//    @Test
//    public void updatePasswordFalse() {
//        DB.connectToDB();
//        User testAdmin = Repository.getUser("testAdmin");
//        Assertions.assertEquals("password", testAdmin.getPassword());
//        Repository.updatePassword(testAdmin,"password1");
//        Assertions.assertNotEquals("password", testAdmin.getPassword());
//        Repository.updatePassword(testAdmin,"password");
//    }
//
//    @Test
//    public void getDBRowEmployeeTrue() throws SQLException {
//        DB.connectToDB();
//        ResultSet query = Request.getDBRowEmployee("Jane Doe");
//        Integer id = null;
//        String employee = null;
//        String floor = null;
//        String roomNumber = null;
//        Date dateSubmitted = null;
//        String requestStatus = null;
//        String requestType = null;
//        while (query.next()) {
//            id = query.getInt("id");
//            employee = query.getString("employee");
//            floor = query.getString("floor");
//            roomNumber = query.getString("roomNumber");
//            dateSubmitted = query.getDate("dateSubmitted");
//            requestStatus = query.getString("requestStatus");
//            requestType = query.getString("requestType");
//        }
//        Assertions.assertEquals(2, id);
//        Assertions.assertEquals("Jane Doe", employee);
//        Assertions.assertEquals("2",floor);
//        Assertions.assertEquals("201", roomNumber );
//        Assertions.assertEquals("2019-01-02", dateFormat.format(dateSubmitted));
//        Assertions.assertEquals("Pending", requestStatus);
//        Assertions.assertEquals("Meal", requestType);
//    }
//    @Test
//    public void updateRequestStatusTrue() {
//        DB.connectToDB();
//        Request testRequest = Request.getRequest(3);
//        Assertions.assertEquals("Pending", testRequest.getRequestStatus());
//        testRequest.updateRequestStatus("Pending", "Complete");
//        Assertions.assertEquals("Complete", testRequest.getRequestStatus());
//        testRequest.setRequestStatus("Pending");
//    }
//    @Test
//    public void getNodexCoordTrue() {
//        DB.connectToDB();
//        Node testNode = Node.getNode(100);
//        Assertions.assertEquals(2265, testNode.getxCoord());
//    }
//    @Test
//    public void updateFloorTrue() {
//        //DB.connectToDB();
//        Node testNode = Node.getNode(105);
//        Assertions.assertEquals("L2", testNode.getFloor());
//        testNode.updateFloor("L1");
//        Assertions.assertEquals("L1", testNode.getFloor());
//        testNode.updateFloor("L2");
//    }
//
//    //DEBUG getMoveFromNode
////    @Test
////    public void getMoveFromNodeTrue() {
////        DB.connectToDB(url, dbusername, dbpassword);
////        Move testMove = Move.getMoveFromNode(100);
////        //System.out.println(testMove.getLongName());
////        Assertions.assertEquals("Hallway 10 Floor L1", testMove.getLongName());
////    }
//
//    @Test
//    public void getMoveFromLongNameTrue() {
//        DB.connectToDB();
//        Move testMove = Move.getMoveFromLongName("Hallway 10 Floor L1");
//        //System.out.println(testMove.getLongName());
//        Assertions.assertEquals(100, testMove.getNodeID());
//    }
//    //PULL FROM FRONT END
//    @Test
//    public void getMealRequestTrue() {
//        DB.connectToDB();
//        MealRequest testMRequest = MealRequest.getRequest(1);
//        Integer id = testMRequest.getId();
//        String orderFrom = testMRequest.getOrderFrom();
//        String food = testMRequest.getFood();
//        String drink = testMRequest.getDrink();
//        String snack = testMRequest.getSnack();
//        String mealModification = testMRequest.getMealModification();
//
//        Assertions.assertEquals(1, id);
//        Assertions.assertEquals("Location1", orderFrom);
//        Assertions.assertEquals("Pizza", food);
//        Assertions.assertEquals("Coke", drink );
//        Assertions.assertEquals("Fries", snack);
//        Assertions.assertEquals("No ketchup", mealModification);
//    }
//    @Test
//    public void getLocationNameFromLongName() {
//        DB.connectToDB();
//        LocationName testLocationName = LocationName.getLocationNameFromLongName("Hallway 10 Floor L1");
//        Assertions.assertEquals("Hallway C010L1", testLocationName.getShortName());
//    }
//
//    @Test
//    public void getDBRowStartNodeTrue() throws SQLException {
//        DB.connectToDB();
//        ResultSet rs = Edge.getDBRowStartNode("100");
//        String endnode = null;
//        while (rs.next()) {
//            endnode = rs.getString("endnode");
//        }
//        Assertions.assertEquals("190", endnode);
//    }
//
//    @Test
//    public void updateDBEndNodeTrue() {
//        DB.connectToDB();
//        Edge testEdge = Edge.getEdge("105_1750");
//        Assertions.assertEquals(1750, testEdge.getEndNodeID());
//        testEdge.updateDBEndNode("110");
//        Assertions.assertEquals(110, testEdge.getEndNodeID());
//        testEdge.updateDBEndNode("1750");
//    }
//
//    @Test
//    public void getConfRequestTrue() {
//        DB.connectToDB();
//        ConferenceRequest testCRequest = ConferenceRequest.getRequest(4);
//        Integer id = testCRequest.getId();
//        Date daterequested = testCRequest.getDateRequested();
//        String eventname = testCRequest.getEventName();
//        String bookingreason = testCRequest.getBookingReason();
//
//        Assertions.assertEquals(4, id);
//        Assertions.assertEquals("2019-01-04", dateFormat.format(daterequested));
//        Assertions.assertEquals("Event1", eventname);
//        Assertions.assertEquals("Reason1", bookingreason);
//    }
//
//    @Test
//    public void insertNewRowFromDBTrue() {
//        String[] col = {"employee", "floor", "roomNumber", "dateSubmitted", "requestType", "requestStatus"};
//        String[] val = {"Bob", "2", "202", "2019-02-05", "Pending", "Conference"};
//        DB.insertRow("Requests", col, val);
//        Assertions.assertEquals("Bob", Request.getRequest(42).getEmployee());
//    }
//    @Test
//    public void insertDBRowNewRequesttrue() {
//        String[] val = {"Alisha", "2", "202", "2019-02-05", "Complete", "Conference"};
//        Request.insertDBRowNewRequest(val);
//        Assertions.assertEquals("Alisha", Request.getRequest(47).getEmployee());
//    }
//}
//
