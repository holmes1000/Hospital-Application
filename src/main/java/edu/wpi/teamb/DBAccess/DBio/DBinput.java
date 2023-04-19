package edu.wpi.teamb.DBAccess.DBio;

import edu.wpi.teamb.DBAccess.DB;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

class DBinput {

    /**
     * This method imports a CSV file into the Nodes table
     *
     * @param filename The name of the CSV file to be imported
     * @throws SQLException if the table is not found
     * @throws IOException  if the file is not found
     */
    public static void importNodesFromCSV(String filename, int location) throws SQLException {

        DB.connectToDB();

        // int location can be 1 (root folder for program), 2 (custom location), or 3
        // (developer: CSV Files in package)

        try {
            BufferedReader br = switch (location) {
                case 0 -> new BufferedReader(new FileReader(filename));
                case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
            };

            String line = "";
            String splitBy = ",";

            DB.forceConnect();
            Statement tableStmt = DB.c.createStatement();

            String dropMovesTable = "DROP TABLE IF EXISTS Moves";
            int dropUpdateMoves = tableStmt.executeUpdate(dropMovesTable);

            String dropNodesTable = "DROP TABLE IF EXISTS Nodes";
            int dropUpdateNodes = tableStmt.executeUpdate(dropNodesTable);

            String createTableNodes = "CREATE TABLE Nodes (nodeID INT PRIMARY KEY, xCoord INT, yCoord INT, floor VARCHAR(255), building VARCHAR(255));";
            int tableUpdateNodes = tableStmt.executeUpdate(createTableNodes);

            String createTableMoves = "CREATE TABLE moves " +
                    "(nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date)," +
                    "constraint fk_nodeID foreign key(nodeID) references nodes(nodeID)," +
                    "constraint fk_longName foreign key(longName) references locationNames(longName));";
            int tableUpdateMoves = tableStmt.executeUpdate(createTableMoves);
            importMovesFromCSV("Moves", 3);

            while (((line = br.readLine()) != null)) {
                String[] nodeValues = line.split(splitBy);
                if (!(nodeValues[0].equals("nodeID"))) {
                    // System.out.println(nodeValues[0]);
                    String rowQuery = "INSERT INTO Nodes (nodeID, xCoord, yCoord, floor, building) VALUES ("
                            + nodeValues[0]
                            + ","
                            + nodeValues[1]
                            + ","
                            + nodeValues[2]
                            + ","
                            + "'"
                            + nodeValues[3]
                            + "'"
                            + ","
                            + "'"
                            + nodeValues[4]
                            + "'"
                            + ");";
                    // System.out.println(rowQuery);
                    Statement rowStmt = DB.c.createStatement();
                    int rowUpdate = rowStmt.executeUpdate(rowQuery);
                    rowStmt.close();
                }
            }
            br.close();
            tableStmt.close();
        } catch (IOException e) {
            //return 0;
        }
        //return 1;
    }

