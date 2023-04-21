package edu.wpi.teamb.DBAccess.Full;

import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public interface IFull {
    ArrayList<?> listFullRequests(List<?> list);

    void printRequestType();

    void handleEditRequestMenu();

    Image setRequestTypeIconImageView();

    int getId();

    void setId(int id);

    String getEmployee();

    void setEmployee(String employee);

    Timestamp getDateSubmitted();

    void setDateSubmitted(Timestamp dateSubmitted);

    String getRequestStatus();

    void setRequestStatus(String requestStatus);

    String getLocationName();
    void setLocationName(String locationName);

    String getNotes();

    void setNotes(String notes);


    void setRequestType();

    String getRequestType();
}
