package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DButils;

import java.sql.*;


/**
 * A request to move a room to another Room at a specifid Date
 * 
 */
public class Move {

    private int nodeID;
    private String longName;
    private Date date= new Date(0,0,0);

    /**
     * Creates a new Move object with empty values
     */
    public Move() {
        this.nodeID = 0;
        this.longName = "";
        this.date = null;
    }

    /**
     * Creates a new Move object from the given parameters
     *
     * @param nodeID   the nodeID
     * @param longName the locationName
     * @param date     the date
     */
    public Move(int nodeID, String longName, Date date) {
        this.nodeID = nodeID;
        this.longName = longName;
        this.date = date;
    }

    /**
     * Creates a new Move object from a result set
     *
     * @param rs the result set to create the Move from
     */
    public Move(ResultSet rs) {
        try {
            this.nodeID = rs.getInt("nodeID");
            this.longName = rs.getString("longName");
            try{
            this.date = Date.valueOf(mdy2ymd(rs.getString("date")));
            }catch (Exception e){
                this.date = Date.valueOf(rs.getString("date"));
            }
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (nodeID != move.nodeID) return false;
        if (longName != null ? !longName.equals(move.longName) : move.longName != null) return false;
        return date != null ? date.equals(move.date) : move.date == null;
    }

    private String mdy2ymd(String mdy) {
        String[] parts = mdy.split("/");
        return parts[2] + "-" + parts[0] + "-" + parts[1];
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
        this.nodeID = nodeID;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return "NodeID: " + nodeID + " LongName: " + longName + " Date: " + date;
    }
}