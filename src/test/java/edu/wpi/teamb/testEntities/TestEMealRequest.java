//package edu.wpi.teamb.testEntities;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import edu.wpi.teamb.entities.requests.IRequest;
//import edu.wpi.teamb.entities.requests.EMealRequest;
//import org.junit.jupiter.api.Test;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//
//public class TestEMealRequest {
//  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//  String dateString = "2014-02-11";
//
//  Date testDate = Calendar.getInstance().getTime();
//  Date testDate2 = sdf.parse(dateString);
//  EMealRequest EMealRequest1 = new EMealRequest("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
//  EMealRequest EMealRequest2 = new EMealRequest("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
//  EMealRequest EMealRequest3 = new EMealRequest("John Smither", "Floor 3", "Room 105", testDate2, "Five Guys", "Hot Dog", "Sprite", "Waffle Fries", "Gluten free", IRequest.RequestStatus.InProgress);
//
//  public TestEMealRequest() throws ParseException {
//  }
//
//  @Test
//  void testMealRequestEquals() {
//    assertTrue(EMealRequest1.equals(EMealRequest2));
//    assertFalse(EMealRequest1.equals(EMealRequest3));
//  }
//
//  @Test
//  void testMealRequestHashCodeEquals() {
//    assertEquals(EMealRequest1.hashCode(), EMealRequest2.hashCode());
//    assertNotEquals(EMealRequest1.hashCode(), EMealRequest3.hashCode());
//  }
//
//  @Test
//  void testMealRequestGetEmployee() {
//    assertEquals("John Smith", EMealRequest1.getEmployee());
//    assertNotEquals("Jane Doe", EMealRequest3.getEmployee());
//  }
//
//  @Test
//  void testMealRequestGetDrink() {
//    assertEquals("Coke", EMealRequest1.getDrink());
//    assertNotEquals("Coke", EMealRequest3.getDrink());
//  }
//
//  @Test
//  void testMealRequestGetRequestStatus() {
//    assertEquals(IRequest.RequestStatus.Pending, EMealRequest1.getRequestStatus());
//    assertNotEquals(IRequest.RequestStatus.Pending, EMealRequest3.getRequestStatus());
//  }
//
//  @Test
//  void testMealRequestGetSnack() {
//    assertEquals("Fries", EMealRequest1.getSnack());
//    assertNotEquals("Fries", EMealRequest3.getSnack());
//  }
//
//  @Test
//  void testMealRequestGetMealModification() {
//    assertEquals("No ketchup", EMealRequest1.getMealModification());
//    assertNotEquals("Fries", EMealRequest3.getMealModification());
//  }
//
//  @Test
//  void testMealRequestGetFood() {
//    assertEquals("Burger", EMealRequest1.getFood());
//    assertNotEquals("Fries", EMealRequest3.getFood());
//  }
//
//  @Test
//  void testMealRequestGetOrderFrom() {
//    assertEquals("McDonalds", EMealRequest1.getOrderFrom());
//    assertNotEquals("McDonalds", EMealRequest3.getOrderFrom());
//  }
//
//  @Test
//  void testMealRequestGetDateSubmitted() {
//    assertEquals(testDate, EMealRequest1.getDateSubmitted());
//    assertNotEquals(testDate, EMealRequest3.getDateSubmitted());
//  }
//
//  @Test
//  void testMealRequestGetFloor() {
//    assertEquals("Floor 1", EMealRequest1.getFloor());
//    assertNotEquals("Floor 1", EMealRequest3.getFloor());
//  }
//
//  @Test
//  void testMealRequestGetRoomNumber() {
//    assertEquals("Room 101", EMealRequest1.getRoomNumber());
//    assertNotEquals("Room 101", EMealRequest3.getRoomNumber());
//  }
//
//  @Test
//  void testMealRequestGetRequestType() {
//    assertEquals(IRequest.RequestType.MealDelivery, EMealRequest1.getRequestType());
//    assertNotEquals(IRequest.RequestType.ConferenceRoom, EMealRequest3.getRequestType());
//  }
//
//  @Test
//  void testMealRequestSetters() {
//    EMealRequest EMealRequest4 = new EMealRequest();
//    EMealRequest4.setEmployee("John Smith");
//    EMealRequest4.setFloor("Floor 1");
//    EMealRequest4.setRoomNumber("Room 101");
//    EMealRequest4.setDateSubmitted(testDate);
//    EMealRequest4.setOrderFrom("McDonalds");
//    EMealRequest4.setFood("Burger");
//    EMealRequest4.setDrink("Coke");
//    EMealRequest4.setSnack("Fries");
//    EMealRequest4.setMealModification("No ketchup");
//    EMealRequest4.setRequestStatus(IRequest.RequestStatus.Pending);
//    assertTrue(EMealRequest1.equals(EMealRequest4));
//    assertFalse(EMealRequest3.equals(EMealRequest4));
//  }
//}
