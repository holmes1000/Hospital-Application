package edu.wpi.teamb.DBAccess;

import java.sql.*;
import java.util.ArrayList;

public class DButils {

    /**
     * Allows the user to enter an SQL query to be executed (last resort only!)
     *
     * @param update is the query to be executed
     */
    void freeUpdate(String update) {
        try {
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            stmt.executeQuery(update);
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
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT " + columns + " FROM teamb." + table + " WHERE " + cond;
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
    public static ResultSet getCol(String table, String column) {
        try {
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT " + column + " FROM teamb." + table;
            ResultSet rs = stmt.executeQuery(query);
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.getCol': " + e.getMessage());
            return null;
        }
    }

    /**
     * Turns a column of a ResultSet into an ArrayList of Strings
     *
     * @param rs the ResultSet to convert
     * @return an ArrayList of Strings
     */
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
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "UPDATE teamb." + table + " SET " + strArray2UpdateFormat(columns, value) + " WHERE " + cond;
            //testing
            System.out.println(query);
            //testing
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.updateRow': " + e.getMessage());
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
            if (value[i] == null) {
                ret += cols[i] + " = " + "null" + ",";
                continue;
            } else if (value[i].equals("null")) {
                ret += cols[i] + " = " + value[i] + ",";
                continue;
            }
            ret += cols[i] + " = '" + value[i] + "',";
        }
        if (value[length - 1] == null) {
            ret += cols[length - 1] + " = " + "null";
            return ret;
        } else if (value[length - 1].equals("null")) {
            ret += cols[length - 1] + " = " + value[length - 1];
            return ret;
        } else ret += cols[length - 1] + " = '" + value[length - 1] + "'";

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
            Connection c = DBconnection.getDBconnection().getConnection();
            c.setAutoCommit(false);
            Statement stmt = c.createStatement();
            String update = "INSERT INTO teamb."  + table + " (" + strArray2InsertFormatCol(columns) + ") VALUES ("
                    + strArray2InsertFormat(value) + ")";
            //testing
            System.out.println(update);
            //testing
            stmt.executeUpdate(update);
            c.commit();
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.insertRow': " + e.getMessage());
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
        Connection c = null;
        try {
            c = DBconnection.getDBconnection().getConnection();
            c.setAutoCommit(false);
            String insert = "INSERT INTO teamb.requests(employee, requeststatus, requesttype, locationname, notes) VALUES ( ?, ?, ?, ?, ?)";
            String query = "SELECT nextval(pg_get_serial_sequence('requests','id'))";
            String query1 = "SELECT currval(pg_get_serial_sequence('requests','id'))";
            stmt = c.prepareStatement(insert);
            stmt.setString(1, value[0]);
            stmt.setString(2, value[1]);
            stmt.setString(3, value[2]);
            stmt.setString(4, value[3]);
            stmt.setString(5, value[4]);
            stmt.executeUpdate();
            currvalStatement = c.createStatement();
            //currvalResultSet = currvalStatement.executeQuery(query);
            currvalResultSet = currvalStatement.executeQuery(query1);
            if (currvalResultSet.next()) {
                id = currvalResultSet.getInt(1);
            }
            c.commit();
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.insertRowRequests': " + e.getMessage());
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
            if (arr[i].equals("null")) {
                formattedStr += "null";
                formattedStr += ",'";
                continue;
            }
            formattedStr += arr[i];
            if (i != arr.length - 1) {
                if (arr[i+1] == null) formattedStr += "',";
                else if (arr[i+1].equals("null")) formattedStr += "',";
                else formattedStr += "','";
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
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "DELETE FROM teamb." + table + " WHERE " + cond;
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.deleteRow': " + e.getMessage());
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
        String countQuery = "SELECT COUNT(*) FROM teamb." + table;
        int listSize = 0;
        try {
            Statement countStmt = DBconnection.getDBconnection().getConnection().createStatement();
            ResultSet countRs = countStmt.executeQuery(countQuery);
            countRs.next();

            listSize = countRs.getInt(1);
            countRs.close();
        } catch (SQLException e) {
            System.out.println("ERROR Query Failed in method 'DB.getIDlist': " + e.getMessage());
        }

        String idQuery = "SELECT " + idColName + "  FROM teamb." + table;

        ResultSet idRs = null;
        int[] IDs = new int[listSize];

        try {
            Statement idStmt = DBconnection.getDBconnection().getConnection().createStatement();
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

    public static ResultSet getTable(String table) {
        ResultSet rs = null;
        try {
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "SELECT * FROM teamb." + table;
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.getTable': " + e.getMessage());
        }
        return rs;
    }

    public static void resetMap() {
        try {
            Statement stmt = DBconnection.getDBconnection().getConnection().createStatement();
            String query = "DELETE FROM edges;\n" +
                    "DELETE FROM moves;\n" +
                    "DELETE FROM locationnames;\n" +
                    "DELETE FROM nodes;\n" +
                    "INSERT INTO edges (startnode, endnode) SELECT startnode, endnode FROM edgebackup;\n" +
                    "INSERT INTO nodes (nodeid, xcoord, ycoord, floor, building) SELECT nodeid, xcoord, ycoord, floor, building FROM nodebackup;\n" +
                    "INSERT INTO locationnames (longname, shortname, nodetype) SELECT longname, shortname, nodetype FROM locationnamebackup;\n" +
                    "INSERT INTO moves (nodeid, longname, date) SELECT nodeid, longname, date FROM movebackup;";
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DButils.resetMap': " + e.getMessage());
        }
    }

    //Put master CSV export function here

    //Put master CSV import function here

}
