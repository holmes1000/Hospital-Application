package edu.wpi.teamb.DBAccess.DAO;

import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.ORMs.Signage;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SignageDAOImpl implements IDAO {

    ArrayList<Signage> signs;

    public SignageDAOImpl() {
        signs = getAllHelper();
    }
    @Override
    public Signage get(Object id) {
        Date date = (Date) id;
        ResultSet rs = DButils.getRowCond("Signage", "*", " date = '" + date + "'");
        try {
            if (rs != null) {
                if (rs.isBeforeFirst()) {
                    rs.next();
                    return new Signage(rs);
                } else throw new Exception("No rows found"); }
        } catch (Exception e) {
            // handle error
            System.err.println("ERROR Query Failed in method 'SignageDAOImpl.get': " + e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public ArrayList getAll() {
        return signs;
    }

    public ArrayList<Signage> getAllHelper() {
        ArrayList<Signage> signs = new ArrayList<Signage>();
        try {
            ResultSet rs = DButils.getCol("Signage", "*");
            while (rs.next()) {
                signs.add(new Signage(rs));
            }
            return signs;
        } catch (Exception e) {
            System.err.println("ERROR Query Failed in method 'SignageDAOImpl.getAllHelper': " + e.getMessage());
        }
        return signs;
    }

    @Override
    public void setAll() {
        signs = getAllHelper();
    }

    @Override
    public void add(Object object) {
        Signage s = (Signage) object;
        String[] cols = {"direction", "screen", "date"};
        String[] vals = {s.getDirection(), Integer.toString(s.getScreen()), String.valueOf(s.getDate())};
        DButils.insertRow("Signage", cols, vals);
        signs.add(s);
    }

    @Override
    public void delete(Object object) {
        Signage s = (Signage) object;
        DButils.deleteRow("Signage", "date = '" + s.getDate() + "'");
        signs.remove(s);
    }

    @Override
    public void update(Object object) {
        Signage s = (Signage) object;
        String[] cols = {"direction", "screen", "date"};
        String[] vals = {s.getDirection(), Integer.toString(s.getScreen()), String.valueOf(s.getDate())};
        DButils.updateRow("Signage", cols, vals, "date = '" + s.getDate() + "'");
        setAll();
    }
}
