package edu.wpi.teamb.testEntities;

import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.teamb.entities.requests.IRequest;
import edu.wpi.teamb.entities.requests.MealRequestE;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestMealRequestE {
  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  String dateString = "2014-02-11";

  Date testDate = Calendar.getInstance().getTime();
  Date testDate2 = sdf.parse(dateString);
  MealRequestE mealRequestE1 = new MealRequestE("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
  MealRequestE mealRequestE2 = new MealRequestE("John Smith", "Floor 1", "Room 101", testDate, "McDonalds", "Burger", "Coke", "Fries", "No ketchup", IRequest.RequestStatus.Pending);
  MealRequestE mealRequestE3 = new MealRequestE("John Smither", "Floor 3", "Room 105", testDate2, "Five Guys", "Hot Dog", "Sprite", "Waffle Fries", "Gluten free", IRequest.RequestStatus.InProgress);

  public TestMealRequestE() throws ParseException {
  }

  @Test
  void testMealRequestEquals() {
    assertTrue(mealRequestE1.equals(mealRequestE2));
    assertFalse(mealRequestE1.equals(mealRequestE3));
  }

  @Test
  void testMealRequestHashCodeEquals() {
    assertEquals(mealRequestE1.hashCode(), mealRequestE2.hashCode());
    assertNotEquals(mealRequestE1.hashCode(), mealRequestE3.hashCode());
  }

  @Test
  void testMealRequestGetEmployee() {
    assertEquals("John Smith", mealRequestE1.getEmployee());
    assertNotEquals("Jane Doe", mealRequestE3.getEmployee());
  }

  @Test
  void testMealRequestGetDrink() {
    assertEquals("Coke", mealRequestE1.getDrink());
    assertNotEquals("Coke", mealRequestE3.getDrink());
  }

  @Test
  void testMealRequestGetRequestStatus() {
    assertEquals(IRequest.RequestStatus.Pending, mealRequestE1.getRequestStatus());
    assertNotEquals(IRequest.RequestStatus.Pending, mealRequestE3.getRequestStatus());
  }

  @Test
  void testMealRequestGetSnack() {
    assertEquals("Fries", mealRequestE1.getSnack());
    assertNotEquals("Fries", mealRequestE3.getSnack());
  }

  @Test
  void testMealRequestGetMealModification() {
    assertEquals("No ketchup", mealRequestE1.getMealModification());
    assertNotEquals("Fries", mealRequestE3.getMealModification());
  }

  @Test
  void testMealRequestGetFood() {
    assertEquals("Burger", mealRequestE1.getFood());
    assertNotEquals("Fries", mealRequestE3.getFood());
  }

  @Test
  void testMealRequestGetOrderFrom() {
    assertEquals("McDonalds", mealRequestE1.getOrderFrom());
    assertNotEquals("McDonalds", mealRequestE3.getOrderFrom());
  }

  @Test
  void testMealRequestGetDateSubmitted() {
    assertEquals(testDate, mealRequestE1.getDateSubmitted());
    assertNotEquals(testDate, mealRequestE3.getDateSubmitted());
  }

  @Test
  void testMealRequestGetFloor() {
    assertEquals("Floor 1", mealRequestE1.getFloor());
    assertNotEquals("Floor 1", mealRequestE3.getFloor());
  }

  @Test
  void testMealRequestGetRoomNumber() {
    assertEquals("Room 101", mealRequestE1.getRoomNumber());
    assertNotEquals("Room 101", mealRequestE3.getRoomNumber());
  }

  @Test
  void testMealRequestGetRequestType() {
    assertEquals(IRequest.RequestType.MealDelivery, mealRequestE1.getRequestType());
    assertNotEquals(IRequest.RequestType.ConferenceRoom, mealRequestE3.getRequestType());
  }

  @Test
  void testMealRequestSetters() {
    MealRequestE mealRequestE4 = new MealRequestE();
    mealRequestE4.setEmployee("John Smith");
    mealRequestE4.setFloor("Floor 1");
    mealRequestE4.setRoomNumber("Room 101");
    mealRequestE4.setDateSubmitted(testDate);
    mealRequestE4.setOrderFrom("McDonalds");
    mealRequestE4.setFood("Burger");
    mealRequestE4.setDrink("Coke");
    mealRequestE4.setSnack("Fries");
    mealRequestE4.setMealModification("No ketchup");
    mealRequestE4.setRequestStatus(IRequest.RequestStatus.Pending);
    assertTrue(mealRequestE1.equals(mealRequestE4));
    assertFalse(mealRequestE3.equals(mealRequestE4));
  }
}
