package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import com.sun.javafx.application.LauncherImpl;

public class Main {

  public static void main(String[] args) {
    Repository.getRepository();
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
