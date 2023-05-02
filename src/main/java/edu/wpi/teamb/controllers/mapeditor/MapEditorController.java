package edu.wpi.teamb.controllers.mapeditor;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DBoutput;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Edge;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.entities.DefaultStart;
import edu.wpi.teamb.entities.EMapEditor;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Builder;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ResponseCache;
import java.sql.Array;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

import static javafx.scene.paint.Color.RED;

public class MapEditorController {
  public static Node currentNode;
  @FXML
  private JFXHamburger menuBurger;
  @FXML
  private JFXDrawer menuDrawer;
  @FXML
  private ImageView helpIcon;
  @FXML
  private ImageView imageViewPathfinder;
  @FXML
  private StackPane stackPaneMapView;

  private EMapEditor editor;
  @FXML
  private MFXButton btnL1;
  @FXML
  private MFXButton btnL2;
  @FXML
  private MFXButton btn1;
  @FXML
  private MFXButton btn2;
  @FXML
  private MFXButton btn3;
  @FXML
  private MFXToggleButton toggleNodes;
  @FXML
  private MFXToggleButton toggleLocationNames;
  @FXML
  private MFXToggleButton toggleEdges;
  @FXML
  private MFXToggleButton toggleMoves;
  @FXML
  private MFXButton btnViewMoveMap;
  @FXML private MFXButton btnFindPath;

  //Objects for updating nav bar
  @FXML private Pane navPane;
  @FXML private VBox vboxActivateNav;
  @FXML private VBox vboxActivateNav1;
  private boolean navLoaded;

  //Objects that get superimposed

  public GesturePane pane = new GesturePane();
  Group nodeGroup = new Group();
  Group edgeGroup = new Group();
  Group nameGroup = new Group();
  Pane locationCanvas;
  Pane fullNodeCanvas;
  //public static ArrayList<Node> nodeList = new ArrayList<>();
  public static ArrayList<FullNode> fullNodesList = new ArrayList<>();
  private ArrayList<FullNode> floorList = new ArrayList<>();

  //Other misc items
  public String currentFloor = "1";
  private ArrayList<LocationName> locationNameList = new ArrayList<>();
  @FXML
  private VBox vboxBtns;
  @FXML
  private VBox vboxAddNode;
  private ArrayList<Node> floorNodes = new ArrayList<>();
  private Pane menuPane;
  private boolean editingNode = false; // Used for the submitting details button
  @FXML
  Text tfState;
  @FXML
  private MFXButton btnAlignNodes;

  @FXML private MFXButton btnPathfinder;

  // New States
  MapEditorState addNodeState = new AddNodeState();
  MapEditorState editNodeState = new EditNodeState();
  MapEditorState deleteNodeState = new DeleteNodeState();
  MapEditorState addEdgeState = new AddEdgeState();
  MapEditorState deleteEdgeState = new DeleteEdgeState();
  MapEditorState addMoveState = new AddMoveState();
  MapEditorState alignNodesState = new AlignNodesState();

  // Create the states
  MapEditorContext mapEditorContext = new MapEditorContext();
  private Set<Circle> selectedNodes = new HashSet<>();

  private Set<Circle> nodesToAlign = new HashSet<>();

  private double m = 0;
  private double c = 0;
  Circle c1;
  Circle c2;

  // New menu buttons
  @FXML
  private MFXButton btnSubmitMove;
  @FXML
  private DatePicker dateToMove;
  @FXML
  private MenuButton btnMenuNode;
  private CustomMenuItem btnAddNode = new CustomMenuItem();
  private CustomMenuItem btnEditNode = new CustomMenuItem();
  private CustomMenuItem btnDeleteNode = new CustomMenuItem();
  @FXML
  private MenuButton btnMenuEdge;
  @FXML
  private CustomMenuItem itemAddEdge = new CustomMenuItem();
  @FXML
  private CustomMenuItem itemDeleteEdge = new CustomMenuItem();
  @FXML
  private MenuButton btnMenuMove;
  @FXML
  private CustomMenuItem itemAddMove = new CustomMenuItem();
  @FXML
  private CustomMenuItem itemDeleteMove = new CustomMenuItem();
  @FXML
  private CustomMenuItem itemViewMoves = new CustomMenuItem();
  @FXML
  private MenuButton btnMenuTools;
  @FXML
  private CustomMenuItem itemAlign = new CustomMenuItem();
  @FXML
  private CustomMenuItem itemSetDefault = new CustomMenuItem();

  @FXML
  private MenuButton btnMenuBackup;
  @FXML
  private CustomMenuItem itemResetFromBackup = new CustomMenuItem();
  private MoveMap moveMap;
  private EditNodeMenuController editNodeMenuController;
  private AddNodeMenuController addNodeMenuController;

