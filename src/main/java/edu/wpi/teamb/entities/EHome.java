package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.Alert;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.util.ArrayList;

public class EHome {
    public EHome() {}

    public ArrayList<Request> getAllRequests() {
        return Repository.getRepository().getAllRequests();
    }

    public FullMealRequest getMealRequest(int id) {
        return Repository.getRepository().getMealRequest(id);
    }
    public FullConferenceRequest getConferenceRequest(int id) {
        return Repository.getRepository().getConferenceRequest(id);
    }

    public FullFlowerRequest getFlowerRequest(int id) {
        return Repository.getRepository().getFlowerRequest(id);
    }
    public FullOfficeRequest getOfficeRequest(int id) {
        return Repository.getRepository().getOfficeRequest(id);
    }

    public FullFurnitureRequest getFurnitureRequest(int id) {
        return Repository.getRepository().getFurnitureRequest(id);
    }

    public ArrayList<Alert> getAllAlerts() {return Repository.getRepository().getAllAlerts();}
}
