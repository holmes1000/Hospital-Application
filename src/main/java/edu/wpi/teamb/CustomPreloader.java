package edu.wpi.teamb;
import edu.wpi.teamb.ProgressBar.RingProgressIndicator;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import java.io.InputStream;
import java.util.Objects;

public class CustomPreloader extends Preloader {

    private static final double WIDTH = 1280;
    private static final double HEIGHT = 720;

    private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    public CustomPreloader() {
        // Constructor is called before everything.
        System.out.println(Bapp.STEP() + "Custom Preloader constructor called, thread: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
        Image image = new Image("edu/wpi/teamb/img/bwh-background.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);
        System.out.println(Bapp.STEP() + "MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());

        // If preloader has complex UI it's initialization can be done in CustomPreloader#init
        Platform.runLater(() -> {
            RingProgressIndicator ringProgressIndicator = new RingProgressIndicator();
            ringProgressIndicator.setRingWidth(200);
            ringProgressIndicator.makeIndeterminate();
            progress = new Label("0%");
            StackPane root = new StackPane(progress);
            root.setAlignment(Pos.CENTER);
            root.getChildren().add(imageView);
            root.getChildren().add(ringProgressIndicator);
            progress.toFront();
            scene = new Scene(root, WIDTH, HEIGHT);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(Bapp.STEP() + "CustomPreloader#start (showing preloader stage), thread: " + Thread.currentThread().getName());

        this.preloaderStage = primaryStage;

        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        // Handle application notification in this point (see Bapp#init).
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                // Called after CustomPreloader#start is called.
                System.out.println(Bapp.STEP() + "BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                // Called before Bapp#init is called.
                System.out.println(Bapp.STEP() + "BEFORE_INIT");
                break;
            case BEFORE_START:
                // Called after Bapp#init and before Bapp#start is called.
                System.out.println(Bapp.STEP() + "BEFORE_START");

                preloaderStage.hide();
                break;
        }
    }
}
