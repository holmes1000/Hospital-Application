package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullFlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.FlowerRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        ResultSet rs = getDBRowAllRequests();
        ArrayList<FlowerRequest> frs = new ArrayList<FlowerRequest>();
        while (rs.next()) {
            frs.add(new FlowerRequest(rs));
        }
        return FullFlowerRequest.listFullFlowerRequests(frs);
    }

    @Override
    public void add(Object request) {
        String[] flowerReq = (String[]) request;
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date dateSubmitted;
//        Date dateRequested;
//        try {
//            dateSubmitted = (Date) dateFormat.parse(flowerReq[3]);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        String[] values = {flowerReq[0], flowerReq[1], flowerReq[2], flowerReq[3], flowerReq[4], flowerReq[5], flowerReq[6], flowerReq[7], flowerReq[8], flowerReq[9], flowerReq[10], flowerReq[11]};
        int id = insertDBRowNewFlowerRequest(values);
        ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
        Date dateSubmitted = null;
        try {
            rs.next();
            dateSubmitted = rs.getDate("dateSubmitted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        flowerRequests.add(new FullFlowerRequest(id, flowerReq[0], flowerReq[1], flowerReq[2], dateSubmitted, flowerReq[4], flowerReq[5], flowerReq[6], flowerReq[7], flowerReq[8], flowerReq[9], flowerReq[10]));
        RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, flowerReq[0], flowerReq[1], flowerReq[2], dateSubmitted, flowerReq[4], flowerReq[5], flowerReq[10]));
    }

    @Override
    public void delete(Object request) {
        FullFlowerRequest ffr = (FullFlowerRequest) request;
        DB.deleteRow("flowerrequests", "id" + ffr.getId() + "");
        DB.deleteRow("requests", "id =" + ffr.getId() + "");
        flowerRequests.remove(ffr);
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocation_name());
        RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
    }

    @Override
    public void update(Object request) {
        FullFlowerRequest ffr = (FullFlowerRequest) request;
        String[] values = {Integer.toString(ffr.getId()), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted().toString(), ffr.getRequestStatus(), ffr.getFlowerType(), ffr.getFlowerType(), ffr.getColor(), ffr.getType(), ffr.getMessage(), ffr.getSpecialInstructions()};
        String[] colsFlower = {"flowertype", "color", "type", "message", "specialinstructions"};
        String[] valuesFlower = { values[6], values[7], values[8], values[9], values[10]};
        String[] colsReq = {"employee", "floor", "roomnumber", "datesubmitted", "requeststatus", "requesttype"};
        String[] valuesReq = {values[1], values[2], values[3], values[4], values[5], values[6]};
        DB.updateRow("conferencerequests", colsFlower, valuesFlower, "id = " + values[0]);
        DB.updateRow("requests", colsReq, valuesReq, "id = " + values[0]);
        for (int i = 0; i < flowerRequests.size(); i++) {
            if (flowerRequests.get(i).getId() == ffr.getId()) {
                flowerRequests.set(i, ffr);
            }
        }
        Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocation_name());
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

    public void updateRowSpecialInstructions(String specialInstructions, String[] col, String[] val) {
        updateRows(col, val, "specialInstructions = " + specialInstructions);
    }

    public String toString(FlowerRequest request) {
        return request.getId() + " " + request.getFlowerType() + " " + request.getColor() + " " + request.getType() + " " + request.getMessage() + " " + request.getSpecialInstructions();
    }

    public static int insertDBRowNewFlowerRequest(String[] values) {
        String[] colsFlower = {"id", "flowerType", "color", "Type", "message", "specialInstructions"};
        String[] colsReq = {"employee", "floor", "roomnumber", "requeststatus","requesttype", "location_name"};
        String[] valuesReq = {values[0], values[1], values[2], values[3], values[4], values[10]};
        int id = DB.insertRowRequests("requests", colsReq, valuesReq);
        String[] valuesFlower = {Integer.toString(id), values[6], values[7], values[8], values[9], values[11]};
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

    public ResultSet getDBRowType(String type) {
        return getDBRowFromCol("type", type);
    }

    public ResultSet getDBRowMessage(String message) {
        return getDBRowFromCol("message", message);
    }

    public ResultSet getDBRowSpecialInstructions(String specialInstructions) {
        return getDBRowFromCol("specialInstructions", specialInstructions);
    }
    private void updateRows(String[] col, String[] val, String cond) {
        if (col.length != val.length)
            throw new IllegalArgumentException("Column and value arrays must be the same length");
        DB.updateRow("flowerrequests", col, val, cond);
    }

    public void updateRowID(int id, String[] col, String[] val) {
        updateRows(col, val, "id = " + id);
    }

    public void updateRowFlowerType(String flowerType, String[] col, String[] val) {
        updateRows(col, val, "flowerType = " + flowerType);
    }

    public void updateRowColor(String color, String[] col, String[] val) {
        updateRows(col, val, "color = " + color);
    }

    public void updateRowType(String type, String[] col, String[] val) {
        updateRows(col, val, "type = " + type);
    }

    public void updateRowMessage(String message, String[] col, String[] val) {
        updateRows(col, val, "message = " + message);
    }
}
