package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DBconnection;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.ORMs.Alert;
import edu.wpi.teamb.DBAccess.ORMs.Sign;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

public class SignDAOImpl implements IDAO {

    private static ArrayList<Sign> signs;


    public SignDAOImpl() {
        signs = getAllHelper();
    }
    @Override
    public Sign get(Object id) {
        String idString = (String) id;
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(idString)) {
                return s;
            }
        } return null;
    }
    @Override
    public ArrayList<Sign> getAll() {
        return signs;
    }

    public ArrayList<Sign> getAllHelper() {
        ArrayList<Sign> signs = new ArrayList<Sign>();
        try {
            ResultSet rs = DButils.getCol("signs", "*");
            while (rs.next()) {
                signs.add(new Sign(rs));
            }
            return signs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'SignDAOImpl.getAllHelper': " + e.getMessage());
        }
        DBconnection.getDBconnection().closeDBconnection();
        DBconnection.getDBconnection().forceClose();
        return signs;
    }

    @Override
    public void setAll() {
        signs = getAllHelper();
    }

    @Override
    public void add(Object object) {
        Sign s = (Sign) object;
        insertSign(s);
        signs.add(s);
    }

    @Override
    public void delete(Object object) {
        Sign s = (Sign) object;
        deleteSpecificSign(s);
        signs.remove(s);
    }

    @Override
    public void update(Object object) {
        Sign s = (Sign) object;
        updateSign(s);
        signs.set(signs.indexOf(s), s);
    }

    public void updateBlock(Object object) {
        Sign s = (Sign) object;
        updateSignageGroup(s);
        signs.set(signs.indexOf(s), s);
    }

    public void insertSign(Sign s) {
        String[] cols = { "signageGroup", "locationName", "direction", "startDate", "endDate", "signLocation" };
        String[] values = {s.getSignageGroup().replace("'",""), s.getLocationName().replace("'",""), s.getDirection(), String.valueOf(s.getStartDate()), String.valueOf(s.getEndDate()), s.getSignLocation()};
        DButils.insertRow("signs", cols, values);
    }

    public void deleteSignGroup(Sign s) {
        DButils.deleteRow("signs", "signagegroup = '" + s.getSignageGroup() + "'");
    }

    public void deleteSpecificSign(Sign s) {
        DButils.deleteRow("signs", "signagegroup = '" + s.getSignageGroup() + "' AND locationName = '" + s.getLocationName() + "'");
    }

    public void updateSign(Sign s) {
        String[] cols = { "locationName", "direction", "startDate", "endDate", "signLocation" };
        String[] values = {s.getLocationName(), s.getDirection(), String.valueOf(s.getStartDate()), String.valueOf(s.getEndDate()), s.getSignLocation()};
        DButils.updateRow("signs", cols, values, "signagegroup = '" + s.getSignageGroup() + "' AND locationName = '" + s.getLocationName() + "'");
    }

    public void transferSign(String oldName, Sign s) {
        String[] cols = { "signageGroup", "locationName", "direction", "startDate", "endDate", "signLocation" };
        String[] values = {s.getSignageGroup(), s.getLocationName(), s.getDirection(), String.valueOf(s.getStartDate()), String.valueOf(s.getEndDate()), s.getSignLocation()};
        DButils.updateRow("signs", cols, values, "signagegroup = '" + s.getSignageGroup() + "' AND locationName = '" + oldName + "'");
    }

    public void updateSignageGroup(Sign s) {
        String[] cols = { "signLocation" };
        String[] values = {s.getSignLocation()};
        DButils.updateRow("signs", cols, values, "signagegroup = '" + s.getSignageGroup() + "'");
    }

    public HashSet<String> getSignageGroupsFromDB() {
        ArrayList<String> blocks = new ArrayList<String>();
        try {
            ResultSet rs = DButils.getCol("signs", "signageGroup");
            while (rs.next()) {
                blocks.add(rs.getString("signageGroup"));
            }
            return new HashSet<String>(blocks);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'SignDAOImpl.getSignBlocks': " + e.getMessage());
        }
        return new HashSet<String>(blocks);
    }

    public ArrayList<String> getLocationNamesFromDB(String signBlock) {
        ArrayList<String> blocks = new ArrayList<String>();
        try {
            ResultSet rs = DButils.getRowCond("signs", "locationName", "signageGroup like " + signBlock + "");
            while (rs.next()) {
                blocks.add(rs.getString("locationName"));
            }
            return blocks;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'SignDAOImpl.getLocationNames': " + e.getMessage());
        }
        return blocks;
    }

    public HashSet<String> getSignageGroups() {
        ArrayList<String> blocks = new ArrayList<String>();
        for (Sign s : signs) {
            blocks.add(s.getSignageGroup());
        }
        return new HashSet<String>(blocks);
    }

    public ArrayList<String> getLocationNames(String signBlock) {
        ArrayList<String> names = new ArrayList<String>();
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signBlock)) {
                names.add(s.getLocationName());
            }
        }
        return names;
    }

    public ArrayList<String> getCorrespondingDirections(String signageGroup, ArrayList<String> locationNames) {
        ArrayList<String> directions = new ArrayList<String>();
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signageGroup) && locationNames.contains(s.getLocationName())) {
                directions.add(s.getDirection());
            }
        }
        return directions;
    }

    public Date getStartDate(String signageGroup, String locationName) {
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signageGroup)) {
                return s.getStartDate();
            }
        }
        return null;
    }

    public Date getEndDate(String signageGroup, String locationName) {
        for (Sign s : signs) {
            if (s.getSignageGroup().equals(signageGroup)) {
                return s.getEndDate();
            }
        }
        return null;
    }

}
