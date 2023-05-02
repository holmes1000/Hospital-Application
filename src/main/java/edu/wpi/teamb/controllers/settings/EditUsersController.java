package edu.wpi.teamb.controllers.settings;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

public class EditUsersController {
    @FXML
    private JFXHamburger menuBurger;
    @FXML private JFXDrawer menuDrawer;

    @FXML private MFXFilterComboBox<String> cbPermissionLevel;
    @FXML private MFXPasswordField textPassword;
    @FXML private MFXTextField textUsername;
    @FXML private MFXTextField textEmail;
    @FXML private MFXTextField textName;

    @FXML private MFXButton btnAddUser;
    @FXML private MFXButton btnDeleteUser;
    @FXML private MFXButton btnEditUser;
    @FXML private MFXButton btnBack;
    @FXML private MFXButton btnReset;
    @FXML private VBox vboxEditUser;
    @FXML private VBox tableVbox;
    @FXML private Pane navPane;
    @FXML private TableView<User> tbUsers;
    private int tableSize = 0;
    @FXML VBox vboxActivateNav;
    @FXML VBox vboxActivateNav1;

    private boolean navLoaded;

    @FXML
    public void initialize() throws IOException {
        navPane.setPickOnBounds(false);
        menuDrawer.setPickOnBounds(false);
        initNavBar();
        initializeFields();
        initButtons();
        initializeNavGates();
    }

    //EditUserController editUserController = new EditUserController();

    public void initializeFields() {
        ArrayList<User> listOfUsers = Repository.getRepository().getAllUsers();
        ObservableList<String> usernames = FXCollections.observableArrayList();
        ObservableList<String> permissionLevels = FXCollections.observableArrayList();

        // Initialize the permission level combo boxes
        permissionLevels.add("ADMIN");
        permissionLevels.add("EMPLOYEE");
        cbPermissionLevel.setItems(permissionLevels);

        userTable();
        navLoaded = false;
        activateNav();
        deactivateNav();
        // Hide the edit vbox
        //vboxEditUser.setVisible(false);

        Collections.sort(permissionLevels);
        Collections.sort(usernames);
    }

