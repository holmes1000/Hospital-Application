package edu.wpi.teamb.controllers;

import edu.wpi.teamb.entities.Login;
import edu.wpi.teamb.exceptions.EmptyLoginCredentialsException;
import edu.wpi.teamb.exceptions.IncorrectPasswordException;
import edu.wpi.teamb.navigation.Navigation;
import edu.wpi.teamb.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXRadioButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class LoginController {
  @FXML private MFXButton btnLogin;
  @FXML private MFXButton btnForgotPassword;
  @FXML private MFXButton btnCreateAccount;
  @FXML private MFXRadioButton radioRememberMe;
  @FXML private MFXTextField textUsername;
  @FXML private MFXTextField textPassword;
  @FXML private Text errorMsg;
  private Login login;

  public LoginController() {
    this.login = Login.getLogin();
  }

  @FXML
  public void clickLogin(ActionEvent event) throws IOException {
    checkLogin();
  }

  public void clickForgotPassword(ActionEvent event) throws IOException {
    System.out.println("Not yet implemented");
  }

  public void clickCreateAccount(ActionEvent event) throws IOException {
    System.out.println("Not yet implemented");
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
      if (login.checkLogin(textUsername.getText(), textPassword.getText())) {
        errorMsg.setText("Logged in Successful!");
        Navigation.navigate(Screen.HOME);
      } else {
        //should never run this section unless there is a catastrophic error of some sorts
        errorMsg.setText("Something has gone very wrong");
      }
    } catch (EmptyLoginCredentialsException e) {
      errorMsg.setText("Fields are empty...Enter your data.");
    } catch (NullPointerException e) {
      errorMsg.setText("Username does not exist in our system.");
    } catch (IncorrectPasswordException e) {
      errorMsg.setText("Wrong password entered for the given username.");
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @FXML
  public void clickExit() {
    System.exit(0);
  }
}
