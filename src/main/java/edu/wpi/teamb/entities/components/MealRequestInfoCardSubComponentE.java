package edu.wpi.teamb.entities.components;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;

import java.util.ArrayList;

public class MealRequestInfoCardSubComponentE {

    /**
     * This method is used to get all the location names from the database
     * @return ArrayList of LocationName objects
     */
    public ArrayList<LocationName> getLocationNames() {
        return Repository.getRepository().getAllLocationNames();
    }

    public ArrayList<String> getFoodList() {
        ArrayList<String> foodList = new ArrayList<String>();
        foodList.add("Pizza");
        foodList.add("Pasta");
        foodList.add("Soup");
        return foodList;
    }

    public ArrayList<String> getSnackList() {
        ArrayList<String> snackList = new ArrayList<String>();
        snackList.add("Chips");
        snackList.add("Apple");
        return snackList;
    }

    public ArrayList<String> getDrinksList() {
        ArrayList<String> drinkList = new ArrayList<String>();
        drinkList.add("Water");
        drinkList.add("Coca-Cola");
        drinkList.add("Ginger-Ale");
        return drinkList;
    }
}
