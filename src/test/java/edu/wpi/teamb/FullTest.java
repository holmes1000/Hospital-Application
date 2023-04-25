package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DAO.RequestDAOImpl;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
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
        assertEquals(fullconferencerequest.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
        assertEquals(fullconferencerequest.getNotes(), "Test Notes");
        assertEquals(fullconferencerequest.getEventName(), "Test Event Name");
        assertEquals(fullconferencerequest.getLocationName(), "Test Location Name");
        assertEquals(fullconferencerequest.getRequestStatus(), "Test Request Status");


        ArrayList<ConferenceRequest> mr = new ArrayList<ConferenceRequest>();
        ArrayList<FullConferenceRequest> fmrs = new ArrayList<FullConferenceRequest>();
        ConferenceRequest fr = new ConferenceRequest(258, Timestamp.valueOf("2020-01-01 00:00:00"), "Test Event Name", "Test Booking Reason", 100);
        Request r = RequestDAOImpl.getRequest(fr.getId());
        FullConferenceRequest b = new FullConferenceRequest(r, fr);
        fmrs.add(b);
        mr.add(fr);
        assertEquals(((ArrayList<FullConferenceRequest>) fullconferencerequest.listFullRequests(mr)).get(0).getId(), fmrs.get(0).getId());
        assertEquals(((ArrayList<FullConferenceRequest>) fullconferencerequest.listFullRequests(mr)).get(0).getRequestType(), fmrs.get(0).getRequestType());
        assertEquals(((ArrayList<FullConferenceRequest>) fullconferencerequest.listFullRequests(mr)).get(0).getEventName(), fmrs.get(0).getEventName());
        assertEquals(((ArrayList<FullConferenceRequest>) fullconferencerequest.listFullRequests(mr)).get(0).getBookingReason(), fmrs.get(0).getBookingReason());


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

    @Test
    public void testFullFurnitureRequest() {
        FullFurnitureRequest FFR = new FullFurnitureRequest();
        FFR.setId(100);
        FFR.setEmployee("Test Employee");
        FFR.setRequestType();
        FFR.setDateSubmitted(Timestamp.valueOf("2022-01-01 00:10:40"));
        FFR.setNotes("Test Notes");
        FFR.setLocationName("Test Location Name");
        FFR.setRequestStatus("Test Request Status");
        FFR.setAssembly(true);
        FFR.setModel("Test Model");
        FFR.setType("Test Type");


        assertEquals(FFR.getId(), 100);
        assertEquals(FFR.getEmployee(), "Test Employee");
        assertEquals(FFR.getRequestType(), "Furniture");
        assertEquals(FFR.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
        assertEquals(FFR.getNotes(), "Test Notes");
        assertEquals(FFR.getLocationName(), "Test Location Name");
        assertEquals(FFR.getRequestStatus(), "Test Request Status");
        assertEquals(FFR.getAssembly(), true);
        assertEquals(FFR.getModel(), "Test Model");
        assertEquals(FFR.getType(), "Test Type");


        ArrayList<FurnitureRequest> mr = new ArrayList<FurnitureRequest>();
        ArrayList<FullFurnitureRequest> fmrs = new ArrayList<FullFurnitureRequest>();
        FurnitureRequest fr = new FurnitureRequest(258, "Furniture", "Test Model", true);
        Request r = RequestDAOImpl.getRequest(fr.getId());
        FullFurnitureRequest b = new FullFurnitureRequest(r, fr);
        fmrs.add(b);
        mr.add(fr);
        assertEquals(((ArrayList<FullFurnitureRequest>) FFR.listFullRequests(mr)).get(0).getId(), fmrs.get(0).getId());
        assertEquals(((ArrayList<FullFurnitureRequest>) FFR.listFullRequests(mr)).get(0).getRequestType(), fmrs.get(0).getRequestType());
        assertEquals(((ArrayList<FullFurnitureRequest>) FFR.listFullRequests(mr)).get(0).getModel(), fmrs.get(0).getModel());
        assertEquals(((ArrayList<FullFurnitureRequest>) FFR.listFullRequests(mr)).get(0).getAssembly(), fmrs.get(0).getAssembly());

    }

    @Test
    public void testFullOfficeRequest() {
        FullOfficeRequest FOR = new FullOfficeRequest();
        FOR.setId(100);
        FOR.setEmployee("Test Employee");
        FOR.setRequestType();
        FOR.setDateSubmitted(Timestamp.valueOf("2022-01-01 00:10:40"));
        FOR.setNotes("Test Notes");
        FOR.setLocationName("Test Location Name");
        FOR.setRequestStatus("Test Request Status");
        FOR.setType("Test Type");
        FOR.setNotes("Test Notes");
        FOR.setItem("Test Item");


        assertEquals(FOR.getId(), 100);
        assertEquals(FOR.getEmployee(), "Test Employee");
        assertEquals(FOR.getRequestType(), "Office");
        assertEquals(FOR.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
        assertEquals(FOR.getNotes(), "Test Notes");
        assertEquals(FOR.getLocationName(), "Test Location Name");
        assertEquals(FOR.getRequestStatus(), "Test Request Status");
        assertEquals(FOR.getType(), "Test Type");
        assertEquals(FOR.getNotes(), "Test Notes");
        assertEquals(FOR.getItem(), "Test Item");



        ArrayList<OfficeRequest> mr = new ArrayList<OfficeRequest>();
        ArrayList<FullOfficeRequest> fmrs = new ArrayList<FullOfficeRequest>();
        OfficeRequest fr = new OfficeRequest(258, "Office", "Test Model", 10);
        Request r = RequestDAOImpl.getRequest(fr.getId());
        FullOfficeRequest b = new FullOfficeRequest(r, fr);
        fmrs.add(b);
        mr.add(fr);
        assertEquals(((ArrayList<FullOfficeRequest>) FOR.listFullRequests(mr)).get(0).getId(), fmrs.get(0).getId());
        assertEquals(((ArrayList<FullOfficeRequest>) FOR.listFullRequests(mr)).get(0).getRequestType(), fmrs.get(0).getRequestType());
        assertEquals(((ArrayList<FullOfficeRequest>) FOR.listFullRequests(mr)).get(0).getNotes(), fmrs.get(0).getNotes());
        assertEquals(((ArrayList<FullOfficeRequest>) FOR.listFullRequests(mr)).get(0).getItem(), fmrs.get(0).getItem());
    }
}
