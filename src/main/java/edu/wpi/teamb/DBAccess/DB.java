package edu.wpi.teamb.DBAccess;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DB {
    public static Connection c = null;
    private static final String url = "jdbc:postgresql://database.cs.wpi.edu/teambdb";
    private static final String username = "teamb";
    private static final String password = "teamb20";

    /**
     * Connects to the database
     *
     * @throws SQLException if the connection fails
     */
    public static void connectToDB() {
        try {
            if (c == null) {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(url, username, password);
            } else if (c.isClosed()) {
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(url, username, password);
            } else {
                c.close();
                Class.forName("org.postgresql.Driver");
                c = DriverManager.getConnection(url, username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    public static void forceConnect () {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Closes the database connection
     *
     * @throws SQLException if the connection fails
     */
    public static void closeDBconnection() throws SQLException {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Allows the user to enter an SQL query to be executed
     *
     * @param query is the query to be executed
     * @throws SQLException if the query fails
     */
    public static void freeQuery(String query) throws SQLException {
        try {
            connectToDB();
            Statement stmt = c.createStatement();
            stmt.executeQuery(query);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.freeQuery': " + e.getMessage());
        } finally {
            if (c != null) {
                closeDBconnection();
            }
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
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "SELECT " + columns + " FROM " + table + " WHERE " + cond;
            rs = stmt.executeQuery(query);
            if (c != null) { closeDBconnection();}
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
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "SELECT " + column + " FROM " + table;
            ResultSet rs = stmt.executeQuery(query);
            if (c != null) { closeDBconnection();}
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.getCol': " + e.getMessage());
            return null;
        }
    }

    public static ArrayList<String> colRStoStringArray(ResultSet rs) throws SQLException {
        int listSize = 0;
        connectToDB();
        ArrayList<String> stringArray = new ArrayList<>();
        while (rs.next()) {
            try {
                stringArray.add(rs.getString(1));
            } catch (SQLException e) {
                System.err.println("ERROR Query Failed in method 'DB.colRStoStringArray': " + e.getMessage());
                return null;
            }
        }
        if (c != null) {closeDBconnection();}
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
        connectToDB();
        try {
            Statement stmt = c.createStatement();
            forceConnect();
            String query = "UPDATE " + table + " SET " + strArray2UpdateFormat(columns, value) + " WHERE " + cond;
            forceConnect();
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
    private static String strArray2UpdateFormat(String[] cols, String[] value) throws RuntimeException, SQLException {
        connectToDB();
        int length = cols.length;
        if (length != value.length) {
            throw new RuntimeException("Length of columns and value must match");
        }
        String ret = "";
        for (int i = 0; i < length - 1; i++) {
            ret += cols[i] + " = '" + value[i] + "',";
        }
        ret += cols[length - 1] + " = '" + value[length - 1] + "'";
        if (c != null) {
            try {
                closeDBconnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
            connectToDB();
            Statement stmt = c.createStatement();
            String update = "INSERT INTO "  + table + " (" + strArray2InsertFormatCol(columns) + ") VALUES ("
                    + strArray2InsertFormat(value) + ")";
            stmt.executeUpdate(update);
            if (c != null) { closeDBconnection();}
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.insertRow': " + e.getMessage());
        }
    }
    public static int insertRowRequests(String table, String[] columns, String[] value) {
        int id = 0;
        ResultSet rs;
        PreparedStatement stmt = null;
        Statement currvalStatement = null;
        ResultSet currvalResultSet = null;
        try {
            connectToDB();
            c.setAutoCommit(false);
            String insert = "INSERT INTO requests(employee, requeststatus, requesttype, locationname, notes) VALUES ( ?, ?, ?, ?, ?)";
            String query = "SELECT currval(pg_get_serial_sequence('requests','id'))";
            stmt = c.prepareStatement(insert);
            stmt.setString(1, value[0]);
            stmt.setString(2, value[1]);
            stmt.setString(3, value[2]);
            stmt.setString(4, value[3]);
            stmt.setString(5, value[4]);
            stmt.executeUpdate();
            currvalStatement = c.createStatement();
            currvalResultSet = currvalStatement.executeQuery(query);
            if (currvalResultSet.next()) {
                id = currvalResultSet.getInt(1);
            }
            c.commit();
            if (c != null) {
                closeDBconnection();
            }
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.insertRowRequests': " + e.getMessage());
        }
        return id;
    }

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
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "DELETE FROM " + table + " WHERE " + cond;
            stmt.executeUpdate(query);
            if (c != null) { closeDBconnection();}
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.deleteRow': " + e.getMessage());
        }
    }

    public static String getLongNameFromNodeID(int nodeID) {
        try {
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "SELECT * from nodes join moves m on nodes.nodeid = m.nodeid where nodes.nodeid = " + nodeID;
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (c != null) { closeDBconnection();}
            String set = rs.getString("longName");
            rs.close();
            return set;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'DB.getLongNameFromNodeID': " + e.getMessage());
            return null;
        }
    }


    public static int[] getIDlist(String table, String idColName) {
        connectToDB();
        String countQuery = "SELECT COUNT(*) FROM " + table;
        int listSize = 0;
        try {
            Statement countStmt = c.createStatement();
            ResultSet countRs = countStmt.executeQuery(countQuery);
            countRs.next();

            listSize = countRs.getInt(1);
            countRs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String idQuery = "SELECT " + idColName + "  FROM " + table;

        ResultSet idRs = null;
        int[] IDs = new int[listSize];

        try {
            Statement idStmt = c.createStatement();
            idRs = idStmt.executeQuery(idQuery);
            for (int i = 0; i < listSize; i++) {
                idRs.next();
                IDs[i] = idRs.getInt(idColName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (c != null) { closeDBconnection();}
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IDs;
    }

    public static String getShortNameFromNodeID(int nodeID) {
        try {
            String longName = getLongNameFromNodeID(nodeID);
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "SELECT shortname from locationnames join moves m on locationnames.longname = m.longname";
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            if (c != null) { closeDBconnection();}
            String set = rs.getString("shortname");
            rs.close();
            return set;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
    }

    public static ResultSet joinFullNodes() {
        try{
            connectToDB();
            Statement stmt = c.createStatement();
            String query = "SELECT * FROM nodes, moves, locationnames WHERE nodes.nodeid = moves.nodeid AND moves.longname = locationnames.longname";
            ResultSet rs = stmt.executeQuery(query);
            if (c != null) { closeDBconnection();}
            return rs;
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
    }
}
