package edu.wpi.teamb.entities.requests;

import java.sql.Date;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.DAO.Repository;

public class EMoveRequest extends RequestImpl {

    private Move move;
    //private Request request;

    public EMoveRequest(int ID, String longName, Date date, String employee, String floor, String roomNumber, Date dateSubmitted, String requestStatus) {
        this.move = new Move(ID, longName, date);
        //TODO firgour what request ID is
        //this.request = new Request(ID, employee, floor, roomNumber, dateSubmitted, requestStatus, "Move", longName);
    }

    public EMoveRequest(Move move) {
        this.move = move;
    }

    public EMoveRequest() {
        
    }


    @Override
    public int getRequestID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestID'");
    }

    @Override
    public String getFloor() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFloor'");
    }

    @Override
    public String getRoomNumber() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoomNumber'");
    }

    @Override
    public Date getDateSubmitted() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDateSubmitted'");
    }

    @Override
    public RequestType getRequestType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRequestType'");
    }

    @Override
    public void submitRequest(String[] inputs) {
        move = new Move(Integer.parseInt(inputs[0]), inputs[1], java.sql.Date.valueOf(inputs[2]));
       // request = new Request(Integer.parseInt(inputs[0]), inputs[3], inputs[4], inputs[5], Date.valueOf(inputs[6]), inputs[7], "Move", inputs[1]);
        Repository.getRepository().addMove(move);
    }

    public void removeRequest() {
        Repository.getRepository().deleteMove(move);
    }

    public void updateRequest(String[] inputs) {
        move = new Move(Integer.parseInt(inputs[0]), inputs[1], java.sql.Date.valueOf(inputs[2]));
        //request = new Request(Integer.parseInt(inputs[0]), inputs[3], inputs[4], inputs[5], Date.valueOf(inputs[6]), inputs[7], "Move", inputs[1]);
        Repository.getRepository().updateMove(move);
    }

    public void updateRequest() {
        Repository.getRepository().updateMove(move);
    }

    /**
     * cheack teh equality of the objects
     * @param obj the object to be compared
     * @return true if equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        //check equality of the object
        if (this == obj) {
            return true;
        }
        //check if the object is null
        if (obj == null) {
            return false;
        }
        //check if the object is of the same class
        if (getClass() != obj.getClass()) {
            return false;
        }
        //cast the object to the class
        EMoveRequest other = (EMoveRequest) obj;
        //check if the attributes are equal
        if (move == null) {
            if (other.move != null) {
                return false;
            }
        } else if (!move.equals(other.move)) {
            return false;
        }
        // if (request == null) {
        //     if (other.request != null) {
        //         return false;
        //     }
        // } else if (!request.equals(other.request)) {
        //     return false;
        // }
        return true;
    }

    /**
     * generate the hash of the object
     * @return the hash of the object
     */
    @Override
    public int hashCode() {
        //ganertae Hash of object
        Objects.hash(move/* , request*/);
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hashCode'");
    }

    /**
     * Updates the location of the node in the database
     * NOTE: This method is not implemented yet
     */
    public void updateNodeLocation()
    {
        //TODO: implement
    }

}