    public void initButtons() {
        btnAddUser.setTooltip(new Tooltip("Click to add a new user"));
        btnEditUser.setTooltip(new Tooltip("Click to edit a user's information"));
        btnDeleteUser.setTooltip(new Tooltip("Click to delete a user"));
        btnReset.setTooltip(new Tooltip("Click to reset the fields"));
        btnBack.setTooltip(new Tooltip("Click to go back to the settings"));
        textEmail.setTooltip(new Tooltip("Enter a valid email address"));
        textUsername.setTooltip(new Tooltip("Enter a username (this cannot be changed later)"));
        textName.setTooltip(new Tooltip("Enter a first and last name"));
        textPassword.setTooltip(new Tooltip("Enter a password (at least 8 characters)"));
        cbPermissionLevel.setTooltip(new Tooltip("Select a permission level"));
        tbUsers.setTooltip(new Tooltip("Double click a user to edit or delete"));

        btnAddUser.setOnMouseClicked(event -> handleAddUser());
        btnEditUser.setOnMouseClicked(event -> {
            try {
                handleEditUser();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnDeleteUser.setOnMouseClicked(event -> handleDeleteUser());
        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);
        btnBack.setOnMouseClicked(event -> Navigation.navigate(Screen.SETTINGS));
        btnReset.setOnMouseClicked(event -> handleReset());
        BooleanBinding bb = new BooleanBinding() {
            {
                super.bind(textUsername.textProperty(),
                        textPassword.textProperty(),
                        textEmail.textProperty(),
                        textName.textProperty(),
                        cbPermissionLevel.valueProperty());
            }

            @Override
            protected boolean computeValue() {
                return (textUsername.getText().isEmpty()
                        || textPassword.getText().isEmpty()
                        || textEmail.getText().isEmpty()
                        || textName.getText().isEmpty()
                        || cbPermissionLevel.getValue() == null);
            }
        };
        btnAddUser.disableProperty().bind(bb);

        ChangeListener<String> changeListener = (observable, oldValue, newValue) -> {
            btnReset.setDisable(false);
        };
        textUsername.textProperty().addListener(changeListener);
        textPassword.textProperty().addListener(changeListener);
        textEmail.textProperty().addListener(changeListener);
        textName.textProperty().addListener(changeListener);
        cbPermissionLevel.valueProperty().addListener(changeListener);
    }

    private void handleDeleteUser() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setContentText("Are you sure you want to delete this user?");
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            User user = tbUsers.getSelectionModel().getSelectedItem();
            Repository.getRepository().deleteUser(user); // Delete the user
            //tbUsers.refresh(); // Refresh the table
            updateTable();
            createAlert("User Deleted", "User Deleted Successfully");
        }
        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);
    }

    private void handleEditUser() throws IOException {
        User user = tbUsers.getSelectionModel().getSelectedItem();
        System.out.println("Edit user button");
        showEditMenu(user);
        tbUsers.refresh(); // Refresh the table
        btnDeleteUser.setDisable(true);
        btnEditUser.setDisable(true);
    }

    private void showEditMenu(User user) throws IOException {
        Parent root;
        EditUserController.setCurrentUser(user);
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/settings/EditUser.fxml")));
        Stage stage = new Stage();
        stage.setTitle("Edit User Details");
        stage.setScene(new Scene(root, 400, 600));
        stage.show();
    }

    private void handleAddUser() {
        User newUser = new User();
        newUser.setName(textName.getText());
        newUser.setUsername(textUsername.getText().toLowerCase());
        newUser.setPassword(textPassword.getText());
        newUser.setEmail(textEmail.getText().toLowerCase());
        if (cbPermissionLevel.getValue() != null)
            newUser.setPermissionLevel(permissionLevelToInt(cbPermissionLevel.getValue()));
        if (usernameDoesNotExist(newUser) && emailDoesNotExist(newUser)) {
            if (!nullInputs(newUser)) {
                Repository.getRepository().addUser(newUser);
                createAlert("User added", "User added successfully");
            }
            else {
                createAlert("Empty fields", "Please enter all fields");
            }
        }
        else if (!emailDoesNotExist(newUser)) {
            createAlert("Email already exists", "Please enter a different email");
        }
        else if (!usernameDoesNotExist(newUser))
        {
            createAlert("Username already exists", "Please enter a different username");
        }
        initializeFields(); // Refresh the combo box
    }

    private boolean nullInputs(User user) {
        return user.getName().equals("") || user.getUsername().equals("") || user.getPassword().equals("") || user.getEmail().equals("");
    }


    /**
     * Checks if the username already exists in the database
     * @param user
     * @return
     */
    boolean usernameDoesNotExist(User user) {
        ArrayList<User> listOfUsers = Repository.getRepository().getAllUsers();
        for (User u : listOfUsers) {
            if (u.getUsername().equals(user.getUsername())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the email already exists in the database
     * @param user
     * @return
     */
    boolean emailDoesNotExist(User user) {
        ArrayList<User> listOfUsers = Repository.getRepository().getAllUsers();
        for (User u : listOfUsers) {
            if (u.getEmail().equals(user.getEmail())) {
                return false;
            }
        }
        return true;
    }

    private void handleReset() {
        textName.clear();
        textUsername.clear();
        textPassword.clear();
        textEmail.clear();
        cbPermissionLevel.clear();
    }

    private void createAlert(String title, String context) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(context);
        alert.showAndWait();
    }

    @FXML
    public void tbItemSelected() {
        // when clicking on a row in the table, the remove button is enabled and the
        // data is loaded into the form
        tbUsers.setOnMouseClicked(event -> {
            if (tbUsers.getSelectionModel().getSelectedItem() != null) {
                btnDeleteUser.setDisable(false);
                btnEditUser.setDisable(false);

                User user = tbUsers.getSelectionModel().getSelectedItem();
                // Clear fields
            }
        });

    }

    private void userTable() {
        tbUsers.getColumns().clear();
        // add User attributes to the table (Name, Username, Password, Email, Permission Level)
        TableColumn<User, String> names = new TableColumn<>("Name");
        names.setStyle("-fx-alignment: CENTER;");
        names.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        TableColumn<User, String> usernames = new TableColumn<>("Username");
        usernames.setStyle("-fx-alignment: CENTER;");
        usernames.setCellValueFactory(new PropertyValueFactory<User, String>("username"));

        TableColumn<User, String> emails = new TableColumn<>("Email");
        emails.setStyle("-fx-alignment: CENTER;");
        emails.setCellValueFactory(new PropertyValueFactory<User, String>("email"));

        TableColumn<User, Integer> permissions = new TableColumn<>("Permission Level");
        permissions.setStyle("-fx-alignment: CENTER;");
        permissions.setCellValueFactory(new PropertyValueFactory<User, Integer>("permissionLevel"));

        tbUsers.getColumns().addAll(names, usernames, emails, permissions);
        updateTable();
    }

    private void updateTable() {
        // empty the table and refill database
        tbUsers.getItems().clear();
        ArrayList<User> users = Repository.getRepository().getAllUsers();
        for (User user : users) {
            tbUsers.getItems().add(user);
            tableSize++;
        }
        tbUsers.refresh();
    }

    /**
     * Method to convert string permission level to int
     * @param permissionLevel
     * @return
     */
    private int permissionLevelToInt(String permissionLevel) {
        if (permissionLevel.equals("ADMIN")) {
            return 0;
        }
        if (permissionLevel.equals("EMPLOYEE")) {
            return 1;
        }
        else
            return 2; // Error
    }

    private String permissionLevelToString(int permissionLevel) {
        if (permissionLevel == 0) {
            return "ADMIN";
        }
        if (permissionLevel == 1) {
            return "EMPLOYEE";
        }
        else
            return "Error"; // Error
    }


    /**
     * For some reason there are occasions when the nav-bar gates for toggling its handling does not start correctly
     * This fixes this issue
     */
    public void initializeNavGates(){
        activateNav();
        deactivateNav();
        navPane.setMouseTransparent(true);
        vboxActivateNav.setDisable(false);
        navLoaded = false;
        vboxActivateNav1.setDisable(true);
    }

    /**
     * Utilizes a gate to swap between handling the navdrawer and the rest of the page
     * Swaps ownership of the strip to the navdraw
     */

    public void activateNav(){
        vboxActivateNav.setOnMouseEntered(event -> {
            if(!navLoaded) {
                navPane.setMouseTransparent(false);
                navLoaded = true;
                vboxActivateNav.setDisable(true);
                vboxActivateNav1.setDisable(false);
            }
        });
    }

    /**
     * Utilizes a gate to swap between handling the navdrawer and the rest of the page
     * Swaps ownership of the strip to the page
     */
    public void deactivateNav(){
        vboxActivateNav1.setOnMouseEntered(event -> {
            if(navLoaded){
                navPane.setMouseTransparent(true);
                vboxActivateNav.setDisable(false);
                navLoaded = false;
                vboxActivateNav1.setDisable(true);
            }
        });
    }
    public void initNavBar() {
        // https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
        try {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/NavDrawer.fxml"));
            VBox vbox = loader.load();
            NavDrawerController navDrawerController = loader.getController();
            menuDrawer.setSidePane(vbox);
            navPane.setMouseTransparent(true);
            navLoaded = false;
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
                        vboxActivateNav1.toFront();
                    } else {
                        menuDrawer.toFront();
                        menuBurger.toFront();
                        menuDrawer.open();
                    }
                });
    }
}
