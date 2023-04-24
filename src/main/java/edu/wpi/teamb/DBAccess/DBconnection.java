package edu.wpi.teamb.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {

    private Connection c = null;
    private final String url = "jdbc:postgresql://database.cs.wpi.edu/teambdb";
    private final String username = "teamb";
    private final String password = "teamb20";

    private static class SingletonHelper {
        //Nested class is referenced after getRepository() is called
        private static final DBconnection dbConnection = new DBconnection();
    }

    public static DBconnection getDBconnection() {
        return SingletonHelper.dbConnection;
    }

    private DBconnection() { connectToDB(); }

    public Connection getConnection() {
        connectToDB();
        forceConnect();
        return c;
    }

    /**
     * Connects to the database if not already connected
     */
    private void connectToDB() {
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

    /**
     * Tries forcibly connecting to the database
     */
    private void forceConnect () {
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
     * Closes the database connection if it is open
     */
    private void closeDBconnection() {
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
     * Tries forcibly closing the database connection
     */
    private void forceClose() {
        try {
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
