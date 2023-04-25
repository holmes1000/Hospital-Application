package edu.wpi.teamb.controllers.pathfinder;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.DButils;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import edu.wpi.teamb.pathfinding.PathFinding;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.EPathfinder;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import static javafx.scene.paint.Color.*;

public class PathfinderController {
  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private ImageView helpIcon;
  @FXML private MFXButton btnFindPath;

  @FXML private MFXFilterComboBox<String> startNode;
  @FXML private MFXFilterComboBox<String> endNode;


  @FXML private MFXComboBox<String> algorithmDropdown;
  @FXML private MFXListView<String> listView = new MFXListView<>();
  @FXML private VBox VboxPathfinder;
  @FXML private StackPane stackPaneMapView;
  @FXML private ImageView imageViewPathfinder;
  @FXML private MFXButton btnL1;
    @FXML private MFXButton btnL2;
    @FXML private MFXButton btn1;
    @FXML private MFXButton btn2;
    @FXML private MFXButton btn3;
    @FXML private MFXDatePicker datePicker;
    @FXML private MFXToggleButton toggleAvoidStairs;
    @FXML private MFXToggleButton toggleShowNames;

    @FXML private MFXButton previousFloor;
    @FXML private MFXButton nextFloor;

    private String currentFloor = "1";
    private HashMap<Integer,ArrayList<Move>> move_map = new HashMap<>();
    private ArrayList<Move> upcoming_moves = new ArrayList<>();

    private String firstFloorVisited;
    private String lastFloorVisited;
    private ArrayList<String> floorsVisited;

    HashMap<String,ArrayList<Node>> nodes_by_floor = new HashMap<>();
  private EPathfinder EPathfinder;

    public GesturePane pane = new GesturePane();
    ArrayList<FullNode> fullNodes = new ArrayList<>();
    HashMap<String,FullNode> fullNodesByLongname = new HashMap<>();
    HashMap<Integer,FullNode> fullNodesByID = new HashMap<>();
    ArrayList<String> filtered_names = new ArrayList<>();
    Group pathGroup;
    Group nameGroup;
    Pane locationCanvas;
  @FXML
  public void initialize() throws IOException {
      Platform.setImplicitExit(false);
      initNavBar();
      hoverHelp();
      initButtons();
      getMoveMap();

      for (Integer id : PathFinding.ASTAR.getFullNodesByID().keySet()) {
          fullNodesByID.put(id,PathFinding.ASTAR.getFullNodesByID().get(id));
      }
      // Initialize the path
      //nodeList = editor.getNodeList();

      this.stackPaneMapView = new StackPane(); // no longer @FXML
      this.pathGroup = new Group();
      this.locationCanvas = new Pane();
      this.nameGroup = new Group();
//      this.filteredFullNodes = new HashMap<>();
      getFilteredLongnames();
      this.pane.setContent(stackPaneMapView);
      this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML
      this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
      this.stackPaneMapView.getChildren().add(this.locationCanvas);

      this.locationCanvas.getChildren().add(pathGroup);
      this.locationCanvas.getChildren().add(nameGroup);
      this.fullNodes = PathFinding.ASTAR.getFullNodes();
//      this.filteredFullNodes = new HashMap<>();

      pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
      pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
      Platform.runLater(() -> this.pane.centreOn(new Point2D(2190, 910)));
      System.out.println("PathfinderController initialized");


      ObservableList<String> nodes = FXCollections.observableArrayList();
      ObservableList<String> algorithms = FXCollections.observableArrayList();
      algorithms.add("AStar (Default)");
      algorithms.add("Breadth First Search");
      algorithms.add("Depth First Search");
      algorithms.add("Dijkstra Search");
      algorithms.add("BStar");


      nodes.addAll(getFilteredLongnames());
      algorithmDropdown.setItems(algorithms);
      startNode.setItems(nodes);
      endNode.setItems(nodes);
      startNode.getSearchText();
      endNode.getSearchText();
      handleDate();
      changeButtonColor(currentFloor);
      algorithmDropdown.selectFirst();


      listView.getSelectionModel().selectionProperty().addListener(new ChangeListener<ObservableMap<Integer, String>>() {
          @Override
          public void changed(ObservableValue<? extends ObservableMap<Integer, String>> observable, ObservableMap<Integer, String> oldValue, ObservableMap<Integer, String> newValue) {
              if (!listView.getSelectionModel().getSelectedValues().isEmpty()) {
                  String selectedLongName = listView.getSelectionModel().getSelectedValues().get(0);
                  Integer index = listView.getItems().indexOf(selectedLongName);
//                  System.out.println(index);
                  Node node = PathFinding.ASTAR.get_node_map().get(EPathfinder.getPath().get(index));
                  FullNode n = fullNodesByID.get(node.getNodeID());
                  String floor = n.getFloor();
                  if (!currentFloor.equals(floor)) {
                      switchFloor(floor);
                  }
                  pane.centreOnX(n.getxCoord());
                  pane.centreOnY(n.getyCoord());
              }
              if (currentFloor.equals(firstFloorVisited) && floorsVisited.size() >1 ) {nextFloor.setDisable(false); previousFloor.setDisable(true);}
              else if (currentFloor.equals((lastFloorVisited))&& floorsVisited.size() >1) {nextFloor.setDisable(true); previousFloor.setDisable(false);}
          }
      });


  }


