package edu.wpi.teamb.entities.requests;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EFurnitureRequest extends RequestImpl {
    private String furnitureType;
    private String model;
    private String assembly;

public EFurnitureRequest(String furnitureType,
                             String model,
                             String assembly) {
        this.furnitureType = furnitureType;
        this.model = model;
        this.assembly = assembly;
    }


    public EFurnitureRequest() {
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.FurnitureDelivery;
    }

    @Override
    public void submitRequest(String[] inputs) {
        Repository.getRepository().addFurnitureRequest(inputs);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String getFurnitureType() {
        return furnitureType;
    }

    public void setFurnitureType(String furnitureType) {
        this.furnitureType = furnitureType;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAssembly() {
        return assembly;
    }

    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    public boolean checkSpecialRequestFields() {
        if (this.furnitureType == null || this.model == null || this.assembly == null) {
            return false;
        }
        return true;
    }
}