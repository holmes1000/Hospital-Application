package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DButils;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String password;
    private int permissionLevel;
    private String position;

    public User() {
        this.username = "";
        this.password = "";
        this.permissionLevel = 0;
        this.position = "";
    }

    /**
     * Creates a node from the given parameters
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param permissionLevel the permission level of the user
     * @param position the position of the user
     */
    public User(String username, String password, int permissionLevel, String position) {
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.position = position;
    }

    /**
     * Creates a node from a result set
     *
     * @param rs the result set to create the node from
     * @throws java.sql.SQLException if the result set is empty
     */
    public User(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getString("username"),
                rs.getString("password"),
                rs.getInt("permissionLevel"),
                rs.getString("position"));
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    // Access from Database Methods

    // Methods to get information about the node from the database

    // list information about this node object
    @Override
    /**
     * Returns a string of all the information about the user
     *
     * @return String of all the information about the user
     */
    public String toString() {
        return username + ", " + permissionLevel + ", " + position;
    }

    /**
     * Returns a string of all the information about the user in a formatted way
     *
     * @return String of all the information about the user in a formatted way
     */
    public String userInfo() {
        return "Username: " + username + "\tPermission Level: " + permissionLevel + "\tPosition: " + position;
    }
}
