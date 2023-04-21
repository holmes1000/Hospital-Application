package edu.wpi.teamb.DBAccess.DBio;

import edu.wpi.teamb.DBAccess.DBConnection;
import edu.wpi.teamb.DBAccess.DButils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBoutput {

    /**
     * This method exports the Nodes table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public static void exportNodesToCSV(String filename, int location) {

        try {
            String allQuery = "SELECT * FROM Nodes";
            Statement allStmt = DBConnection.getDBconnection().getConnection().createStatement();
            ResultSet allRS = allStmt.executeQuery(allQuery);

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
            System.err.println("ERROR: Could not write to file " + filename + " in method 'exportNodesToCSV'");
        } catch (SQLException e) {
            System.err.println("ERROR: SQL query failed in method 'exportNodesToCSV'");
        }
    }

    /**
     * This method exports the Edges table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public static void exportEdgesToCSV(String filename, int location) {

        try {
            String allQuery = "SELECT * FROM Edges";
            Statement allStmt = DBConnection.getDBconnection().getConnection().createStatement();
            ResultSet allRS = allStmt.executeQuery(allQuery);

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
            System.err.println("ERROR: Could not write to file " + filename + " in method 'exportEdgesToCSV'");
        } catch (SQLException e) {
            System.err.println("ERROR: SQL query failed in method 'exportEdgesToCSV'");
        }
    }

    /**
     * This method exports the LocationNames table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */
    public static void exportLocationNamesToCSV(String filename, int location) throws SQLException {

        try {
            String allQuery = "SELECT * FROM locationNames";
            Statement allStmt = DBConnection.getDBconnection().getConnection().createStatement();
            ResultSet allRS = allStmt.executeQuery(allQuery);

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
            System.err.println("ERROR: Could not write to file " + filename + " in method 'exportLocationNamesToCSV'");
        } catch (SQLException e) {
            System.err.println("ERROR: SQL query failed in method 'exportLocationNamesToCSV'");
        }
    }

    /**
     * This method exports the Moves table into a CSV file
     *
     * @param filename The name of the CSV file to be exported (excludes '.csv' extension unless
     *                 location is 2)
     * @param location The location of the CSV file to be exported as an int --
     *                 int location can be 1 (root folder for program),
     *                 2 (custom location), or 3
     *                 (developer: CSV Files in package)
     */

    public static void exportMovesToCSV(String filename, int location) {

        try {
            String allQuery = "SELECT * FROM moves";
            Statement allStmt = DBConnection.getDBconnection().getConnection().createStatement();
            ResultSet allRS = allStmt.executeQuery(allQuery);

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
            System.err.println("ERROR: Could not write to file " + filename + " in method 'exportMovesToCSV'");
        } catch (SQLException e) {
            System.err.println("ERROR: SQL query failed in method 'exportMovesToCSV'");
        }
    }

}
