package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.Full.FullMealRequest;
import edu.wpi.teamb.DBAccess.ORMs.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
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
        assertEquals(fullconferencerequest.getRequestType(), "Conference"
//        assertEquals(fullconferencerequest.getDateSubmitted(), Timestamp.valueOf("2022-01-01 00:10:40"));
//        assertEquals(fullconferencerequest.getNotes(), "Test Notes");
//        assertEquals(fullconferencerequest.getEventName(), "Test Event Name");
//        assertEquals(fullconferencerequest.getLocationName(), "Test Location Name");
//        assertEquals(fullconferencerequest.getRequestStatus(), "Test Request Status");


    }

    @Test
    public void testFullMealRequest()
    {

        FullMealRequest fmr = new FullMealRequest();
        //test setters

    }

}
