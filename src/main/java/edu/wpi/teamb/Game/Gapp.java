package edu.wpi.teamb.Game;


import edu.wpi.teamb.Bapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Gapp extends Application {

    private static Stage primaryStage;
    private static BorderPane rootPane;


	@Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Hospital Help Desk");

        Gapp.primaryStage = primaryStage;
        primaryStage.setResizable(false);
    
        final FXMLLoader loader = new FXMLLoader(Gapp.class.getResource("./rsc/Screens/StartScn.fxml"));
        final BorderPane root = loader.load();
    
        Gapp.rootPane = root;
    
        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        // primaryStage.setScene(primaryStage.getScene());
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    public void changeScene(String scene)
    {
        Group g = new Group();
        
        primaryStage.setScene(new Scene(g));
        //change the scene to gicen scene filepath
        primaryStage.setScene(new Scene(new Group()));
    }
}
