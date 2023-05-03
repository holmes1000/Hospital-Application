package edu.wpi.teamb.controllers;

import edu.wpi.teamb.entities.ELogin;
import edu.wpi.teamb.exceptions.EmptyLoginCredentialsException;
import edu.wpi.teamb.exceptions.IncorrectPasswordException;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

public class LoginController {
  @FXML private MFXButton btnLogin;
  @FXML private MFXButton btnForgotPassword;
  @FXML private MFXButton btnCreateAccount;
  @FXML private MFXTextField textUsername;
  @FXML private MFXTextField textPassword;
  @FXML private Text errorMsg;
  private ELogin loginE;

  public LoginController() {
    this.loginE = ELogin.getLogin();
  }

  @FXML
  public void clickLogin(ActionEvent event) throws IOException {
    checkLogin();
  }

  public void clickForgotPassword(ActionEvent event) throws IOException {
    Parent root;
    try {
      root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("edu/wpi/teamb/views/settings/ForgotPassword.fxml")));
      Stage stage = new Stage();
      stage.setTitle("Forgot Password");
      stage.setScene(new Scene(root, 400, 600));
      stage.show();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks if the username and password entered are correct.
   *
   * @throws IOException
   */
  private void checkLogin() throws IOException {
    // first check if empty
    // otherwise send the username and password data over to Login entity to be checked with
    // database
    try {
      if (loginE.checkLogin(textUsername.getText(), textPassword.getText())) {
        // if login is successful, then send 2FA email
        loginE.send2FAEmail();
        //perform 2Factor Authentication
        //if successful, perform2FactorAuthentication() will handle navigate to home page
        perform2FactorAuthentication();
//        errorMsg.setText("Logged in Successful!");
      } else {
        //should never run this section unless there is a catastrophic error of some sorts
        //errorMsg.setText("Something has gone very wrong");
        submissionAlert("ERROR", "Something has gone very wrong.");
      }
    } catch (EmptyLoginCredentialsException e) {
      submissionAlert("Login Error", "Please fill in all fields.");
    } catch (NullPointerException e) {
      submissionAlert("Login Error", "Please check username and/or password.");
    } catch (IncorrectPasswordException e) {
      //errorMsg.setText("Please check username and/or password.");
        submissionAlert("Incorrect Password", "Please check username and/or password.");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Performs 2FA.
   *
   * @throws IOException
   */
  private void perform2FactorAuthentication() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("2-Factor Authentication: ");
    dialog.setHeaderText("Please enter the verification code sent to the email address associated with the username: " + loginE.getUsername());
    dialog.setContentText("Verification Code:");

    // create a formatter that only allows numbers
    UnaryOperator<TextFormatter.Change> filter = change -> {
      String text = change.getText();
      if (text.matches("\\d{0,6}")) {
        return change;
      }
      return null;
    };
    TextFormatter<String> formatter = new TextFormatter<>(filter);
    dialog.getEditor().setTextFormatter(formatter);

    // create a validation support object to show error messages
    ValidationSupport validationSupport = new ValidationSupport();
    validationSupport.setErrorDecorationEnabled(true);
    //validationSupport.registerValidator(dialog.getEditor(), Validator.createEmptyValidator("Please enter a number"));
    validationSupport.registerValidator(dialog.getEditor(), Validator.createPredicateValidator((String s) -> s.length() == 6, "Please enter a 6 digit number"));

    // Binding to disable the OK button until input is valid
    Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
    okButton.disableProperty().bind(validationSupport.invalidProperty());

    //set an onkeypressed event to the textfield to automatically close the dialog if the right code has been entered
    dialog.getEditor().setOnKeyTyped(e -> {
      if (loginE.verify2FAVerificationCode(Integer.parseInt(dialog.getEditor().getText()))) {
        //condition where 2-factor authentication code matched
        //errorMsg.setText("Logged in Successful!");
        Navigation.navigate(Screen.HOME);
        dialog.close();
      }
    });

    // Show the dialog and wait for the result
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent()) {
      if (loginE.verify2FAVerificationCode(Integer.parseInt(dialog.getEditor().getText()))) {
        //condition where 2-factor authentication code matched
        //errorMsg.setText("Logged in Successful!");
        Navigation.navigate(Screen.HOME);
      } else {
        //condition where 2-factor authentication code did not match
        //errorMsg.setText("Incorrect 2-factor authentication code. Please try again.");
        submissionAlert("2-Factor Authentication", "Incorrect 2-factor authentication code. Please try again.");
      }
    } else {
      System.out.println("User closed 2FA menu");
      //condition where user canceled 2-factor authentication
      //errorMsg.setText("2-factor authentication canceled. Login failed.");
      //submissionAlert("2-Factor Authentication", "2-factor authentication canceled. Login failed.");
    }
  }

  //enum for 2Factor authentication statuses
  private enum TwoFactorStatus {

    SUCCESS("SUCCESS"),
    CANCELED("CANCELED"),
    FAILED("FAILED");

    private final String status;

    TwoFactorStatus(String status) {
      this.status = status;
    }

     private String getStatus() {
      return status;
    }
  }

  void submissionAlert(String title, String message) {
    // Create an alert
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
