package edu.wpi.teamb.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.entities.Pathfinder;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;

import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.EventHandler;
import java.io.IOException;

public class NavDrawerController {
  @FXML private JFXButton btnConferenceRequest;
  @FXML private JFXButton btnHelp;
  @FXML private JFXButton btnHome;
  @FXML private JFXButton btnLogout;
  @FXML private JFXButton btnMapEditor;
  @FXML private JFXButton btnPathfinder;
  @FXML private JFXButton btnRequests;
  @FXML private JFXButton btnSettings;
  @FXML private JFXButton btnSignage;
  @FXML private JFXButton btnExit;

  @FXML
  void clickHelp() {
    System.out.println("Not yet implemented");
  }

  @FXML
  void clickHome() {
    btnHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }

  @FXML
  void clickLogout() {
    btnLogout.setOnMouseClicked(event -> Navigation.navigate(Screen.LOGIN));
  }

  @FXML
  void clickMapEditor() {
    btnMapEditor.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP_EDITOR));
  }

  @FXML
  void clickPathfinder() {
    btnPathfinder.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDER));
  }

  @FXML
  void clickRequests() {
    btnRequests.setOnMouseClicked(event -> Navigation.navigate(Screen.CREATE_NEW_REQUEST));
  }

  @FXML
  void clickSettings() {
    btnSettings.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGS));
  }

  @FXML
  void clickSignage() {
    btnSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
  }

  @FXML
  void clickExit() { btnExit.setOnMouseClicked(event -> {
    final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/ExitPopOver.fxml"));
    PopOver popOver = new PopOver();
    try {
      popOver.setContentNode(popupLoader.load());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    popOver.show(btnExit);
    //opOver.setFadeOutDuration(new Duration(5000));
    //popOver.hide(new Duration(5000));
    //System.exit(0);
  });

  }

}
