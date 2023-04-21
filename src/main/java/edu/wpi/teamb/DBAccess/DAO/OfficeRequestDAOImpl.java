package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullOfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OfficeRequestDAOImpl implements IDAO {
    ArrayList<FullOfficeRequest> officeRequests;

    public OfficeRequestDAOImpl() throws SQLException {
        officeRequests = getAllHelper();
    }
    @Override
    public FullOfficeRequest get(Object id) {
        Integer idInt = (Integer) id;
        OfficeRequest or = null;
        Request r = null;
        try {
            ResultSet rs = DB.getRowCond("officerequests", "*", "id = " + idInt);
            rs.next();
            or = new OfficeRequest(rs);
            ResultSet rs1 = RequestDAOImpl.getDBRowID(idInt);
            rs1.next();
            r = new Request(rs1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new FullOfficeRequest(r, or);
    }

    @Override
    public ArrayList<FullOfficeRequest> getAll() {
        return officeRequests;
    }

    public ArrayList<FullOfficeRequest> getAllHelper() throws SQLException {
        ResultSet rs = getDBRowAllRequests();
        ArrayList<OfficeRequest> ors = new ArrayList<OfficeRequest>();
        while (rs.next()) {
            ors.add(new OfficeRequest(rs));
        }
        return FullOfficeRequest.listFullOfficeRequests(ors);
    }

    @Override
    public void add(Object request) {
        String[] officeReq = (String[]) request;
        String[] values = {officeReq[0], officeReq[1], "Office", officeReq[2], officeReq[3], officeReq[4], officeReq[5], officeReq[6]};
        int id = insertDBRowNewOfficeRequest(values);
        ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
        Timestamp dateSubmitted = null;
        try {
            rs.next();
            dateSubmitted = rs.getTimestamp("dateSubmitted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        officeRequests.add(new FullOfficeRequest(id, officeReq[0], dateSubmitted, officeReq[1], officeReq[2], officeReq[3], officeReq[4], officeReq[5], Integer.valueOf(officeReq[6])));
        RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, officeReq[0], dateSubmitted, officeReq[1], officeReq[2], officeReq[3], officeReq[4]));
    }

    @Override
    public void delete(Object request) {
        FullOfficeRequest ffr = (FullOfficeRequest) request;
        DB.deleteRow("officerequests", "id" + ffr.getId() + "");
        DB.deleteRow("requests", "id =" + ffr.getId() + "");
        officeRequests.remove(ffr);
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
    }

    @Override
    public void update(Object request) {
        FullOfficeRequest ofr = (FullOfficeRequest) request;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //String[] values = {Integer.toString(ofr.getId()), ofr.getEmployee(), ofr.getDateSubmitted().toString(), ofr.getRequestStatus(), ofr.getRequestType(), ofr.getItem(), Integer.toString(ofr.getQuantity()), ofr.getSpecialInstructions(), ofr.getType()};
        String[] colsOffice = {"type", "item", "quantity"};
        String[] valuesOffice = {ofr.getType(), ofr.getItem(), String.valueOf(ofr.getQuantity())};
        String[] colsReq = {"employee", "datesubmitted", "requeststatus", "requesttype"};
        String[] valuesReq = {ofr.getEmployee(), String.valueOf(ofr.getDateSubmitted()), ofr.getRequestStatus(), ofr.getRequestType()};
        DB.updateRow("furniturerequests", colsOffice, valuesOffice, "id = " + ofr.getId());
        DB.updateRow("requests", colsReq, valuesReq, "id = " + ofr.getId());
        for (int i = 0; i < officeRequests.size(); i++) {
            if (officeRequests.get(i).getId() == ofr.getId()) {
                officeRequests.set(i, ofr);
            }
        }
        Request req = new Request(ofr.getId(), ofr.getEmployee(), ofr.getDateSubmitted(), ofr.getRequestStatus(), ofr.getRequestType(), ofr.getLocationName(), ofr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().update(req);
    }

    /**
     * Gets all rows from the database of conference requests
     *
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowAllRequests() throws SQLException {
        return DB.getCol("officerequests", "*");
    }

    public void updateRowSpecialInstructions(String specialInstructions, String[] col, String[] val) {
        updateRows(col, val, "specialInstructions = " + specialInstructions);
    }

    public static int insertDBRowNewOfficeRequest(String[] values) {
        String[] colsOffice = {"id", "type", "item", "quantity"};
        String[] colsReq = {"employee", "requeststatus","requesttype", "locationname", "notes"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4]};
        int id = DB.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesOffice = {Integer.toString(id), values[5], values[6], values[7]};
        DB.insertRow("officerequests", colsOffice, valuesOffice);
        return id;
    }

    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("officerequests", "*", col + " = " + value);
    }

    public ResultSet getDBRowID(int id) {
        return getDBRowFromCol("id", Integer.toString(id));
    }
    private void updateRows(String[] col, String[] val, String cond) {
        if (col.length != val.length)
            throw new IllegalArgumentException("Column and value arrays must be the same length");
        DB.updateRow("officerequests", col, val, cond);
    }
}
