package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.entities.requests.MealRequestE;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import org.controlsfx.control.PopOver;

public class MealRequestController {

  @FXML private MFXButton btnSubmit;
  @FXML private MFXButton btnCancel;
  @FXML private MFXButton btnReset;
  @FXML private MFXTextField roomTextBox;
  @FXML private ImageView helpIcon;
  @FXML private MFXComboBox<String> cbAvailableMeals;
  @FXML private MFXComboBox<String> cbAvailableDrinks;
  @FXML private MFXComboBox<String> cbAvailableSnacks;
  @FXML private MFXTextField txtFldNotes;
  @FXML private MFXComboBox<String> cbOrderLocation;
   @FXML private MFXComboBox<String> cbEmployeesToAssign;
  @FXML private MFXComboBox<String> cbFloorSelect; // Floor
    @FXML private MFXFilterComboBox<String> cbLongName;

    private MealRequestE mealRequestE;

    public MealRequestController(){
        this.mealRequestE = new MealRequestE();
    }
  @FXML
  public void initialize() throws IOException, SQLException{
      initializeFields();
      clickHelp();
  }

  @FXML
  public void clickSubmit() {
    btnSubmit.setOnMouseClicked(event -> {

        //Get all fields from request
        String orderfrom = cbOrderLocation.getSelectedItem();
        String food = cbAvailableMeals.getSelectedItem();
        String drink = cbAvailableDrinks.getSelectedItem();
        String snack = cbAvailableSnacks.getSelectedItem();
        String mealmodification = txtFldNotes.getText();
        String employee = cbEmployeesToAssign.getSelectedItem();
        String floor = cbFloorSelect.getSelectedItem();
        String longName = cbLongName.getSelectedItem();
        String roomnumber = roomTextBox.getText();
        String requeststatus =("Pending");
        String mealtype = ("Meal");

        //Check for required fields before allowing submittion
        if(((food != null) || (drink != null) || (snack != null)) && (employee!= null) && (longName != null)){

            //Set the gathered fields into a string array
            String[] output = {employee, floor, roomnumber, requeststatus, mealtype, orderfrom, food, drink, snack, mealmodification, longName};
            mealRequestE.submitRequest(output);
            clickResetForm();
            Navigation.navigate(Screen.CREATE_NEW_REQUEST);
        } else {

            //If the required fields are not filled, bring up pop-over indicating such
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/NotAllFieldsCompleteError.fxml"));
            PopOver popOver = new PopOver();
            popOver.setDetachable(true);
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
            popOver.setArrowSize(0.0);
            try {
                popOver.setContentNode(popupLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            popOver.show(btnSubmit);
        }

    });
  }

  @FXML
  public void clickCancel() {
    btnCancel.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }

  //Resets the form by resetting all fields
  @FXML
  public void clickResetForm() {
    btnReset.setOnMouseClicked(
        event -> {

            //Reset the combo-boxes
          cbOrderLocation.clear();
          cbOrderLocation.replaceSelection("Order Location");
          cbFloorSelect.clear();
          cbFloorSelect.replaceSelection("Floor");
          cbEmployeesToAssign.clear();
          cbEmployeesToAssign.replaceSelection("Employees Available");
          cbAvailableMeals.clear();
          cbAvailableMeals.replaceSelection("Available Meals:");
          cbAvailableDrinks.clear();
          cbAvailableDrinks.replaceSelection("Available Drinks:");
          cbAvailableSnacks.clear();
          cbAvailableSnacks.replaceSelection("Available Snacks:");
          cbLongName.clear();
          cbLongName.replaceSelection("All Room Names: ");

          //Reset text fields
          roomTextBox.clear();
          roomTextBox.replaceSelection("Room:");
          txtFldNotes.clear();
        });
  }

  //When clicking on help, brings up a pop-over with help information
  @FXML
  public void clickHelp() {
    helpIcon.setOnMouseClicked(
        event -> {

            //Load the FXML component
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/MealRequestHelpPopOver.fxml"));

            //Create a new popover with no arrow and orient it to appear above the help button
            PopOver popOver = new PopOver();
            popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
            popOver.setArrowSize(0.0);

            //Load FXML into the popOver
            try {
                popOver.setContentNode(popupLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            popOver.show(helpIcon);
        });
  }

  private void initializeFields() throws SQLException {
    // DROPDOWN INITIALIZATION
      ObservableList<String> longNames = FXCollections.observableArrayList();
      longNames.addAll(Repository.getRepository().getAllLongNames());
      cbLongName.setItems(longNames);

    ObservableList<String> locations =
        FXCollections.observableArrayList("Tower", "Connors Center", "Shapiro Center");
    cbOrderLocation.setItems(locations);

    // DROPDOWN INITIALIZATION
    ObservableList<String> employees =
            FXCollections.observableArrayList();
    employees.addAll(mealRequestE.getUsernames());
    cbEmployeesToAssign.setItems(employees);

    // DROPDOWN INITIALIZATION
    ObservableList<String> floors =
        FXCollections.observableArrayList("Lower Floor 1", "Lower Floor 2", "Floor 1", "Floor 2", "Floor 3");
    cbFloorSelect.setItems(floors);

    // DROPDOWN INITIALIZATION
    ObservableList<String> meals = FXCollections.observableArrayList("Pizza", "Pasta", "Soup");
    cbAvailableMeals.setItems(meals);

    // DROPDOWN INITIALIZATION
    ObservableList<String> drinks =
        FXCollections.observableArrayList("Water", "Coca-Cola", "Ginger-Ale");
    cbAvailableDrinks.setItems(drinks);

    // DROPDOWN INITIALIZATION
    ObservableList<String> snacks = FXCollections.observableArrayList("Chips", "Apple");
    cbAvailableSnacks.setItems(snacks);

  }

  @FXML
  public void clickExit() {
    System.exit(0);
  }
}
