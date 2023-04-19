//package edu.wpi.teamb.testEntities;
//import static org.junit.jupiter.api.Assertions.*;
//import edu.wpi.teamb.entities.requests.ConferenceRequest;
//import edu.wpi.teamb.entities.requests.IRequest;
//import org.junit.jupiter.api.Test;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.text.ParseException;
//
//public class TestConferenceRequest {
//    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    String dateString = "2014-02-11";
//    String dateString2 = "2020-02-11";
//    String dateString3 = "2021-02-11";
//    Date testDateSub = Calendar.getInstance().getTime();
//    Date testDateSub2 = sdf.parse(dateString);
//    Date testDateReq1 = sdf.parse(dateString2);
//    Date testDateReq2 = sdf.parse(dateString3);
//    List<String> availableRooms = Arrays.asList("Room 101", "Room 102", "Room 103", "Room 104", "Room 105");
//    List<String> availableRooms2 = Arrays.asList("Room 101", "Room 102", "Room 103", "Room 104", "Room 105", "Room 106", "Room 107", "Room 108", "Room 109", "Room 110");
//    ConferenceRequest confRequest1 = new ConferenceRequest("John Smith", "Floor 1", "Room 101", testDateSub, testDateReq1, "Meeting", "Emergency", availableRooms, IRequest.RequestStatus.Pending);
//    ConferenceRequest confRequest2 = new ConferenceRequest("John Smith", "Floor 1", "Room 101", testDateSub, testDateReq1, "Meeting", "Emergency", availableRooms, IRequest.RequestStatus.Pending);
//    ConferenceRequest confRequest3 = new ConferenceRequest("John Smither", "Floor 3", "Room 105", testDateSub2, testDateReq2, "Another Meeting", "Want to hang out", availableRooms2, IRequest.RequestStatus.InProgress);
//
//    public TestConferenceRequest() throws ParseException {
//    }
//
//    @Test
//    void testConfRequestEquals() {
//        assertTrue(confRequest1.equals(confRequest2));
//        assertFalse(confRequest1.equals(confRequest3));
//    }
//
//    @Test
//    void testConfRequestHashCodeEquals() {
//        assertEquals(confRequest1.hashCode(),confRequest2.hashCode());
//        assertNotEquals(confRequest1.hashCode(),confRequest3.hashCode());
//    }
//
//    @Test
//    void testConfRequestGetEmployee() {
//        assertEquals("John Smith", confRequest1.getEmployee());
//        assertNotEquals("Jane Doe", confRequest3.getEmployee());
//    }
//
//    @Test
//    void testConfRequestGetRequestStatus() {
//        assertEquals(IRequest.RequestStatus.Pending, confRequest1.getRequestStatus());
//        assertNotEquals(IRequest.RequestStatus.Pending, confRequest3.getRequestStatus());
//    }
//
//    @Test
//    void testConfRequestGetBookingReason() {
//        assertEquals("Emergency", confRequest1.getBookingReason());
//        assertNotEquals("Emergency", confRequest3.getBookingReason());
//    }
//
//    @Test
//    void testConfRequestGetEventName() {
//        assertEquals("Meeting", confRequest1.getEventName());
//        assertNotEquals("Meeting", confRequest3.getEventName());
//    }
//
//    @Test
//    void testConfRequestGetDateRequested() {
//        assertEquals(testDateReq1, confRequest1.getDateRequested());
//        assertNotEquals(testDateReq1, confRequest3.getDateRequested());
//    }
//
//    @Test
//    void testConfRequestGetDateSubmitted() {
//        assertEquals(testDateSub, confRequest1.getDateSubmitted());
//        assertNotEquals(testDateSub, confRequest3.getDateSubmitted());
//    }
//
//    @Test
//    void testConfRequestGetFloor() {
//        assertEquals("Floor 1", confRequest1.getFloor());
//        assertNotEquals("Floor 1", confRequest3.getFloor());
//    }
//
//    @Test
//    void testConfRequestGetRoomNumber() {
//        assertEquals("Room 101", confRequest1.getRoomNumber());
//        assertNotEquals("Room 101", confRequest3.getRoomNumber());
//    }
//
//    @Test
//    void testConfRequestGetRequestType() {
//        assertEquals(IRequest.RequestType.ConferenceRoom, confRequest1.getRequestType());
//        assertNotEquals(IRequest.RequestType.MealDelivery, confRequest3.getRequestType());
//    }
//
//    @Test
//    void testGetAvailableRooms() {
//        assertEquals( Arrays.asList("Room 101", "Room 102", "Room 103", "Room 104", "Room 105"), confRequest1.getAvailableRooms());
//        assertNotEquals( Arrays.asList("Room 101", "Room 102", "Room 103", "Room 104", "Room 105"), confRequest3.getAvailableRooms());
//    }
//
//    @Test
//    void testConfRequestSetters() {
//        ConferenceRequest confRequest4 = new ConferenceRequest();
//        confRequest4.setEmployee("John Smith");
//        confRequest4.setFloor("Floor 1");
//        confRequest4.setRoomNumber("Room 101");
//        confRequest4.setDateSubmitted(testDateSub);
//        confRequest4.setDateRequested(testDateReq1);
//        confRequest4.setEventName("Meeting");
//        confRequest4.setBookingReason("Emergency");
//        confRequest4.setAvailableRooms(availableRooms);
//        confRequest4.setRequestStatus(IRequest.RequestStatus.Pending);
//        assertTrue(confRequest1.equals(confRequest4));
//        assertFalse(confRequest3.equals(confRequest4));
//    }
//
//
//
//}
