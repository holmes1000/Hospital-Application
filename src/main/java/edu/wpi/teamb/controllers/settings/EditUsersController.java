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

public class EditUsersController {
    @FXML
    private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;
    @FXML private MFXComboBox<String> cbUsername;
    @FXML private MFXComboBox<String> cbPermissionLevel;
    @FXML private MFXTextField textPassword;
    @FXML private MFXTextField textUsername;
    @FXML private MFXTextField textPosition;
    @FXML private MFXComboBox<String> cbPermissionLevelEdit;
    @FXML private MFXTextField textPasswordEdit;
    @FXML private MFXTextField textUsernameEdit;
    @FXML private MFXTextField textPositionEdit;
    @FXML private MFXButton btnAddUser;
    @FXML private MFXButton btnDeleteUser;
    @FXML private MFXButton btnEditUser;
    @FXML private MFXButton btnSaveEdits;
    @FXML private VBox vboxEditUser;

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
        cbUsername.setItems(usernames);

        // Initialize the permission level combo boxes
        permissionLevels.add("ADMIN");
        permissionLevels.add("EMPLOYEE");
        cbPermissionLevel.setItems(permissionLevels);
        cbPermissionLevelEdit.setItems(permissionLevels);

        // Hide the edit vbox
        //vboxEditUser.setVisible(false);
    }

    public void initButtons() {
        btnAddUser.setOnMouseClicked(event -> handleAddUser());
        btnEditUser.setOnMouseClicked(event -> handleEditUser());
        btnDeleteUser.setOnMouseClicked(event -> handleDeleteUser());
        btnSaveEdits.setOnMouseClicked(event -> handleSaveEdits());
    }

    private void handleDeleteUser() {
        Repository.getRepository().deleteUser(Repository.getRepository().getUser(cbUsername.getValue())); // Delete the user
        initializeFields(); // Refresh the combo box
    }

    private void handleEditUser() {
        String userToEdit = cbUsername.getSelectedItem();
        User user = Repository.getRepository().getUser(userToEdit);
        textUsernameEdit.setText(user.getUsername());
        textPasswordEdit.setText(user.getPassword());
        textPositionEdit.setText(user.getPosition());
        cbPermissionLevelEdit.selectItem(setPermissionLevel(user.getPermissionLevel()));
        //vboxEditUser.setVisible(true);
        System.out.println("Edit user button");
        initializeFields(); // Refresh the combo box
    }

    private void handleAddUser() {
        User newUser = new User();
        newUser.setUsername(textUsername.getText());
        newUser.setPassword(textPassword.getText());
        newUser.setPosition(textPosition.getText());
        newUser.setPermissionLevel(getPermissionLevel(cbPermissionLevel.getValue()));
        Repository.getRepository().addUser(newUser);
        initializeFields(); // Refresh the combo box
    }

    private void handleSaveEdits() {
        User user = new User();
        user.setUsername(textUsernameEdit.getText());
        user.setPassword(textPasswordEdit.getText());
        user.setPosition(textPositionEdit.getText());
        if (cbPermissionLevel != null)
            user.setPermissionLevel(getPermissionLevel(cbPermissionLevelEdit.getValue()));
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
