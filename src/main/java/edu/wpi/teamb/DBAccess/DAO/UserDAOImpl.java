package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.ORMs.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAOImpl implements IDAO {
    ArrayList<User> users;

    public UserDAOImpl() throws SQLException {
        users = getAllHelper();
    }

    public User get(Object id) {
        String username = (String) id;
        ResultSet rs = DButils.getRowCond("Users", "*", "username = '" + username + "'");
        try {
            if (rs != null) {
                if (rs.isBeforeFirst()) {
                    rs.next();
                    return new User(rs);
                } else throw new SQLException("No rows found"); }
        } catch (SQLException e) {
            // handle error
            System.err.println("ERROR Query Failed in method 'UserDAOImpl.get': " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public ArrayList<User> getAll() {
        return users;
    }

    @Override
    public void setAll() { users = getAllHelper(); }

    /**
     * Gets all users
     *
     * @return A list of all users
     */
    public ArrayList<User> getAllHelper() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            ResultSet rs = DButils.getCol("users", "*");
            while (rs.next()) {
                users.add(new User(rs));
            }
            return users;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'UserDAOImpl.getAllHelper': " + e.getMessage());
        }
        return users;
    }

    @Override
    public void update(Object u) {
        User user = (User) u;
        String[] values = {user.getUsername(), user.getPassword(), user.getPosition(), String.valueOf(user.getPermissionLevel())};
        String[] colsUser = {"password", "position", "permissionlevel"};
        String[] valuesUser = {values[1], values[2], values[3]};
        DButils.updateRow("users", colsUser, valuesUser, "username = '" + values[0] +"'");
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername() == user.getUsername()) {
                users.set(i, user);
            }
        }
    }

    // Method to Delete the Database

    /**
     * Deletes the row in the database that matches the username of this user object
     *
     * @param u 0 to confirm, anything else to cancel
     */
    @Override
    public void delete(Object u) {
        User user = (User) u;
        DButils.deleteRow("Users", "username = '" + user.getUsername() + "'");
        users.remove(user);
    }

    @Override
    public void add(Object u) {
        User user = (User) u;
        String[] cols = {"username", "password", "permissionLevel", "position"};
        String[] vals = {user.getUsername(), user.getPassword(), Integer.toString(user.getPermissionLevel()), user.getPosition()};
        DButils.insertRow("Users", cols, vals);
        users.add(user);
    }
    /**
     * Searches through the database for the row(s) that matches the given column
     * and value
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return the result set of the row(s) that matches the given column and value
     */

    public ResultSet getDBRowFromCol(String col, String value) {
        return DButils.getRowCond("Users", "*", col + " = " + value);
    }

    /**
     * Gets the row from the database that matches the given nodeID
     *
     * @param username the nodeID to search for
     * @return the result set of the row that matches the given nodeID
     */

    public ResultSet getDBRowUsername(String username) {
        return getDBRowFromCol("username", "'" + username + "'");
    }

    /**
     * Gets the row(s) from the database that matches the given permission level
     *
     * @param permissionLevel the permission level to search for
     * @return the result set of the row that matches the given permission level
     */


    public ResultSet getDBRowPermissionLevel(int permissionLevel) {
        return getDBRowFromCol("permissionLevel",  Integer.toString(permissionLevel));
    }

    /**
     * Gets the row(s) from the database that matches the given position
     *
     * @param position the position to search for
     * @return the result set of the row that matches the given position
     */


    public ResultSet getDBRowPosition(String position) {
        return getDBRowFromCol("position", position);
    }

    // Method to Update the Database

    /**
     * Updates the database with the information in this user object
     *
     * @param col   the columns to update
     * @param value the values to update
     */
    public void updateRow(User user, String[] col, String[] value) {
        if (col == null || value == null)
            throw new IllegalArgumentException("The column and value arrays must be the same length");
        DButils.updateRow("Users", col, value, "username = '" + user.getUsername() + "'");
    }

    //TODO: Unsire if we want to beable to change the node ID
    /**
     * Update the permission level in the database
     *
     * @param username the new username
     *
     */
    public void updateUsername(User user, String username) {
        String[] col = { "username" };
        String[] value = { username };
        updateRow(user, col, value);
        user.setUsername(username);
    }

    /**
     * Update the password in the database
     *
     * @param password the new password
     *
     */
    // TODO: Check in with team if coords can be <= 0
    public void updatePassword(User user, String password) {
        if (password.length() < 8)
            throw new IllegalArgumentException("The password is too short -- it must be at least 8 characters long.");
        String[] col = { "password" };
        String[] value = { password };
        updateRow(user, col, value);
        user.setPassword(password);
    }

    /**
     * Update the permission level in the database
     *
     * @param permissionLevel the new permission level
     *
     */
    public void updatePermissionLevel(User user, int permissionLevel) {
        if (permissionLevel < 0)
            throw new IllegalArgumentException("The permission level must be greater than 0.");
        String[] col = { "permissionLevel" };
        String[] value = { Integer.toString(permissionLevel) };
        updateRow(user, col, value);
        user.setPermissionLevel(permissionLevel);
    }

    /**
     * Update the position in the database
     *
     * @param position the new floor
     *
     */
    public void updatePosition(User user, String position) {
        if (position == null || position.equals(""))
            throw new IllegalArgumentException("The floor is null or empty");
        String[] col = { "position" };
        String[] value = { position };
        updateRow(user, col, value);
        user.setPosition(position);
    }

}
