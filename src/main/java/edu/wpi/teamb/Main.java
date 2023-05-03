package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import com.sun.javafx.application.LauncherImpl;
import edu.wpi.teamb.DBAccess.DBconnection;
import org.postgresql.util.PSQLException;

public class Main {

  public static void main(String[] args) {

    Repository.getRepository();
    //Repository.getRepository().exportAllToCSVFiles(3);

    LauncherImpl.launchApplication(Bapp.class, CustomPreloader.class, args);
  }
}
