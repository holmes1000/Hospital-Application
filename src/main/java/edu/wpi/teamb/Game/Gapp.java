package edu.wpi.teamb.Game;

import edu.wpi.teamb.Bapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class Gapp extends Application {

    private static Stage primaryStage;
    private static BorderPane rootPane;

    public static Image[] personImages = new Image[] {
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/HappyCustomer.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/MadCustomer.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/VeryMadCustomer.png"))
    };
    public static Image[] patientImages = new Image[] {
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/heart.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/brokenLimb.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/hungry.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/nonSevere.png")),
    };
    public static Image[] deskImages = new Image[] {
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/Desk_heart.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/Desk_brokenLimb.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/Desk_hungry.png")),
            new Image(Bapp.class.getResourceAsStream("./img/Game/rsc/images/Desk_nonSevere.png")),
    };

    public static Stage getPrimaryStage()
    {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Hospital Help Desk");

        Gapp.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        URL a = Bapp.class.getResource("./views/Game/rsc/Screens/StartScn.fxml");

       // add to a's path : rsc/Screens/StartScn.fxm

        FXMLLoader loader = new FXMLLoader(a);
        //loader.setLocation(c);
        BorderPane root = loader.load();

        Gapp.rootPane = root;

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setScene(primaryStage.getScene());
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    
    public static void Newstart(Stage primaryStage) throws Exception {
        Game.stop();
        Game.kill();
        primaryStage.setTitle("Hospital Help Desk");

        Gapp.primaryStage = primaryStage;
        primaryStage.setResizable(false);
        URL a = Bapp.class.getResource("./views/Game/rsc/Screens/StartScn.fxml");

       // add to a's path : rsc/Screens/StartScn.fxm

        FXMLLoader loader = new FXMLLoader(a);
        //loader.setLocation(c);
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
            BorderPane newroot = (new FXMLLoader(Bapp.class.getResource(scenePath))).load();
            primaryStage.setScene(new Scene(newroot));
            ;
        } catch (Exception e) {
            System.err.println("ERROR: Unable to load: " + scenePath + "\n" + e.getMessage());
        }

    }

    public static BorderPane getRootPane() {
        return rootPane;
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
    public static Thread runGame() {
        Game g = Game.newGame();
        //make this thread synchrounous with gamehthread
        
        gameThread = new Thread()
        {
            public void run()
            {
                g.start(Thread.currentThread());
            }
        
        };

        gameThread.start();
        return gameThread;
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
