package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LocationNameDAOImpl implements IDAO {

    static ArrayList<LocationName> locationNames;

    public LocationNameDAOImpl() throws SQLException {
        locationNames = getAllHelper();
    }

    public LocationName get(Object id) {
        String name = (String) id;
        ResultSet rs = DB.getRowCond("LocationNames", "*", "longname = " + name);
        try {
        if (rs.isBeforeFirst()) { // if there is something it found
            rs.next();
            return new LocationName(rs); // make the locationName
        } else
            throw new SQLException("No rows found");
    } catch (SQLException e) {
        // handle error

        System.err.println("ERROR Query Failed: " + e.getMessage());
        return null;
    }
    }

    /**
     * Gets all locations
     *
     * @return A list of all locations
     */
    @Override
    public ArrayList<LocationName> getAll() {
        return locationNames;
    }

    /**
     * Gets all locations
     *
     * @return A list of all locations
     */
    public ArrayList<LocationName> getAllHelper() {
        ArrayList<LocationName> lns = new ArrayList<LocationName>();
        try {
            ResultSet rs = DB.getCol("locationnames", "*");
            while (rs.next()) {
                lns.add(new LocationName(rs));
            }
            return lns;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'LocationNameDAOImpl.getAllHelper': " + e.getMessage());
        }
        return lns;
    }

    /**
     * Adds a location
     *
     * @param l The location to add
     */
    @Override
    public void add(Object l) {
        LocationName location = (LocationName) l;
        String[] cols = {"longName", "shortName", "nodeType"};
        String[] vals = {location.getLongName(), location.getShortName(), location.getNodeType()};
        DB.insertRow("locationnames", cols, vals);
        locationNames.add(location);
    }

    /**
     * Deletes a location
     *
     * @param l The location to delete
     */
    @Override
    public void delete(Object l) {
        LocationName location = (LocationName) l;
        DB.deleteRow("LocationNames", "longName = " + location.getLongName());
        locationNames.remove(location);
    }

    /**
     * Updates a location
     *
     * @param l The location to update
     */
    @Override
    public void update(Object l) {
        LocationName location = (LocationName) l;
        String[] cols = {"longName", "shortName", "nodeType"};
        String[] vals = {location.getLongName(), location.getShortName(), location.getNodeType()};
        DB.updateRow("LocationNames", cols, vals, "longName = " + location.getLongName());
        for (int i = 0; i < locationNames.size(); i++) {
            if (locationNames.get(i).getLongName().equals(location.getLongName())) {
                locationNames.set(i, location);
            }
        }
    }

    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("LocationNames", "*", col + " = " + value);
    }

    public ResultSet getDBRowAllLocationNames() {
        return DB.getRowCond("LocationNames", "*", "TRUE");
    }

    /**
     * Gets a ResultSet of rows from the LocationNames table that match the given longName
     *
     * @param longName the longName to look for to get LocationName data
     */

    public ResultSet getDBRowLongName(String longName) {
        return getDBRowFromCol("longName", "" + longName + "");
    }

    /**
     * Gets a ResultSet of rows from the LocationNames table that match the given shortName
     *
     * @param shortName the shortName to look for to get LocationName data
     */

    public ResultSet getDBRowShortName(String shortName) {
        return getDBRowFromCol("shortName", "" + shortName + "");
    }

    /**
     * Gets a ResultSet of rows from the LocationNames table that match the given nodeType
     *
     * @param nodeType the date to look for to get Move data
     */

    public ResultSet getDBRowNodeType(String nodeType) {
        return getDBRowFromCol("nodeType", "" + nodeType + "");
    }

    /**
     * Returns a string of all the information about the LocationName
     *
     * @return String of all the information about the LocationName
     */
    public String toString(LocationName ln) {
        return ln.getLongName() + ", " + ln.getShortName() + ", " + ln.getNodeType();
    }

    public ArrayList<String> getLongNamesAlphebeticalOrder() {
        ArrayList<String> longNames = new ArrayList<>();
        for (LocationName ln : locationNames) {
            longNames.add(ln.getLongName());
        }
        longNames.sort(String::compareToIgnoreCase);
        return longNames;
    }

    public ArrayList<String> getShortNamesAlphebeticalOrder() {
        ArrayList<String> shortNames = new ArrayList<>();
        for (LocationName ln : locationNames) {
            shortNames.add(ln.getLongName());
        }
        shortNames.sort(String::compareToIgnoreCase);
        return shortNames;
    }

    public ArrayList<String> getNodeTypesUniqueAlphabetical() {
        ArrayList<String> uniqueAlphabetical = new ArrayList<>();
        for (LocationName ln : locationNames) {
            if (!uniqueAlphabetical.contains(ln.getNodeType())) {
                uniqueAlphabetical.add(ln.getNodeType());
            }
        }
        uniqueAlphabetical.sort(String::compareToIgnoreCase);
        return uniqueAlphabetical;
    }

}
