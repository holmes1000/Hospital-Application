package edu.wpi.teamb.controllers.requests;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.entities.requests.MoveRequestE;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

public class MoveRequestController {

    @FXML
    private MFXButton btnSubmit;
    @FXML
    private MFXButton btnCancel;
    @FXML
    private MFXButton btnReset;
    // @FXML private MFXTextField roomTextBox;
    @FXML
    private ImageView helpIcon;
    @FXML
    private VBox tableVbox;
    // @FXML private MFXComboBox<String> cbOrderLocation;
    // @FXML private MFXComboBox<String> cbEmployeesToAssign;
    // @FXML private MFXComboBox<String> cbFloorSelect; // Floor

    @FXML
    private MFXComboBox<String> cdRoomToMove;
    @FXML
    private MFXComboBox<Integer> cdWheretoMove;
    @FXML
    private DatePicker dateOfMove;
    @FXML
    private TableView<Move> tbFutureMoves;
    @FXML
    private MFXButton btnRemoveMove;
    @FXML
    private MFXButton btnEditRequuest;
    @FXML
    private MFXRadioButton radPastMove;

    private MoveRequestE moveRequestE;

    private int tableSize = 0;

    public MoveRequestController() {
        this.moveRequestE = new MoveRequestE();
    }

    @FXML
    public void initialize() throws IOException, SQLException {
        ObservableList<String> longNames = FXCollections.observableArrayList();
        longNames.addAll(Repository.getRepository().getAllLongNames());
        cdRoomToMove.setItems(longNames);

        ObservableList<Integer> NodeID = FXCollections.observableArrayList();
        ArrayList<Node> a = Repository.getRepository().getAllNodes();
        for (Node n : a) {
            NodeID.add(n.getNodeID());
        }
        cdWheretoMove.setItems(NodeID);
        moveTable();
        // start with radiobutton unchecked
        radPastMove.setSelected(false);
        btnRemoveMove.setDisable(true);
        btnEditRequuest.setDisable(true);
        initializeFields();
        hoverHelp();
    }

    private void moveTable() {
        // add moverequests attributes to table, id, longname,and date
        TableColumn<Move, Integer> ids = new TableColumn<>("ID");
        ids.setCellValueFactory(new PropertyValueFactory<Move, Integer>("nodeID"));

        TableColumn<Move, String> locs = new TableColumn<>("Location");
        locs.setCellValueFactory(new PropertyValueFactory<Move, String>("longName"));

        TableColumn<Move, Date> dates = new TableColumn<>("Dates");
        dates.setCellValueFactory(new PropertyValueFactory<Move, Date>("date"));

        tbFutureMoves.getColumns().addAll(ids, locs, dates);

        // ArrayList<Move> moves = Repository.getRepository().getAllMoves();
        // Date now = new Date(System.currentTimeMillis());

        // for (Move move : moves) {
        //     if (move.getDate().after(now)||radPastMove.isSelected()) {
        //         tbFutureMoves.getItems().add(move);
        //         tableSize++;
        //     }
        // }
        updateTable();
    }

    // TODO: This is a temporary fix to the table not updating when a new move is
    // submitted, Should updates from database everytime. Think f a better way to do
    // this if to slow
    private void updateTable() {
        // empty the table and refil database
        tbFutureMoves.getItems().clear();
        ArrayList<Move> moves = Repository.getRepository().getAllMoves();
        Date now = new Date(System.currentTimeMillis()-1000*60*60*24);

        for (Move move : moves) {
            if (move.getDate().after(now)||radPastMove.isSelected()) {
                tbFutureMoves.getItems().add(move);
                tableSize++;
            }
        }
        tbFutureMoves.refresh();
    }

    private String mdy2ymd(String date) {
        String[] parts = date.split("/");
        return parts[2] + "-" + parts[0] + "-" + parts[1];
    }

    private String ymd2ymd2(String date) {
        String[] parts = date.split("-");
        return parts[0] + "-" + parts[1] + "-" + parts[2];
    }

    private String ymd2mdy(String date) {
        String[] parts = date.split("-");
        return parts[1] + "/" + parts[2] + "/" + parts[0];
    }

    @FXML 
    public void viewMoves()
    {
        updateTable();
    }

