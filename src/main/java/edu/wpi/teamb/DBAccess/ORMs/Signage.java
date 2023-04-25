package edu.wpi.teamb.DBAccess.ORMs;

import java.sql.Date;
import java.sql.ResultSet;

public class Signage {
    String direction;
    int screen;
    Date date;

    public Signage(String direction, int screen, Date date) {
        this.direction = direction;
        this.screen = screen;
        this.date = date;
    }

    public Signage() {
        this.direction = "";
        this.screen = 0;
        this.date = new Date(0);
    }

    public Signage(ResultSet rs) {
        try {
            this.direction = rs.getString("direction");
            this.screen = rs.getInt("screen");
            this.date = rs.getDate("date");
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
}
