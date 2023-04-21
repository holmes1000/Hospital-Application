package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DButils;

import java.sql.*;


public class LocationName {
    private String longName;
    private String shortName;
    private String nodeType;

    /**
     * Creates a new LocationName object with empty values
     */
    public LocationName() {
        this.longName = "";
        this.shortName = "";
        this.nodeType = "";
    }

    /**
     * Creates a new Location object from the given parameters
     *
     * @param longName  the nodeID
     * @param shortName the locationName
     * @param nodeType  the date
     */
    public LocationName(String longName, String shortName, String nodeType) {
        this.longName = longName;
        this.shortName = shortName;
        this.nodeType = nodeType;
    }

    /**
     * Creates a new LocationName object from a result set
     *
     * @param rs the result set to create the move from
     */
    public LocationName(ResultSet rs) {
        try {
            this.longName = rs.getString("longName");
            this.shortName = rs.getString("shortName");
            this.nodeType = rs.getString("nodeType");
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
        }
    }

    /**
     * Creates a LocationName object using a longName
     *
     * @param longName the node to look for to get Move data
     */
    public static LocationName getLocationNameFromLongName(String longName) {
        ResultSet rs = DButils.getRowCond("LocationNames", "*", "longName = '" + longName + "'");
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                return new LocationName(rs);
            } else
                throw new SQLException("No rows found");
        } catch (SQLException e) {
            // handle error

            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }

    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public String toString() {
        return "Long Name: " + longName + " Short Name: " + shortName + " Node Type: " + nodeType;
    }
}