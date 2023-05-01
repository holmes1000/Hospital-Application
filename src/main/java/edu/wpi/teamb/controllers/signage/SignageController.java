package edu.wpi.teamb.controllers.signage;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import edu.wpi.teamb.Bapp;
import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Node;
import edu.wpi.teamb.DBAccess.ORMs.Sign;
import edu.wpi.teamb.controllers.NavDrawerController;
import edu.wpi.teamb.entities.ESignage;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static javafx.scene.paint.Color.RED;

public class SignageController {

  @FXML private JFXHamburger menuBurger;
  @FXML private JFXDrawer menuDrawer;
  @FXML private MFXComboBox<String> cbLocation;
  @FXML private MFXButton btnSignageForm;
  @FXML private MFXButton btnRemoveSign;
  @FXML private VBox signVbox;
  public GesturePane pane = new GesturePane();
    Group nodeGroup = new Group();
    Pane locationCanvas;
    Pane nodeCanvas;
  private ESignage signageE;
    @FXML
    private StackPane stackPaneMapView;
    @FXML
    private ImageView imageViewPathfinder;
    @FXML private Pane navPane;
    @FXML private VBox vboxActivateNav;
    @FXML private VBox vboxActivateNav1;
    private boolean navLoaded;
    private FullNode centerNode;



  @FXML
  public void initialize() throws IOException {
      signageE = new ESignage();
      initNavBar();
      initializeFields();
      initalizeComboBox();
      init_signage_form_btn();
      signVbox.getChildren().clear();
//      vboxImage.getChildren().clear();
      loadPageBasedOnGroup("Shapiro 2 Screen 1 (info desk) May 23");

      this.stackPaneMapView = new StackPane(); // no longer @FXML
      // Used for nodes

      this.locationCanvas = new Pane();
      this.pane.setContent(stackPaneMapView);
      this.imageViewPathfinder = new ImageView(Bapp.getHospitalListOfFloors().get(3)); // no longer @FXML
      //Establishing everything that must occur in the stackpane

      //Fitting the scrollpane
      pane.setScrollMode(GesturePane.ScrollMode.ZOOM);
      pane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

      pane.toFront();
//      pane.setLayoutX(480);
//      pane.setLayoutY(400);


      Platform.runLater(() -> display_first_time());
      navPane.setMouseTransparent(true);
      initializeNavGates();
      cbLocation.selectFirst();
      displayMap();

  }

    private void initalizeComboBox() {
        HashSet<String> signageGroups = signageE.getSignageGroups();
        //convert the hashset to an arrayList
        ArrayList<String> signageGroupsList = new ArrayList<String>();
        for (String element : signageGroups) {
            signageGroupsList.add(element);
        }
        ObservableList<String> signageGroupsObservableList = FXCollections.observableArrayList(signageGroupsList);
        cbLocation.setItems(signageGroupsObservableList);
        cbLocation.setTooltip(new Tooltip("Click the dropdown arrow to select a sign"));
    }

    private void init_signage_form_btn(){
      btnSignageForm.setTooltip(new Tooltip("Click to add a new sign"));
        btnSignageForm.setOnMouseClicked(e -> handleSignageForm());
        btnRemoveSign.setTooltip(new Tooltip("Click to remove a sign"));
        btnRemoveSign.setOnMouseClicked(e -> handleRemoveSigns());
    }

    public void clickCbLocation() {
      signVbox.getChildren().clear();
      displaySelection();

  }

    public void displaySelection() {
        String item = cbLocation.getSelectedItem();
        loadPageBasedOnGroup(item);
        displayMap();
    }

  public void loadPageBasedOnGroup(String group) {
      ArrayList<Sign> allSigns = signageE.getAllSigns();
      ArrayList<Sign> groupSigns = new ArrayList<>();
      for (Sign sign : allSigns) {
          if (sign.getSignageGroup().equals(group)) {
              groupSigns.add(sign);
          }
      }
      int index = 0;
      for (Sign s : groupSigns) {
          try {
              FXMLLoader loader =
                      new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/SignageComponentIndividualDirection.fxml"));
              AnchorPane pane = loader.load();
              //get the controller that was loaded by the loader
              SignageComponentIndividualDirectionController controller = loader.getController();
              //set the location of the sign
              controller.setSignageLocationText(s.getLocationName());
              //set the direction of the sign
              if(s.getDirection().equals("stop here") && index== 0){
                  index++;
                  Label label = new Label();
                  label.setText("Stop Here");
                  label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
                  signVbox.getChildren().add(0, label);
              }
              controller.setSignageDirectionIcons(s.getDirection());
              signVbox.getChildren().addAll(pane);
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
      double vBoxHeight = 446;
      double childHeight = 57;
      double spacing = (vBoxHeight - (childHeight * signVbox.getChildren().size())) / (signVbox.getChildren().size());
      signVbox.setSpacing(spacing);
  }

  public void displayMap(){
      String item = cbLocation.getSelectedItem().toString();
      System.out.println(item);
//      System.out.println(item);
      int[] xy = signageE.getSignXandY(item);
      this.pane.centreOn(new Point2D(xy[0], xy[1]));
      Circle c = new Circle();
      c.setFill(RED);
      c.setRadius(4);
      c.setCenterX(xy[0]);
      c.setCenterY(xy[1]);
      Text text = new Text();
      text.setX(xy[0]-5);
      text.setY(xy[1] + 15);
      text.setText("You are here");

      this.stackPaneMapView.getChildren().clear();
      this.locationCanvas.getChildren().clear();
      this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
      this.nodeGroup.getChildren().add(c);
      this.nodeGroup.getChildren().add(text);
      this.locationCanvas.getChildren().add(nodeGroup);
      this.stackPaneMapView.getChildren().add(this.locationCanvas);
      this.locationCanvas.toFront();



      centerNode = signageE.getSignNode(item);
      System.out.println("displayed");

      //Add button on top that redirects to pathfinding and sets default location
  }

  private void display_first_time(){
      int nodeID = 2170;
      Node n = Repository.getRepository().getNode(nodeID);
      int x = n.getxCoord();
      int y = n.getyCoord();
      this.pane.centreOn(new Point2D(x, y));
      Circle c = new Circle();
      c.setFill(RED);
      c.setRadius(4);
      c.setCenterX(x);
      c.setCenterY(y);
      Text text = new Text();
      text.setX(x-5);
      text.setY(y+ 15);
      text.setText("You are here");

      this.stackPaneMapView.getChildren().clear();
      this.locationCanvas.getChildren().clear();
      this.stackPaneMapView.getChildren().add(this.imageViewPathfinder);
      this.nodeGroup.getChildren().add(c);
      this.nodeGroup.getChildren().add(text);
      this.locationCanvas.getChildren().add(nodeGroup);
      this.stackPaneMapView.getChildren().add(this.locationCanvas);
      this.locationCanvas.toFront();
  }

//    public void loadPage2() {
//        try {
//            FXMLLoader loader =
//                    new FXMLLoader(getClass().getResource("/edu/wpi/teamb/views/components/SignageComponent2.fxml"));
//            Pane pane = loader.load();
//            signVbox.getChildren().addAll(pane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

  public void initializeFields() {
      ObservableList<String> locations = FXCollections.observableArrayList(signageE.getSignageGroups());
      cbLocation.setItems(locations);
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

    private void handleSignageForm() {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/signage/SignageForm.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Add Signage");
            stage.setScene(new Scene(root, 800, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRemoveSigns(){
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/signage/RemoveSignageForm.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Remove Signage");
            stage.setScene(new Scene(root, 800, 400));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
