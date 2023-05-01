package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Node;
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

    public ArrayList<String> getAllLongnames() {return Repository.getRepository().getAllLongNames();}

    public int[] getSignXandY(String signageGroup) {
        ArrayList<Sign> signs = Repository.getRepository().getAllSigns();
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signageGroup)) {
                String signLocation = s.getLocationName(); // change to get sign location (Info Node 19 Floor 2)
//                System.out.println("location name: "+ signLocation);
                int nodeID = 2170;
//                System.out.println("node id: "+ nodeID);
                Node n = Repository.getRepository().getNode(nodeID);
                return new int[] {n.getxCoord(), n.getyCoord()};
            }
        }
        int nodeID = 2170;
        Node n = Repository.getRepository().getNode(nodeID);
        return new int[] {n.getxCoord(), n.getyCoord()};
    }

    public FullNode getSignNode(String signageGroup) {
        FullNode n = new FullNode(1,1,1,"","","","","");
        ArrayList<Sign> signs = Repository.getRepository().getAllSigns();
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signageGroup)) {
                String signLocation = s.getLocationName();
                int nodeID = 2170;
                n = Repository.getRepository().getFullNode(nodeID);
            }
        }
        return n;
    }
}
