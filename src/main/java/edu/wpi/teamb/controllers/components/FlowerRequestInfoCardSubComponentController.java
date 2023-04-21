package edu.wpi.teamb.controllers.components;

import edu.wpi.teamb.DBAccess.Full.FullFlowerRequest;
import edu.wpi.teamb.entities.components.EFlowerRequestInfoCardSubComponent;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;

public class FlowerRequestInfoCardSubComponentController implements IInfoCardSubComponentController{
    @FXML public AnchorPane flowerRequestInfoSubComponentAnchorPane;
    @FXML public Label flowerTypeLabel;
    @FXML public Label colorLabel;
    @FXML public Label flowerBuschellTypeLabel;
    @FXML public Label MessageTypeLabel;
    @FXML public Label specialInstructionsLabel;
    //edit field
    private boolean editable;
    private EFlowerRequestInfoCardSubComponent EFlowerRequestInfoCardSubComponent;
    //edit components
    private MFXComboBox<String> flowerTypeComboBox;
    private MFXComboBox<String> colorComboBox;
    private MFXComboBox<String> flowerBuschellTypeComboBox;
    private MFXTextField MessageTypeTextField;
    private MFXTextField specialInstructionsTextField;



    @FXML public void initialize() throws IOException {
        EFlowerRequestInfoCardSubComponent = new EFlowerRequestInfoCardSubComponent();
        editable = false;
    }

    @Override
    public void toggleEditMode() {
        toggleEditable();
        editColor();
        editFlowerType();
        editFlowerBuschellType();
        editMessageType();
        editSpecialInstructions();
    }

    @Override
    public void sendRequest(Object request) {
        if (request instanceof FullFlowerRequest fullFlowerRequest) {
            setFlowerTypeLabel(fullFlowerRequest.getFlowerType());
            setColorLabel(fullFlowerRequest.getColor());
            setFlowerBuschellTypeLabel(fullFlowerRequest.getSize());
            setMessageTypeLabel(fullFlowerRequest.getMessage());
            setSpecialInstructionsLabel(fullFlowerRequest.getNotes());
        }
    }

    private void editFlowerType() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = flowerTypeLabel.getLayoutX();
            double labelYCoord = flowerTypeLabel.getLayoutY();

            //create a combo box if null
            if(flowerTypeComboBox == null) {
                flowerTypeComboBox = new MFXComboBox<>();
            }
            //set the location of the combo box
            int heightOffset = -10;
            flowerTypeComboBox.setLayoutX(labelXCoord);
            flowerTypeComboBox.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = flowerTypeLabel.getWidth();
            double labelHeight = flowerTypeLabel.getHeight();

            //assign the dimensions to the combo box
            flowerTypeComboBox.setPrefWidth(100);
//            orderFromComboBox.setPrefHeight();

            //replacement of the label with the combo box
            int index = flowerRequestInfoSubComponentAnchorPane.getChildren().indexOf(flowerTypeLabel);
            flowerRequestInfoSubComponentAnchorPane.getChildren().add(index, flowerTypeComboBox);

            //get the flower types
            ArrayList<String> flowerTypes = EFlowerRequestInfoCardSubComponent.getFlowerTypes();
            flowerTypeComboBox.getItems().addAll(flowerTypes);

