package edu.wpi.teamb.entities.components;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.User;

import java.util.ArrayList;


public class InfoCard {
    public ArrayList<User> getUsernames() {
        return Repository.getRepository().getAllUsers();
    }

    public ArrayList<LocationName> getLocationNames() {
        return Repository.getRepository().getAllLocationNames();
    }

}
