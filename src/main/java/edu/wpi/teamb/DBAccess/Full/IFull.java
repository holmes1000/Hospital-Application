package edu.wpi.teamb.DBAccess.Full;

import edu.wpi.teamb.DBAccess.ORMs.OfficeRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface IFull {
    ArrayList<?> listFullRequests(List<?> list);

    void printRequestType();

}