  public MapEditorController() throws SQLException {
    this.editor = new EMapEditor();
  }

  @FXML
  public void initialize() throws IOException, SQLException {
    initNavBar();
    hoverHelp();
    initButtons();
    initStateBtn();
    PathFinding.ASTAR.init_pathfinder();
    moveMap = new MoveMap(); // Create move map
    fullNodesList = Repository.getRepository().getAllFullNodes();

    this.stackPaneMapView = new StackPane(); // no longer @FXML
    // Used for nodes
    this.locationCanvas = new Pane();

    // Used for location names
    this.fullNodeCanvas = new Pane();

    this.pane.setContent(stackPaneMapView);
    this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML

    //Establishing everything that must occur in the stackpane
    this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
    this.stackPaneMapView.getChildren().add(this.locationCanvas);

    this.locationCanvas.getChildren().add(nodeGroup);
    this.locationCanvas.getChildren().add(edgeGroup);
    this.locationCanvas.getChildren().add(nameGroup);
    this.locationCanvas.getChildren().add(moveMap.getPathGroup());
    this.locationCanvas.getChildren().add(moveMap.getMoveInfo());
    moveMap.getPathGroup().setVisible(false);
    moveMap.getMoveInfo().setVisible(false);

    //Fitting the scrollpane
    pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);


    draw(currentFloor);

    changeButtonColor(currentFloor);
    Platform.runLater(() -> this.pane.centreOn(new Point2D(2190, 910)));


    FileInputStream inputNode = new FileInputStream("src/main/resources/edu/wpi/teamb/img/icons/node.png");
    FileInputStream inputEdge = new FileInputStream("src/main/resources/edu/wpi/teamb/img/icons/edge.png");
    FileInputStream inputTools = new FileInputStream("src/main/resources/edu/wpi/teamb/img/icons/tools.png");
    FileInputStream inputMove = new FileInputStream("src/main/resources/edu/wpi/teamb/img/icons/move.png");
    FileInputStream inputReset = new FileInputStream("src/main/resources/edu/wpi/teamb/img/icons/reset.png");

    Image imageNode = new Image(inputNode);
    Image imageEdge = new Image(inputEdge);
    Image imageTools = new Image(inputTools);
    Image imageMove = new Image(inputMove);
    Image imageReset = new Image(inputReset);

    ImageView imageViewNode = new ImageView(imageNode);
    ImageView imageViewEdge = new ImageView(imageEdge);
    ImageView imageViewTools = new ImageView(imageTools);
    ImageView imageViewMove = new ImageView(imageMove);
    ImageView imageViewReset = new ImageView(imageReset);

    btnMenuNode.setGraphic(imageViewNode);
    btnMenuEdge.setGraphic(imageViewEdge);
    btnMenuTools.setGraphic(imageViewTools);
    btnMenuMove.setGraphic(imageViewMove);
    btnMenuBackup.setGraphic(imageViewReset);
    btnAlignNodes.setVisible(false);
    btnSubmitMove.setVisible(false);
    dateToMove.setVisible(false);
    btnFindPath.setVisible(false);

    initializeNavGates();

    editNodeMenuController = new EditNodeMenuController();
    addNodeMenuController = new AddNodeMenuController();

    editNodeMenuController.setMapEditorController(this);
    addNodeMenuController.setMapEditorController(this);

