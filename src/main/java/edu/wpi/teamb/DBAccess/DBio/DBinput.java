package edu.wpi.teamb.DBAccess.DBio;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DBConnection;
import edu.wpi.teamb.DBAccess.DButils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

class DBinput {

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
    public static void importNodesFromCSV(String filename, int location) {

        String line = "";
        String splitBy = ",";

        BufferedReader br = null;

        try {
            try {
                br = switch (location) {
                    case 0 -> new BufferedReader(new FileReader(filename));
                    case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                    case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                    case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                    default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                };
            } catch (FileNotFoundException e) {
                System.out.println("Error reading the file in the method 'DBinput.importNodesFromCSV'");
                e.printStackTrace();
            }

            Statement tableStmt = Repository.getRepository().getConnection().createStatement();

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

            try {
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
                        Statement rowStmt = DBConnection.getDBconnection().getConnection().createStatement();
                        int rowUpdate = rowStmt.executeUpdate(rowQuery);
                        rowStmt.close();
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error reading the file in the method 'DBinput.importNodesFromCSV'");
                e.printStackTrace();
            }
            tableStmt.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Query could not be executed in the method 'DBinput.importNodesFromCSV'");
            e.printStackTrace();
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
     * @throws SQLException if the SQL query is invalid
     */
    public static void importEdgesFromCSV(String filename, int location) {

        String line = "";
        String splitBy = ",";

        BufferedReader br = null;

        try {
            try {
                br = switch (location) {
                    case 0 -> new BufferedReader(new FileReader(filename));
                    case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                    case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                    case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                    default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                };
            } catch (FileNotFoundException e) {
                System.out.println("Error reading the file in the method 'DBinput.importEdgesFromCSV'");
                e.printStackTrace();
            }

            Statement tableStmt = DBConnection.getDBconnection().getConnection().createStatement();
            String dropEdgesTable = "DROP TABLE IF EXISTS Edges";
            int dropUpdate = tableStmt.executeUpdate(dropEdgesTable);
            String createTable = "CREATE TABLE edges (startNode INT, endNode INT, primary key (startNode, endNode));";
            int tableUpdate = tableStmt.executeUpdate(createTable);

            try {
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
                        Statement rowStmt = DBConnection.getDBconnection().getConnection().createStatement();
                        int rowUpdate = rowStmt.executeUpdate(rowQuery);
                        rowStmt.close();
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error reading the file in the method 'DBinput.importEdgesFromCSV'");
                e.printStackTrace();
            }
            tableStmt.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Query could not be executed in the method 'DBinput.importNodesFromCSV'");
            e.printStackTrace();
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
    public static void importLocationNamesFromCSV(String filename, int location) {

        String line = "";
        String splitBy = ",";

        BufferedReader br = null;

        try {
            try {
                br = switch (location) {
                    case 0 -> new BufferedReader(new FileReader(filename));
                    case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                    case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                    case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                    default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                };
            } catch (FileNotFoundException e) {
                System.out.println("Error reading the file in the method 'DBinput.importLocationNamesFromCSV'");
                e.printStackTrace();
            }

            Statement tableStmt = DBConnection.getDBconnection().getConnection().createStatement();
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

            try {
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
                        Statement rowStmt = DBConnection.getDBconnection().getConnection().createStatement();
                        int rowUpdate = rowStmt.executeUpdate(rowQuery);
                        rowStmt.close();
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error reading the file in the method 'DBinput.importLocationNamesFromCSV'");
                e.printStackTrace();
            }
            tableStmt.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Query could not be executed in the method 'DBinput.importLocationNamesFromCSV'");
            e.printStackTrace();
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
    public static void importMovesFromCSV(String filename, int location) {

        String line = "";
        String splitBy = ",";

        BufferedReader br = null;

        try {
            try {
                br = switch (location) {
                    case 0 -> new BufferedReader(new FileReader(filename));
                    case 1 -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                    case 2 -> new BufferedReader(new FileReader(filename + ".csv"));
                    case 3 -> new BufferedReader(new FileReader("./src/main/resources/CSV Files/" + filename + ".csv"));
                    default -> new BufferedReader(new FileReader("./" + filename + ".csv"));
                };
            } catch (FileNotFoundException e) {
                System.out.println("Error reading the file in the method 'DBinput.importMovesFromCSV'");
                e.printStackTrace();
            }

            Statement tableStmt = DBConnection.getDBconnection().getConnection().createStatement();
            String dropMovesTable = "DROP TABLE IF EXISTS moves";
            int dropUpdate = tableStmt.executeUpdate(dropMovesTable);

            String createTable = "CREATE TABLE moves " +
                    "(nodeID INT, longName VARCHAR(255), date VARCHAR(20), primary key (nodeID, longName, date)," +
                    "constraint fk_nodeID foreign key(nodeID) references nodes(nodeID)," +
                    "constraint fk_longName foreign key(longName) references locationNames(longName));";
            int tableUpdate = tableStmt.executeUpdate(createTable);

            try {
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
                        Statement rowStmt = DBConnection.getDBconnection().getConnection().createStatement();
                        int rowUpdate = rowStmt.executeUpdate(rowQuery);
                        rowStmt.close();
                    }
                }
                br.close();
            } catch (IOException e) {
                System.out.println("Error reading the file in the method 'DBinput.importMovesFromCSV'");
                e.printStackTrace();
            }
            tableStmt.close();
        } catch (SQLException e) {
            System.out.println("ERROR: Query could not be executed in the method 'DBinput.importMovesFromCSV'");
        }
    }

}
