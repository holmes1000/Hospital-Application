package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullFurnitureRequest;
import edu.wpi.teamb.DBAccess.ORMs.FurnitureRequest;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FurnitureRequestDAOImpl implements IDAO {
    ArrayList<FullFurnitureRequest> furnitureRequests;

        public FurnitureRequestDAOImpl() throws SQLException {
            furnitureRequests = getAllHelper();
        }

        @Override
        public ArrayList<FullFurnitureRequest> getAll() {
            return furnitureRequests;
        }
        @Override
        public FullFurnitureRequest get(Object id) {
            Integer idInt = (Integer) id;
            FurnitureRequest fr = null;
            Request r = null;
            try {
                ResultSet rs = DB.getRowCond("furniturerequests", "*", "id = " + idInt);
                rs.next();
                fr = new FurnitureRequest(rs);
                ResultSet rs1 = RequestDAOImpl.getDBRowID(idInt);
                rs1.next();
                r = new Request(rs1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new FullFurnitureRequest(r, fr);
        }

        public ArrayList<FullFurnitureRequest> getAllHelper() throws SQLException {
            ResultSet rs = getDBRowAllRequests();
            ArrayList<FurnitureRequest> frs = new ArrayList<FurnitureRequest>();
            while (rs.next()) {
                frs.add(new FurnitureRequest(rs));
            }
            return FullFurnitureRequest.listFullFurnitureRequests(frs);
        }

        @Override
        public void add(Object request) {
            String[] furnitureReq = (String[]) request;
            String[] values = {furnitureReq[0], furnitureReq[1], "Furniture", furnitureReq[2], furnitureReq[3], furnitureReq[4], furnitureReq[5], furnitureReq[6]};
            int id = insertDBRowNewFurnitureRequest(values);
            ResultSet rs = DB.getRowCond("requests", "dateSubmitted", "id = " + id);
            Timestamp dateSubmitted = null;
            try {
                rs.next();
                dateSubmitted = rs.getTimestamp("dateSubmitted");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            furnitureRequests.add(new FullFurnitureRequest(id, furnitureReq[0], dateSubmitted, furnitureReq[1], furnitureReq[2], furnitureReq[3], furnitureReq[4], furnitureReq[5], Boolean.getBoolean(furnitureReq[6])));
            RequestDAOImpl.getRequestDaoImpl().getRequests().add(new Request(id, furnitureReq[0], dateSubmitted, furnitureReq[1], "Furniture", furnitureReq[2], furnitureReq[3]));
        }

        @Override
        public void delete(Object request) {
            FullFurnitureRequest ffr = (FullFurnitureRequest) request;
            DB.deleteRow("furniturerequests", "id" + ffr.getId() + "");
            DB.deleteRow("requests", "id =" + ffr.getId() + "");
            furnitureRequests.remove(ffr);
            Request req = new Request(ffr.getId(), ffr.getEmployee(), ffr.getDateSubmitted(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes());
            RequestDAOImpl.getRequestDaoImpl().getRequests().remove(req);
        }

        @Override
        public void update(Object request) {
            FullFurnitureRequest ffr = (FullFurnitureRequest) request;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //String[] values = {Integer.toString(ffr.getId()), ffr.getEmployee(), ffr.getFloor(), ffr.getRoomNumber(), ffr.getDateSubmitted().toString(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getType(), ffr.getModel(), String.valueOf(ffr.isAssembly())};
            String[] colsFurniture = {"type", "model", "assembly"};
            String[] valuesFurniture = {ffr.getType(), ffr.getModel(), String.valueOf(ffr.isAssembly())};
            String[] colsReq = {"employee", "requeststatus", "requesttype", "locationname", "notes"};
            String[] valuesReq = {ffr.getEmployee(), ffr.getRequestStatus(), ffr.getRequestType(), ffr.getLocationName(), ffr.getNotes()};;
            DB.updateRow("furniturerequests", colsFurniture, valuesFurniture, "id = " + ffr.getId());
            DB.updateRow("requests", colsReq, valuesReq, "id = " + ffr.getId());
            for (int i = 0; i < furnitureRequests.size(); i++) {
                if (furnitureRequests.get(i).getId() == ffr.getId()) {
                    furnitureRequests.set(i, ffr);
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
            return DB.getCol("furniturerequests", "*");
        }

        public void updateRowSpecialInstructions(String specialInstructions, String[] col, String[] val) {
            updateRows(col, val, "specialInstructions = " + specialInstructions);
        }

        public String toString(FurnitureRequest request) {
            return request.getId() + " " + request.getType() + " " + request.getModel() + " " + request.isAssembly();
        }

        public static int insertDBRowNewFurnitureRequest(String[] values) {
            String[] colsFurniture = {"id", "type", "model", "assembly",};
            String[] colsReq = {"employee", "requeststatus","requesttype", "locationname", "notes"};
            String[] valuesReq = {values[0], values[1], values[2], values[3], values[4]};
            int id = DB.insertRowRequests("requests", colsReq, valuesReq);
            String[] valuesFurniture = {Integer.toString(id), values[5], values[6], values[7]};
            DB.insertRow("furniturerequests", colsFurniture, valuesFurniture);
            return id;
        }

        private ResultSet getDBRowFromCol(String col, String value) {
            return DB.getRowCond("furniturerequests", "*", col + " = " + value);
        }

        public ResultSet getDBRowID(int id) {
            return getDBRowFromCol("id", Integer.toString(id));
        }

        public ResultSet getDBRowFurnitureType(String furnitureType) {
            return getDBRowFromCol("type", furnitureType);
        }

        public ResultSet getDBRowModel(String model) {
            return getDBRowFromCol("model", model);
        }

        public ResultSet getDBRowAssembly(String assembly) {
            return getDBRowFromCol("assembly", assembly);
        }
        private void updateRows(String[] col, String[] val, String cond) {
            if (col.length != val.length)
                throw new IllegalArgumentException("Column and value arrays must be the same length");
            DB.updateRow("furniturerequests", col, val, cond);
        }

        public void updateRowID(int id, String[] col, String[] val) {
            updateRows(col, val, "id = " + id);
        }

        public void updateRowAssembly(String assembly, String[] col, String[] val) {
            updateRows(col, val, "assembly = " + assembly);
        }

        public void updateRowModel(String model, String[] col, String[] val) {
            updateRows(col, val, "model = " + model);
        }

        public void updateRowAssembly(boolean assembly, String[] col, String[] val) {
            updateRows(col, val, "assembly = " + assembly);
        }

}
