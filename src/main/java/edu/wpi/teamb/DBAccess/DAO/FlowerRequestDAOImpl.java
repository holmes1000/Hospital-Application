package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.Full.FullFactory;
import edu.wpi.teamb.DBAccess.Full.FullFlowerRequest;
import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class FlowerRequestDAOImpl implements IDAO {

    ArrayList<FullFlowerRequest> flowerRequests;

    public FlowerRequestDAOImpl() throws SQLException {
        flowerRequests = getAllHelper();
    }

    @Override
    public ArrayList<FullFlowerRequest> getAll() {
        return flowerRequests;
    }
    @Override
    public FullFlowerRequest get(Object id) {
        Integer idInt = (Integer) id;
        FlowerRequest fr = null;
        Request r = null;
        try {
            ResultSet rs = DB.getRowCond("flowerrequests", "*", "id = " + idInt);
            rs.next();
            fr = new FlowerRequest(rs);
            ResultSet rs1 = RequestDAOImpl.getDBRowID(idInt);
            rs1.next();
            r = new Request(rs1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new FullFlowerRequest(r, fr);
    }

    public ArrayList<FullFlowerRequest> getAllHelper() throws SQLException {
        FullFactory ff = new FullFactory();
        IFull flower = ff.getFullRequest("Flower");
        ResultSet rs = getDBRowAllRequests();
        ArrayList<FlowerRequest> frs = new ArrayList<FlowerRequest>();
        while (rs.next()) {
            frs.add(new FlowerRequest(rs));
        }
        return (ArrayList<FullFlowerRequest>) flower.listFullRequests(frs);
    }

    @Override
    public void add(Object request) {
        String[] flowerReq = (String[]) request;
        String[] values = {flowerReq[0], flowerReq[1], "Flower", flowerReq[2], flowerReq[3], flowerReq[4], flowerReq[5], flowerReq[6], flowerReq[7]};
        int id = insertDBRowNewFlowerRequest(values);
        ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
        Timestamp dateSubmitted = null;
        try {
            rs.next();
            dateSubmitted = rs.getTimestamp("dateSubmitted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        flowerRequests.add(new FullFlowerRequest(id, flowerReq[0], dateSubmitted, flowerReq[1], flowerReq[2], flowerReq[3], flowerReq[4], flowerReq[5], flowerReq[6], flowerReq[7]));
        RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, flowerReq[0], dateSubmitted, flowerReq[1], "Flower", flowerReq[2], flowerReq[3]));
    }

    @Override
    public void delete(Object request) {
        FullFlowerRequest ffr = (FullFlowerRequest) request;
        DB.deleteRow("flowerrequests", "id" + ffr.getId() + "");
        DB.deleteRow("requests", "id =" + ffr.getId() + "");
        flowerRequests.remove(ffr);
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
    }

    @Override
    public void update(Object request) {
        FullFlowerRequest ffr = (FullFlowerRequest) request;
        String[] colsFlower = {"flowertype", "color", "size", "message"};
        String[] valuesFlower = {ffr.getFlowerType(), ffr.getColor(), ffr.getSize(), ffr.getMessage()};
        String[] colsReq = {"employee", "datesubmitted", "requeststatus", "requesttype", "locationname", "notes"};
        String[] valuesReq = {ffr.getEmployee(), String.valueOf(ffr.getDateSubmitted()), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes()};
        DB.updateRow("conferencerequests", colsFlower, valuesFlower, "id = " + ffr.getId());
        DB.updateRow("requests", colsReq, valuesReq, "id = " + ffr.getId());
        for (int i = 0; i < flowerRequests.size(); i++) {
            if (flowerRequests.get(i).getId() == ffr.getId()) {
                flowerRequests.set(i, ffr);
            }
        }
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes());
        RequestDAOImpl.getRequestDaoImpl().update(req);
    }

    /**
     * Gets all rows from the database of conference requests
     *
     * @return the result set of the row(s) that matches the given column and value
     */
    private ResultSet getDBRowAllRequests() throws SQLException {
        return DB.getCol("flowerrequests", "*");
    }

    public String toString(FlowerRequest request) {
        return request.getId() + " " + request.getFlowerType() + " " + request.getColor() + " " + request.getSize() + " " + request.getMessage();
    }

    public static int insertDBRowNewFlowerRequest(String[] values) {
        String[] colsFlower = {"id", "flowerType", "color", "size", "message"};
        String[] colsReq = {"employee", "requeststatus","requesttype", "locationname", "notes"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4]};
        int id = DB.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesFlower = {Integer.toString(id), values[5], values[6], values[7], values[8]};
        DB.insertRow("flowerrequests", colsFlower, valuesFlower);
        return id;
    }

    private ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("FlowerRequests", "*", col + " = " + value);
    }

    public ResultSet getDBRowID(int id) {
        return getDBRowFromCol("id", Integer.toString(id));
    }

    public ResultSet getDBRowFlowerType(String flowerType) {
        return getDBRowFromCol("flowerType", flowerType);
    }

    public ResultSet getDBRowColor(String color) {
        return getDBRowFromCol("color", color);
    }

    public ResultSet getDBRowSize(String size) {
        return getDBRowFromCol("size", size);
    }

    public ResultSet getDBRowMessage(String message) {
        return getDBRowFromCol("message", message);
    }

    private void updateRows(String[] col, String[] val, String cond) {
        if (col.length != val.length)
            throw new IllegalArgumentException("Column and value arrays must be the same length");
        DB.updateRow("flowerrequests", col, val, cond);
    }

    public void updateRowFlowerType(String flowerType, String[] col, String[] val) {
        updateRows(col, val, "flowerType = " + flowerType);
    }

    public void updateRowColor(String color, String[] col, String[] val) {
        updateRows(col, val, "color = " + color);
    }

    public void updateRowSize(String size, String[] col, String[] val) {
        updateRows(col, val, "size = " + size);
    }

    public void updateRowMessage(String message, String[] col, String[] val) {
        updateRows(col, val, "message = " + message);
    }
}
