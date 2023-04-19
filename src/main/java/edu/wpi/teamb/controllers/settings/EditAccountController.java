package edu.wpi.teamb.controllers.settings;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.controllers.NavDrawerController;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EditAccountController {
    @FXML
    private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private MFXComboBox<String> cbPermissionLevel;
    @FXML private MFXTextField tfPassword;
    @FXML private MFXTextField tfUsername;
    @FXML private MFXTextField tfPosition;
    @FXML private MFXTextField tfName;
    @FXML private MFXTextField tfEmail;
    @FXML private MFXButton btnSaveEdits;

    @FXML
    public void initialize() throws IOException {
        initNavBar();
        initializeFields();
        initButtons();
    }

    public void initializeFields() {
        ArrayList<User> listOfUsers = Repository.getRepository().getAllUsers();
        ObservableList<String> usernames = FXCollections.observableArrayList();
        ObservableList<String> permissionLevels = FXCollections.observableArrayList();

        // Initialize the username combo box
        for (User user : listOfUsers) {
            usernames.add(user.getUsername());
        }

        // Initialize the permission level combo boxes
        permissionLevels.add("ADMIN");
        permissionLevels.add("EMPLOYEE");
        cbPermissionLevel.setItems(permissionLevels);
        cbPermissionLevel.setItems(permissionLevels);

        // Hide the edit vbox
        //vboxEditUser.setVisible(false);
    }

    public void initButtons() {
        btnSaveEdits.setOnMouseClicked(event -> handleSaveEdits());
    }

    private void handleSaveEdits() {
        User user = new User();
        user.setUsername(tfUsername.getText());
        user.setPassword(tfPassword.getText());
        user.setPosition(tfPosition.getText());
        if (cbPermissionLevel != null)
            user.setPermissionLevel(getPermissionLevel(cbPermissionLevel.getValue()));
        Repository.getRepository().updateUser(user);
        initializeFields(); // Refresh the combo box
    }

    /**
     * Method to convert string permission level to int
     * @param permissionLevel
     * @return
     */
    private int getPermissionLevel(String permissionLevel) {
        if (permissionLevel.equals("ADMIN")) {
            return 0;
        }
        if (permissionLevel.equals("EMPLOYEE")) {
            return 1;
        }
        else
            return 2; // Error
    }

    private String setPermissionLevel(int permissionLevel) {
        if (permissionLevel == 0) {
            return "ADMIN";
        }
        if (permissionLevel == 1) {
            return "EMPLOYEE";
        }
        else
            return "Error"; // Error
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
