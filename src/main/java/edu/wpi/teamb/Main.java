package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import com.sun.javafx.application.LauncherImpl;
import edu.wpi.teamb.DBAccess.DBconnection;
import org.postgresql.util.PSQLException;

public class Main {

  public static void main(String[] args) {
//    Repository.getRepository().importAlertsFromCSV("C:\\Users\\Ronit\\Desktop\\alerts.csv", 0);
    Repository.getRepository();
//    try {
//      DBconnection.getDBconnection().getConnection();
//    } catch (Exception e) {
//      System.out.println("Error: Couldn't initialize WPI-server database.");
//      System.out.println("Switching to remote AWS server...");
//      DBconnection.getDBconnection().switchTo(1);
//      Repository.getRepository();
//    }
//    Repository.getRepository().switchTo(1);
//    Repository.getRepository().exportNodesToCSV("Nodes", 3);
//    Repository.getRepository().exportEdgesToCSV("Edges", 3);
//    Repository.getRepository().exportLocationNamesToCSV("LocationNames", 3);
//    Repository.getRepository().exportMovesToCSV("Moves", 3);
//    Repository.getRepository().exportRequestsToCSV("Requests", 3);
//    Repository.getRepository().exportConferenceRequestsToCSV("ConferenceRequests", 3);
//    Repository.getRepository().exportFlowerRequestsToCSV("FlowerRequests", 3);
//    Repository.getRepository().exportFurnitureRequestsToCSV("FurnitureRequests", 3);
//    Repository.getRepository().exportMealRequestsToCSV("MealRequests", 3);
//    Repository.getRepository().exportOfficeRequestsToCSV("OfficeRequests", 3);

    LauncherImpl.launchApplication(Bapp.class, CustomPreloader.class, args);
  }

  // shortcut: psvm
}
