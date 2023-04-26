package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.Full.FullFlowerRequest;
import edu.wpi.teamb.DBAccess.Full.FullMealRequest;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.entities.requests.EAllRequests;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EntitiesTest {


    @Test
    public void TestEAllRequests() {
        EAllRequests AR = new EAllRequests();


        FullMealRequest MR = AR.getMealRequest(258);
        FullMealRequest MR2 = Repository.getRepository().getMealRequest(258);

        assertEquals(MR.getRequestType(), MR2.getRequestType());
        assertEquals(MR.getRequestStatus(), MR2.getRequestStatus());
        assertEquals(MR.getId(), MR2.getId());
        assertEquals(MR.getEmployee(), MR2.getEmployee());
        assertEquals(MR.getDateSubmitted(), MR2.getDateSubmitted());
        assertEquals(MR.getDrink(), MR2.getDrink());



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
        FullConferenceRequest CR = AR.getConferenceRequest(id);

        assertEquals(CR.getRequestType(), "Conference");
        assertEquals(CR.getRequestStatus(), "Pending");
        assertEquals(CR.getId(), id);
        assertEquals(CR.getEmployee(), "TestConf");
        assertEquals(CR.getDateSubmitted(), fcR.getDateSubmitted());
        Repository.getRepository().deleteConferenceRequest(fcR);



        Repository.getRepository().addFlowerRequest(new String[] { "TestConf", "Pending", "BTM Conference Center",
                "alphabet", "9999-09-09 00:00:00.0", "TestConfEvent", "TestConfReason", "-1", "1" });
        ArrayList<FullFlowerRequest> b = Repository.getRepository().getAllFlowerRequests();
        int idFR = -1;
        FullFlowerRequest ffR= null;
        for (FullFlowerRequest i : b) {
            if (i.getNotes() == "alphabet") {
                idFR = i.getId();
                ffR = i;
                break;
            }

        }
        FullFlowerRequest FR = AR.getFlowerRequest(idFR);

        assertEquals(FR.getRequestType(), "Flower");
        assertEquals(FR.getRequestStatus(), "Pending");
        assertEquals(FR.getId(), idFR);
        assertEquals(FR.getEmployee(), "TestConf");
        assertEquals(FR.getDateSubmitted(), ffR.getDateSubmitted());

        Repository.getRepository().deleteFlowerRequest(ffR);

    }

    @Test
    public void TestEPathfinder() {

    }
}
