package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.Full.FullFlowerRequest;
import edu.wpi.teamb.DBAccess.Full.FullMealRequest;
import edu.wpi.teamb.DBAccess.ORMs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.ArrayList;

public class FullTest {

    @Test
    public void testFullConferenceRequest() {

        FullConferenceRequest fullconferencerequest = new FullConferenceRequest();
        fullconferencerequest.setId(100);
        fullconferencerequest.setDateRequested(Timestamp.valueOf("2020-01-01 00:00:00"));
        fullconferencerequest.setBookingReason("Test Booking Reason");
        fullconferencerequest.setEmployee("Test Employee");
        fullconferencerequest.setDuration(100);
        fullconferencerequest.setRequestType("Conference");
        fullconferencerequest.setDateSubmitted(Timestamp.valueOf("2022-01-01 00:10:40"));
        fullconferencerequest.setNotes("Test Notes");
        fullconferencerequest.setEventName("Test Event Name");
        fullconferencerequest.setLocationName("Test Location Name");
        fullconferencerequest.setRequestStatus("Test Request Status");

        assertEquals(fullconferencerequest.getId(), 100);
        assertEquals(fullconferencerequest.getDateRequested(), Timestamp.valueOf("2020-01-01 00:00:00"));
        assertEquals(fullconferencerequest.getBookingReason(), "Test Booking Reason");
        assertEquals(fullconferencerequest.getEmployee(), "Test Employee");
        assertEquals(fullconferencerequest.getDuration(), 100);
        assertEquals(fullconferencerequest.getRequestType(), "Conference");
        // assertEquals(fullconferencerequest.getDateSubmitted(),
        // Timestamp.valueOf("2022-01-01 00:10:40"));
        // assertEquals(fullconferencerequest.getNotes(), "Test Notes");
        // assertEquals(fullconferencerequest.getEventName(), "Test Event Name");
        // assertEquals(fullconferencerequest.getLocationName(), "Test Location Name");
        // assertEquals(fullconferencerequest.getRequestStatus(), "Test Request
        // Status");

    }








    
    @Test
    public void testFullMealRequest() {

        FullMealRequest fmr = new FullMealRequest();
        assertNotNull(fmr);
        // test innital values
        assertEquals(fmr.getOrderFrom(), "");
        assertEquals(fmr.getFood(), "");
        assertEquals(fmr.getDrink(), "");
        assertEquals(fmr.getSnack(), "");

        // setters
        fmr.setId(100);
        fmr.setEmployee("Test Employee");

        fmr.setRequestType("Meal");
        fmr.setDateSubmitted(Timestamp.valueOf("2022-01-01 00:10:40"));
        fmr.setNotes("Test Notes");
        fmr.setLocationName("Test Location Name");
        fmr.setRequestStatus("Test Request Status");
        fmr.setRequestType();

        fmr.setDrink("blood");
        fmr.setFood("beans");
        fmr.setSnack("shoelaces");
        fmr.setOrderFrom("Your mom");

        // tests
        assertEquals(fmr.getId(), 100);
        assertEquals(fmr.getEmployee(), "Test Employee");
        assertEquals(fmr.getRequestType(), "Meal");
        assertEquals(fmr.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
        assertEquals(fmr.getNotes(), "Test Notes");
        assertEquals(fmr.getLocationName(), "Test Location Name");
        assertEquals(fmr.getRequestStatus(), "Test Request Status");
        assertEquals(fmr.getRequestType(), "Meal");

        assertEquals(fmr.getDrink(), "blood");
        assertEquals(fmr.getFood(), "beans");
        assertEquals(fmr.getSnack(), "shoelaces");
        assertEquals(fmr.getOrderFrom(), "Your mom");

        // TODO: Test listFullReuqests
        ArrayList<MealRequest> mr = new ArrayList<MealRequest>();
        ArrayList<FullMealRequest> fmrs = new ArrayList<FullMealRequest>();
        MealRequest fr = new MealRequest(258, "Cafe", "Pizza", "Coca-cola", "Apple");
        Request r = RequestDAOImpl.getRequest(fr.getId());
        FullMealRequest b = new FullMealRequest(r, fr);
        fmrs.add(b);
        mr.add(fr);
        assertEquals(((ArrayList<FullMealRequest>) fmr.listFullRequests(mr)).get(0).getId(), fmrs.get(0).getId());
        assertEquals(((ArrayList<FullMealRequest>) fmr.listFullRequests(mr)).get(0).getDrink(), fmrs.get(0).getDrink());
        assertEquals(((ArrayList<FullMealRequest>) fmr.listFullRequests(mr)).get(0).getFood(), fmrs.get(0).getFood());
        assertEquals(((ArrayList<FullMealRequest>) fmr.listFullRequests(mr)).get(0).getSnack(), fmrs.get(0).getSnack());

    }

    @Test
    void testFullFlowerRequest() {
        FullFlowerRequest ffr = new FullFlowerRequest();
        assertNotNull(ffr);
        // test innital values
        assertNull(ffr.getFlowerType());
        assertNull(ffr.getColor());
        assertNull(ffr.getSize());

        // setters
        ffr.setId(100);
        ffr.setEmployee("Test Employee");
        ffr.setDateSubmitted(Timestamp.valueOf("2022-01-01 00:10:40"));
        ffr.setNotes("Test Notes");
        ffr.setLocationName("Test Location Name");
        ffr.setRequestStatus("Test Request Status");
        ffr.setRequestType();

        ffr.setFlowerType("blood");
        ffr.setColor("beans");
        ffr.setSize("shoelaces");

        // tests
        assertEquals(ffr.getId(), 100);
        assertEquals(ffr.getEmployee(), "Test Employee");
        assertEquals(ffr.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
        assertEquals(ffr.getNotes(), "Test Notes");
        assertEquals(ffr.getLocationName(), "Test Location Name");
        assertEquals(ffr.getRequestStatus(), "Test Request Status");
        assertEquals(ffr.getRequestType(), "Flower");

        assertEquals(ffr.getFlowerType(), "blood");
        assertEquals(ffr.getColor(), "beans");
        assertEquals(ffr.getSize(), "shoelaces");

        // TODO: Test listFullReuqests
        ArrayList<FlowerRequest> fr = new ArrayList<FlowerRequest>();
        ArrayList<FullFlowerRequest> ffrs = new ArrayList<FullFlowerRequest>();
        FlowerRequest f = new FlowerRequest(190, "Lily", "Yellow", "Single Flower", "");
        Request r = RequestDAOImpl.getRequest(f.getId());
        FullFlowerRequest b = new FullFlowerRequest(r, f);
        ffrs.add(b);
        fr.add(f);
        assertEquals(((ArrayList<FullFlowerRequest>) ffr.listFullRequests(fr)).get(0).getId(), ffrs.get(0).getId());
        assertEquals(((ArrayList<FullFlowerRequest>) ffr.listFullRequests(fr)).get(0).getFlowerType(),
                ffrs.get(0).getFlowerType());
        assertEquals(((ArrayList<FullFlowerRequest>) ffr.listFullRequests(fr)).get(0).getColor(),
                ffrs.get(0).getColor());
        assertEquals(((ArrayList<FullFlowerRequest>) ffr.listFullRequests(fr)).get(0).getSize(), ffrs.get(0).getSize());

    }

}
