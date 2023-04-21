package edu.wpi.teamb.DBAccess;

import java.sql.*;
import java.util.ArrayList;
import edu.wpi.teamb.DBAccess.*;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import oracle.jdbc.replay.ReplayStatistics;

public class DButils {

    /**
     * Allows the user to enter an SQL query to be executed (last resort only!)
     *
     * @param query is the query to be executed
     */
    void freeQuery(String query) {
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.freeQuery': " + e.getMessage());
        }
    }

    /**
     * Gets the specified colums from a row that matches
     *
     * @param table   the able in the db to search in
     * @param columns the cols to get; format like: col1,col2, ...
     * @param cond    a condition that selects rows that meet that condition
     * @return Returns a ResultSet that contains the information queried. returns
     *         null if an error occurs
     */
    public static ResultSet getRowCond(String table, String columns, String cond) {
        ResultSet rs = null;
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String query = "SELECT " + columns + " FROM " + table + " WHERE " + cond;
            rs = stmt.executeQuery(query);
            if (rs != null) {
                return rs;
            } else throw new SQLException("No rows found");
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.getRowCond': " + e.getMessage());
            return null;
        }
    }

    /**
     * Gets the specified columns given a column name
     *
     * @param table   the able in the db to search in
     * @param column the column being searched for
     * @return Returns a ResultSet that contains the information queried. returns
     *         null if an error occurs
     */
    public static ResultSet getCol(String table, String column) throws SQLException {
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String query = "SELECT " + column + " FROM " + table;
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.getCol': " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> colRStoStringArray(ResultSet rs) {
        int listSize = 0;
        ArrayList<String> stringArray = new ArrayList<>();
        try {
            while (rs.next()) {

                stringArray.add(rs.getString(1));
            }
        } catch (SQLException e) {
                System.err.println("ERROR Query Failed in method 'DB.colRStoStringArray': " + e.getMessage());
                return null;
            }
        return stringArray;
    }

    /**
     * In a given table,updates the value of the columns of that match the condition
     * and error occurs if the number of columns and values doesn't match
     *
     * @param table   the table to update
     * @param columns the columns to change
     * @param value   the values to change the corresponding column to
     * @param cond    the condition that determines which rows to update
     */
    public static void updateRow(String table, String[] columns, String[] value, String cond) {
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String query = "UPDATE " + table + " SET " + strArray2UpdateFormat(columns, value) + " WHERE " + cond;
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.updateRow': " + e.getMessage());
        }
    }

    /**
     * Formats the cols and values to work with updating the database
     *
     * @param cols  cols that match up with corresponding value
     * @param value values to match with corresponding cols
     * @return string
     * @throws RuntimeException When length of col array and value array are not
     *                          equal
     */
    private static String strArray2UpdateFormat(String[] cols, String[] value) throws RuntimeException {

        int length = cols.length;
        if (length != value.length) {
            throw new RuntimeException("Length of columns and value must match");
        }
        String ret = "";
        for (int i = 0; i < length - 1; i++) {
            ret += cols[i] + " = '" + value[i] + "',";
        }
        ret += cols[length - 1] + " = '" + value[length - 1] + "'";

        return ret;
    }

    /**
     * Inserts a row into the table with the given values
     *
     * @param table   the table to insert into
     * @param columns the columns to insert into
     * @param value   the values to insert into the corresponding columns
     */
    public static void insertRow(String table, String[] columns, String[] value) {
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String update = "INSERT INTO "  + table + " (" + strArray2InsertFormatCol(columns) + ") VALUES ("
                    + strArray2InsertFormat(value) + ")";
            stmt.executeUpdate(update);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.insertRow': " + e.getMessage());
        }
    }

    /**
     * Formats the cols and values to work with inserting into the database
     *
     * @param table  the table to insert into
     * @param columns cols that match up with corresponding value
     * @param value values to match with corresponding cols
     * @return int id of the inserted row
     */
    public static int insertRowRequests(String table, String[] columns, String[] value) {
        int id = 0;
        ResultSet rs;
        PreparedStatement stmt = null;
        Statement currvalStatement = null;
        ResultSet currvalResultSet = null;
        try {
            Repository.getRepository().getConnection().setAutoCommit(false);
            String insert = "INSERT INTO requests(employee, floor, roomnumber, requeststatus, requesttype, location_name) VALUES ( ?, ?, ?, ?, ?, ?)";
            String query = "SELECT currval(pg_get_serial_sequence('requests','id'))";
            stmt = Repository.getRepository().getConnection().prepareStatement(insert);
            stmt.setString(1, value[0]);
            stmt.setString(2, value[1]);
            stmt.setString(3, value[2]);
            stmt.setString(4, value[3]);
            stmt.setString(5, value[4]);
            stmt.setString(6, value[5]);
            stmt.executeUpdate();
            currvalStatement = Repository.getRepository().getConnection().createStatement();
            currvalResultSet = currvalStatement.executeQuery(query);
            if (currvalResultSet.next()) {
                id = currvalResultSet.getInt(1);
            }
            Repository.getRepository().getConnection().commit();
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.insertRowRequests': " + e.getMessage());
        }
        return id;
    }

    /**
     * Formats a String array (DB cols) to work with inserting into the database
     *
     * @param arr a String array of values to be formatted
     * @return a String of the formatted array
     */

    public static String strArray2InsertFormatCol(String[] arr) {
        String formattedStr = "";
        for (int i = 0; i < arr.length; i++) {
            formattedStr += arr[i];
            if (i != arr.length - 1) {
                formattedStr += ",";
            }
        }
        return formattedStr;
    }

    /**
     * Formats a String array (such as cols and values) to work with inserting into the database
     *
     * @param arr a String array of values to be formatted
     * @return a String of the formatted array
     */

    public static String strArray2InsertFormat(String[] arr) {
        String formattedStr = "'";
        for (int i = 0; i < arr.length; i++) {
            formattedStr += arr[i];
            if (i != arr.length - 1) {
                formattedStr += "','";
            } else {
                formattedStr += "'";
            }
        }
        return formattedStr;
    }

    /**
     * Deletes row(s) from the table that match the condition
     *
     * @param table Table to delete from
     * @param cond  condition to decide which rows get deleted
     */
    public static void deleteRow(String table, String cond) {
        try {
            Statement stmt = Repository.getRepository().getConnection().createStatement();
            String query = "DELETE FROM " + table + " WHERE " + cond;
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.deleteRow': " + e.getMessage());
        }
    }

    /**
     * Gets a list of all the ids from a certain table
     *
     * @param table Table to get from
     * @param idColName   Column to get ids from
     * @return an array of ints with all the ids from a certain table
     */
    public static int[] getIDlist(String table, String idColName) {
        String countQuery = "SELECT COUNT(*) FROM " + table;
        int listSize = 0;
        try {
            Statement countStmt = Repository.getRepository().getConnection().createStatement();
            ResultSet countRs = countStmt.executeQuery(countQuery);
            countRs.next();

            listSize = countRs.getInt(1);
            countRs.close();
        } catch (SQLException e) {
            System.out.println("ERROR Query Failed in method 'DB.getIDlist': " + e.getMessage());
        }

        String idQuery = "SELECT " + idColName + "  FROM " + table;

        ResultSet idRs = null;
        int[] IDs = new int[listSize];

        try {
            Statement idStmt = Repository.getRepository().getConnection().createStatement();
            idRs = idStmt.executeQuery(idQuery);
            for (int i = 0; i < listSize; i++) {
                idRs.next();
                IDs[i] = idRs.getInt(idColName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IDs;
    }

    //Put master CSV export function here

    //Put master CSV import function here

}