  public void initNodeName (){

  }

  public void getMoveMap(){
      HashMap<Integer,ArrayList<Move>> move_map = new HashMap<>();
      ArrayList<Move> moves = Repository.getRepository().getAllMoves();
      ArrayList<Move> currentMove = new ArrayList<>();
      for (Move move : moves) {
          if (move_map.containsKey(move.getNodeID())) {currentMove = move_map.get(move.getNodeID());}
          else {currentMove = new ArrayList<>();}
          currentMove.add(move);
          move_map.put(move.getNodeID(),currentMove);
      }
//      System.out.println(move_map.get(105));
      this.move_map = move_map;

  }

  public void handleDate(){
      datePicker.setValue(LocalDate.now()); // Init to current date
      LocalDate date_inputted = datePicker.getCurrentDate();
      handle_move();
      datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
          @Override
          public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
              //Print date change to console
              System.out.println("New date selected: " + newValue);
              handle_move();

          }
      });
  }

    void drawName(FullNode n, Integer x, Integer y) {
        for (FullNode fn : fullNodes) {
            if (!Objects.equals(fn.getNodeType(), "HALL") && Objects.equals(fn.getFloor(),currentFloor)) {
                if (fn.getLongName().equals(n.getLongName())) {
                    Text name = new Text(fn.getShortName());
                    for (Integer i = 0; i <= 6; i++) {
                        if (nameGroup.contains(new Point2D(x+i, y+i))) {
                            y += 1;
                        }
                    }
                    name.setX(x+5);
                    name.setY(y+5);
                    nameGroup.getChildren().add(name);
                }
            }
        }
    }

  public void handle_move() {
//      System.out.println("handling moves");
      upcoming_moves.clear();
      HashMap<Integer,Move> nodes_to_update = new HashMap<>();
      LocalDate current_date = datePicker.getValue();
      LocalDate tempDate;
      for (Integer id : move_map.keySet()){
          if (move_map.get(id).size() >= 1) {
              tempDate = move_map.get(id).get(0).getDate().toLocalDate().minusYears(1);
              for (Move move : move_map.get(id)) {
                  LocalDate move_date =  move.getDate().toLocalDate();

                  if ((move_date.isAfter(tempDate)) && move_date.isBefore(current_date)) {
                      tempDate = move_date;
                      nodes_to_update.put(move.getNodeID(),move);
                  }

                  else if (move_date.equals(current_date)) {
                      tempDate = move_date;
                      nodes_to_update.put(move.getNodeID(),move);
                  }
                  if (move_date.isAfter(current_date) || move_date.equals(current_date)) {upcoming_moves.add(move);}
              }

          }
      }
      ArrayList<String> longname_list = new ArrayList<>();
//      for (Integer id : nodes_to_update.keySet()) {
//          if (longname_list.contains(nodes_to_update.get(id).getLongName())); {
//              if (nodes_to_update.get(id).getNodeID() == fullNodesByID.get(nodes_to_update.get(id).getNodeID()).getNodeID()) {nodes_to_update.remove(nodes_to_update.get(id));}
//          }
//          longname_list.add(nodes_to_update.get(id).getLongName());
//      }
      update_nodes_from_moves(nodes_to_update);
      ObservableList<String> nodes = FXCollections.observableArrayList();
      nodes.addAll(getFilteredLongnames());
      startNode.setItems(nodes);
      endNode.setItems(nodes);
//      System.out.println("handled");
//      System.out.println("nodes to update");
//      System.out.println(nodes_to_update);


  }

    public void update_nodes_from_moves(HashMap<Integer,Move> nodes_to_update){
            fullNodes = new ArrayList<>();
            ArrayList<FullNode> fn = Repository.getRepository().getAllFullNodes();
        for (Integer id : fullNodesByID.keySet()){
            if (nodes_to_update.containsKey(id)){
                FullNode newNode = fullNodesByID.get(id);
                newNode.setLongName(nodes_to_update.get(id).getLongName());
//                System.out.println(nodes_to_update.get(id).getLongName()  + nodes_to_update.get(id).getNodeID());
                if (!newNode.getLongName().equals(fullNodesByID.get(id).getLongName())){
                newNode.setShortName(PathFinding.ASTAR.getFullNodesByID().get(id).getShortName());
                }
                fullNodes.add(newNode);
            }
            else {
                fullNodes.add(fullNodesByID.get(id));
            }
        }
        getFilteredLongnames();
    }

  public ArrayList<String> getFilteredLongnames(){
      nameGroup.getChildren().clear();
      ArrayList<String> filtered_names = new ArrayList<>();
      fullNodesByLongname = new HashMap<>();
      fullNodesByID = new HashMap<>();
      for (FullNode node : fullNodes){
          fullNodesByID.put(node.getNodeID(),node);
          fullNodesByLongname.put(node.getLongName(),node);

          if (!node.getNodeType().equals("HALL")) {
              filtered_names.add(node.getLongName());
              drawName(node, node.getxCoord(), node.getyCoord());
          }
//          if (node.getNodeType().equals("STAI") || node.getNodeType().equals("ELEV")) {
//              filtered_names.remove(node.getLongName());
//
//          }
//          else {
//              filteredFullNodes.put(node.getLongName(),node);
//          }
      }
      Collections.sort(filtered_names);
      this.filtered_names = filtered_names;
      nameGroup.toFront();
      return filtered_names;
  }


    public void moveAlert(String input) {
      ArrayList<String> message = new ArrayList<>();
      for (Move move : upcoming_moves) {
          if (input.contains(move.getLongName())) {
              message.add(move.getLongName() + " will be moving to node " + move.getNodeID() + " on " + move.getDate());
          }
      }

        // Create an alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upcoming Moves");
        alert.setHeaderText(null);
        String alert_message = "";
        for (String string : message) {
            alert_message += string + "\n";
        }
        alert.setContentText(alert_message);
        alert.showAndWait();
    }

  public void label_nodes(){
      
  }

  public ArrayList<Integer> ListOfNodeIDs () throws SQLException {
      PathFinding.ASTAR.force_init();
        HashMap<Integer, Node> a = PathFinding.ASTAR.get_node_map();

        return new ArrayList<Integer>(a.keySet());
    }


  public PathfinderController() throws SQLException {
        this.EPathfinder = new EPathfinder();
  }

  public void drawPath(ArrayList<Node> nodes){
      if (nodes != null) {
          for (int i = 0; i < nodes.size() - 1; i++) {
              Node n = nodes.get(i);
              Node next = nodes.get(i + 1);
              // Check if nodes are neighboring before drawing the line
              if (PathFinding.ASTAR.get_node_map().get(n.getNodeID()).getNeighborIds().contains(PathFinding.ASTAR.get_node_map().get(next.getNodeID()).getNodeID())) {
                  Line line = new Line(n.getxCoord(), n.getyCoord(), next.getxCoord(), next.getyCoord());
                  animateLine(line);
                  line.setStrokeWidth(4);
                  pathGroup.getChildren().add(line);
              }
          }
          for (Node n : nodes) {
//          pathGroup.getChildren().clear();
              if (n == nodes.get(0)) {
                  Circle circle = new Circle(n.getxCoord(), n.getyCoord(), 5, GREEN);
                  pathGroup.getChildren().add(circle);
                  Tooltip tooltip = new Tooltip(fullNodesByID.get(n.getNodeID()).getLongName());
                  Tooltip.install(circle,tooltip);
                  tooltip.setShowDelay(Duration.millis(5));
                  tooltip.setStyle("-fx-font-size: 14px;");
                  pane.centreOnX(n.getxCoord());
                  pane.centreOnY(n.getyCoord());

              } else if (n == nodes.get(nodes.size() - 1)) {
                  Circle circle = new Circle(n.getxCoord(), n.getyCoord(), 5, PURPLE);
                  pathGroup.getChildren().add(circle);
                  Tooltip tooltip = new Tooltip(fullNodesByID.get(n.getNodeID()).getLongName());
                  Tooltip.install(circle,tooltip);
                  tooltip.setShowDelay(Duration.millis(5));
                  tooltip.setStyle("-fx-font-size: 14px;");

              }
//              else { // Commented out to only draw start and end of a path on a floor
//                  Circle circle = new Circle(n.getxCoord(), n.getyCoord(), 5, RED);
//                  pathGroup.getChildren().add(circle);
//                  Tooltip tooltip = new Tooltip(fullNodesByID.get(n.getNodeID()).getLongName());
//                  Tooltip.install(circle,tooltip);
//                  tooltip.setShowDelay(Duration.millis(5));
//                  tooltip.setStyle("-fx-font-size: 14px;");
//              }
          }

          pathGroup.toFront();
      }
  }

  private void animateLine(Line line)
  {
      // https://stackoverflow.com/questions/36727777/how-to-animate-dashed-line-javafx
      line.getStrokeDashArray().setAll(25d, 15d, 10d, 20d);
      final double maxOffset =
              line.getStrokeDashArray().stream()
                      .reduce(
                              0d,
                              (a, b) -> a - b
                      );

      Timeline timeline = new Timeline(
              new KeyFrame(
                      Duration.ZERO,
                      new KeyValue(
                              line.strokeDashOffsetProperty(),
                              0,
                              Interpolator.LINEAR
                      )
              ),
              new KeyFrame(
                      Duration.seconds(2),
                      new KeyValue(
                              line.strokeDashOffsetProperty(),
                              maxOffset,
                              Interpolator.LINEAR
                      )
              )
      );
      timeline.setCycleCount(Timeline.INDEFINITE);
      timeline.play();
  }

    private void changeButtonColor(String currentFloor) {
        switch (currentFloor) {
            case "L1" -> {
                btnL1.setStyle("-fx-background-color: #f6bd38");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "L2" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #f6bd38");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "1" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #f6bd38");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "2" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #f6bd38");
                btn3.setStyle("-fx-background-color: #1C4EFE");
            }
            case "3" -> {
                btnL1.setStyle("-fx-background-color: #1C4EFE");
                btnL2.setStyle("-fx-background-color: #1C4EFE");
                btn1.setStyle("-fx-background-color: #1C4EFE");
                btn2.setStyle("-fx-background-color: #1C4EFE");
                btn3.setStyle("-fx-background-color: #f6bd38");
            }
        }
    }


    public void initButtons() {
        clickFloorBtn("L1");
        clickFloorBtn("L2");
        clickFloorBtn("1");
        clickFloorBtn("2");
        clickFloorBtn("3");

        previousFloor.setVisible(false);
        nextFloor.setVisible(false);
        toggleShowNames.setSelected(true);
        toggleShowNames.setOnMouseClicked(event->{handleToggleShowNames();});
    }

   public void handleToggleShowNames() {
       if(toggleShowNames.isSelected()){
           nameGroup.setVisible(true);
           System.out.println("Location names on");
       } else {
           nameGroup.setVisible(false);
           System.out.println("Location names off");
           toggleShowNames.setSelected(false);
       }
    }

    public void clickFloorBtn(String floor) {
        btnL1.setOnMouseClicked(event->{
            currentFloor = "L1";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(0));
            drawPath(nodes_by_floor.get("L1"));
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().add(pathGroup);
            getFilteredLongnames();
        });
        btnL2.setOnMouseClicked(event->{
            currentFloor = "L2";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(1));
            drawPath(nodes_by_floor.get("L2"));
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().add(pathGroup);
            getFilteredLongnames();
        });
        btn1.setOnMouseClicked(event->{
            currentFloor = "1";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(3));
            drawPath(nodes_by_floor.get("1"));
            locationCanvas.getChildren().add(pathGroup);
            getFilteredLongnames();
        });
        btn2.setOnMouseClicked(event->{
            currentFloor = "2";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(4));
            drawPath(nodes_by_floor.get("2"));
            locationCanvas.getChildren().add(pathGroup);
            getFilteredLongnames();
        });
        btn3.setOnMouseClicked(event->{
            currentFloor = "3";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(5));
            drawPath(nodes_by_floor.get("3"));
            locationCanvas.getChildren().add(pathGroup);
            getFilteredLongnames();
        });
    }

  public void clickFindPath() throws SQLException {
      btnFindPath.setOnMouseClicked(event-> {
          ArrayList<Node> nodePath = new ArrayList<>();
          ArrayList<String> string_path = new ArrayList<>();
          VboxPathfinder.getChildren().clear();
          if (!(startNode.getSelectedItem() == null)  && !(endNode.getSelectedItem() == null)) {
              int start = fullNodesByID.get(fullNodesByLongname.get(startNode.getSelectedItem()).getNodeID()).getNodeID();
              int end = fullNodesByID.get(fullNodesByLongname.get(endNode.getSelectedItem()).getNodeID()).getNodeID();

              String[] path = new String[0];
              try {

                  if (algorithmDropdown.getSelectedItem() != null) {
                      if (toggleAvoidStairs.isSelected()) {
                          path = EPathfinder.getShortestPath("AStar", "Elevators", start, end);
                      }
//                  else if (toggleAvoidElevators.isSelected()) {path = pathfinder.getShortestPath("AStar","Stairs",start, end);}
                      else if (algorithmDropdown.getSelectedItem().equals("Breadth First Search")) {
                          path = EPathfinder.getShortestPath("Breadth First Search", "None", start, end);
                      } else if (algorithmDropdown.getSelectedItem().equals("Depth First Search")) {
                          path = EPathfinder.getShortestPath("Depth First Search", "None", start, end);
                      } else if (algorithmDropdown.getSelectedItem().equals("Dijkstra Search")) {
                          path = EPathfinder.getShortestPath("Dijkstra Search", "None", start, end);
                      } else {
                          path = EPathfinder.getShortestPath("AStar", "None", start, end);
                      }
                  } else {
                      path = EPathfinder.getShortestPath("AStar", "None", start, end);
                  }


                  ArrayList<Integer> int_path = EPathfinder.getPath();
                  //
                  //firstFloorVisited = int_path.get(0);
                  //lastFloorVisited = int_path.get(int_path.size()-1);
                  //
                  nodes_by_floor = new HashMap<>();
                  for (Integer id : int_path) {
                      FullNode node = fullNodesByID.get(id);
                      nodePath = nodes_by_floor.get(node.getFloor());
//                  System.out.println(nodePath);
                      if (nodePath == null) {
                          nodePath = new ArrayList<>();
                      }
                      nodePath.add(PathFinding.ASTAR.get_node_map().get(id));
                      nodes_by_floor.put(node.getFloor(), nodePath);
                      string_path.add(node.getLongName());
                  }

                  String floor = PathFinding.ASTAR.get_node_map().get(start).getFloor();
                  switchFloor(floor);

              } catch (SQLException e) {
                  throw new RuntimeException(e);
              }
              //I want clickFindPath to create paths only for the necessary floors.

              //Assume all images were already added to the stackPane

              //Add the image to the Front
              ObservableList<String> items = FXCollections.observableArrayList(string_path);
//              listView = new MFXListView<>();
              listView.setItems(items);
              VboxPathfinder.getChildren().addAll(listView);
              listView.getSelectionModel().clearSelection();
              floorsVisited = new ArrayList<>();
              String lastFloor = fullNodesByID.get(start).getFloor();
              floorsVisited.add(lastFloor);
              for (Node node : nodePath) {
                  if (!lastFloor.equals(node.getFloor())){
                      lastFloor = node.getFloor();
                      floorsVisited.add(node.getFloor());
                  }
              }
          }

          if (floorsVisited != null) {
              System.out.println(floorsVisited);
              previousFloor.setVisible(true);
              previousFloor.setDisable(true);
              previousFloor.setOnMouseClicked(e->clickPreviousFloor());
              nextFloor.setVisible(true);
              nextFloor.setOnMouseClicked(e->clickNextFloor());
//          floorsVisited = new ArrayList<String>(nodes_by_floor.keySet());
              firstFloorVisited = floorsVisited.get(0);
              lastFloorVisited = floorsVisited.get(floorsVisited.size() - 1);
          }
          for (Move move : upcoming_moves){
              if (startNode.getSelectedItem().equals(move.getLongName())) {
                  moveAlert(startNode.getSelectedItem());
              }
              if (endNode.getSelectedItem().equals(move.getLongName())) {
                  moveAlert(endNode.getSelectedItem());
              }


          }
      });
  }

    public void clickPreviousFloor(){
        //Set<String> floorsVisited = nodes_by_floor.keySet();
        String next = currentFloor;
        if(!currentFloor.equals(firstFloorVisited)){
            if(currentFloor.equals(lastFloorVisited)){
                int i = floorsVisited.indexOf(currentFloor);
                next = floorsVisited.get(i-1);
                switchFloor(next);
                nextFloor.setDisable(false);
//                System.out.println("hi");
            }else{
                int i = floorsVisited.indexOf(currentFloor);
                next = floorsVisited.get(i-1);
                switchFloor(next);
            }
        }

        if(currentFloor.equals(firstFloorVisited)){
            previousFloor.setDisable(true);
        }



//            //currentFloorNum = listOfFloors.get(0);
//            //maxFloorNum = listOfFloors.get(listOfFloors.size() - 1);
//            if(currentFloorNum > minFloorNum){
//                int index = listOfFloors.indexOf(currentFloorNum) - 1;
//                currentFloor = Integer.toString(listOfFloors.get(index));
//                currentFloorNum = listOfFloors.get(index);
//                changeButtonColor(currentFloor);
//                pathGroup.getChildren().clear();
//                imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(currentFloorNum));
//                drawPath(nodes_by_floor.get(currentFloor));
//                locationCanvas.getChildren().add(pathGroup);
//
//            }
//            if(listOfFloors.indexOf(currentFloorNum) == 0){
//                previousArrow.setDisable(true);
//            }

    }

    public void clickNextFloor(){
      String next = currentFloor;

        if(!currentFloor.equals(lastFloorVisited)){
            if(currentFloor.equals(firstFloorVisited)){
                int i = floorsVisited.indexOf(currentFloor);
                next = floorsVisited.get(i+1);
                switchFloor(next);
                previousFloor.setDisable(false);
                System.out.println("hi");
            }else{
                int i = floorsVisited.indexOf(currentFloor);
                next = floorsVisited.get(i+1);
                switchFloor(next);
            }
        }

        if(currentFloor.equals(lastFloorVisited)){
            nextFloor.setDisable(true);
        }
        if (floorsVisited.size() == 1){nextFloor.setDisable(true); previousFloor.setDisable(true);}



            //currentFloorNum = listOfFloors.get(0);
            //maxFloorNum = listOfFloors.get(listOfFloors.size() - 1);
//            if(currentFloorNum < minFloorNum){
//                int index = listOfFloors.indexOf(currentFloorNum) + 1;
//                currentFloor = Integer.toString(listOfFloors.get(index));
//                currentFloorNum = listOfFloors.get(index);
//                changeButtonColor(currentFloor);
//                pathGroup.getChildren().clear();
//                imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(currentFloorNum));
//                drawPath(nodes_by_floor.get(currentFloor));
//                locationCanvas.getChildren().add(pathGroup);
//            }
//            if(listOfFloors.indexOf(currentFloorNum) == listOfFloors.size()- 1){
//                previousArrow.setDisable(true);
//            }


    }


    private void switchFloor(String floor) {
        Integer floorNum = 0;
        currentFloor = floor;
        if (floor.equals("L1")){floorNum = 0;}
        if (floor.equals("L2")){floorNum = 1;}
        if (floor.equals("")){floorNum = 2; floor = "L";}
        if (floor.equals("1")){floorNum = 3;}
        if (floor.equals("2")){floorNum = 4;}
        if (floor.equals("3")){floorNum = 5;}

        pathGroup.getChildren().clear();
        imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(floorNum));
        changeButtonColor(floor);
        drawPath(nodes_by_floor.get(floor));
        //pathGroup.toFront();
        if (!locationCanvas.getChildren().contains(pathGroup)){
            locationCanvas.getChildren().add(pathGroup);}
    }


  @FXML
  public void hoverHelp() {
    helpIcon.setOnMouseClicked(
        event -> {
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/PathfindingHelpPopOver.fxml"));
            PopOver popOver = new PopOver();
            popOver.setDetachable(true);
            popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
            popOver.setArrowSize(0.0);
            try {
                popOver.setContentNode(popupLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            popOver.show(helpIcon);
        });
  }

  public void initNavBar() {
    //https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
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
