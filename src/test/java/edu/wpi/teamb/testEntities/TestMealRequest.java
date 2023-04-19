package edu.wpi.teamb.testEntities;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.entities.requests.MealRequest;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestMealRequest {
  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  String dateString = "2014-02-11";

  Date testDate = Calendar.getInstance().getTime();
  Date testDate2 = sdf.parse(dateString);
  MealRequest mealRequest1 = new MealRequest("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
  MealRequest mealRequest2 = new MealRequest("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
  MealRequest mealRequest3 = new MealRequest("John Smither", "Floor 3", "Room 105", testDate2, "Five Guys", "Hot Dog", "Sprite", "Waffle Fries", "Gluten free", IRequest.RequestStatus.InProgress);

  public TestMealRequest() throws ParseException {
  }

  @Test
  void testMealRequestEquals() {
    assertTrue(mealRequest1.equals(mealRequest2));
    assertFalse(mealRequest1.equals(mealRequest3));
  }

  @Test
  void testMealRequestHashCodeEquals() {
    assertEquals(mealRequest1.hashCode(),mealRequest2.hashCode());
    assertNotEquals(mealRequest1.hashCode(),mealRequest3.hashCode());
  }

  @Test
  void testMealRequestGetEmployee() {
    assertEquals("John Smith", mealRequest1.getEmployee());
    assertNotEquals("Jane Doe", mealRequest3.getEmployee());
  }

  @Test
  void testMealRequestGetDrink() {
    assertEquals("Coke", mealRequest1.getDrink());
    assertNotEquals("Coke", mealRequest3.getDrink());
  }

  @Test
  void testMealRequestGetRequestStatus() {
    assertEquals(IRequest.RequestStatus.Pending, mealRequest1.getRequestStatus());
    assertNotEquals(IRequest.RequestStatus.Pending, mealRequest3.getRequestStatus());
  }

  @Test
  void testMealRequestGetSnack() {
    assertEquals("Fries", mealRequest1.getSnack());
    assertNotEquals("Fries", mealRequest3.getSnack());
  }

  @Test
  void testMealRequestGetMealModification() {
    assertEquals("No ketchup", mealRequest1.getMealModification());
    assertNotEquals("Fries", mealRequest3.getMealModification());
  }

  @Test
  void testMealRequestGetFood() {
    assertEquals("Burger", mealRequest1.getFood());
    assertNotEquals("Fries", mealRequest3.getFood());
  }

  @Test
  void testMealRequestGetOrderFrom() {
    assertEquals("McDonalds", mealRequest1.getOrderFrom());
    assertNotEquals("McDonalds", mealRequest3.getOrderFrom());
  }

  @Test
  void testMealRequestGetDateSubmitted() {
    assertEquals(testDate, mealRequest1.getDateSubmitted());
    assertNotEquals(testDate, mealRequest3.getDateSubmitted());
  }

  @Test
  void testMealRequestGetFloor() {
    assertEquals("Floor 1", mealRequest1.getFloor());
    assertNotEquals("Floor 1", mealRequest3.getFloor());
  }

  @Test
  void testMealRequestGetRoomNumber() {
    assertEquals("Room 101", mealRequest1.getRoomNumber());
    assertNotEquals("Room 101", mealRequest3.getRoomNumber());
  }

  @Test
  void testMealRequestGetRequestType() {
    assertEquals(IRequest.RequestType.MealDelivery, mealRequest1.getRequestType());
    assertNotEquals(IRequest.RequestType.ConferenceRoom, mealRequest3.getRequestType());
  }

  @Test
  void testMealRequestSetters() {
    MealRequest mealRequest4 = new MealRequest();
    mealRequest4.setEmployee("John Smith");
    mealRequest4.setFloor("Floor 1");
    mealRequest4.setRoomNumber("Room 101");
    mealRequest4.setDateSubmitted(testDate);
    mealRequest4.setOrderFrom("McDonalds");
    mealRequest4.setFood("Burger");
    mealRequest4.setDrink("Coke");
    mealRequest4.setSnack("Fries");
    mealRequest4.setMealModification("No ketchup");
    mealRequest4.setRequestStatus(IRequest.RequestStatus.Pending);
    assertTrue(mealRequest1.equals(mealRequest4));
    assertFalse(mealRequest3.equals(mealRequest4));
  }
}
