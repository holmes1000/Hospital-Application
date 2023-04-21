package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.DBAccess.ORMs.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestDAOImpl implements IDAO {
    // TODO: check if this needs to implement IRequestDAO

    private ArrayList<Request> requests;

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    private RequestDAOImpl() {
        try{
            requests = getAllHelper();
        } catch (SQLException e) {
            System.out.println("SQL Exception in RequestDAOImpl constructor");
        }
    }

    private static class SingletonHelper {
        private static final RequestDAOImpl requestDaoImp = new RequestDAOImpl();
    }

    public static RequestDAOImpl getRequestDaoImpl() {
        return SingletonHelper.requestDaoImp;
    }

    @Override
    public IFull get(Object id) {
        int whichRequest = 0;
        ResultSet rs = DB.getRowCond("requests", "requesttype", "id = " + id);
        String requestType = null;
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                requestType = rs.getString("requesttype");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        switch (requestType) {
            case "Meal":
                MealRequestDAOImpl mr = null;
                try {
                    mr = new MealRequestDAOImpl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return mr.get(id);
            case "Conference":
                ConferenceRequestDAOImpl cr = null;
                try {
                    cr = new ConferenceRequestDAOImpl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return cr.get(id);
            case "Flower":
                FlowerRequestDAOImpl fr = null;
                try {
                    fr = new FlowerRequestDAOImpl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return fr.get(id);
            case "Furniture":
                FurnitureRequestDAOImpl ffr = null;
                try {
                    ffr = new FurnitureRequestDAOImpl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return ffr.get(id);
            case "Office":
                OfficeRequestDAOImpl or = null;
                try {
                    or = new OfficeRequestDAOImpl();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        }
        return null;
    }

    @Override
    public ArrayList<Request> getAll() {
        return requests;
    }

    public ArrayList<Request> getAllHelper() throws SQLException {
        ResultSet rs = DB.getCol("requests", "*");
        ArrayList<Request> rqs = new ArrayList<Request>();
        while (rs.next()) {
            Request r = new Request(rs);
            rqs.add(r);
        }
        return rqs;
    }

    public ArrayList<IFull> getAllHelper1() {
        ResultSet rs = null;
        ArrayList<IFull> rqs = new ArrayList<IFull>();
        try {
            rs = DB.getCol("requests", "*");
            while (rs.next()) {
                Request r = new Request(rs);
                rqs.add(get(r.getId()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rqs;
    }

    /**
     * adds given request
     *
     * @param request to add
     */
    //TODO: figure out a way to NEVER DO THIS (pls dont make me do this)
    @Override
    public void add(Object request) {
        Request r = (Request) request;
        Request req = new Request(r.getId(), r.getEmployee(), r.getDateSubmitted(), r.getRequestStatus(), r.getRequestType(), r.getLocationName(), r.getNotes());
        addHelper(req);
    }

    public void addHelper(Object request) {
        Request r = (Request) request;
        Request req = new Request(r.getId(), r.getEmployee(), r.getDateSubmitted(), r.getRequestStatus(), r.getRequestType(), r.getLocationName(), r.getNotes());
        requests.add(req);
    }

    /**
     * deletes given request
     *
     * @param request to delete
     */
    //TODO: figure out a way to NEVER DO THIS (pls dont make me do this) (again)
    @Override
    public void delete(Object request) {

    }

    /**
     * updates given request with the given values
     *
     * @param request to update with new values
     */
    //TODO: figure out a way to NEVER DO THIS (pls dont make me do this) (again) (again)
    @Override
    public void update(Object request) {
        Request r = (Request) request;
        Request req = new Request(r.getId(), r.getEmployee(), r.getDateSubmitted(), r.getRequestStatus(), r.getRequestType(), r.getLocationName(), r.getNotes());
        getRequestIndex(req.getId());
        requests.set(getRequestIndex(req.getId()), req);
    }

    public int getRequestIndex(int id) {
        for (Request r : requests) {
            if (r.getId() == id) {
                return requests.indexOf(r);
            }
        }
        return -1;
    }

    //Insert into Database Methods

    /**
     * Inserts into the database the given request
     *
     * @param values the values to insert into the corresponding columns
     */
    //TODO: figure out a way to NEVER DO THIS (pls dont make me do this) (again) (again) (again)

    public static int insertDBRowNewRequest(String[] values) {
        String[] col = {"employee", "floor", "roomNumber", "dateSubmitted", "requestStatus", "requestType", "locationname"};
        int id = DB.insertRowRequests("requests", col, values);
        return id;
    }

    // Access from Database Methods

    // Methods to get information about the request from the database

    /**
     * Searches through the database for the row(s) that matches the given column
     * and value
     *
     * @param col   the column to search for
     * @param value the value to search for
     * @return the result set of the row(s) that matches the given column and value
     */
    private static ResultSet getDBRowFromCol(String col, String value) {
        return DB.getRowCond("requests", "*", col + " = " + value);
    }

    /**
     * Gets the row from the database that matches the given employee
     *
     * @param employee the employee to search for
     * @return the result set of the row that matches the given employee
     */
    public static ResultSet getDBRowEmployee(String employee) {
        return getDBRowFromCol("employee", "'" + employee + "'");
    }

    public static ResultSet getDBRowID(int id) {
        return getDBRowFromCol("id", Integer.toString(id));
    }

    /**
     * Gets the row(s) from the database that matches the given floor
     *
     * @param floor the floor to search for
     * @return the result set of the row that matches the given floor
     */
    public ResultSet getDBRowFloor(int floor) {
        return getDBRowFromCol("floor", Integer.toString(floor));
    }

    /**
     * Gets the row(s) from the database that matches the given room number
     *
     * @param roomnumber the room number to search for
     * @return the result set of the row that matches the given room number
     */
    public ResultSet getDBRowPosition(String roomnumber) {
        return getDBRowFromCol("roomnumber", roomnumber);
    }

    /**
     * Gets all the rows from the database
     *
     * @return the result set of all rows
     */
    public ResultSet getDBRowAllRequests() {
        return DB.getRowCond("Requests", "*", "TRUE");
    }

    // Method to Update the Database

    /**
     * Updates the database with the information in this Request object
     *
     * @param col   the columns to update
     * @param value the values to update
     */
    private void updateRows(String[] col, String[] value, String condition) {
        if (col.length != value.length) {
            throw new IllegalArgumentException("The column and value arrays must be the same length");
        }
        DB.updateRow("Requests", col, value, condition);
    }

    /**
     * Update the employee  in the database
     *
     * @param oldEmployee the old employee
     * @param newEmployee the new employee
     */
    public void updateEmployee(String oldEmployee, String newEmployee, Request r) {
        String[] col = {"employee"};
        String[] value = {"'" + newEmployee + "'"};
        updateRows(col, value, "employee = '" + oldEmployee + "'");
        r.setEmployee(newEmployee);
    }


    //Get rid of oldrequeststatus param and just use the requeststatus field?

    /**
     * Update the request status  in the database
     *
     * @param oldRequestStatus the old request status
     * @param newRequestStatus the new request status
     */
    public void updateRequestStatus(String oldRequestStatus, String newRequestStatus, Request r) {
        String[] col = {"requeststatus"};
        String[] value = {"'" + newRequestStatus + "'"};
        updateRows(col, value, "requeststatus = '" + oldRequestStatus + "'");
        r.setRequestStatus(newRequestStatus);
    }

    /**
     * Update the request type  in the database
     *
     * @param oldRequestType the old request type
     * @param newRequestType the new request type
     */
    public void updateRequestType(String oldRequestType, String newRequestType, Request r) {
        String[] col = {"requesttype"};
        String[] value = {"'" + newRequestType + "'"};
        updateRows(col, value, "requesttype = '" + oldRequestType + "'");
        r.setRequestType(newRequestType);
    }

//  obsolete function below
//  public void updateDBRequest() {
//    updateEmployee(this.employee);
//    updatePassword(this.password);
//    updatePermissionLevel(this.permissionLevel);
//    updatePosition(this.position);
//  }

    // Method to Delete the Database

    /**
     * Deletes the row in the database that matches the employee of this Request object
     *
     * @param confirm 0 to confirm, anything else to cancel
     */
    public void deleteDBRequest(int confirm, Request r) {
        if (confirm == 0)
            DB.deleteRow("Requests", "employee = '" + r.getEmployee() + "'");
        else {
            System.out.println("Delete cancelled");
        }
    }

    // list information about this Request object


}
