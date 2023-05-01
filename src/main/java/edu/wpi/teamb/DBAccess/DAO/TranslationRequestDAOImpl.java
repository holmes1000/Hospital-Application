package edu.wpi.teamb.DBAccess.DAO;


import edu.wpi.teamb.DBAccess.DBconnection;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.Full.FullFactory;
import edu.wpi.teamb.DBAccess.Full.FullTranslationRequest;
import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.DBAccess.ORMs.TranslationRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class TranslationRequestDAOImpl implements IDAO {
    ArrayList<FullTranslationRequest> translationRequests;


    public TranslationRequestDAOImpl() {
        translationRequests = getAllHelper();
    }

    /**
     * Gets all local TranslationRequest objects
     *
     * @return an ArrayList of all local TranslationRequest objects
     */
    @Override
    public ArrayList<FullTranslationRequest> getAll() {
        return translationRequests;
    }

    /**
     * Sets all TranslationRequest objects using the database
     */
    @Override
    public void setAll() { translationRequests = getAllHelper(); }


    /**
     * Gets a TranslationRequest including the information from request table
     *
     * @param id of the request
     * @return a FullTranslationRequest with information from request and translation request tables
     */
    @Override
    public FullTranslationRequest get(Object id) {
        Integer idInt = (Integer) id;
        for (FullTranslationRequest fr : translationRequests) {
            if (fr.getId() == idInt) {
                return fr;
            }
        } return null;
    }


    /**
     * Gets all TranslationRequest objects from the database
     *
     * @return an ArrayList of all TranslationRequest objects from the database
     */
    public ArrayList<FullTranslationRequest> getAllHelper() {
        FullFactory ff = new FullFactory();
        IFull furn = ff.getFullRequest("Translation");
        ArrayList<TranslationRequest> frs = new ArrayList<TranslationRequest>();
        try {
            ResultSet rs = getDBRowAllRequests();
            while (rs.next()) {
                frs.add(new TranslationRequest(rs));
            }
            return (ArrayList<FullTranslationRequest>) furn.listFullRequests(frs);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'TranslationRequestDAOImpl.getAllHelper': " + e.getMessage());
        }
        DBconnection.getDBconnection().closeDBconnection();
        DBconnection.getDBconnection().forceClose();
        return (ArrayList<FullTranslationRequest>) furn.listFullRequests(frs);
    }


    /**
     * Adds a TranslationRequest object to the both the database and local list
     *
     * @param request the TranslationRequest object to be added
     */
    @Override
    public void add(Object request) {
        String[] translationReq = (String[]) request;
        String[] values = {translationReq[0], translationReq[1], "Translation", translationReq[2], translationReq[3], translationReq[4], translationReq[5], translationReq[6]};
        int id = insertDBRowNewTranslationRequest(values);
        ResultSet rs = DButils.getRowCond("requests", "dateSubmitted", "id = " + id);
        Timestamp dateSubmitted = null;
        try {
            rs.next();
            dateSubmitted = rs.getTimestamp("dateSubmitted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FullTranslationRequest ffr = new FullTranslationRequest(id, translationReq[0], dateSubmitted, translationReq[1], translationReq[2], translationReq[3], translationReq[4], translationReq[5], translationReq[6]);
        translationRequests.add(ffr);
        RequestDAOImpl.getRequestDaoImpl().getAll().add(new Request(ffr));
    }


    /**
     * Removes a TranslationRequest from the both the database and the local list
     *
     * @param request the TranslationRequest object to be removed
     */
    @Override
    public void delete(Object request) {
        FullTranslationRequest ffr = (FullTranslationRequest) request;
        DButils.deleteRow("translationrequests", "id =" + ffr.getId() + "");
        DButils.deleteRow("requests", "id =" + ffr.getId() + "");
        translationRequests.remove(ffr);
        Request req = new Request(ffr);
        RequestDAOImpl.getRequestDaoImpl().getAll().remove(req);
    }


    /**
     * Updates a TranslationRequest object in both the database and local list
     *
     * @param request the TranslationRequest object to be updated
     */
    @Override
    public void update(Object request) {
        FullTranslationRequest ffr = (FullTranslationRequest) request;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String[] values = {Integer.toString(ffr.getId()), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted().toString(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getType(), ffr.getModel(), String.valueOf(ffr.isAssembly())};
        String[] colsTranslation = {"type", "model", "assembly"};
        String[] valuesTranslation = {ffr.getLanguage(), ffr.getMedical(), ffr.getMessage()};
        String[] colsReq = {"employee", "requeststatus", "requesttype", "locationname", "notes"};
        String[] valuesReq = {ffr.getEmployee(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes()};;
        DButils.updateRow("translationrequests", colsTranslation, valuesTranslation, "id = " + ffr.getId());
        DButils.updateRow("requests", colsReq, valuesReq, "id = " + ffr.getId());
        for (int i = 0; i < translationRequests.size(); i++) {
            if (translationRequests.get(i).getId() == ffr.getId()) {
                translationRequests.set(i, ffr);
            }
        }
        Request req = new Request(ffr);
        RequestDAOImpl.getRequestDaoImpl().update(req);
    }


    /**
     * Gets all rows from the database of conference requests
     *
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowAllRequests() throws SQLException {
        return DButils.getCol("translationrequests", "*");
    }


    /**
     * Updates the special instructions of a row in the TranslationRequests table
     *
     * @param specialInstructions the specialInstructions to update
     * @param col the column to search for the value
     * @param val the value to search for in the column
     */
    public void updateRowSpecialInstructions(String specialInstructions, String[] col, String[] val) {
        updateRows(col, val, "specialInstructions = " + specialInstructions);
    }


    /**
     * Returns a String representation of a FlowerRequest
     *
     * @param request the FlowerRequest to turn into a String
     * @return the String representation of the FlowerRequest
     */
    public String toString(TranslationRequest request) {
        return request.getId() + " " + request.getLanguage() + " " + request.getMedical() + " " + request.getMessage();
    }


    /**
     * Inserts a new row into the TranslationRequests table
     *
     * @param values the values to insert into the row
     * @return the id of the row that was inserted
     */
    public static int insertDBRowNewTranslationRequest(String[] values) {
        String[] colsTranslation = {"id", "language", "medical", "message",};
        String[] colsReq = {"employee", "requeststatus","requesttype", "locationname", "notes"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4]};
        int id = DButils.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesTranslation = {Integer.toString(id), values[5], values[6], values[7]};
        DButils.insertRow("translationrequests", colsTranslation, valuesTranslation);
        return id;
    }


    /**
     * Returns a ResultSet of the row(s) that matches the given column and value in the TranslationRequests table
     *
     * @param col the column to search for the value
     * @param value the value to search for in the column
     * @return a ResultSet of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowFromCol(String col, String value) {
        return DButils.getRowCond("translationrequests", "*", col + " = " + value);
    }


    /**
     * Returns a ResultSet of the row(s) that matches the given ID in the TranslationRequests table
     *
     * @param id the id of the row to get
     * @return a ResultSet of the row(s) that matches the given ID
     */
    public ResultSet getDBRowID(int id) {
        return getDBRowFromCol("id", Integer.toString(id));
    }


    /**
     * Returns a ResultSet of the row(s) that matches the given translationType in the TranslationRequests table
     *
     * @param translationType the translationType of the row to get
     * @return a ResultSet of the row(s) that matches the given translationType
     */
    public ResultSet getDBRowTranslationType(String translationType) {
        return getDBRowFromCol("language", translationType);
    }


    /**
     * Returns a ResultSet of the row(s) that matches the given model in the TranslationRequests table
     *
     * @param model the model of the row to get
     * @return a ResultSet of the row(s) that matches the given model
     */
    public ResultSet getDBRowModel(String model) {
        return getDBRowFromCol("model", model);
    }


    /**
     * Returns a ResultSet of the row(s) that matches the given assembly in the TranslationRequests table
     *
     * @param assembly the employee of the row to get
     * @return a ResultSet of the row(s) that matches the given assembly
     */
    public ResultSet getDBRowAssembly(String assembly) {
        return getDBRowFromCol("assembly", assembly);
    }


    /**
     * Updates the rows in the TranslationRequests table that match the given condition
     *
     * @param col the columns to update
     * @param val the values to update the columns to
     * @param cond the condition to search for
     */
    private void updateRows(String[] col, String[] val, String cond) {
        if (col.length != val.length)
            throw new IllegalArgumentException("Column and value arrays must be the same length");
        DButils.updateRow("TranslationRequests", col, val, cond);
    }


    /**
     * Updates the rows in the TranslationRequests table that match the given ID
     *
     * @param id the id of the row to update
     * @param col the columns to update
     * @param val the values to update the columns to
     */
    public void updateRowID(int id, String[] col, String[] val) {
        updateRows(col, val, "id = " + id);
    }


    /**
     * Updates the rows in the TranslationRequests table that match the given translationType
     *
     * @param translationType the translationType of the row to update
     * @param col the columns to update
     * @param val the values to update the columns to
     */
    public void updateRowTranslationType(String translationType, String[] col, String[] val) {
        updateRows(col, val, "translationType = " + translationType);
    }


    /**
     * Updates the rows in the TranslationRequests table that match the given model
     *
     * @param medical the model of the row to update
     * @param col the columns to update
     * @param val the values to update the columns to
     */
    public void updateRowModel(String medical, String[] col, String[] val) {
        updateRows(col, val, "medical = " + medical);
    }

    /**
     * Updates the rows in the TranslationRequests table that match the given assembly
     * @param message the assembly of the row to update
     * @param col the columns to update
     * @param val the values to update the columns to
     */
    public void updateRowMessage(String message, String[] col, String[] val) {
        updateRows(col, val, "message = " + message);
    }
}