package edu.wpi.teamb.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBconnection {

    private Connection c = null;
    private final String postgresURL = "jdbc:postgresql://database.cs.wpi.edu/teambdb";
    private final String postgresUsername = "teamb";
    private final String postgresPassword = "teamb20";

    private final String AWSurl = "jdbc:postgresql://softengb.ctxwbmb4mcba.us-east-2.rds.amazonaws.com:5432/postgres";
    private final String AWSusername = "teamb";
    private final String AWSpassword = "billgates";
    private int databaseServer = 0; // 0 = postgres, 1 = AWS

    private static class SingletonHelper {
        //Nested class is referenced after getRepository() is called
        private static final DBconnection dbConnection = new DBconnection(0);
        //number determines default server (0 = postgres, 1 = AWS)
    }

    public static DBconnection getDBconnection() {
        return SingletonHelper.dbConnection;
    }

    // 0 = postgres, 1 = AWS
    public void setDatabaseServer(int databaseServer) {
        this.databaseServer = databaseServer;
    }

    public int getDatabaseServer() {
        return databaseServer;
    }

    private DBconnection(int databaseServer) {
        this.databaseServer = databaseServer;
        connectToDB();
    }

    public Connection getConnection() {
        connectToDB();
        forceConnect();
        return c;
    }

    public void switchTo(int databaseServer) {
        forceClose();
        this.databaseServer = databaseServer;
        connectToDB();
    }

    /**
     * Connects to the database if not already connected
     */
    private void connectToDB() {
        try {
            if (c == null) {
                Class.forName("org.postgresql.Driver");
                if (databaseServer == 0) c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
                else if (databaseServer == 1) c = DriverManager.getConnection(AWSurl, AWSusername, AWSpassword);
                else c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
            } else if (c.isClosed()) {
                Class.forName("org.postgresql.Driver");
                if (databaseServer == 0) c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
                else if (databaseServer == 1) c = DriverManager.getConnection(AWSurl, AWSusername, AWSpassword);
                else c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
            } else {
                c.close();
                Class.forName("org.postgresql.Driver");
                if (databaseServer == 0) c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
                else if (databaseServer == 1) c = DriverManager.getConnection(AWSurl, AWSusername, AWSpassword);
                else c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
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
            if (databaseServer == 0) c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
            else if (databaseServer == 1) c = DriverManager.getConnection(AWSurl, AWSusername, AWSpassword);
            else c = DriverManager.getConnection(postgresURL, postgresUsername, postgresPassword);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Closes the database connection if it is open
     */
    public void closeDBconnection() {
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
    public void forceClose() {
        try {
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }

}
