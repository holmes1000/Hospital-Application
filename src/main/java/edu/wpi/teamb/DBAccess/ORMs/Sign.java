package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Sign {

    private String signageGroup;
    private String locationName;
    private String direction;
    private Date startDate;
    private Date endDate;
    private String signLocation;

    public Sign(){
        this.signageGroup = "";
        this.locationName = "";
        this.direction = "stop here";
        this.startDate = Date.valueOf(LocalDate.now());
        this.endDate = null;
        this.signLocation = "";
    }

    public Sign(String signageGroup, String locationName, String direction, Date startDate, Date endDate, String signLocation) {
        this.signageGroup = signageGroup;
        this.locationName = locationName;
        this.direction = direction;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signLocation = signLocation;
    }

    public Sign(ResultSet rs) throws SQLException {
        this(
                rs.getString("signageGroup"),
                rs.getString("locationName"),
                rs.getString("direction"),
                rs.getDate("startDate"),
                rs.getDate("endDate"),
                rs.getString("signLocation")
        );
    }

    public String getSignageGroup() {
        return signageGroup;
    }

    public void setSignageGroup(String signageGroup) {
        this.signageGroup = signageGroup;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getSignLocation() {return signLocation;}

    public void setSignLocation(String signLocation) {this.signLocation = signLocation;}

    public String toString() {
        return "Signage Group: " + signageGroup + "\n" +
                "Location Name: " + locationName + "\n" +
                "Direction: " + direction + "\n" +
                "Start Date: " + startDate + "\n" +
                "End Date: " + endDate + "\n" +
                "Sign Location " + signLocation;
    }
}
