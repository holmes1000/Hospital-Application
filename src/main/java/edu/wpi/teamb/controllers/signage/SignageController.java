package edu.wpi.teamb.controllers.signage;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.ORMs.Sign;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.controllers.components.SignageComponent1Controller;
import edu.wpi.teamb.controllers.components.SignageComponentIndividualDirectionController;
import edu.wpi.teamb.entities.ESignage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SignageController {

  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private MFXFilterComboBox<String> cbLocation;
  @FXML private VBox signVbox;
  private ESignage signageE;

  @FXML
  public void initialize() throws IOException {
      signageE = new ESignage();
      initNavBar();
      initializeFields();
      initalizeComboBox();
      signVbox.getChildren().clear();
      loadPageBasedOnGroup("Shapiro 2 Screen 1 (info desk) May 23");
  }

    private void initalizeComboBox() {
        HashSet<String> signageGroups = signageE.getSignageGroups();
        //convert the hashset to an arrayList
        ArrayList<String> signageGroupsList = new ArrayList<String>();
        for (String element : signageGroups) {
            signageGroupsList.add(element);
        }
        ObservableList<String> signageGroupsObservableList = FXCollections.observableArrayList(signageGroupsList);
        Collections.sort(signageGroupsList);
        cbLocation.setItems(signageGroupsObservableList);
    }

    public void clickCbLocation() {
      signVbox.getChildren().clear();
      displaySelection();
  }

    public void displaySelection() {
        String item = cbLocation.getSelectedItem();
        loadPageBasedOnGroup(item);
    }

  public void loadPageBasedOnGroup(String group) {
      ArrayList<Sign> allSigns = signageE.getAllSigns();
      ArrayList<Sign> groupSigns = new ArrayList<>();
      for (Sign sign : allSigns) {
          if (sign.getSignageGroup().equals(group)) {
              groupSigns.add(sign);
          }
      }
      int index = 0;
      for (Sign s : groupSigns) {
          try {
              FXMLLoader loader =
                      new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/SignageComponentIndividualDirection.fxml"));
              AnchorPane pane = loader.load();
              //get the controller that was loaded by the loader
              SignageComponentIndividualDirectionController controller = loader.getController();
              //set the location of the sign
              controller.setSignageLocationText(s.getLocationName());
              //set the direction of the sign
              if(s.getDirection().equals("stop here") && index== 0){
                  index++;
                  Label label = new Label();
                  label.setText("Stop Here");
                  label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                  signVbox.getChildren().add(0, label);
              }
              controller.setSignageDirectionIcons(s.getDirection());
              signVbox.getChildren().addAll(pane);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      signVbox.setSpacing(5);
  }

//    public void loadPage2() {
//        try {
//            FXMLLoader loader =
//                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/SignageComponent2.fxml"));
//            Pane pane = loader.load();
//            signVbox.getChildren().addAll(pane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

  public void initializeFields() {
      ObservableList<String> locations = FXCollections.observableArrayList(signageE.getSignageGroups());
      cbLocation.setItems(locations);
  }

    public void initNavBar() {
        // https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/NavDrawer.fxml"));
            VBox vbox = loader.load();
            NavDrawerController navDrawerController = loader.getController();
            menuDrawer.setSidePane(vbox);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerBackArrowBasicTransition burgerOpen =
                new HamburgerBackArrowBasicTransition(menuBurger);
        burgerOpen.setRate(-1);
        menuBurger.addEventHandler(
                javafx.scene.input.MouseEvent.MOUSE_PRESSED,
                (e) -> {
                    burgerOpen.setRate(burgerOpen.getRate() * -1);
                    burgerOpen.play();
                    if (menuDrawer.isOpened()) {
                        menuDrawer.close();
                    } else {
                        menuDrawer.open();
                    }
                });
    }
}
