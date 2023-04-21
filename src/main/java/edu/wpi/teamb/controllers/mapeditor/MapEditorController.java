package edu.wpi.teamb.controllers.mapeditor;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.DBio.DBoutput;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.LocationName;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.entities.ELogin;
import edu.wpi.teamb.entities.EMapEditor;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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

  @FXML private MFXButton uploadBtn;
  @FXML private MFXButton exportBtn;
  @FXML private MFXToggleButton toggleNodes;
  @FXML private MFXToggleButton toggleLocationNames;
  @FXML private FileChooser fileChooser;
  @FXML private MFXButton resetFromBackupBtn;

  //Objects that get superimposed

  public GesturePane pane = new GesturePane();
  Group nodeGroup;
  Group lineGroup;
  Group nameGroup;
  Pane locationCanvas;
  Pane fullNodeCanvas;

  //node contextMenu items
  private MenuItem locationItem;
  private MenuItem editLocationItem;
  private MenuItem viewConnectedEdges;
  private MenuItem deleteItem;
  private MenuItem editItem;

  private ArrayList<Node> nodeList = new ArrayList<>();
//  private ArrayList<FullNode> fullNodes = new ArrayList<>();

    private Node nodeL1;
    private  Node nodeL2;
    private Node node1;
    private  Node node2;
    private Node node3;

  private ArrayList<Node> floorList = new ArrayList<>();

  //Context menus
  private ContextMenu contextMenu;

  //Other misc items
  private String currentFloor = "1";
  private ArrayList<LocationName> locationNameList = new ArrayList<>();
  @FXML private VBox vboxBtns;
