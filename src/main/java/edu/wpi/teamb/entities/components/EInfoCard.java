package edu.wpi.teamb.entities.components;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.User;

import java.util.ArrayList;


public class EInfoCard {
    public ArrayList<User> getUsernames() {
        return Repository.getRepository().getAllUsers();
    }

    public ArrayList<LocationName> getLocationNames() {
        return Repository.getRepository().getAllLocationNames();
    }

    public void deleteRequest(IFull fullRequest) {
        switch (fullRequest.getRequestType()) {
            case "Meal":
                Repository.getRepository().deleteMealRequest((FullMealRequest) fullRequest);
                break;
            case "Conference":
                Repository.getRepository().deleteConferenceRequest((FullConferenceRequest) fullRequest);
                break;
            case "Flower":
                Repository.getRepository().deleteFlowerRequest((FullFlowerRequest) fullRequest);
                break;
            case "Office":
                Repository.getRepository().deleteOfficeRequest((FullOfficeRequest) fullRequest);
                break;
            case "Furniture":
                Repository.getRepository().deleteFurnitureRequest((FullFurnitureRequest) fullRequest);
                break;
        }
    }
}
