package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoveDAOImpl implements IDAO {

    static ArrayList<Move> moves;

    public MoveDAOImpl() throws SQLException {
        moves = getAllHelper();
    }

    @Override
    public Move get(Object id) {
        ResultSet rs = DB.getRowCond("Moves", "*", "nodeID = " + id + "");
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                return new Move(rs);
            } else
                throw new SQLException("No rows found");
        } catch (SQLException e) {
            // handle error

            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
    }
    @Override
    public ArrayList<Move> getAll() {
        return moves;
    }

    /**
     * Gets all moves
     *
     * @return A list of all moves
     */
    public ArrayList<Move> getAllHelper() throws SQLException {
        ResultSet rs = DB.getCol("moves", "*");
        ArrayList<Move> mvs = new ArrayList<Move>();
        while (rs.next()) {
            mvs.add(new Move(rs));
        }
        return mvs;
    }

    /**
     * Adds a move
     *
     * @param m The move to add
     */
    @Override
    public void add(Object m) {
        Move move = (Move) m;
        insertDBMove(move);
        moves.add(move);
    }

    /**
     * Deletes a move
     *
     * @param m The move to delete
     */
    @Override
    public void delete(Object m) {
        Move move = (Move) m;
        DB.deleteRow("Moves", "nodeID = " + move.getNodeID()+" AND longName = '"+move.getLongName()+"'"+ " AND date = '" + move.getDate() + "'");
        moves.remove(move);
    }

    /**
     * Updates a move
     *
     * @param m The move to update
     */
    @Override
    public void update(Object m) {
        Move move = (Move) m;
        String[] cols = {"nodeID", "longName", "date"};
        String[] vals = {Integer.toString(move.getNodeID()), move.getLongName(), move.getDate().toString()};
        DB.updateRow("Moves", cols, vals, "nodeID = " + move.getNodeID());
        for (int i = 0; i < moves.size(); i++) {
            if (moves.get(i).getNodeID() == (move.getNodeID())) {
                moves.set(i, move);
            }
        }
    }

    /**
     * Creates a Move object using a longName
     *
     * @param longName the longName to look for to get Move data
     */

    public static Move getMoveFromLongName(String longName) {
        ResultSet rs = DB.getRowCond("Moves", "*", "longName = '" + longName + "'");
        try {
            if (rs.isBeforeFirst()) {
                rs.next();
                return new Move(rs);
            } else
                throw new SQLException("No rows found");
        } catch (SQLException e) {
            // handle error
            System.err.println("ERROR Query Failed: " + e.getMessage());
            return null;
        }
    }

    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("Moves", "*", col + " = " + value);
    }


    public ResultSet getDBRowAllMoves() {
        return DB.getRowCond("moves", "*", "TRUE");
    }

    /**
     * Gets a ResultSet of rows from the Moves table that match the given nodeID
     *
     * @param nodeID the node to look for to get Move data
     */

    public ResultSet getDBRowNodeID(int nodeID) {
        return getDBRowFromCol("nodeID", "" + nodeID + "");
    }

    /**
     * Gets a ResultSet of rows from the Moves table that match the given longName
     *
     * @param longName the longName to look for to get Move data
     */

    public ResultSet getDBRowLongName(String longName) {
        return getDBRowFromCol("longName", "'" + longName + "'");
    }

    /**
     * Gets a ResultSet of rows from the Moves table that match the given date
     *
     * @param date the date to look for to get Move data
     */

    public ResultSet getDBRowDate(Date date) {
        return getDBRowFromCol("date", "" + date + "");
    }



    public void insertDBMove(Move m) {
        String[] cols = {"nodeID", "longName", "date"};
        String[] vals = {Integer.toString(m.getNodeID()), m.getLongName(), m.getDate().toString()};
        DB.insertRow("Moves", cols, vals);
    }
}