    System.out.println("MapEditorController initialized");
  }

  /**
   * Handles the alignment of nodes
   */
  private void alignNodes() {
    if (mapEditorContext.getState() == alignNodesState) {
      // Iterate through the selected nodes
      edgeGroup.getChildren().clear();
      for (Circle c : nodesToAlign) {
        FullNode n = Repository.getRepository().getFullNode(Integer.parseInt(c.getId())); // get the node
        n.setyCoord(startAndEndPoints(n.getxCoord()));  // Set the y coordinate
        Repository.getRepository().updateFullNode(n); // Update the node
      }
      btnAlignNodes.setVisible(false);
      nodesToAlign.clear(); // Clear the selected nodes list
      mapEditorContext.setState(new ViewState());
      System.out.println("Aligning all selected nodes");
      refreshMap();
    }
  }


  /**
   * Determines the state we are in and changes the text field accordingly
   */
  void determineState() {
    if (mapEditorContext.getState() == addEdgeState) {
      System.out.println("Adding edge");
      tfState.setText("Adding Edge");
    } else if (mapEditorContext.getState() == addNodeState) {
      System.out.println("Adding Node");
      tfState.setText("Adding Node");
    } else if (mapEditorContext.getState() == deleteEdgeState) {
      System.out.println("Deleting edge");
      tfState.setText("Delete Edge");
    } else if (mapEditorContext.getState() == deleteNodeState) {
      System.out.println("Deleting node");
      tfState.setText("Deleting Node");
    } else if (mapEditorContext.getState() == editNodeState) {
      System.out.println("Editing node");
      tfState.setText("Editing Node");
    } else if (mapEditorContext.getState() == addMoveState) {
      System.out.println("Adding Move");
      tfState.setText("Adding Move");
    } else if (mapEditorContext.getState() == alignNodesState) {
      System.out.println("Selecting nodes");
      tfState.setText("Selecting Nodes");
    }
    else
      tfState.setText("Viewing");
  }


  /**
   * Draws all the nodes on the map
   *
   * @param floor
   * @throws SQLException
   */
  public void draw(String floor) {
    System.out.println("Clearing the children");
    this.nodeGroup.getChildren().clear();
    this.nameGroup.getChildren().clear();
    this.edgeGroup.getChildren().clear();
    int count = 0;
    // For each node, create a circle
    for (FullNode n : fullNodesList) {
      if (n.getFloor().equals(floor)) {
        drawNode(n);
        drawEdge(n);
        drawName(n);
        count++;
      }
    }
    System.out.println("Number of nodes redrawn: " + count + " " + nodeGroup.getChildren().size());
    System.out.println("Drawing the edges, names, and nodes for floor " + floor);
    nodeGroup.toFront();
  }

  /**
   * Draws the edges on the map
   *
   * @param n Node to draw edges for
   */
  public void drawEdge(FullNode n) {
    //Gets the full node of the current node, as well as the neighbors of this node
    ArrayList<Integer> neighbors = PathFinding.ASTAR.get_node_map().get(n.getNodeID()).getNeighborIds();
    if (neighbors != null) {
      for (int i = 0; i < neighbors.size(); i++) {

        //Get the full node of the neighbor, check to see if they're both elevators or stairs
        Node neighborNode = PathFinding.ASTAR.get_node_map().get(neighbors.get(i));

        //Draws the lines of the neighbors on the current floor
        //This gets redone every floor which is slightly unoptimized but this should never span enough
        //To make this a large issue
        if (neighborNode.getFloor().equals(n.getFloor())) {
          Line line = new Line(n.getxCoord(), n.getyCoord(), neighborNode.getxCoord(), neighborNode.getyCoord());
          line.setStrokeWidth(4);
          line.setId(neighborNode.getNodeID() + "_" + n.getNodeID());
          line.setOnMouseClicked(event -> handleDeleteEdge(event, line));
          edgeGroup.getChildren().add(line);

        }
      }
    }
    edgeGroup.toFront();
    nodeGroup.toFront();
  }

  /**
   * Draws a node on the map
   *
   * @param n The node to be drawn
   */
  public void drawNode(FullNode n) {
    // Create the circle
    Circle c = new Circle(n.getxCoord(), n.getyCoord(), 5, RED);
    c.setId(String.valueOf(n.getNodeID())); // Set the circle's ID to the node's ID
    // change the color of a circle when hovered
    c.setOnMouseEntered(event -> {
      c.setFill(Color.GREEN);
      c.setRadius(10);
    });
    c.setOnMouseExited(event -> {
      c.setFill(Color.RED);
      c.setRadius(5);
    });
    c.setOnMouseClicked(event -> {
      try {
        selectedNodes.add(c); // Used for creating edges and moves
        nodesToAlign.add(c); // Used for aligning nodes
        checkSelectedNodes(); // Check if two circles are selected to create an edge
        checkNodesToAlign(); // Check if at least two circles are selected to align

        // Set node click event handlers
        if (mapEditorContext.getState() == editNodeState) {
          handleEditNode(n);
        }
        if (mapEditorContext.getState() == deleteNodeState) {
          handleDeleteNode(event, n);
        }
        System.out.println("Node " + n.getNodeID() + " clicked");
      } catch (SQLException | IOException e) {
        throw new RuntimeException(e);
      }
    }); // Set the handler for clicking on a node


    // Add the circle to the nodeGroup
    floorList.add(n);
    nodeGroup.getChildren().add(c);
    nodeGroup.toFront();
  }


  /**
   * Checks if at least 2 nodes are selected to align
   */
  private void checkNodesToAlign() {
    List<Integer> xCoords = new ArrayList<>();
    List<Integer> yCoords = new ArrayList<>();

    // Populate the list
    if (nodesToAlign.size() > 2) {
      for (Circle c : nodesToAlign) {
        xCoords.add((int) c.getCenterX());
        yCoords.add((int) c.getCenterY());
        System.out.println(c);
      }
      System.out.println(nodesToAlign.size());
    }

    // Assign new Y coordinates
    if (mapEditorContext.getState() == alignNodesState) {
      calcBestFit(xCoords, yCoords);  // Calculate the best fit
      btnAlignNodes.setVisible(true);
    }
  }
  private void setDefaultPosition() {
    List<Integer> xCoords = new ArrayList<>();
    List<Integer> yCoords = new ArrayList<>();

    // Populate the list
    if (nodesToAlign.size() > 2) {
      for (Circle c : nodesToAlign) {
        xCoords.add((int) c.getCenterX());
        yCoords.add((int) c.getCenterY());
        System.out.println(c);
      }
      System.out.println(nodesToAlign.size());
    }

    // Assign new Y coordinates
    if (mapEditorContext.getState() == alignNodesState) {
      calcBestFit(xCoords, yCoords);  // Calculate the best fit
      btnAlignNodes.setVisible(true);
    }
  }

  private int startAndEndPoints(int x) {
    return (int) (m * x + c);
  }


  /**
   * Function to calculate the best fit line
   *
   * @param x
   * @param y
   */
  private void calcBestFit(List<Integer> x, List<Integer> y) {
    int n = x.size();
    double sum_x = 0, sum_y = 0,
            sum_xy = 0, sum_x2 = 0;
    for (int i = 0; i < n; i++) {
      sum_x += x.get(i);
      sum_y += y.get(i);
      sum_xy += x.get(i) * y.get(i);
      sum_x2 += Math.pow(x.get(i), 2);
    }

    m = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - Math.pow(sum_x, 2));
    c = (sum_y - m * sum_x) / n;
  }


  /**
   * Method to check if two nodes are selected and if so, add an edge between them (in the add edge state)
   */
  private void checkSelectedNodes() {
    if (selectedNodes.size() == 2) {
      Iterator<Circle> iterator = selectedNodes.iterator();
      c1 = iterator.next();
      c2 = iterator.next();
      System.out.println(c1.toString());
      System.out.println(c2.toString());
      selectedNodes.clear();
      if (mapEditorContext.getState() == addEdgeState) {
        handleAddEdge(c1, c2);
      }
      else if (mapEditorContext.getState() == addMoveState) {
        handleAddMove(c1,c2);
      }
    }
  }

  /**
   * Draws the location names on the map
   */
  void drawName(FullNode n) {
    for (FullNode fn : fullNodesList) {
      if (!Objects.equals(fn.getNodeType(), "HALL")) {
        if (fn.getNodeID() == n.getNodeID()) {
          Text name = new Text(fn.getShortName());
          name.setX(n.getxCoord() + 5);
          name.setY(n.getyCoord() + 5);
          nameGroup.getChildren().add(name);
        }
      }
    }
  }


  /**
   * Handles the reset from backup button click
   */
  void handleResetFromBackupBtn() {
    Repository.getRepository().resetNodesFromBackup();
    //nodeList = Repository.getRepository().getAllNodes();
    fullNodesList = Repository.getRepository().getAllFullNodes();
    submissionAlert("Reset from backup successful");
    // Refresh the map
    refreshMap();
  }

  /**
   * Gets the maximum ID of the list of nodes
   */
  public int getMaxID() {  // Get the max ID of the list of nodes
    int maxID = 0;
    for (FullNode n : fullNodesList) {
      if (n.getNodeID() > maxID) {
        maxID = n.getNodeID();
      }
    }
    return maxID;
  }

  /**
   * Refreshes the map
   */
  void refreshMap() {
    selectedNodes.clear();
    nodesToAlign.clear();
    mapEditorContext.setState(new ViewState());
    determineState();
    // Clear the map
    nodeGroup.getChildren().clear();
    edgeGroup.getChildren().clear();
    nameGroup.getChildren().clear();
    PathFinding.ASTAR.force_init();
    fullNodesList = Repository.getRepository().getAllFullNodes();
    draw(currentFloor);
    drawMoveMap(currentFloor);
    System.out.println("Refreshing map for floor " + currentFloor + "...");
  }

  private void handleAddNode() {
    stackPaneMapView.setOnMouseClicked(this::tapToAddNode);
    System.out.println("Added tapToAddNode event handler");
  }

  private void tapToAddNode(MouseEvent e) {
    if (mapEditorContext.getState() == addNodeState) {
      // Method to allow for double click to add a new node
      try {
        Node n = new Node();
        n.setxCoord((int) e.getX());
        n.setyCoord((int) e.getY());
        n.setFloor(currentFloor);
        addNodeMenuController.setCurrentFloor(currentFloor);
        n.setNodeID(getMaxID() + 5);
        showAddNodeMenu(n);
        mapEditorContext.setState(new ViewState());
        determineState();
        editingNode = false;
        System.out.println("Added a node at " + e.getX() + ", " + e.getY());
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  private void showAddNodeMenu(Node n) throws IOException {
    Parent root;
    addNodeMenuController.setCurrentNode(n);
    root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/mapeditor/AddNodeMenu.fxml")));
    Stage stage = new Stage();
    stage.setTitle("Add Node");
    stage.setScene(new Scene(root, 400, 600));
    stage.show();
  }

  private void showEditNodeMenu(FullNode n) throws IOException {
    Parent root;
    editNodeMenuController.setCurrentNode(n);
    root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/mapeditor/EditNodeMenu.fxml")));
    Stage stage = new Stage();
    stage.setTitle("Edit Node");
    stage.setScene(new Scene(root, 400, 600));
    stage.show();
  }

  /**
   * Handles the delete node event
   *
   * @param e
   * @throws SQLException
   */
  private void handleDeleteNode(MouseEvent e, FullNode n) throws SQLException {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Node");
    alert.setContentText("Are you sure you want to delete this node?");
    Optional <ButtonType> action = alert.showAndWait();
    if (action.get() == ButtonType.OK) {
      // Get the node ID from the circle's ID
      int nodeID = n.getNodeID();
      // delete from move table
      Repository.getRepository().deleteMove(Repository.getRepository().getMove(nodeID));
      // Delete the node from the database
      Repository.getRepository().deleteNode(Repository.getRepository().getNode(nodeID));
      PathFinding.ASTAR.get_node_map().remove(n.getNodeID());

      // Remove the node from the map
      nodeGroup.getChildren().remove(n);

      // remove node from list
      for (FullNode node : fullNodesList) {
        if (node.getNodeID() == nodeID) {
          fullNodesList.remove(node);
          break;
        }
      }

      submissionAlert("Node " + nodeID + " has been deleted.");
      refreshMap();
      System.out.println("Node: " + nodeID + " deleted");
    }
  }

  private void handleDeleteEdge(MouseEvent e, Line l) {
    if (mapEditorContext.getState() == deleteEdgeState) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Delete Edge");
      alert.setContentText("Are you sure you want to delete this edge?");
      Optional <ButtonType> action = alert.showAndWait();
      if (action.get() == ButtonType.OK) {
        // Delete the edge from the database
        Edge edge = Repository.getRepository().getEdge(l.getId());
        Repository.getRepository().deleteEdge(edge);

        ArrayList<Edge> edges = Repository.getRepository().getAllEdges();

        // Remove the edge from the map
        edgeGroup.getChildren().remove(l);

        submissionAlert("Edge " + l.getId() + " has been deleted.");
        refreshMap();

        System.out.println("Edge: " + l.getId() + " deleted");
      }
    }
  }


  /**
   * Handles the add edge event
   *
   * @param c1
   * @param c2
   */
  private void handleAddEdge(Circle c1, Circle c2) {
    if (c1 != null && c2 != null && mapEditorContext.getState() == addEdgeState) {
      // Generate the line between the two nodes
      Line line = new Line(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY());
      line.setStrokeWidth(4);
      line.setId(c1.getId() + "_" + c2.getId());
      edgeGroup.getChildren().add(line);
      System.out.println("Added Edge with ID = " + line.getId());

      // Add edge to the database
      Edge edge = new Edge();
      edge.setStartNodeID(Integer.parseInt(c1.getId()));
      edge.setEndNodeID(Integer.parseInt(c2.getId()));
      Repository.getRepository().addEdge(edge);
      refreshMap();
      submissionAlert("Edge added successfully!");
    }
  }

  /**
   * Handles the edit node event
   *
   * @param n
   * @throws SQLException
   */
  private void handleEditNode(FullNode n) throws SQLException, IOException {
    editingNode = true;
    // Get the node ID from the circle's ID
    int nodeID = n.getNodeID();

    // Allow click and drag of the Circle
    for (javafx.scene.Node c : nodeGroup.getChildren()) {
      if (c.getId().equals(String.valueOf(nodeID))) {
        makeDraggable((Circle) c);
        System.out.println("Node: " + nodeID + " is draggable");
      }
    }
    determineState();
  }



  /**
   * https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
   */
  private class Delta {
    public double x;
    public double y;
  }

  /**
   * Allow the user to drag the node
   * //https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
   *
   * @param node
   */
  private void makeDraggable(Circle node) {
    pane.setGestureEnabled(false);    // Disable gestures while dragging
    final Delta dragDelta = new Delta();
    node.setOnMouseEntered(me -> {
      if (!me.isPrimaryButtonDown()) {
        node.getScene().setCursor(Cursor.HAND);
      }
    });
    node.setOnMouseExited(me -> {
      if (!me.isPrimaryButtonDown()) {
        node.getScene().setCursor(Cursor.DEFAULT);
      }
    });
    node.setOnMousePressed(me -> {
      if (me.isPrimaryButtonDown()) {
        node.getScene().setCursor(Cursor.DEFAULT);
      }
      dragDelta.x = me.getX();
      dragDelta.y = me.getY();
      node.getScene().setCursor(Cursor.MOVE);
    });
    node.setOnMouseReleased(me -> {
      if (!me.isPrimaryButtonDown()) {
        node.getScene().setCursor(Cursor.DEFAULT);
      }
    });
    node.setOnMouseDragged(me -> {
      node.setLayoutX(node.getLayoutX() + me.getX() - dragDelta.x);
      node.setLayoutY(node.getLayoutY() + me.getY() - dragDelta.y);
    });
    node.setOnMouseClicked(me -> {
      if (me.getButton() == MouseButton.PRIMARY && me.getClickCount() == 2) {
        node.getScene().setCursor(Cursor.DEFAULT);
        FullNode n = Repository.getRepository().getFullNode(Integer.parseInt(node.getId()));

        System.out.println("true x: " + n.getxCoord() );
        System.out.println("drgged x: " + node.getLayoutX() );
          System.out.println("true y: " + n.getyCoord() );
            System.out.println("drgged y: " + node.getLayoutY() );
        n.setxCoord((int) (n.getxCoord() + node.getLayoutX()));
        n.setyCoord((int) (n.getyCoord() + node.getLayoutY()));
        try {
          showEditNodeMenu(n);
          // set the colors back
          node.setFill(Color.RED);
          node.setRadius(5);
          pane.gestureEnabledProperty().set(true);
          mapEditorContext.setState(new ViewState());
          determineState();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }


  public void initButtons() {
    clickFloorBtn();
    itemResetFromBackup.setOnAction(event -> {
      handleResetFromBackupBtn();
    });

    // initialize the toggles
    toggleEdges.setSelected(true);
    toggleNodes.setSelected(true);
    toggleLocationNames.setSelected(true);
    toggleMoves.setSelected(false);
    
    toggleLocationNames.setOnMouseClicked(event -> {
      handleToggleLocationNames();
    });
    toggleEdges.setOnMouseClicked(event -> {
      handleToggleEdges();
    });
    toggleNodes.setOnMouseClicked(event -> {
      handleToggleNodes();
    });
    toggleMoves.setOnMouseClicked(event -> {
      handleToggleMoves();
    });

    // Init new buttons
    btnAlignNodes.setOnMouseClicked(event -> alignNodes());
    btnSubmitMove.setOnMouseClicked(event -> handleSubmitMove());
    btnFindPath.setOnMouseClicked(event -> handleFindPath());
    btnPathfinder.setOnMouseClicked(event -> Navigation.navigate(Screen.PATHFINDER));
  }

  private void handleToggleMoves() {
    if (toggleMoves.isSelected()) {
      moveMap.getPathGroup().setVisible(true);
      moveMap.getMoveInfo().setVisible(true);
      System.out.println("Moves are on");
    } else {
      //nameGroup.setVisible(false);
      System.out.println("Moves are off");
      moveMap.getPathGroup().setVisible(false);
      moveMap.getMoveInfo().setVisible(false);
    }
  }

  /**
   * Method to allow you to find the path between the most recent move nodes
   */
  private void handleFindPath() {
    FullNode startNode = Repository.getRepository().getFullNode(Integer.parseInt(c1.getId()));
    FullNode endNode = Repository.getRepository().getFullNode(Integer.parseInt(c2.getId()));


    DefaultStart.getInstance().setDefault_start(startNode.getLongName()); //Whatever you want the start to be
    DefaultStart.getInstance().setDefault_end(endNode.getLongName()); //Whatever you want the end to be

    Navigation.navigate(Screen.PATHFINDER);
  }

  private void handleSubmitMove() {
    // Get the date from the date picker
    LocalDate date = dateToMove.getValue();

    // Get the nodes from the circle ids
    FullNode startNode = Repository.getRepository().getFullNode(Integer.parseInt(c1.getId()));
    FullNode endNode = Repository.getRepository().getFullNode(Integer.parseInt(c2.getId()));

    // Get the move data
    Move move = new Move();
    move.setLongName(startNode.getLongName()); // Location name of the start node
    move.setNodeID(endNode.getNodeID()); // ID of end node
    move.setDate(Date.valueOf(date));

    // Add the move to the database
    Repository.getRepository().addMove(move);

    // Add move to the local list
    moveMap.addToMoveMap(move);

    // Redraw the movemap
    drawMoveMap(currentFloor);

    submissionAlert("Move submitted successfully");

    // Hide the submit button and date picker
    btnSubmitMove.setVisible(false);
    dateToMove.setVisible(false);
    btnFindPath.setVisible(true);

    // Refresh the map
    refreshMap();

  }

  private void handleAddMove(Circle c1, Circle c2) {
    btnSubmitMove.setVisible(true);
    dateToMove.setVisible(true);
    dateToMove.setValue(LocalDate.now());
    dateToMove.setDayCellFactory(picker -> new DateCell() {
      public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);
        LocalDate today = LocalDate.now();

        setDisable(empty || date.compareTo(today) < 0 );
      }
    });
  }

  /**
   * Toggles the ability to see location names on the maps
   */
  public void handleToggleLocationNames() {
    if (toggleLocationNames.isSelected()) {
      nameGroup.setVisible(true);
      System.out.println("Location names on");
    } else {
      nameGroup.setVisible(false);
      System.out.println("Location names off");
      toggleLocationNames.setSelected(false);
    }
  }

  /**
   * Toggles the ability to see nodes on the maps
   */
  public void handleToggleNodes() {
    if (toggleNodes.isSelected()) {
      nodeGroup.setVisible(true);
      System.out.println("Nodes on");
    } else {
      nodeGroup.setVisible(false);
      System.out.println("Nodes off");
      toggleNodes.setSelected(false);
    }
  }

  /**
   * Toggles the ability to see edges on the maps
   */
  public void handleToggleEdges() {
    if (toggleEdges.isSelected()) {
      edgeGroup.setVisible(true);
      System.out.println("Edges on");
    } else {
      edgeGroup.setVisible(false);
      System.out.println("Edges off");
      toggleEdges.setSelected(false);
    }
  }

  public void clickFloorBtn() {
    btnL1.setTooltip(new Tooltip("Lower Level 1"));
    btnL1.setOnMouseClicked(event -> {
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(0));
      currentFloor = "L1";
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getFullNodesByFloor("L1");
      edgeGroup.getChildren().clear();
      nodeGroup.getChildren().clear();
      draw("L1");
      drawMoveMap(currentFloor);
    });
    btnL2.setTooltip(new Tooltip("Lower Level 2"));
    btnL2.setOnMouseClicked(event -> {
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(1));
      currentFloor = "L2";
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getFullNodesByFloor("L2");
      edgeGroup.getChildren().clear();
      nodeGroup.getChildren().clear();
      draw("L2");
      drawMoveMap(currentFloor);
    });
    btn1.setTooltip(new Tooltip("Level 1"));
    btn1.setOnMouseClicked(event -> {
      currentFloor = "1";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(3));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getFullNodesByFloor("1");
      edgeGroup.getChildren().clear();
      nodeGroup.getChildren().clear();
      draw("1");
      drawMoveMap(currentFloor);
    });
    btn2.setTooltip(new Tooltip("Level 2"));
    btn2.setOnMouseClicked(event -> {
      currentFloor = "2";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(4));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getFullNodesByFloor("2");
      edgeGroup.getChildren().clear();
      nodeGroup.getChildren().clear();
      draw("2");
      drawMoveMap(currentFloor);
    });
    btn3.setTooltip(new Tooltip("Level 3"));
    btn3.setOnMouseClicked(event -> {
      currentFloor = "3";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(5));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getFullNodesByFloor("3");
      edgeGroup.getChildren().clear();
      nodeGroup.getChildren().clear();
      draw("3");
      drawMoveMap(currentFloor);
    });
  }

  private void drawMoveMap(String currentFloor) {
    locationCanvas.getChildren().remove(moveMap.getPathGroup());
    moveMap.getPathGroup().getChildren().clear();
    moveMap.displayMoves(currentFloor);
    locationCanvas.getChildren().add(moveMap.getPathGroup());
  }
  private void setMenuItemTooltip(MenuButton b, CustomMenuItem c, String text, String toolTipText) {
    // change the color of the custom menu item
    Text text1 = new Text(text);
    Tooltip tooltip = new Tooltip(toolTipText);
    c.setContent(text1);
    Tooltip.install(c.getContent(), tooltip);
    // Set text properties
    text1.setWrappingWidth(112);
    text1.setX(10.0);
    text1.setY(25.0);
    b.getItems().addAll(c);
  }

  public void initStateBtn() {
    // Init New State Buttons
    setMenuItemTooltip(btnMenuNode, btnAddNode, "Add Node", "Click on the map where you would like to add a node");
    setMenuItemTooltip(btnMenuNode, btnDeleteNode, "Delete Node", "Click a node on the map to delete it");
    setMenuItemTooltip(btnMenuNode, btnEditNode, "Edit Node", "Click a node on the map to edit it");

    setMenuItemTooltip(btnMenuEdge, itemAddEdge, "Add Edge", "Click two nodes on the map to add an edge");
    setMenuItemTooltip(btnMenuEdge, itemDeleteEdge, "Delete Edge", "Click an edge on the map to delete it");

    setMenuItemTooltip(btnMenuTools, itemAlign, "Align Nodes", "Click on the map where you would like to add a node");
    setMenuItemTooltip(btnMenuTools, itemSetDefault, "Set Default Position", "Click a node on the map to set a default location");

    setMenuItemTooltip(btnMenuBackup, itemResetFromBackup, "Reset from Backup", "Click to reset the nodes/edges from the database");

    setMenuItemTooltip(btnMenuMove, itemAddMove, "Add Move", "Click a node you'd like to move, then a node where it should move to");

    itemAddEdge.setOnAction(event -> {
      mapEditorContext.setState(addEdgeState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
    btnAddNode.setOnAction(event -> {
      mapEditorContext.setState(addNodeState);
      mapEditorContext.getState().printStatus();
      handleAddNode();
      determineState();
    });
    itemDeleteEdge.setOnAction(event -> {
      mapEditorContext.setState(deleteEdgeState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
    btnDeleteNode.setOnAction(event -> {
      mapEditorContext.setState(deleteNodeState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
    btnEditNode.setOnAction(event -> {
      mapEditorContext.setState(editNodeState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
    itemAddMove.setOnAction(event -> {
      mapEditorContext.setState(addMoveState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
    itemAlign.setOnAction(event -> {
      mapEditorContext.setState(alignNodesState);
      mapEditorContext.getState().printStatus();
      determineState();
    });
  }


  /**
   * Changes the color of the buttons to indicate which floor is currently being viewed
   *
   * @param currentFloor
   */
  private void changeButtonColor(String currentFloor) {
    switch (currentFloor) {
      case "L1" -> {
        btnL1.setStyle("-fx-background-color: #f6bd38");
        btnL2.setStyle("-fx-background-color: #012d5a");
        btn1.setStyle("-fx-background-color: #012d5a");
        btn2.setStyle("-fx-background-color: #012d5a");
        btn3.setStyle("-fx-background-color: #012d5a");
      }
      case "L2" -> {
        btnL1.setStyle("-fx-background-color: #012d5a");
        btnL2.setStyle("-fx-background-color: #f6bd38");
        btn1.setStyle("-fx-background-color: #012d5a");
        btn2.setStyle("-fx-background-color: #012d5a");
        btn3.setStyle("-fx-background-color: #012d5a");
      }
      case "1" -> {
        btnL1.setStyle("-fx-background-color: #012d5a");
        btnL2.setStyle("-fx-background-color: #012d5a");
        btn1.setStyle("-fx-background-color: #f6bd38");
        btn2.setStyle("-fx-background-color: #012d5a");
        btn3.setStyle("-fx-background-color: #012d5a");
      }
      case "2" -> {
        btnL1.setStyle("-fx-background-color: #012d5a");
        btnL2.setStyle("-fx-background-color: #012d5a");
        btn1.setStyle("-fx-background-color: #012d5a");
        btn2.setStyle("-fx-background-color: #f6bd38");
        btn3.setStyle("-fx-background-color: #012d5a");
      }
      case "3" -> {
        btnL1.setStyle("-fx-background-color: #012d5a");
        btnL2.setStyle("-fx-background-color: #012d5a");
        btn1.setStyle("-fx-background-color: #012d5a");
        btn2.setStyle("-fx-background-color: #012d5a");
        btn3.setStyle("-fx-background-color: #f6bd38");
      }
    }
  }

  @FXML
  public void hoverHelp() {
    helpIcon.setOnMouseClicked(
            event -> {
              final FXMLLoader popupLoader = new FXMLLoader(Bapp.class.getResource("views/components/popovers/MapEditorHelpPopOver.fxml"));
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
    helpIcon.setOnMouseExited(event -> {
    });
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

  /**
   * Utilizes a gate to swap between handling the navdrawer and the rest of the page
   * Swaps ownership of the strip to the navdraw
   */
  public void initNavBar() {
    // https://github.com/afsalashyana/JavaFX-Tutorial-Codes/tree/master/JavaFX%20Navigation%20Drawer/src/genuinecoder
    try {
      FXMLLoader loader =
              new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/NavDrawer.fxml"));
      VBox vbox = loader.load();
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
                vboxActivateNav1.toFront();
              } else {
                menuDrawer.toFront();
                menuBurger.toFront();
                menuDrawer.open();
              }
            });
  }

  void submissionAlert(String message) {
    // Create an alert
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Submission Successful");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}