package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.ORMs.Edge;
import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IDAO<T, G> {
    public <T> T get(Object id);

    public ArrayList<T> getAll();

    public void setAll();

    public void add(T object);
    public void delete(T object);
    public void update(T object);
}
