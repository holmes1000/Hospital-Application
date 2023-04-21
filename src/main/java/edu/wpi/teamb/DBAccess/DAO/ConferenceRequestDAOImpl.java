package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.ConferenceRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;;

public class ConferenceRequestDAOImpl implements IDAO {
    ArrayList<FullConferenceRequest> conferenceRequests;

    public ConferenceRequestDAOImpl() throws SQLException {
        conferenceRequests = getAllHelper();
    }

    /**
     * gets a Conference request including the information from request table
     * @param id of the request
     * @return FullConferenceRequest information from request and conference request table
     * @throws SQLException if the request is not found
     */
    @Override
    public FullConferenceRequest get(Object id) {
        int idInt = (Integer) id;
        ConferenceRequest cr = null;
        Request r = null;
        try {
            ResultSet rs = DB.getRowCond("conferencerequests", "*", "id = " + idInt);
            rs.next();
            cr = new ConferenceRequest(rs);
            ResultSet rs1 = RequestDAOImpl.getDBRowID(idInt);
            rs1.next();
            r = new Request(rs1);
        } catch (SQLException e) {
            throw new RuntimeException("Conference Request not found");
        }
        return new FullConferenceRequest(r, cr);
    }

    /**
     * gets all Conference requests
     *
     * @return list of Conference requests
     */
    public ArrayList<FullConferenceRequest> getAll() {
        return conferenceRequests;
    }

    /**
     * gets all Conference requests including the information from request table from the database
     *
     * @return list of conference requests
     */
    public ArrayList<FullConferenceRequest> getAllHelper() throws SQLException {
        ResultSet rs = getDBRowAllRequests();
        ArrayList<ConferenceRequest> crs = new ArrayList<ConferenceRequest>();
        while (rs.next()) {
            crs.add(new ConferenceRequest(rs));
        }
        return FullConferenceRequest.listFullConferenceRequests(crs);
    }

    /**
     * adds given request
     *
     * @param request to add
     */
    @Override
    public void add(Object request) {
        String[] confReq = (String[]) request;
        String[] values = {confReq[0], confReq[1], "Conference", confReq[2], confReq[3], confReq[4], confReq[5], confReq[6], confReq[7]};
        int id = insertDbRowNewConferenceRequest(values);
        ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
        Timestamp dateSubmitted = null;
        ResultSet rs1 = DB.getRowCond("conferencerequests", "dateRequested", "id = " + id);
        Timestamp dateRequested = null;
        try {
            rs.next();
            dateSubmitted = rs.getTimestamp("dateSubmitted");
            rs1.next();
            dateRequested = rs1.getTimestamp("dateRequested");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conferenceRequests.add(new FullConferenceRequest(id, confReq[0], dateSubmitted, confReq[1], confReq[2], confReq[3], dateRequested, confReq[5], confReq[6], Integer.parseInt(confReq[7])));
        RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, confReq[0], dateSubmitted, confReq[2], "Conference", confReq[3], confReq[4]));
    }

    /**
     * deletes given request
     *
     * @param request to delete
     */
    @Override
    public void delete(Object request) {
        FullConferenceRequest fcr = (FullConferenceRequest) request;
        DB.deleteRow("conferencerequests", "id" + fcr.getId() + "");
        DB.deleteRow("requests", "id =" + fcr.getId() + "");
        conferenceRequests.remove(fcr);
        Request req = new Request(fcr.getId(), fcr.getEmployee(), fcr.getDateSubmitted(), fcr.getRequestStatus(), fcr.getRequestType(), fcr.getLocationName(), fcr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
    }

    /**
     * updates given request with the given values
     *
     * @param request to update with new values
     */
    @Override
    public void update(Object request) {
        FullConferenceRequest fcr = (FullConferenceRequest) request;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] colsConf = {"daterequested", "eventname", "bookingreason"};
        String[] valuesConf = {String.valueOf(fcr.getDateRequested()), fcr.getEventName(), fcr.getBookingReason(), String.valueOf(fcr.getDuration())};
        String[] colsReq = {"employee", "datesubmitted", "requeststatus", "requesttype", "locationname", "notes"};
        String[] valuesReq = {fcr.getEmployee(), String.valueOf(fcr.getDateSubmitted()), fcr.getRequestStatus(), fcr.getRequestType(), fcr.getLocationName(), fcr.getNotes()};
        DB.updateRow("conferencerequests", colsConf, valuesConf, "id = " + fcr.getId());
        DB.updateRow("requests", colsReq, valuesReq, "id = " + fcr.getId());
        for (int i = 0; i < conferenceRequests.size(); i++) {
            if (conferenceRequests.get(i).getId() == fcr.getId()) {
                conferenceRequests.set(i, fcr);
            }
        }
        Request req = new Request(fcr.getId(), fcr.getEmployee(), fcr.getDateSubmitted(), fcr.getRequestStatus(), fcr.getRequestType(), fcr.getLocationName(), fcr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().update(req);
    }

    /**
     * inserts a new conference request into the database
     * @param values to insert
     */
    public static int insertDbRowNewConferenceRequest(String[] values) {
        String[] colsConf = {"id", "daterequested", "eventname", "bookingreason", "duration"};
        String[] colsReq = {"employee", "requeststatus", "requesttype", "locationname", "notes"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4]};
        int id = DB.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesConf = {Integer.toString(id),values[5], values[6], values[7], values[8]};
        DB.insertRow("conferencerequests", colsConf, valuesConf);
        return id;
    }

    // Access from Database Methods

    // Methods to get information about the conference request from the database

    /**
     * Searches through the database for the row(s) that matches the given column
     * and value
     *
     * @param id   the id of the conferenceRequest to search for
     * @return the result set of the row(s) that matches the given column and value
     */
    public ResultSet getDBRowID(int id) {
        return getDBRowFromCol("id", Integer.toString(id));
    }

    /**
     * Gets the row(s) from the database that matches the given order from
     *
     * @param col the column to search for
     * @param value the value to search for
     * @return the result set of the row that matches the given orderfrom
     */

    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("ConferenceRequests", "*", col + " = " + value);
    }

    /**
     * Gets all rows from the database of conference requests
     *
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowAllRequests() throws SQLException {
        return DB.getCol("conferencerequests", "*");
    }
}
