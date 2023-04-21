package edu.wpi.teamb;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullFactory;
import edu.wpi.teamb.DBAccess.Full.IFull;
import edu.wpi.teamb.ProgressBar.RingProgressIndicator;
import edu.wpi.teamb.controllers.NavDrawerController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import com.sun.javafx.application.LauncherImpl;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    Repository.getRepository();
    LauncherImpl.launchApplication(Bapp.class, CustomPreloader.class, args);
  }

  // shortcut: psvm
}
