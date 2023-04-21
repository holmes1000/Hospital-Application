package edu.wpi.teamb.controllers.components;
import edu.wpi.teamb.DBAccess.FullMealRequest;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.entities.components.EMealRequestInfoCardSubComponent;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class MealRequestInfoCardSubComponentController implements IInfoCardSubComponentController {
    @FXML public Label orderFromLabel;
    @FXML public Label foodLabel;
    @FXML public Label snackLabel;
    @FXML public Label drinkLabel;
    @FXML public Label mealModificationLabel;
    @FXML public AnchorPane mealRequestInfoSubComponentAnchorPane;

    //editable combo boxes
    @FXML private MFXComboBox<String> orderFromComboBox;
    private MFXComboBox<String> foodComboBox;
    private MFXComboBox<String> snackComboBox;
    private MFXComboBox<String> drinkComboBox;
    private MFXTextField mealModificationTextField;

    //field entity object
    private EMealRequestInfoCardSubComponent EMealRequestInfoCardSubComponent;

    private boolean editable;
    @FXML public void initialize() throws IOException {
        EMealRequestInfoCardSubComponent = new EMealRequestInfoCardSubComponent();
        editable = false;
    }

    /**
     * toggles all applicable fields to edit mode
     */
    @Override
    public void toggleEditMode() {
        toggleEditable();
        editOrderFrom();
        editFood();
        editSnack();
        editDrink();
        editMealModification();
    }

    @Override
    public void sendRequest(Object request) {
        if (request instanceof FullMealRequest fullMealRequest) {
            setOrderFromLabel(fullMealRequest.getOrderFrom());
            setFoodLabel(fullMealRequest.getFood());
            setSnackLabel(fullMealRequest.getSnack());
            setDrinkLabel(fullMealRequest.getDrink());
            setMealModificationLabel(fullMealRequest.getNotes());
        }
    }

    private void editOrderFrom() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = orderFromLabel.getLayoutX();
            double labelYCoord = orderFromLabel.getLayoutY();

            //create a combo box if null
            if(orderFromComboBox == null) {
                orderFromComboBox = new MFXComboBox<String>();
            }
            //set the location of the combo box
            int comboBoxHeightOffset = -10;
            orderFromComboBox.setLayoutX(labelXCoord);
            orderFromComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

            //get the dimensions of the label
            double labelWidth = orderFromLabel.getWidth();
            double labelHeight = orderFromLabel.getHeight();

            //assign the dimensions to the combo box
            orderFromComboBox.setPrefWidth(labelWidth);
//            orderFromComboBox.setPrefHeight();

            //replacement of the label with the combo box
            int index = mealRequestInfoSubComponentAnchorPane.getChildren().indexOf(orderFromLabel);
            mealRequestInfoSubComponentAnchorPane.getChildren().add(index, orderFromComboBox);

            //get the list of location
            ArrayList<LocationName> locationNames = EMealRequestInfoCardSubComponent.getLocationNames();
            ArrayList<String> locationNamesAsString = new ArrayList<>();
            for (LocationName location: locationNames) {
                locationNamesAsString.add(location.getLongName());
            }
            //add it to the comboBox
            orderFromComboBox.getItems().addAll(locationNamesAsString);

            //set the combo box text to the current order from text
            orderFromComboBox.setText(orderFromLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            orderFromComboBox.toFront();

            orderFromComboBox.setOnMouseClicked(event -> {
                System.out.println("ComboBox clicked!");
            });
        } else {
            mealRequestInfoSubComponentAnchorPane.getChildren().remove(orderFromComboBox);
            orderFromComboBox = null;
        }
    }

    /**
     * switches the food label to be a combobox
     */
    private void editFood() {
        if(editable) {
            //get the coordinates of the food from label
            double labelXCoord = foodLabel.getLayoutX();
            double labelYCoord = foodLabel.getLayoutY();

            //create a combo box if null
            if(foodComboBox == null) {
                foodComboBox = new MFXComboBox<String>();
            }
            //set the location of the combo box
            int comboBoxHeightOffset = -10;
            foodComboBox.setLayoutX(labelXCoord);
            foodComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

            //get the dimensions of the label
            double labelWidth = foodLabel.getWidth();
            double labelHeight = foodLabel.getHeight();

            //assign the dimensions to the combo box
            foodComboBox.setPrefWidth(labelWidth);
            foodComboBox.setPrefHeight(labelHeight);

            //replacement of the label with the combo box
            int index = mealRequestInfoSubComponentAnchorPane.getChildren().indexOf(foodLabel);
            mealRequestInfoSubComponentAnchorPane.getChildren().add(index, foodComboBox);

            //get the list of foods
            ArrayList<String> foodsList = EMealRequestInfoCardSubComponent.getFoodList();
            //add it to the comboBox
            foodComboBox.getItems().addAll(foodsList);

            //set the combo box text to the current order from text
            foodComboBox.setText(foodLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            foodComboBox.toFront();
        } else {
            mealRequestInfoSubComponentAnchorPane.getChildren().remove(foodComboBox);
            foodComboBox = null;
        }
    }

    /**
     * switches the snack label to be a combobox
     */
    private void editSnack() {
        if(editable) {
            //get the coordinates of the food from label
            double labelXCoord = snackLabel.getLayoutX();
            double labelYCoord = snackLabel.getLayoutY();

            //create a combo box if null
            if(snackComboBox == null) {
                snackComboBox = new MFXComboBox<String>();
            }
            //set the location of the combo box
            int comboBoxHeightOffset = -10;
            snackComboBox.setLayoutX(labelXCoord);
            snackComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

            //get the dimensions of the label
            double labelWidth = snackLabel.getWidth();
            double labelHeight = snackLabel.getHeight();

            //assign the dimensions to the combo box
            snackComboBox.setPrefWidth(labelWidth);
            snackComboBox.setPrefHeight(labelHeight);

            //replacement of the label with the combo box
            int index = mealRequestInfoSubComponentAnchorPane.getChildren().indexOf(snackLabel);
            mealRequestInfoSubComponentAnchorPane.getChildren().add(index, snackComboBox);

            //get the list of snacks
            ArrayList<String> snacksList = EMealRequestInfoCardSubComponent.getSnackList();
            //add it to the comboBox
            snackComboBox.getItems().addAll(snacksList);

            //set the combo box text to the current snack label text
            snackComboBox.selectItem(snackLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            snackComboBox.toFront();
        } else {
            mealRequestInfoSubComponentAnchorPane.getChildren().remove(snackComboBox);
            snackComboBox = null;
        }
    }

    /**
     * switches the drink label to be a combobox
     */
    private void editDrink() {
        if(editable) {
            //get the coordinates of the food from label
            double labelXCoord = drinkLabel.getLayoutX();
            double labelYCoord = drinkLabel.getLayoutY();

            //create a combo box if null
            if(drinkComboBox == null) {
                drinkComboBox = new MFXComboBox<String>();
            }
            //set the location of the combo box
            int comboBoxHeightOffset = -10;
            drinkComboBox.setLayoutX(labelXCoord);
            drinkComboBox.setLayoutY(labelYCoord + comboBoxHeightOffset);

            //get the dimensions of the label
            double labelWidth = drinkLabel.getWidth();
            double labelHeight = drinkLabel.getHeight();

            //assign the dimensions to the combo box
            drinkComboBox.setPrefWidth(labelWidth);
            drinkComboBox.setPrefHeight(labelHeight);

            //replacement of the label with the combo box
            int index = mealRequestInfoSubComponentAnchorPane.getChildren().indexOf(drinkLabel);
            mealRequestInfoSubComponentAnchorPane.getChildren().add(index, drinkComboBox);

            //get the list of drinks
            ArrayList<String> drinksList = EMealRequestInfoCardSubComponent.getDrinksList();
            //add it to the comboBox
            drinkComboBox.getItems().addAll(drinksList);

            //set the combo box text to the current snack label text
            drinkComboBox.selectItem(drinkLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            drinkComboBox.toFront();
        } else {
            mealRequestInfoSubComponentAnchorPane.getChildren().remove(drinkComboBox);
            drinkComboBox = null;
        }
    }

    /**
     * switches the meal modification label to be a text field
     */
    private void editMealModification() {
        if(editable) {
            //get the coordinates of the food from label
            double labelXCoord = mealModificationLabel.getLayoutX();
            double labelYCoord = mealModificationLabel.getLayoutY();

            //create a combo box if null
            if(mealModificationTextField == null) {
                mealModificationTextField = new MFXTextField();
            }
            //set the location of the text field
            int textFieldHeightOffset = 5;
            mealModificationTextField.setLayoutX(labelXCoord);
            mealModificationTextField.setLayoutY(labelYCoord + textFieldHeightOffset);

            //get the dimensions of the label
            double labelWidth = mealModificationLabel.getWidth();
            double labelHeight = mealModificationLabel.getHeight();

            //assign the dimensions to the text field
            mealModificationTextField.setPrefWidth(500);
            mealModificationTextField.setPrefHeight(labelHeight);

            //replacement of the label with the text field
            int index = mealRequestInfoSubComponentAnchorPane.getChildren().indexOf(mealModificationLabel);
            mealRequestInfoSubComponentAnchorPane.getChildren().add(index, mealModificationTextField);

            //set the text field to the current snack label text
            mealModificationTextField.setText(mealModificationLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            mealModificationTextField.toFront();
        } else {
            mealRequestInfoSubComponentAnchorPane.getChildren().remove(mealModificationTextField);
            mealModificationTextField = null;
        }
    }

    /**
     * sets the text of the order from text field
     */
    public void setOrderFromLabel(String orderFrom) {
        orderFromLabel.setText(orderFrom);
    }

    /**
     * sets the text of the food text field
     */
    public void setFoodLabel(String food) {
        foodLabel.setText(food);
    }

    /**
     * sets the text of the snack text field
     */
    public void setSnackLabel(String snack) {
        snackLabel.setText(snack);
    }

    /**
     * sets the text of the drink text field
     */
    public void setDrinkLabel(String drink) {
        drinkLabel.setText(drink);
    }

    /**
     * sets the text of the meal modification text field
     */
    public void setMealModificationLabel(String mealModification) {
        mealModificationLabel.setText(mealModification);
    }

    /**
     * toggles the editable state
     */
    private void toggleEditable() {
        editable = !editable;
    }
}
