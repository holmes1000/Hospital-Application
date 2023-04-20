package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullOfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public ArrayList<FullOfficeRequest> getAllHelper() {
        ArrayList<OfficeRequest> ors = new ArrayList<OfficeRequest>();
        try {
            ResultSet rs = getDBRowAllRequests();
            while (rs.next()) {
                ors.add(new OfficeRequest(rs));
            }
            return FullOfficeRequest.listFullOfficeRequests(ors);
        } catch (SQLException e) {
            System.err.println("ERROR Query Failed in method 'OfficeRequestDAOImpl.getAllHelper': " + e.getMessage());
        }
        return FullOfficeRequest.listFullOfficeRequests(ors);
    }

    @Override
    public void add(Object request) {
        String[] officeReq = (String[]) request;
        String[] values = {officeReq[0], officeReq[1], officeReq[2], officeReq[3], officeReq[4], officeReq[5], officeReq[6], officeReq[7], officeReq[8], officeReq[9]};
        int id = insertDBRowNewOfficeRequest(values);
        ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
        Date dateSubmitted = null;
        try {
            rs.next();
            dateSubmitted = rs.getDate("dateSubmitted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        officeRequests.add(new FullOfficeRequest(id, officeReq[0], officeReq[1], officeReq[2], dateSubmitted, officeReq[3], officeReq[9], officeReq[5], Integer.parseInt(officeReq[7]), officeReq[8], officeReq[4]));
        RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, officeReq[0], officeReq[1], officeReq[2], dateSubmitted, officeReq[3], officeReq[4], officeReq[9]));
    }

    @Override
    public void delete(Object request) {
        FullOfficeRequest ffr = (FullOfficeRequest) request;
        DB.deleteRow("officerequests", "id" + ffr.getId() + "");
        DB.deleteRow("requests", "id =" + ffr.getId() + "");
        officeRequests.remove(ffr);
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName());
        RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
    }

    @Override
    public void update(Object request) {
        FullOfficeRequest ofr = (FullOfficeRequest) request;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] values = {Integer.toString(ofr.getId()), ofr.getEmployee(), ofr.getFloor(), ofr.getRoomNumber(), ofr.getDateSubmitted().toString(), ofr.getRequestStatus(), ofr.getRequestType(), ofr.getItem(), Integer.toString(ofr.getQuantity()), ofr.getSpecialInstructions(), ofr.getType()};
        String[] colsOffice = {"item", "quantity", "specialinstructions", "type"};
        String[] valuesOffice = { values[6], values[7], values[8], values[9], values[10]};
        String[] colsReq = {"employee", "floor", "roomnumber", "datesubmitted", "requeststatus", "requesttype"};
        String[] valuesReq = {values[1], values[2], values[3], values[4], values[5], values[6]};
        DB.updateRow("furniturerequests", colsOffice, valuesOffice, "id = " + values[0]);
        DB.updateRow("requests", colsReq, valuesReq, "id = " + values[0]);
        for (int i = 0; i < officeRequests.size(); i++) {
            if (officeRequests.get(i).getId() == ofr.getId()) {
                officeRequests.set(i, ofr);
            }
        }
        Request req = new Request(ofr.getId(), ofr.getEmployee(), ofr.getFloor(), ofr.getRoomNumber(), ofr.getDateSubmitted(), ofr.getRequestStatus(), ofr.getRequestType(), ofr.getLocationName());
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
        String[] colsOffice = {"id", "item", "quantity", "specialInstructions", "type"};
        String[] colsReq = {"employee", "floor", "roomnumber", "requeststatus","requesttype", "location_name"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4], values[9]};
        int id = DB.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesOffice = {Integer.toString(id), values[5], values[7], values[8], values[6]};
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
