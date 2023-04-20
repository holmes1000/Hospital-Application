package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.MealRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public FullConferenceRequest getConfenferenceRequest(int id) {
        return Repository.getRepository().getConferenceRequest(id);
    }

    public FullFlowerRequest getFlowerRequest(int id) {
        return Repository.getRepository().getFlowerRequest(id);
    }
    public FullOfficeRequest getOfficeRequest(int id) {
        return Repository.getRepository().getOfficeRequest(id);
    }

    public ArrayList<String> getUsernames() {
        try{
            ResultSet usernames = DB.getCol("users", "username");
            ArrayList<String> userList = new ArrayList<String>();
            while(usernames.next()){
                userList.add(usernames.getString("username"));
            }
            usernames.close();
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FullFurnitureRequest getFurnitureRequest(int id) {
        return Repository.getRepository().getFurnitureRequest(id);
    }
}
