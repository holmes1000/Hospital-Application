package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.Date;
import java.sql.ResultSet;

public class Signage {
    String direction;
    int screen;
    Date date;
    String locationName;

    public Signage(String direction, int screen, Date date, String locationName) {
        this.direction = direction;
        this.screen = screen;
        this.date = date;
        this.locationName = locationName;
    }

    public Signage() {
        this.direction = "";
        this.screen = 0;
        this.date = new Date(0);
        this.locationName = "";
    }

    public Signage(ResultSet rs) {
        try {
            this.direction = rs.getString("direction");
            this.screen = rs.getInt("screen");
            this.date = rs.getDate("date");
            this.locationName = rs.getString("locationname");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