            //set the value of the combo box to the current value of the label
            flowerTypeComboBox.setText(flowerTypeLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            flowerTypeComboBox.toFront();
        } else {
            flowerRequestInfoSubComponentAnchorPane.getChildren().remove(flowerTypeComboBox);
            flowerTypeComboBox = null;
        }
    }

    private void editColor() {
        if(editable) {
            //get the coordinates of the order from label
            double labelXCoord = colorLabel.getLayoutX();
            double labelYCoord = colorLabel.getLayoutY();

            //create a combo box if null
            if(colorComboBox == null) {
                colorComboBox = new MFXComboBox<>();
            }
            //set the location of the combo box
            int heightOffset = -10;
            colorComboBox.setLayoutX(labelXCoord);
            colorComboBox.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = colorLabel.getWidth();
            double labelHeight = colorLabel.getHeight();

            //assign the dimensions to the combo box
            colorComboBox.setPrefWidth(100);
//            orderFromComboBox.setPrefHeight();

            //replacement of the label with the combo box
            int index = flowerRequestInfoSubComponentAnchorPane.getChildren().indexOf(colorLabel);
            flowerRequestInfoSubComponentAnchorPane.getChildren().add(index, colorComboBox);

            //get the colors to populate the combobox
            ArrayList<String> colors = EFlowerRequestInfoCardSubComponent.getColors();
            colorComboBox.getItems().addAll(colors);

            //set the combo box text to the current order from text
            colorComboBox.setText(colorLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            colorComboBox.toFront();
        } else {
            flowerRequestInfoSubComponentAnchorPane.getChildren().remove(colorComboBox);
            colorComboBox = null;
        }
    }

    public void editFlowerBuschellType() {
        if (editable) {
            //get the coordinates of the order from label
            double labelXCoord = flowerBuschellTypeLabel.getLayoutX();
            double labelYCoord = flowerBuschellTypeLabel.getLayoutY();

            //create a combo box if null
            if (flowerBuschellTypeComboBox == null) {
                flowerBuschellTypeComboBox = new MFXComboBox<>();
            }
            //set the location of the combo box
            int heightOffset = -10;
            flowerBuschellTypeComboBox.setLayoutX(labelXCoord);
            flowerBuschellTypeComboBox.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = flowerBuschellTypeLabel.getWidth();
            double labelHeight = flowerBuschellTypeLabel.getHeight();

            //assign the dimensions to the combo box
            flowerBuschellTypeComboBox.setPrefWidth(100);
//            flowerBuschellTypeComboBox.setPrefHeight(labelHeight);

            //replacement of the label with the combo box
            int index = flowerRequestInfoSubComponentAnchorPane.getChildren().indexOf(flowerBuschellTypeLabel);
            flowerRequestInfoSubComponentAnchorPane.getChildren().add(index, flowerBuschellTypeComboBox);

            //get the flower buschell types
            ArrayList<String> flowerBuschellTypes = EFlowerRequestInfoCardSubComponent.getBuschellTypes();
            flowerBuschellTypeComboBox.getItems().addAll(flowerBuschellTypes);

            //set the combo box text to the current order from text
            flowerBuschellTypeComboBox.setText(flowerBuschellTypeLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            flowerBuschellTypeComboBox.toFront();
        } else {
            flowerRequestInfoSubComponentAnchorPane.getChildren().remove(flowerBuschellTypeComboBox);
            flowerBuschellTypeComboBox = null;
        }
    }

    public void editMessageType() {
        if (editable) {
            //get the coordinates of the order from label
            double labelXCoord = MessageTypeLabel.getLayoutX();
            double labelYCoord = MessageTypeLabel.getLayoutY();

            //create a combo box if null
            if (MessageTypeTextField == null) {
                MessageTypeTextField = new MFXTextField();
            }
            //set the location of the combo box
            int heightOffset = -10;
            MessageTypeTextField.setLayoutX(labelXCoord);
            MessageTypeTextField.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = MessageTypeLabel.getWidth();
            double labelHeight = MessageTypeLabel.getHeight();

            //assign the dimensions to the combo box
            MessageTypeTextField.setPrefWidth(200);
//                MessageTypeTestField.setPrefHeight();

            //replacement of the label with the combo box
            int index = flowerRequestInfoSubComponentAnchorPane.getChildren().indexOf(MessageTypeLabel);
            flowerRequestInfoSubComponentAnchorPane.getChildren().add(index, MessageTypeTextField);

            //set the combo box text to the current order from text
            MessageTypeTextField.setText(MessageTypeLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            MessageTypeTextField.toFront();
        } else {
            flowerRequestInfoSubComponentAnchorPane.getChildren().remove(MessageTypeTextField);
            MessageTypeTextField = null;
        }
    }

    public void editSpecialInstructions() {
        if (editable) {
            //get the coordinates of the order from label
            double labelXCoord = specialInstructionsLabel.getLayoutX();
            double labelYCoord = specialInstructionsLabel.getLayoutY();

            //create a combo box if null
            if (specialInstructionsTextField == null) {
                specialInstructionsTextField = new MFXTextField();
            }
            //set the location of the combo box
            int heightOffset = -10;
            specialInstructionsTextField.setLayoutX(labelXCoord);
            specialInstructionsTextField.setLayoutY(labelYCoord + heightOffset);

            //get the dimensions of the label
            double labelWidth = specialInstructionsLabel.getWidth();
            double labelHeight = specialInstructionsLabel.getHeight();

            //assign the dimensions to the combo box
            specialInstructionsTextField.setPrefWidth(300);
//            specialInstructionsTextField.setPrefHeight(100);

            //replacement of the label with the combo box
            int index = flowerRequestInfoSubComponentAnchorPane.getChildren().indexOf(specialInstructionsLabel);
            flowerRequestInfoSubComponentAnchorPane.getChildren().add(index, specialInstructionsTextField);

            //set the combo box text to the current order from text
            specialInstructionsTextField.setText(specialInstructionsLabel.getText());

            //Critically important line of code. DO NOT DELETE UNDER ANY CIRCUMSTANCES
            specialInstructionsTextField.toFront();
        } else {
            flowerRequestInfoSubComponentAnchorPane.getChildren().remove(specialInstructionsTextField);
            specialInstructionsTextField = null;
        }
    }

    public void setFlowerTypeLabel(String flowerType) {
        flowerTypeLabel.setText(flowerType);
    }

    public void setColorLabel(String color) {
        colorLabel.setText(color);
    }

    public void setFlowerBuschellTypeLabel(String flowerBuschelType) {
        flowerBuschellTypeLabel.setText(flowerBuschelType);
    }

    public void setMessageTypeLabel(String messageType) {
        MessageTypeLabel.setText(messageType);
    }

    public void setSpecialInstructionsLabel(String specialInstructions) {
        specialInstructionsLabel.setText(specialInstructions);
    }

    public void toggleEditable() {
        editable = !editable;
    }
}
