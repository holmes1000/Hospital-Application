package edu.wpi.teamb.controllers.mapeditor;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DBoutput;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.entities.EMapEditor;
import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.controls.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import static javafx.scene.paint.Color.RED;

public class MapEditorController {
  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private ImageView helpIcon;
  @FXML private MFXComboBox<String> NodeSelector;
  @FXML private MFXListView NodeInfo;
  @FXML private VBox VboxNodes;
  @FXML private ImageView imageViewPathfinder;
  @FXML private StackPane stackPaneMapView;

  private EMapEditor editor;
  @FXML private MFXButton btnL1;
  @FXML private MFXButton btnL2;
  @FXML private MFXButton btn1;
  @FXML private MFXButton btn2;
  @FXML private MFXButton btn3;

  @FXML private MFXButton btnNode;
  @FXML private MFXButton btnEdge;
  @FXML private MFXButton btnAlign;
  @FXML private MFXTextField tfState;

  @FXML private MFXButton uploadBtn;
  @FXML private MFXButton exportBtn;
  @FXML private MFXToggleButton toggleNodes;
  @FXML private MFXToggleButton toggleLocationNames;
  @FXML private MFXToggleButton toggleEdges;
  @FXML private FileChooser fileChooser;
  @FXML private MFXButton resetFromBackupBtn;

  //Objects that get superimposed

  public GesturePane pane = new GesturePane();
  Group nodeGroup;
  Group edgeGroup;
  Group nameGroup;
  Pane locationCanvas;
  Pane fullNodeCanvas;
  private ArrayList<Node> nodeList = new ArrayList<>();
  private ArrayList<FullNode> fullNodesList = new ArrayList<>();
    private Node nodeL1;
    private  Node nodeL2;
    private Node node1;
    private  Node node2;
    private Node node3;

  private ArrayList<Node> floorList = new ArrayList<>();

  //Other misc items
  private String currentFloor = "1";
  private ArrayList<LocationName> locationNameList = new ArrayList<>();
  @FXML private VBox vboxBtns;
@FXML private VBox vboxAddNode;
  private ArrayList<Node> floorNodes = new ArrayList<>();
  public boolean boolEditingNode = false;
  public boolean boolSubmittedDetails = false;
  private Pane menuPane;
  private boolean editingNode = false; // Used for the submitting details button

  @FXML private MFXButton btnAdd;
  @FXML private MFXButton btnEdit;
  @FXML private MFXButton btnDelete;
  @FXML private MFXButton btnView;
  @FXML private MFXComboBox<String> cbNodeType;
  @FXML private MFXTextField tfLongName;
  @FXML private MFXTextField tfShortName;
  @FXML private MFXButton btnSubmitNodeDetails;
  @FXML private MFXTextField tfNodeId;
  private boolean boolAddingNode = false;

  // Create the states
  MapEditorContext mapEditorContext = new MapEditorContext();
  MapEditorState editState = new EditState();
  MapEditorState deleteState = new DeleteState();
  MapEditorState addState = new AddState();
  MapEditorState viewState = new ViewState();
  private boolean handlingNodes = false;
  private boolean handlingEdges = false;
  private int newX;
  private int newY;

  int fullNodeX;
  int fullNodeY;
  public MapEditorController() throws SQLException {
    this.editor = new EMapEditor();
  }

  @FXML
  public void initialize() throws IOException, SQLException {
    initNavBar();
    hoverHelp();
    initializeFields();
    initButtons();
    initStateBtn();
    PathFinding.ASTAR.init_pathfinder();
    // Initialize the edges, nodes, and names on the map
    nodeList = Repository.getRepository().getAllNodes();
    fullNodesList = Repository.getRepository().getAllFullNodes();
//    fullNodes = Repository.getRepository().getFullNodes();

    this.stackPaneMapView = new StackPane(); // no longer @FXML
    // Used for nodes
    this.locationCanvas = new Pane();

    this.nodeGroup = new Group();
    this.nameGroup = new Group();
    this.edgeGroup = new Group();

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

    //Fitting the scrollpane
    pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);


    btnSubmitNodeDetails.setOnMouseClicked(event -> handleSubmitNodeDetails());
    draw( currentFloor);

    changeButtonColor(currentFloor);
    Platform.runLater(() -> this.pane.centreOn(new Point2D(2190, 910)));

