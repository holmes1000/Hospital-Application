package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Sign;

import java.util.ArrayList;
import java.util.HashSet;

public class ESignage {

    public ESignage () {

    }

    public HashSet<String> getSignageGroups() {
        //replace with repository function
        return Repository.getRepository().getSignageGroups();
    }

    public ArrayList<String> getLocationNames(String signageGroup) {
        return Repository.getRepository().getLocationNames(signageGroup);
    }

    public ArrayList<Sign> getAllSigns() {
        return Repository.getRepository().getAllSigns();
    }
}