@FXML private VBox vboxAddNode;
  private boolean abilityToSeeNames;
  private boolean abilityToSeeEdges;
  private Tooltip nameToolTip;

  private ArrayList<Node> floorNodes = new ArrayList<>();
  public boolean boolEditingNode = false;
  public boolean boolSubmittedDetails = false;
  private Pane menuPane;
  private boolean editingNode = false; // Used for the submitting details button

  @FXML private MFXComboBox<String> cbNodeType;
  @FXML private MFXTextField tfLongName;
  @FXML private MFXTextField tfShortName;
  @FXML private MFXButton btnSubmitNodeDetails;
  @FXML private MFXTextField tfNodeId;
  private boolean boolAddingNode = false;

  int fullNodeX;
  int fullNodeY;
  public MapEditorController() throws SQLException {
    this.editor = new EMapEditor();
  }

  @FXML
  public void initialize() throws IOException, SQLException {




    //pathfinderController = new PathfinderController();
    initNavBar();
    hoverHelp();
    initializeFields();
    initButtons();
    // Initialize the edges, nodes, and names on the map
    nodeList = Repository.getRepository().getAllNodes();
    nameToolTip = new Tooltip();
//    fullNodes = Repository.getRepository().getFullNodes();


    this.stackPaneMapView = new StackPane(); // no longer @FXML
    // Used for nodes
    this.locationCanvas = new Pane();
    this.nodeGroup = new Group();

    // Used for location names
    this.fullNodeCanvas = new Pane();
    this.nameGroup = new Group();

    this.lineGroup = new Group();


    this.pane.setContent(stackPaneMapView);
    this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML

    //Establishing everything that must occur in the stackpane
    this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
    this.stackPaneMapView.getChildren().add(this.locationCanvas);

    this.locationCanvas.getChildren().add(nodeGroup);
    this.locationCanvas.getChildren().add(lineGroup);

    //Fitting the scrollpane
    pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    //Pane addMenuPane = FXMLLoader.load(getClass().getResource("/edu/wpi/teamb/views/components/AddNodeMenu.fxml"));
    //vboxAddNode.getChildren().add(addMenuPane);
    // Method to allow for triple click to add a new node
    if(ELogin.getLogin().getPermissionLevel() == ELogin.PermissionLevel.ADMIN) {
      stackPaneMapView.setOnMouseClicked(e -> {
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 3) {
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


    btnSubmitNodeDetails.setOnMouseClicked(event -> handleSubmitNodeDetails());
    drawNodes( currentFloor);

    changeButtonColor(currentFloor);
    Platform.runLater(() -> this.pane.centreOn(new Point2D(2190, 910)));

    if(ELogin.getLogin().getPermissionLevel() != ELogin.PermissionLevel.ADMIN) {
      NodeSelector.setVisible(false);
      NodeInfo.setVisible(false);
      exportBtn.setVisible(false);
      uploadBtn.setVisible(false);
      resetFromBackupBtn.setVisible(false);

    }

    System.out.println("MapEditorController initialized");
  }

  private void handleSubmitNodeDetails() {
    String shortName = tfShortName.getText();
    String longName = tfLongName.getText();
    String nodeType = cbNodeType.getValue();
    if (nodeType.equals("ELEV")) {
      createElevatorNode();
    } else if (nodeType.equals("STAI")) {
      createStairNode();
    } else createNode();
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

  private void createElevatorNode() {
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

    fullNode = new FullNode(maxID+5, (int) fullNodeX, (int) fullNodeY, "L1", "Full Node Building", "Elevator" + longName + "L1", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode); // Add the node to the nodeList

    fullNode = new FullNode(maxID+10, (int) fullNodeX, (int) fullNodeY, "L2", "Full Node Building", "Elevator" + longName + "L2", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode1 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode1);

    fullNode = new FullNode(maxID+15, (int) fullNodeX, (int) fullNodeY, "1", "Full Node Building", "Elevator" + longName + "1", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode2 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode2); // Add the node to the nodeList

    fullNode = new FullNode(maxID+20, (int) fullNodeX, (int) fullNodeY, "2", "Full Node Building", "Elevator" + longName + "2", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode3 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode3);

    fullNode = new FullNode(maxID+25, (int) fullNodeX, (int) fullNodeY, "3", "Full Node Building", "Elevator" + longName + "3", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode4 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode4);
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

  private void createStairNode() {
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

    fullNode = new FullNode(maxID+5, (int) fullNodeX, (int) fullNodeY, "L1", "Full Node Building", "Stair" + longName + "L1", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode); // Add the node to the nodeList

    fullNode = new FullNode(maxID+10, (int) fullNodeX, (int) fullNodeY, "L2", "Full Node Building", "Stair" + longName + "L2", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode1 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode1);

    fullNode = new FullNode(maxID+15, (int) fullNodeX, (int) fullNodeY, "1", "Full Node Building", "Stair" + longName + "1", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode2 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode2); // Add the node to the nodeList

    fullNode = new FullNode(maxID+20, (int) fullNodeX, (int) fullNodeY, "2", "Full Node Building", "Stair" + longName + "2", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode3 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode3);

    fullNode = new FullNode(maxID+25, (int) fullNodeX, (int) fullNodeY, "3", "Full Node Building", "Stair" + longName + "3", tfShortName.getText(), cbNodeType.getSelectedItem());
    Repository.getRepository().addFullNode(fullNode);

    Node newNode4 = new Node(fullNode.getNodeID(), fullNode.getxCoord(), fullNode.getyCoord(), fullNode.getFloor(), fullNode.getBuilding()); // Create a new node (DEFAULT IS HALL)
    nodeList.add(newNode4);
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
  public void drawNodes(String floor) throws SQLException {
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
      lineGroup.getChildren().clear();

      //Gets the full node of the current node, as well as the neighbors of this node
      ArrayList<Integer> neighbors = Repository.getRepository().getNeighbors(n.getNodeID());
      FullNode nNode = Repository.getRepository().getFullNode(n.getNodeID());

      for (int i = 0; i < neighbors.size(); i++) {

        //Get the full node of the neighbor, check to see if they're both elevators or stairs
        Node neighborNode = Node.getNode(neighbors.get(i));
        FullNode neighborFullNode = Repository.getRepository().getFullNode(neighborNode.getNodeID());
        String floor = neighborFullNode.getFloor();
        //If not stair or elevator reset this to null
        if(!nNode.getNodeType().equals("ELEV") && !nNode.getNodeType().equals("STAI")){
          nodeL1 = null;
          nodeL2 = null;
          node1 = null;
          node2 = null;
          node3 = null;
        } else if((neighborFullNode.getNodeType().equals("ELEV") && nNode.getNodeType().equals("ELEV")) || (neighborFullNode.getNodeType().equals("STAI") && nNode.getNodeType().equals("STAI"))){
          //If the neighborNode is of the same type as the current node
           if(floor.equals("L1")){
             nodeL1 = neighborNode;
           } else if (floor.equals("L2")) {
             nodeL2 = neighborNode;
           } else if (floor.equals("1")) {
             node1 = neighborNode;
           } else if (floor.equals("2")){
             node2 = neighborNode;
           } else {
             node3 = neighborNode;
           }
        }

        //Draws the lines of the neighbors on the current floor
        //This gets redone every floor which is slightly unoptimized but this should never span enough
        //To make this a large issue
        if (neighborNode.getFloor().equals(n.getFloor())) {
          Line line = new Line(n.getxCoord(), n.getyCoord(), neighborNode.getxCoord(), neighborNode.getyCoord());
          line.setStrokeWidth(4);
          lineGroup.getChildren().add(line);
          lineGroup.toFront();
          nodeGroup.toFront();
      }
    }
  }


  //Toggles the ability to see location names
  //Takes in current ability to see them in relation to the bar
  //When on it adds the ability to generate them in the node drawing method
  //When off it replaces text with "bruh" and removed ability to see them by adding delay.
  public void toggleLocationNames(){
    toggleLocationNames.setOnMouseClicked(event -> {
      if(toggleLocationNames.isSelected()){
        System.out.println("Location names on");
        abilityToSeeNames = true;
      } else {
        System.out.println("Location names off");
        abilityToSeeNames = false;
        toggleLocationNames.setSelected(false);
        nameToolTip.setText("bruh");
        nameToolTip.setShowDelay(Duration.minutes(1));
      }
    });
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
      if (event.getButton() == MouseButton.PRIMARY && ELogin.getLogin().getPermissionLevel() == ELogin.PermissionLevel.ADMIN) {
        this.handleNodeClick(event, n);
      }
    });
    //Sets up each individual hover for node name
    //Only toggles whn listNames is active
    c.setOnMouseEntered(event -> {
      if(abilityToSeeNames) {
        nameToolTip.setText(Repository.getRepository().getFullNode(n.getNodeID()).getShortName());
        System.out.println(Repository.getRepository().getFullNode(n.getNodeID()).getShortName());
        System.out.println(n.getNodeID());
        nameToolTip.setShowDelay(Duration.millis(1));
        nameToolTip.hideDelayProperty().set(Duration.seconds(.5));
        Tooltip.install(c, nameToolTip);
      }
    });

    // Add the circle to the nodeGroup
    floorList.add(n);
    nodeGroup.getChildren().add(c);
    nodeGroup.toFront();
  }



  /**
   * Draws a label with the respective nodes short name above the node
   * @param n
   */

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


  /**
   * Handles the reset from backup button click
   */
  @FXML void clickResetFromBackupBtn() {
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
    // Redraw the map
    try {
      drawNodes(currentFloor);
      System.out.println("Refreshing map for floor " + currentFloor + "...");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Handles the node click event (creates a context menu)
   * @param e
   * @param n
   */
  private void handleNodeClick(MouseEvent e, Node n) {
    createNodeContextMenu(n);
    contextMenu.show(this.pane, e.getScreenX(), e.getScreenY());
  }




  /**
   * Method to create context menu and set the actions for the menu items
   * @param n Node
   */
  private void createNodeContextMenu(Node n) {
    // Create the context menu
    contextMenu = new ContextMenu();
    contextMenu.setAutoHide(true);
    //System.out.println("Context menu created for node: " + n.getNodeID() );
    contextMenu.setId(String.valueOf(n.getNodeID()));
    System.out.println("Context menu created for node: " + n.getNodeID());

    locationItem = new MenuItem("Location name: " + Repository.getRepository().getFullNode(n.getNodeID()).getShortName());
    editLocationItem = new MenuItem("Edit Location Name");
    viewConnectedEdges = new MenuItem("View connected edges");
    deleteItem = new MenuItem("Delete");
    editItem = new MenuItem("Edit Node");
    contextMenu.getItems().addAll(locationItem, editLocationItem, viewConnectedEdges, deleteItem, editItem);
    this.editLocationItem.setOnAction(e-> {
      try {
        handleEditName(e, n);
      } catch (SQLException ex){
        throw new RuntimeException(ex);
      }
    });
    //Calls drawEdge for a node when option is selected
    this.viewConnectedEdges.setOnAction(e -> {
      drawEdge(n);
    });
    this.deleteItem.setOnAction(e-> {
      try {
        handleDeleteNode(e);
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
    this.editItem.setOnAction(e-> {
      try {
        handleEditNode(e, n);
      } catch (SQLException ex) {
        throw new RuntimeException(ex);
      }
    });
  }


  private void handleEditName(javafx.event.ActionEvent e, Node n) throws  SQLException {
   int nodeID = n.getNodeID();
   FullNode fullNode = Repository.getRepository().getFullNode(nodeID);
   System.out.println(fullNode.getShortName() + "\n" + fullNode.getLongName());
  }
  /**
   * Handles the delete node event
   * @param e
   * @throws SQLException
   */
  private void handleDeleteNode(javafx.event.ActionEvent e) throws SQLException {
    // Get the node ID from the circle's ID
    int nodeID = Integer.parseInt(contextMenu.getId());
    // delete from move table
    Repository.getRepository().deleteMove(Repository.getRepository().getMove(nodeID));
    // Delete the node from the database
    Repository.getRepository().deleteNode(Repository.getRepository().getNode(nodeID));

    // Remove the node from the map
    nodeGroup.getChildren().remove(contextMenu.getOwnerNode());

    // remove node from list
    for (Node n : nodeList) {
      if (n.getNodeID() == nodeID) {
        nodeList.remove(n);
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
  private void handleEditNode(javafx.event.ActionEvent e, Node n) throws SQLException {
    editingNode = true;
    // Get the node ID from the circle's ID
    int nodeID = Integer.parseInt(contextMenu.getId());
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
    clickFloorBtn("L1");
    clickFloorBtn("L2");
    clickFloorBtn("1");
    clickFloorBtn("2");
    clickFloorBtn("3");
  }

  public void clickFloorBtn(String floor) {
    btnL1.setOnMouseClicked(event->{
      imageViewPathfinder.setImage(Bapp.getHospitalListOfFloors().get(0));
      currentFloor = "L1";
      changeButtonColor(currentFloor);
      floorList = Repository.getRepository().getNodesByFloor("L1");
      try {
        lineGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        drawNodes( "L1");
        if(!nodeL1.equals(null)){
          drawEdge(nodeL1);
        }
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
        lineGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        drawNodes( "L2");
        if(!nodeL2.equals(null)){
          drawEdge(nodeL2);
        }
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
        lineGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        drawNodes( "1");
        if(!node1.equals(null)){
          drawEdge(node1);
        }
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
        lineGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        drawNodes( "2");
        if(!node2.equals(null)){
          drawEdge(node2);
        }
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
        lineGroup.getChildren().clear();
        nodeGroup.getChildren().clear();
        drawNodes( "3");
        if(!node3.equals(null)){
          drawEdge(node3);
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * Handles the click event for the upload button
   */
  @FXML public void clickUploadBtn() {
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
  @FXML public void clickExportBtn() {
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
          try {
            DBoutput.exportEdgesToCSV(absolutePath, 2);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
        case "Location Names" -> {
          try {
            DBoutput.exportLocationNamesToCSV(absolutePath, 2);
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
        }
        default -> new ArrayList<>();
      };
    }
    System.out.println("here"); // 1 means success, 0 means failure
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
    toggleLocationNames();
    toggleLocationNames();
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