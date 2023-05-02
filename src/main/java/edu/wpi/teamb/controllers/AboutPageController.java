package edu.wpi.teamb.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import java.io.IOException;


public class AboutPageController {
    @FXML MFXButton btnClose;

    @FXML private ImageView Zimraan;
    @FXML private ImageView Ronit;
    @FXML private ImageView Alex;
    @FXML private ImageView Samara;
    @FXML private ImageView James;
    @FXML private ImageView Ella;
    @FXML private ImageView Brian;
    @FXML private ImageView Alisha;
    @FXML private ImageView Mahir;
    @FXML private ImageView Jackson;


    @FXML
    public void initialize() throws IOException {
        initializeBtns();
        people();
    }

    private void initializeBtns() {;
        btnClose.setTooltip(new Tooltip("Click to return to home page"));
        btnClose.setOnMouseClicked(e -> handleClose());
    }

    private void handleClose() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    private void people(){
        brian();
        ronit();
        alex();
        samara();
        alisha();
        ella();
        james();
        mahir();
        jackson();
        zimraan();
    }

    private void brian(){
        Tooltip brianText = new Tooltip("I don't like whipped cream");
        brianText.setWrapText(true);
        brianText.setMaxWidth(300);
        brianText.setStyle("-fx-font-size: 20");
        Tooltip.install(Brian,brianText);
    }

    private void ronit(){
        Tooltip ronitText = new Tooltip("I like building pcs, I also like organizing hackathons (3rd complete and 4th one ongoing rn)");
        ronitText.setWrapText(true);
        ronitText.setMaxWidth(300);
        ronitText.setStyle("-fx-font-size: 20");
        Tooltip.install(Ronit,ronitText);
    }

    private void alex(){
        Tooltip alexText = new Tooltip("I know how to breakdance");
        alexText.setWrapText(true);
        alexText.setMaxWidth(300);
        alexText.setStyle("-fx-font-size: 20");
        Tooltip.install(Alex,alexText);
    }

    private void samara(){
        Tooltip smaraText = new Tooltip("I've never been, are there trees in India?");
        smaraText.setWrapText(true);
        smaraText.setMaxWidth(300);
        smaraText.setStyle("-fx-font-size: 20");
        Tooltip.install(Samara,smaraText);
    }

    private void james(){
        Tooltip jamesText = new Tooltip("I own a milwaukee portaband (M18 FUEL!)");
        jamesText.setWrapText(true);
        jamesText.setMaxWidth(300);
        jamesText.setStyle("-fx-font-size: 20");
        Tooltip.install(James,jamesText);
    }

    private void ella(){
        Tooltip ellaText = new Tooltip("I've been to 35 National Parks");
        ellaText.setWrapText(true);
        ellaText.setMaxWidth(300);
        ellaText.setStyle("-fx-font-size: 20");
        Tooltip.install(Ella,ellaText);
    }

    private void mahir(){
        Tooltip mahirText = new Tooltip("Once made Ronit say: 'Will you let me FINISH WHAT I'M SAYING? I DON'T GIVE A FUCK ABOUT NOTABLE QOUTES RIGHT NOW!'");
        mahirText.setWrapText(true);
        mahirText.setMaxWidth(300);
        mahirText.setStyle("-fx-font-size: 20");
        Tooltip.install(Mahir,mahirText);
    }

    private void zimraan(){
        Tooltip zimraanText = new Tooltip("Cheese pizza’s my favorite");
        zimraanText.setWrapText(true);
        zimraanText.setMaxWidth(300);
        zimraanText.setStyle("-fx-font-size: 20");
        Tooltip.install(Zimraan,zimraanText);
    }

    private void alisha(){
        Tooltip alishaText = new Tooltip("'I don't want him to say 'I am concerned' about us' - Alisha");
        alishaText.setWrapText(true);
        alishaText.setMaxWidth(300);
        alishaText.setStyle("-fx-font-size: 20");
        Tooltip.install(Alisha,alishaText);
    }

    private void jackson(){
        Tooltip jacksonText = new Tooltip("'I have no idea how I am gonna write this 7 page essay, and now I am watching animated birds singing' - Jackson");
        jacksonText.setWrapText(true);
        jacksonText.setMaxWidth(300);
        jacksonText.setStyle("-fx-font-size: 20");
        Tooltip.install(Jackson,jacksonText);
    }
}