    /**
     * This method imports a CSV file into the Edges table
     *
     * @param filename The name of the CSV file to be imported
     * @throws SQLException if the SQL query is invalid
     * @throws IOException  if the file cannot be found
     */
    public static void importEdgesFromCSV(String filename, int location) throws SQLException {

        DB.connectToDB();

        String line = "";
        String splitBy = ",";

        try {
            BufferedReader br = switch (location) {
                case 0 -> new BufferedReader(new FileReader(filename));
                case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
            };

            DB.forceConnect();
            Statement tableStmt = DB.c.createStatement();
            String dropEdgesTable = "DROP TABLE IF EXISTS Edges";
            int dropUpdate = tableStmt.executeUpdate(dropEdgesTable);
            String createTable = "CREATE TABLE edges (startNode INT, endNode INT, primary key (startNode, endNode));";
            int tableUpdate = tableStmt.executeUpdate(createTable);
            while (((line = br.readLine()) != null)) {
                String[] edgeValues = line.split(splitBy);
                if (!(edgeValues[0].equals("startNode"))) {
                    // System.out.println(edgeValues[0]);
                    String rowQuery = "INSERT INTO Edges (startNode, endNode) VALUES ("
                            + edgeValues[0]
                            + ","
                            + edgeValues[1]
                            + ");";
                    // System.out.println(rowQuery);
                    DB.forceConnect();
                    Statement rowStmt = DB.c.createStatement();
                    int rowUpdate = rowStmt.executeUpdate(rowQuery);
                    rowStmt.close();
                }
            }
            br.close();
            tableStmt.close();
        } catch (Exception e) {
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

    public static void importLocationNamesFromCSV(String filename, int location) throws SQLException {

        DB.connectToDB();

        String line = "";
        String splitBy = ",";

        BufferedReader br = null;

        try {
            br = switch (location) {
                case 0 -> new BufferedReader(new FileReader(filename));
                case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
            };

            DB.forceConnect();
            Statement tableStmt = DB.c.createStatement();
            String dropMovesTable = "DROP TABLE IF EXISTS moves";
            int dropUpdateMoves = tableStmt.executeUpdate(dropMovesTable);
            String dropLocationNamesTable = "DROP TABLE IF EXISTS locationNames";
            int dropUpdateLocationNames = tableStmt.executeUpdate(dropLocationNamesTable);

            String createTableLocationNames = "CREATE TABLE locationNames " +
                    "(longName VARCHAR(255), shortName VARCHAR(255), nodeType VARCHAR(20), primary key (longName));";
            int tableUpdateLocationNames = tableStmt.executeUpdate(createTableLocationNames);

            String createTableMoves = "CREATE TABLE moves " +
                    "(nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date)," +
                    "constraint fk_nodeID foreign key(nodeID) references nodes(nodeID)," +
                    "constraint fk_longName foreign key(longName) references locationNames(longName));";
            int tableUpdateMoves = tableStmt.executeUpdate(createTableMoves);
            importMovesFromCSV("Moves", 3);

            while (((line = br.readLine()) != null)) {
                String[] locationNameValues = line.split(splitBy);
                if (!(locationNameValues[0].equals("longName"))) {
                    // System.out.println(edgeValues[0]);
                    String rowQuery = "INSERT INTO LocationNames (longName, shortName, nodeType) VALUES ("
                            + "'"
                            + locationNameValues[0]
                            + "'"
                            + ","
                            + "'"
                            + locationNameValues[1]
                            + "'"
                            + ","
                            + "'"
                            + locationNameValues[2]
                            + "'"
                            + ");";
                    // System.out.println(rowQuery);
                    DB.forceConnect();
                    Statement rowStmt = DB.c.createStatement();
                    int rowUpdate = rowStmt.executeUpdate(rowQuery);
                    rowStmt.close();
                }
            }
            br.close();
            tableStmt.close();
        } catch (IOException e) {
            //return 0;
        }
        //return 1;
    }

    /**
     * This method exports the moves table into a CSV file
     *
     * @param filename is the name of the CSV file to be exported
     * @throws SQLException if the SQL query is invalid
     */

    public static void importMovesFromCSV(String filename, int location) throws SQLException {

        DB.connectToDB();

        String line = "";
        String splitBy = ",";

        try {
            BufferedReader br = switch (location) {
                case 0 -> new BufferedReader(new FileReader(filename));
                case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
            };

            DB.forceConnect();
            Statement tableStmt = DB.c.createStatement();
            String dropMovesTable = "DROP TABLE IF EXISTS moves";
            int dropUpdate = tableStmt.executeUpdate(dropMovesTable);

            String createTable = "CREATE TABLE moves " +
                    "(nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date)," +
                    "constraint fk_nodeID foreign key(nodeID) references nodes(nodeID)," +
                    "constraint fk_longName foreign key(longName) references locationNames(longName));";
            int tableUpdate = tableStmt.executeUpdate(createTable);

            while (((line = br.readLine()) != null)) {
                String[] moveValues = line.split(splitBy);
                if (!(moveValues[0].equals("nodeID"))) {
                    // System.out.println(edgeValues[0]);
                    String rowQuery = "INSERT INTO Moves (nodeID, longName, date) VALUES ("
                            + moveValues[0]
                            + ","
                            + "'"
                            + moveValues[1]
                            + "'"
                            + ","
                            + "'"
                            + moveValues[2]
                            + "'"
                            + ");";
                    // System.out.println(rowQuery);
                    DB.forceConnect();
                    Statement rowStmt = DB.c.createStatement();
                    int rowUpdate = rowStmt.executeUpdate(rowQuery);
                    rowStmt.close();
                }
            }
            br.close();
            tableStmt.close();
        } catch (IOException e) {
            //return 0;
        }
        //return 1;
    }

}
