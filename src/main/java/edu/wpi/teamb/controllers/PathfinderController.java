package edu.wpi.teamb.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DB;
import edu.wpi.teamb.DBAccess.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.entities.Pathfinder;
import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import static javafx.scene.paint.Color.*;

public class PathfinderController {
  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private ImageView helpIcon;
  @FXML private MFXButton btnFindPath;

  @FXML private MFXFilterComboBox<String> startNode;
  @FXML private MFXFilterComboBox<String> endNode;


  @FXML private MFXComboBox<String> algorithmDropdown;
  @FXML private MFXListView<String> listView;
  @FXML private VBox VboxPathfinder;
  @FXML private StackPane stackPaneMapView;
  @FXML private ImageView imageViewPathfinder;
  @FXML private MFXButton btnL1;
    @FXML private MFXButton btnL2;
    @FXML private MFXButton btn1;
    @FXML private MFXButton btn2;
    @FXML private MFXButton btn3;

    @FXML private MFXToggleButton toggleAvoidElevators;
    @FXML private MFXToggleButton toggleAvoidStairs;
    private String currentFloor = "1";

    HashMap<String,ArrayList<Node>> nodes_by_floor = new HashMap<>();
  private Pathfinder pathfinder;

    public GesturePane pane = new GesturePane();
    ArrayList<FullNode> fullNodes = new ArrayList<>();
    HashMap<Integer,FullNode> filteredFullNodes = new HashMap<>();
    HashMap<String,FullNode> fullNodesByLongname = new HashMap<>();
    Group pathGroup;
    Pane locationCanvas;
  @FXML
  public void initialize() throws IOException {
      Platform.setImplicitExit(false);
      initNavBar();
      hoverHelp();
      initButtons();
      // Initialize the path
      //nodeList = editor.getNodeList();

      this.stackPaneMapView = new StackPane(); // no longer @FXML
      this.pathGroup = new Group();
      this.locationCanvas = new Pane();
      this.fullNodes = Repository.getRepository().getFullNodes();
      this.filteredFullNodes = new HashMap<>();
      getFilteredLongnames();
      this.pane.setContent(stackPaneMapView);
      this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML
      this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
      this.stackPaneMapView.getChildren().add(this.locationCanvas);

      this.locationCanvas.getChildren().add(pathGroup);
      this.fullNodes = Repository.getRepository().getFullNodes();
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


      nodes.addAll(getFilteredLongnames());
      algorithmDropdown.setItems(algorithms);
      startNode.setItems(nodes);
      endNode.setItems(nodes);
      startNode.getSearchText();
      endNode.getSearchText();
      changeButtonColor(currentFloor);
      algorithmDropdown.selectFirst();


      listView.getSelectionModel().selectionProperty().addListener(new ChangeListener<ObservableMap<Integer, String>>() {
          @Override
          public void changed(ObservableValue<? extends ObservableMap<Integer, String>> observable, ObservableMap<Integer, String> oldValue, ObservableMap<Integer, String> newValue) {
              String selectedLongName = listView.getSelectionModel().getSelectedValues().get(0);
              Integer index = listView.getItems().indexOf(selectedLongName);
              System.out.println(index);
              Node node = PathFinding.ASTAR.get_node_map().get(pathfinder.getPath().get(index));
//              String longname = Repository.getRepository().
              FullNode n = filteredFullNodes.get(node.getNodeID());
              String floor = n.getFloor();
              if (!currentFloor.equals(floor)) {switchFloor(floor);}
              pane.centreOnX(n.getxCoord());
              pane.centreOnY(n.getyCoord());
          }
      });


  }


  public void initNodeName (){

  }

  public ArrayList<String> getFilteredLongnames(){
      ArrayList<String> filtered_names = Repository.getRepository().getAllLongNames();
      for (FullNode node : fullNodes){
          filteredFullNodes.put(node.getNodeID(),node);
          fullNodesByLongname.put(node.getLongName(),node);
//          if (node.getNodeType().equals("STAI") || node.getNodeType().equals("ELEV")) {
//              filtered_names.remove(node.getLongName());
//
//          }
//          else {
//              filteredFullNodes.put(node.getLongName(),node);
//          }
      }
//      Collections.sort(filtered_names);
      return filtered_names;
  }

  public ArrayList<Integer> ListOfNodeIDs () throws SQLException {
      PathFinding.ASTAR.init_pathfinder();
        HashMap<Integer, Node> a = PathFinding.ASTAR.get_node_map();

        return new ArrayList<Integer>(a.keySet());
    }


