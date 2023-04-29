package edu.wpi.teamb.Game;


import java.io.IOException;

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
    
        FXMLLoader loader = new FXMLLoader(Gapp.class.getResource("./rsc/Screens/StartScn.fxml"));
        BorderPane root = loader.load();

        Gapp.rootPane = root;

        final Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setScene(primaryStage.getScene());
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    public static void  changeScene(String scene) 
    {
        try {
            BorderPane newroot = (new FXMLLoader(Gapp.class.getResource(scene))).load();
            primaryStage.setScene(new Scene(newroot));;
        } catch (Exception e) {
            System.err.println( "ERROR: Unable to load: " +scene+"\n" + e.getMessage());
        }
       
    }
}
