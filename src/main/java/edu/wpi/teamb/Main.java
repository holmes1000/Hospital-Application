package edu.wpi.teamb;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import com.sun.javafx.application.LauncherImpl;

public class Main {

  public static void main(String[] args) {
    Repository.getRepository();
//    Repository.getRepository().exportNodesToCSV("Nodes_4_21", 3);
//    Repository.getRepository().exportEdgesToCSV("Edges_4_21", 3);
//    Repository.getRepository().exportLocationNamesToCSV("LocationNames_4_21", 3);
//    Repository.getRepository().exportMovesToCSV("Moves_4_21", 3);
//    Repository.getRepository().exportRequestsToCSV("Requests_4_21", 3);
//    Repository.getRepository().exportConferenceRequestsToCSV("ConferenceRequests_4_21", 3);
//    Repository.getRepository().exportFlowerRequestsToCSV("FlowerRequests_4_21", 3);
//    Repository.getRepository().exportFurnitureRequestsToCSV("FurnitureRequests_4_21", 3);
//    Repository.getRepository().exportMealRequestsToCSV("MealRequests_4_21", 3);
//    Repository.getRepository().exportOfficeRequestsToCSV("OfficeRequests_4_21", 3);
    LauncherImpl.launchApplication(Bapp.class, CustomPreloader.class, args);
  }

  // shortcut: psvm
}
