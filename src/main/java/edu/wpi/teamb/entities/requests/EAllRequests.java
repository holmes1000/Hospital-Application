package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.*;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;

import java.util.ArrayList;

public class EAllRequests {
    private Request request;
    private MealRequest mealRequest;
    private FlowerRequest flowerRequest;
    private  ConferenceRequest conferenceRequest;
    private ArrayList<Request> requests;


    public EAllRequests(){
        request = new Request();
        requests = new ArrayList<Request>();
        mealRequest = new MealRequest();
        conferenceRequest = new ConferenceRequest();
    }
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

    public FullTranslationRequest getTranslationRequest(int id) {
        return Repository.getRepository().getTranslationRequest(id);
    }
}