    System.out.println("MapEditorController initialized");
  }

  private void handleSubmitNodeDetails() {
    createNode();
  }

  private void handleNodes() {
    System.out.println("Handling nodes");
    btnNode.setStyle("-fx-background-color: #f6bd38");
    btnEdge.setStyle("-fx-background-color: #1C4EFE");
    handlingNodes = true;
    handlingEdges = false;
    determineState();
  }

  private void handleEdges() {
    System.out.println("Handling edges");
    btnEdge.setStyle("-fx-background-color: #f6bd38");
    btnNode.setStyle("-fx-background-color: #1C4EFE");
    handlingEdges = true;
    handlingNodes = false;
    determineState();
  }

  private void handleAlign() {
    System.out.println("Handling align");
  }

  private void determineState() {
    if (mapEditorContext.getState() == addState && handlingEdges) {
      System.out.println("Adding edge");
      tfState.setText("Adding Edge");
    } else if (mapEditorContext.getState() == addState && handlingNodes) {
      System.out.println("Adding Node");
      tfState.setText("Adding Node");
    } else if (mapEditorContext.getState() == deleteState && handlingEdges) {
      System.out.println("Deleting edge");
      tfState.setText("Delete Edge");
    } else if (mapEditorContext.getState() == deleteState && handlingNodes) {
      System.out.println("Deleting node");
      tfState.setText("Deleting Node");
    } else if (mapEditorContext.getState() == editState && handlingEdges) {
      System.out.println("Editing edge");
      tfState.setText("Editing Edge");

    } else if (mapEditorContext.getState() == editState && handlingNodes) {
      System.out.println("Editing node");
      tfState.setText("Editing Node");
    } else if (mapEditorContext.getState() == viewState) {
      System.out.println("Viewing State");
      tfState.setText("Viewing State");
    }
  }

  private void createNode() {
      String shortName = tfShortName.getText();
      String longName = tfLongName.getText();
      String nodeType = cbNodeType.getValue();
      FullNode fullNode = null;

      // Get the max ID of the list of nodes
      int maxID = 0;
      for (Node n : nodeList) {
        if (n.getNodeID() > maxID) {
          maxID = n.getNodeID();
        }
      }

      fullNode = new FullNode(maxID+5, (int) fullNodeX, (int) fullNodeY, currentFloor, "Full Node Building", tfLongName.getText(), tfShortName.getText(), cbNodeType.getSelectedItem());
      Repository.getRepository().addFullNode(fullNode);

      Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
      nodeList.add(newNode); // Add the node to the nodeList

      // Add node to the database
      //Repository.getRepository().addNode(newNode);

      System.out.println("Adding a new node with nodeID: " + newNode.getNodeID());
      // Refresh the map
      refreshMap();

      // Reset the fields
      tfShortName.setText("");
      tfLongName.setText("");
      tfNodeId.setText("");
  }

  /**
   * Draws all the nodes on the map
   * @param floor
   * @throws SQLException
   */
  public void draw(String floor) throws SQLException {
    nodeGroup.getChildren().clear();
    nameGroup.getChildren().clear();
    edgeGroup.getChildren().clear();
    // For each node, create a circle
    for (Node n : nodeList) {
      if (n.getFloor().equals(floor)) {
        drawNode(n);
      }
    }
    nodeGroup.toFront();
  }

  //Draws edges for a single node, taking in a node
  //From node it grabs the neighbors from the repository and then creates the 1-3 lines required
  public void drawEdge(Node n){
//      edgeGroup.getChildren().clear();

      //Gets the full node of the current node, as well as the neighbors of this node
      ArrayList<Integer> neighbors = PathFinding.ASTAR.get_node_map().get(n.getNodeID()).getNeighborIds(); // TODO this breaks reset from backup

      for (int i = 0; i < neighbors.size(); i++) {

        //Get the full node of the neighbor, check to see if they're both elevators or stairs
        Node neighborNode = PathFinding.ASTAR.get_node_map().get(neighbors.get(i));

        //Draws the lines of the neighbors on the current floor
        //This gets redone every floor which is slightly unoptimized but this should never span enough
        //To make this a large issue
        if (neighborNode.getFloor().equals(n.getFloor())) {
          Line line = new Line(n.getxCoord(), n.getyCoord(), neighborNode.getxCoord(), neighborNode.getyCoord());
          line.setStrokeWidth(4);
          line.setOnMouseClicked(event -> handleEdgeClick(line));
          edgeGroup.getChildren().add(line);

      }
    }
    edgeGroup.toFront();
    nodeGroup.toFront();
  }

  //Toggles the ability to see location names
  //Takes in current ability to see them in relation to the bar
  //When on it adds the ability to generate them in the node drawing method
  //When off it replaces text with "bruh" and removed ability to see them by adding delay.
  public void handleToggleLocationNames(){
      if(toggleLocationNames.isSelected()){
        nameGroup.setVisible(true);
        System.out.println("Location names on");
      } else {
        nameGroup.setVisible(false);
        System.out.println("Location names off");
        toggleLocationNames.setSelected(false);
      }
  }

  public void handleToggleNodes(){
    if(toggleNodes.isSelected()){
      nodeGroup.setVisible(true);
      System.out.println("Nodes on");
    } else {
      nodeGroup.setVisible(false);
      System.out.println("Nodes off");
      toggleNodes.setSelected(false);
    }
  }

  public void handleToggleEdges(){
    if(toggleEdges.isSelected()){
      edgeGroup.setVisible(true);
      System.out.println("Edges on");
    } else {
      edgeGroup.setVisible(false);
      System.out.println("Edges off");
      toggleEdges.setSelected(false);
    }
  }

  /**
   * Draws a node on the map
   *
   * @param n The node to be drawn
   */
  public void drawNode(Node n) {
    // Create the circle
    Circle c = new Circle(n.getxCoord(), n.getyCoord(), 5, RED);
    c.setId(String.valueOf(n.getNodeID())); // Set the circle's ID to the node's ID
    c.setOnMouseClicked(event -> {
      try {
        this.handleNodeClick(event, n);
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }); // Set the handler for clicking on a node
    drawEdge(n);
    drawName(c, n);

    // Add the circle to the nodeGroup
    floorList.add(n);
    nodeGroup.getChildren().add(c);
    nodeGroup.toFront();
  }

  void drawName(Circle c, Node n) {
    for (FullNode fn : fullNodesList) {
      if (!Objects.equals(fn.getNodeType(), "HALL")) {
        if (fn.getNodeID() == n.getNodeID()) {
          Text name = new Text(fn.getShortName());
          name.setX(c.getCenterX() + 5);
          name.setY(c.getCenterY() + 5);
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
    nodeList = Repository.getRepository().getAllNodes();
    // Refresh the map
    refreshMap();
  }

  /**
   * Adds a node to the map
   * @param x coordinate
   * @param y coordinate
   * @throws SQLException
   */
  public void addNodeToMap(double x, double y) throws SQLException {
    // Get the max ID of the list of nodes
    int maxID = 0;
    for (Node n : nodeList) {
      if (n.getNodeID() > maxID) {
        maxID = n.getNodeID();
      }
    }

    tfNodeId.setText(String.valueOf(maxID + 5)); // Set the nodeID text field to the maxID + 5
  }


  /**
   * Refreshes the map
   */
  private void refreshMap() {
    // Clear the map
    nodeGroup.getChildren().clear();
    edgeGroup.getChildren().clear();
    // Redraw the map
    try {
      draw(currentFloor);
      System.out.println("Refreshing map for floor " + currentFloor + "...");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void handleEdgeClick(Line line) {
  }

  /**
   * Handles the node click event (creates a context menu)
   * @param e
   * @param n
   */
  private void handleNodeClick(MouseEvent e, Node n) throws SQLException {
    if (mapEditorContext.getState() == deleteState) {
      handleDeleteNode(e, n);
    }
    else if (mapEditorContext.getState() == addState) {
      //handleAddNode();
    }
    else if (mapEditorContext.getState() == editState) {
      handleEditNode(e, n);
    }
  }


  private void handleEditName(javafx.event.ActionEvent e, Node n) throws  SQLException {
   int nodeID = n.getNodeID();
   FullNode fullNode = Repository.getRepository().getFullNode(nodeID);
   System.out.println(fullNode.getShortName() + "\n" + fullNode.getLongName());
  }

  private void handleAddNode() {
    // Method to allow for triple click to add a new node
    stackPaneMapView.setOnMouseClicked(e -> {
      if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
        try {
          editingNode = false;
          fullNodeX = (int) e.getX();
          fullNodeY = (int) e.getY();
          addNodeToMap(e.getX(), e.getY());   // get the X and Y of the cursor
          System.out.println("Added a node at " + e.getX() + ", " + e.getY());
        } catch (SQLException ex) {
          throw new RuntimeException(ex);
        }
      }
    });
  }
  /**
   * Handles the delete node event
   * @param e
   * @throws SQLException
   */
  private void handleDeleteNode(MouseEvent e, Node n) throws SQLException {
    // Get the node ID from the circle's ID
    int nodeID = n.getNodeID();
    // delete from move table
    Repository.getRepository().deleteMove(Repository.getRepository().getMove(nodeID));
    // Delete the node from the database
    Repository.getRepository().deleteNode(Repository.getRepository().getNode(nodeID));

    // Remove the node from the map
    nodeGroup.getChildren().remove(n);

    // remove node from list
    for (Node node : nodeList) {
      if (node.getNodeID() == nodeID) {
        nodeList.remove(node);
        break;
      }
    }

    refreshMap();
    System.out.println("Node: " + nodeID + " deleted");
  }

  /**
   * Handles the edit node event
   * @param e
   * @param n
   * @throws SQLException
   */
  private void handleEditNode(MouseEvent e, Node n) throws SQLException {
    editingNode = true;
    // Get the node ID from the circle's ID
    int nodeID = n.getNodeID();
    tfNodeId.setText(String.valueOf(nodeID));     // set the items id in the menu

    // Get the node from the database
    Node newNode = Repository.getRepository().getNode(nodeID);
    FullNode newFullNode = Repository.getRepository().getFullNode(nodeID);

    // set the fields with the full node
    tfShortName.setText(newFullNode.getShortName());
    tfLongName.setText(newFullNode.getLongName());
    cbNodeType.setValue(newFullNode.getNodeType());

    // Allow click and drag of the Circle
    for (javafx.scene.Node c: nodeGroup.getChildren()) {
      if (c.getId().equals(String.valueOf(nodeID))) {
        makeDraggable((Circle) c);
        boolEditingNode = true;
      }
      if (boolEditingNode) {
        //System.out.println("Editing node: " + nodeID);
        stackPaneMapView.setOnMouseClicked(event -> {
          if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            pane.gestureEnabledProperty().set(true);
            // Update the node's location
            newNode.setxCoord((int) (event.getX()));
            newNode.setyCoord((int) (event.getY()));
            System.out.println("Node: " + nodeID + " edited");
            System.out.println("Location: " + newNode.getxCoord() + ", " + newNode.getyCoord());
            boolEditingNode = false;
            // Update the node in the database
            Repository.getRepository().updateNode(newNode);
          }
        });
      }
    }


  }


  /**
   * https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
   */
  private class Delta {
    public double x;
    public double y;
  }

  /**
   *  Allow the user to drag the node
   *  //https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0
   * @param node
   */
  private void makeDraggable(Circle node) {
    boolEditingNode = true;
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
  }


  public void initButtons() {
    clickFloorBtn();
    uploadBtn.setOnMouseClicked(event->{handleUploadBtn();});
    exportBtn.setOnMouseClicked(event->{handleExportBtn();});
    resetFromBackupBtn.setOnMouseClicked(event->{handleResetFromBackupBtn();});

    // initialize the toggles
    toggleEdges.setSelected(true);
    toggleNodes.setSelected(true);
    toggleLocationNames.setSelected(true);
    toggleLocationNames.setOnMouseClicked(event->{handleToggleLocationNames();});
    toggleEdges.setOnMouseClicked(event->{handleToggleEdges();});
    toggleNodes.setOnMouseClicked(event->{handleToggleNodes();});

    btnNode.setOnMouseClicked(event -> handleNodes());
    btnEdge.setOnMouseClicked(event -> handleEdges());
    btnAlign.setOnMouseClicked(event -> handleAlign());
  }

  public void clickFloorBtn() {
    btnL1.setOnMouseClicked(event->{
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(0));
      currentFloor = "L1";
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("L1");
      try {
        edgeGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        draw( "L1");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    btnL2.setOnMouseClicked(event->{
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(1));
      currentFloor = "L2";
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("L2");

      try {
        edgeGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        draw( "L2");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    btn1.setOnMouseClicked(event->{
      currentFloor = "1";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(3));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("1");
      try {
        edgeGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        draw( "1");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    btn2.setOnMouseClicked(event->{
      currentFloor = "2";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(4));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("2");
      try {
        edgeGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        draw( "2");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
    btn3.setOnMouseClicked(event->{
      currentFloor = "3";
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(5));
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("3");
      try {
        edgeGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        draw( "3");
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void initStateBtn() {
    btnView.setOnMouseClicked(event->{
      mapEditorContext.setState(viewState);
      mapEditorContext.getState().printStatus();
      changeStateButtonColor("View");
    } );
    btnEdit.setOnMouseClicked(event->{
      mapEditorContext.setState(editState);
      mapEditorContext.getState().printStatus();
      changeStateButtonColor("Edit");
    } );
    btnAdd.setOnMouseClicked(event->{
      mapEditorContext.setState(addState);
      mapEditorContext.getState().printStatus();
      changeStateButtonColor("Add");
    } );
    btnDelete.setOnMouseClicked(event->{
      mapEditorContext.setState(deleteState);
      mapEditorContext.getState().printStatus();
      changeStateButtonColor("Delete");
    } );
  }

  /**
   * Handles the click event for the upload button
   */
  public void handleUploadBtn() {
    fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));
    File file = fileChooser.showOpenDialog(null);
    if (file != null) {
      System.out.println(file.getAbsolutePath());
    }
  }

  /**
   * Handles the click event for the export button
   */
  public void handleExportBtn() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDirectory = directoryChooser.showDialog(null);
    String absolutePath = null;
    String item = getSelectedItem();
    if (selectedDirectory != null) {
      switch (item) {
        case "Moves" -> absolutePath = selectedDirectory.getAbsolutePath() + "/movetest";
        case "Nodes" -> absolutePath = selectedDirectory.getAbsolutePath() + "/nodetest";
        case "Edges" -> absolutePath = selectedDirectory.getAbsolutePath() + "/edgetest";
        case "Location Names" -> absolutePath = selectedDirectory.getAbsolutePath() + "/locationnametest";
        default -> absolutePath = selectedDirectory.getAbsolutePath() + "/defaulttest";
      };
      switch (item) {
        case "Moves" -> DBoutput.exportMovesToCSV(absolutePath, 2);
        case "Nodes" -> DBoutput.exportNodesToCSV(absolutePath, 2);
        case "Edges" -> {
            DBoutput.exportEdgesToCSV(absolutePath, 2);
        }
        case "Location Names" -> {
            DBoutput.exportLocationNamesToCSV(absolutePath, 2);
        }
        default -> new ArrayList<>();
      };
    }
    System.out.println("here"); // 1 means success, 0 means failure
  }

  /**
   * Changes the color of the buttons to indicate which floor is currently being viewed
   * @param currentFloor
   */
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

  private void changeStateButtonColor(String state) {
    switch (state) {
      case "View"-> {
        btnView.setStyle("-fx-background-color: #f6bd38");
        btnEdit.setStyle("-fx-background-color: #1C4EFE");
        btnDelete.setStyle("-fx-background-color: #1C4EFE");
        btnAdd.setStyle("-fx-background-color: #1C4EFE");
      }
      case "Add" -> {
        btnAdd.setStyle("-fx-background-color: #f6bd38");
        btnEdit.setStyle("-fx-background-color: #1C4EFE");
        btnDelete.setStyle("-fx-background-color: #1C4EFE");
        btnView.setStyle("-fx-background-color: #1C4EFE");
      }
      case "Edit" -> {
        btnEdit.setStyle("-fx-background-color: #f6bd38");
        btnAdd.setStyle("-fx-background-color: #1C4EFE");
        btnDelete.setStyle("-fx-background-color: #1C4EFE");
        btnView.setStyle("-fx-background-color: #1C4EFE");
      }
      case "Delete" -> {
        btnDelete.setStyle("-fx-background-color: #f6bd38");
        btnEdit.setStyle("-fx-background-color: #1C4EFE");
        btnAdd.setStyle("-fx-background-color: #1C4EFE");
        btnView.setStyle("-fx-background-color: #1C4EFE");
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
    helpIcon.setOnMouseExited(event -> {});
  }

  private void initializeFields() {
    ArrayList<String> tables = new ArrayList<String>();
    tables.add("Nodes");
    tables.add("Edges");
    tables.add("Moves");
    tables.add("Location Names");
    NodeSelector.setItems(FXCollections.observableList(tables));

    // initialize cbNodeType options
    ObservableList<String> listOfNodeTypes = FXCollections.observableArrayList(Repository.getRepository().getNodeTypesUniqueAlphabetical());
    cbNodeType.setItems(listOfNodeTypes);
  }

  public void clickNodeSelector() {
    displaySelection();
  }

  public void displaySelection() {
    String item = getSelectedItem();
    ArrayList<?> itemsList = switch (item) {
      case "Moves" -> editor.getMoveList();
      case "Nodes" -> editor.getNodeList();
      case "Edges" -> editor.getEdgeList();
      case "Location Names" -> editor.getLocationNameList();
      default -> new ArrayList<>();
    };

    ObservableList<?> items = FXCollections.observableList(itemsList);
    NodeInfo.setItems(items);
    VboxNodes.getChildren().clear();
    VboxNodes.getChildren().addAll(NodeInfo);
  }

  public String getSelectedItem() {
    return NodeSelector.getSelectedItem();
  }

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
              } else {
                menuDrawer.open();
              }
            });
  }
}