  public PathfinderController() throws SQLException {
        this.pathfinder = new Pathfinder();
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
                  Tooltip tooltip = new Tooltip(DB.getLongNameFromNodeID(n.getNodeID()));
                  Tooltip.install(circle,tooltip);
                  tooltip.setShowDelay(Duration.millis(5));
                  tooltip.setStyle("-fx-font-size: 14px;");
                  pane.centreOnX(n.getxCoord());
                  pane.centreOnY(n.getyCoord());

              } else if (n == nodes.get(nodes.size() - 1)) {
                  Circle circle = new Circle(n.getxCoord(), n.getyCoord(), 5, PURPLE);
                  pathGroup.getChildren().add(circle);
                  Tooltip tooltip = new Tooltip(DB.getLongNameFromNodeID(n.getNodeID()));
                  Tooltip.install(circle,tooltip);
                  tooltip.setShowDelay(Duration.millis(5));
                  tooltip.setStyle("-fx-font-size: 14px;");

              } else {
                  Circle circle = new Circle(n.getxCoord(), n.getyCoord(), 5, RED);
                  pathGroup.getChildren().add(circle);
                  Tooltip tooltip = new Tooltip(DB.getLongNameFromNodeID(n.getNodeID()));
                  Tooltip.install(circle,tooltip);
                  tooltip.setShowDelay(Duration.millis(5));
                  tooltip.setStyle("-fx-font-size: 14px;");
              }
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
        });
        btn1.setOnMouseClicked(event->{
            currentFloor = "1";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(3));
            drawPath(nodes_by_floor.get("1"));
            locationCanvas.getChildren().add(pathGroup);
        });
        btn2.setOnMouseClicked(event->{
            currentFloor = "2";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(4));
            drawPath(nodes_by_floor.get("2"));
            locationCanvas.getChildren().add(pathGroup);
        });
        btn3.setOnMouseClicked(event->{
            currentFloor = "3";
            changeButtonColor(currentFloor);
            locationCanvas.getChildren().remove(pathGroup);
            pathGroup.getChildren().clear();
            imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(5));
            drawPath(nodes_by_floor.get("3"));
            locationCanvas.getChildren().add(pathGroup);
        });
    }

  public void clickFindPath() throws SQLException {
      btnFindPath.setOnMouseClicked(event-> {
          VboxPathfinder.getChildren().clear();
          int start = filteredFullNodes.get(fullNodesByLongname.get(startNode.getSelectedItem()).getNodeID()).getNodeID();
          int end = filteredFullNodes.get(fullNodesByLongname.get(endNode.getSelectedItem()).getNodeID()).getNodeID();

          String[] path = new String[0];
          try {

              if (algorithmDropdown.getSelectedItem() != null) {
                  if (toggleAvoidStairs.isSelected()) {path = pathfinder.getShortestPath("AStar","Elevators",start, end);}
//                  else if (toggleAvoidElevators.isSelected()) {path = pathfinder.getShortestPath("AStar","Stairs",start, end);}
                  else if (algorithmDropdown.getSelectedItem().equals("Breadth First Search")) {
                      path = pathfinder.getShortestPath("Breadth First Search", "None",start, end);
                  }
                  else if (algorithmDropdown.getSelectedItem().equals("Depth First Search")) {
                      path = pathfinder.getShortestPath("Depth First Search", "None", start, end);
                  }
                  else {path = pathfinder.getShortestPath("AStar","None",start, end);}
              }
              else {path = pathfinder.getShortestPath("AStar", "None",start, end);}


              ArrayList<Integer> int_path = pathfinder.getPath();
              ArrayList<Node> nodePath;
              nodes_by_floor = new HashMap<>();
              for (Integer id : int_path){
                  Node node = PathFinding.ASTAR.get_node_map().get(id);
                  nodePath = nodes_by_floor.get(node.getFloor());
//                  System.out.println(nodePath);
                  if (nodePath == null) {nodePath = new ArrayList<>();}
                  nodePath.add(PathFinding.ASTAR.get_node_map().get(id));
                  nodes_by_floor.put(node.getFloor(),nodePath);
              }

              String floor = PathFinding.ASTAR.get_node_map().get(start).getFloor();
              switchFloor(floor);

              } catch (SQLException e) {
                  throw new RuntimeException(e);
              }
              //I want clickFindPath to create paths only for the necessary floors.

              //Assume all images were already added to the stackPane

              //Add the image to the Front
              ObservableList<String> items = FXCollections.observableArrayList(path);
              listView.setItems(items);
              VboxPathfinder.getChildren().addAll(listView);
              listView.getSelectionModel().clearSelection();


      });
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
            final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/PathfindingHelpPopOver.fxml"));
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
