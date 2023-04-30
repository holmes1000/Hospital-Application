package edu.wpi.teamb.Game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Gapp extends Application {

    private static Stage primaryStage;
    private static BorderPane rootPane;

    public static Image[] personImages = new Image[] {
            new Image(Gapp.class.getResourceAsStream("./rsc/images/HappyCustomer.png")),
            new Image(Gapp.class.getResourceAsStream("./rsc/images/MadCustomer.png"))
    };
    public static Image[] patientImages = new Image[] {
            new Image(Gapp.class.getResourceAsStream("./rsc/images/heart.png")),
            new Image(Gapp.class.getResourceAsStream("./rsc/images/brokenLimb.png")),
            new Image(Gapp.class.getResourceAsStream("./rsc/images/hungry.png")),
            new Image(Gapp.class.getResourceAsStream("./rsc/images/nonSevere.png")),
    };

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Hospital Help Desk");

        Gapp.primaryStage = primaryStage;
        primaryStage.setResizable(false);

        FXMLLoader loader = new FXMLLoader(Gapp.class.getResource("./rsc/Screens/StartScn.fxml"));
        BorderPane root = loader.load();

        Gapp.rootPane = root;

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setScene(primaryStage.getScene());
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    /**
     * cahnge the scene to one that that path points to
     * 
     * @param scenePath the path to the scene
     */
    public static void changeScene(String scenePath) {
        try {
            BorderPane newroot = (new FXMLLoader(Gapp.class.getResource(scenePath))).load();
            primaryStage.setScene(new Scene(newroot));
            ;
        } catch (Exception e) {
            System.err.println("ERROR: Unable to load: " + scenePath + "\n" + e.getMessage());
        }

    }

    /**
     * gets the current scene
     * 
     * @return the current scene
     */
    public static Scene getScene() {
        return primaryStage.getScene();
    }

    static Thread gameThread;

    /**
     * runs the game thread
     */
    public static void runGame() {
        Game g = Game.newGame();
        gameThread = new Thread(() -> g.start(Thread.currentThread()));
        gameThread.start();
    }

    /**
     * ends the game thread
     */
    public static void endGame() {
        Game.stop();

        gameThread.interrupt();

        Game.kill();

    }

    /**
     * gets the main canvas node
     * 
     * @return the main canvas node
     * 
     */
    public static Canvas getMainCanvas() {
        return (Canvas) primaryStage.getScene().lookup("#TheCanvas");

    }

}