    @FXML
    public void clickSubmit() {
        btnSubmit.setOnMouseClicked(event -> {
            String what = cdRoomToMove.getSelectedItem();
            Integer where = cdWheretoMove.getSelectedItem();
            Date when = Date.valueOf(dateOfMove.getValue());

            // popup error when not all fields are filled or when date is before current
            // date or when the move is already in the table
            if (what != null && where != null && when != null) {
                if (!tbFutureMoves.getItems().contains(new Move(where, what, when))) {
                    if (when.after(new Date(System.currentTimeMillis()-1000*60*60*24))) {
                        String[] output = { where.toString(), what, when.toString() };
                        moveRequestE.submitRequest(output);
                        clickResetForm();

                        updateTable();

                    } else {
                        // TODO popup error when entered date is before current date
                    }
                } else {
                    // TODO popup error when entered value is already in table
                }

                clickResetForm();

            } else {
                final FXMLLoader popupLoader = new FXMLLoader(
                        Bapp.class.getResource("views/components/popovers/NotAllFieldsCompleteError.fxml"));
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

    private boolean changeRequest = false;

    @FXML
    public void tbSetAlter() {
        // when clicking on a row in the table, the remove button is enabled and the
        // data is loaded into the form
        tbFutureMoves.setOnMouseClicked(event -> {
            if (tbFutureMoves.getSelectionModel().getSelectedItem() != null) {

                btnRemoveMove.setDisable(false);
                // TODO: enable edit button
                // btnEditRequuest.setDisable(false);

                Move move = tbFutureMoves.getSelectionModel().getSelectedItem();
                // make roomtomove box empty
                cdRoomToMove.selectItem(move.getLongName());
                // make wheretomove box empty
                cdWheretoMove.selectItem(move.getNodeID());
                // set date value
                dateOfMove.setValue(LocalDate.parse(ymd2ymd2(move.getDate().toString())));
                

            }
        });

    }

    @FXML
    public void RemoveRequest() {
        // when clicking on the remove button, the selected row is removed from the
        // table and the database
        btnRemoveMove.setOnMouseClicked(event -> {
            if (tbFutureMoves.getSelectionModel().getSelectedItem() != null) {
                Move move = tbFutureMoves.getSelectionModel().getSelectedItem();
                moveRequestE = new MoveRequestE(move);
                moveRequestE.removeRequest();
                tbFutureMoves.getItems().remove(move);
                tableSize--;
                clickResetForm();

            }
        });
        btnRemoveMove.setDisable(true);

        clickResetForm();

    }

    @FXML
    public void EditRequest() {
        // when clicking on the edit button, the selected row is removed from the table
        // and the database
        btnEditRequuest.setOnMouseClicked(event -> {
            if (tbFutureMoves.getSelectionModel().getSelectedItem() != null) {
                Move move = tbFutureMoves.getSelectionModel().getSelectedItem();
                tbFutureMoves.getItems().remove(move);
                moveRequestE = new MoveRequestE(move);
                // set values of move request from form info
                moveRequestE.updateRequest();

                moveRequestE.updateRequest();

                tableSize--;
                clickResetForm();
            }
        });
    }

    @FXML
    public void clickResetForm() {
        btnReset.setOnMouseClicked(
                event -> {
                    cdRoomToMove.clear();
                    cdRoomToMove.replaceSelection("Room to Move");
                    cdWheretoMove.clear();
                    cdWheretoMove.replaceSelection("Where to Move");
                    dateOfMove.setValue(null);
                    btnRemoveMove.setDisable(true);
                    changeRequest = false;
                    btnEditRequuest.setDisable(true);
                    updateTable();
                });
    }

    @FXML
    public void hoverHelp() {
        helpIcon.setOnMouseClicked(
                event -> {
                    final FXMLLoader popupLoader = new FXMLLoader(
                            // TODO: add Move request help popup
                            Bapp.class.getResource("views/components/MealRequestHelpPopOver.fxml"));
                    PopOver popOver = new PopOver();
                    popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
                    popOver.setArrowSize(0.0);
                    try {
                        popOver.setContentNode(popupLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    popOver.show(helpIcon);
                });
        // helpIcon.setOnMouseExited(event -> {});
    }

    private void initializeFields() throws SQLException {
        // initialize combo boxes
        cdRoomToMove.setValue("");
        cdRoomToMove.setPromptText("Room to Move");
        cdWheretoMove.setValue(-1);
        cdWheretoMove.setPromptText("Where to Move");
        dateOfMove.setValue(LocalDate.now());
        // initialize date picker
        dateOfMove.setPromptText("Date of Move");
    }

    @FXML
    public void clickExit() {
        System.exit(0);
    }
}
