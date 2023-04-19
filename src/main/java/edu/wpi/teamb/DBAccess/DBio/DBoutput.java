package edu.wpi.teamb.DBAccess.DBio;

import edu.wpi.teamb.DBAccess.DB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static edu.wpi.teamb.DBAccess.DB.connectToDB;
import static edu.wpi.teamb.DBAccess.DB.forceConnect;

public class DBoutput {

    /**
     * This method exports the Nodes table into a CSV file
     *
     * @param filename The name of the CSV file to be exported
     * @throws SQLException if the query fails
     * @throws IOException if the file cannot be written to
     */
    public static void exportNodesToCSV(String filename, int location) {

        DB.connectToDB();

        try {
            String allQuery = "SELECT * FROM Nodes";
            DB.forceConnect();
            Statement allStmt = DB.c.createStatement();
            DB.forceConnect();
            ResultSet allRS = allStmt.executeQuery(allQuery);
            DB.forceConnect();


            BufferedWriter bw = switch (location) {
                case 0 -> new BufferedWriter(new FileWriter(filename));
                case 1 -> new BufferedWriter(new FileWriter("./" + filename + ".csv"));
                case 2 -> new BufferedWriter(new FileWriter(filename + ".csv"));
                case 3 -> new BufferedWriter(new FileWriter("./src/main/java/resources/CSV Files/" + filename + ".csv"));
                default -> throw new IllegalStateException("Unexpected value: " + location);
            };

            bw.write("nodeID,xCoord,yCoord,floor,building");

            while (allRS.next()) {
                int nodeID = allRS.getInt("nodeID");
                int xCoord = allRS.getInt("xCoord");
                int yCoord = allRS.getInt("yCoord");
                String floor = allRS.getString("floor");
                String building = allRS.getString("building");

                String line = String.format(
                        "%d,%d,%d,%s,%s",
                        nodeID, xCoord, yCoord, floor, building);

                bw.newLine();
                bw.write(line);
            }
            allStmt.close();
            bw.close();
        } catch (IOException e) {
            //return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //return 1;
    }

    /**
     * This method exports the Edges table into a CSV file
     *
     * @param filename The name of the CSV file to be exported
     * @throws SQLException if the SQL query is invalid
     * @throws IOException  if the file cannot be found
     */
    public static void exportEdgesToCSV(String filename, int location) throws SQLException {

        connectToDB();

        String allQuery = "SELECT * FROM Edges";
        DB.forceConnect();
        Statement allStmt = DB.c.createStatement();
        DB.forceConnect();
        ResultSet allRS = allStmt.executeQuery(allQuery);
        DB.forceConnect();

        try {
            BufferedWriter bw = switch (location) {
                case 0 -> new BufferedWriter(new FileWriter(filename));
                case 1 -> new BufferedWriter(new FileWriter("./" + filename + ".csv"));
                case 2 -> new BufferedWriter(new FileWriter(filename + ".csv"));
                case 3 -> new BufferedWriter(new FileWriter("./src/main/java/resources/CSV Files/" + filename + ".csv"));
                default -> throw new IllegalStateException("Unexpected value: " + location);
            };

            bw.write("startNode,endNode");

            while (allRS.next()) {
                String startNode = allRS.getString("startNode");
                String endNode = allRS.getString("endNode");

                String line = String.format("%s,%s", startNode, endNode);

                bw.newLine();
                bw.write(line);
            }
            allStmt.close();
            bw.close();
        } catch (IOException e) {
            //return 0;
        }
        //return 1;
    }

    /**
     * This method exports the locationNames table into a CSV file
     *
     * @param filename The name of the CSV file to be exported
     * @throws SQLException if the SQL query is invalid
     */

    public static void exportLocationNamesToCSV(String filename, int location) throws SQLException {

        DB.connectToDB();

        String allQuery = "SELECT * FROM locationNames";
        DB.forceConnect();
        Statement allStmt = DB.c.createStatement();
        DB.forceConnect();
        ResultSet allRS = allStmt.executeQuery(allQuery);
        DB.forceConnect();

        try {
            BufferedWriter bw = switch (location) {
                case 0 -> new BufferedWriter(new FileWriter(filename));
                case 1 -> new BufferedWriter(new FileWriter("./" + filename + ".csv"));
                case 2 -> new BufferedWriter(new FileWriter(filename + ".csv"));
                case 3 -> new BufferedWriter(new FileWriter("./src/main/java/resources/CSV Files/" + filename + ".csv"));
                default -> throw new IllegalStateException("Unexpected value: " + location);
            };

            bw.write("longname,shortname,nodetype");

            while (allRS.next()) {
                String longName = allRS.getString("longname");
                String shortName = allRS.getString("shortname");
                String nodeType = allRS.getString("nodetype");

                String line = String.format("%s,%s,%s", longName, shortName, nodeType);

                bw.newLine();
                bw.write(line);
            }
            allStmt.close();
            bw.close();
        } catch (IOException e) {
            //return 0;
        }
        //return 1;
    }

    /**
     * This method exports the moves table into a CSV file
     *
     * @param filename The name of the CSV file to be exported
     * @throws SQLException if the SQL query is invalid
     */

    public static void exportMovesToCSV(String filename, int location) {

        connectToDB();

        String allQuery = "SELECT * FROM moves";
        DB.forceConnect();
        Statement allStmt = null;
        DB.forceConnect();
        ResultSet allRS = null;
        DB.forceConnect();

        try {
            allStmt = DB.c.createStatement();
            allRS = allStmt.executeQuery(allQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedWriter bw = switch (location) {
                case 0 -> new BufferedWriter(new FileWriter(filename));
                case 1 -> new BufferedWriter(new FileWriter("./" + filename + ".csv"));
                case 2 -> new BufferedWriter(new FileWriter(filename + ".csv"));
                case 3 -> new BufferedWriter(new FileWriter("./src/main/java/resources/CSV Files/" + filename + ".csv"));
                default -> throw new IllegalStateException("Unexpected value: " + location);
            };

            bw.write("nodeid,longname,date");

            while (allRS.next()) {
                int nodeID = allRS.getInt("nodeID");
                String longName = allRS.getString("longName");
                String date = allRS.getString("date");

                String line = String.format("%d,%s,%s", nodeID, longName, date);

                bw.newLine();
                bw.write(line);
            }
            allStmt.close();
            bw.close();
        } catch (IOException e) {
            //return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //return 1;
    }

}
