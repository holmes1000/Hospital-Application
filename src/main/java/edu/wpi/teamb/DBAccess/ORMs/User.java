package edu.wpi.teamb.DBAccess.ORMs;

import edu.wpi.teamb.DBAccess.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String name;
    private String username;
    private String password;
    private String email;
    private int permissionLevel;

    public User() {
        this.name = "";
        this.username = "";
        this.password = "";
        this.email = "";
        this.permissionLevel = 0;
    }

    /**
     * Creates a node from the given parameters
     * @param name
     * @param username
     * @param password
     * @param email
     * @param permissionLevel
     */
    public User(String name, String username, String password, String email, int permissionLevel) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.permissionLevel = permissionLevel;
    }

    /**
     * Creates a node from a result set
     *
     * @param rs the result set to create the node from
     * @throws java.sql.SQLException if the result set is empty
     */
    public User(ResultSet rs) throws java.sql.SQLException {
        this(
                rs.getString("name"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getInt("permissionLevel"));

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return name + ", " + username + ", " + email + ", " + permissionLevel;
    }

    /**
     * Returns a string of all the information about the user in a formatted way
     *
     * @return String of all the information about the user in a formatted way
     */
    public String userInfo() {
        return "Name" + name + "\tUsername: " + username + "\tEmail" + email + "\tPermission Level: " + permissionLevel;
    }
    public User getUser(String username) {
        ResultSet rs = DB.getRowCond("Users", "*", "username like '" + username + "'");
        try {
            if (rs != null) {
                if (rs.isBeforeFirst()) {
                    rs.next();
                    return new User(rs);
                } else throw new SQLException("No rows found"); }
        } catch (SQLException e) {
            // handle error
            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
        return null;
    }
}
