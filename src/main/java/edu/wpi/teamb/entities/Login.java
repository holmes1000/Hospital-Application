package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.ORMs.User;
import edu.wpi.teamb.exceptions.EmptyLoginCredentialsException;
import edu.wpi.teamb.exceptions.IncorrectPasswordException;

import java.sql.SQLException;

public class Login {
  // unsure if these fields are even needed
  private String username;
  private String password;
  private PermissionLevel permissionLevel;
  // following field will hold global Login instance
  private static Login login;

  /**
   * This constructor will initialize a global Login instance once at the beginning and then return
   * the same instance thereafter
   */
  private Login() {
    // need empty private constructor to prevent multiple instantiation
  }

  /**
   * This method will initialize a global Login instance once at the beginning and then return the
   * same instance thereafter
   *
   * @return Login instance
   */
  public static synchronized Login getLogin() {
    if (login == null) {
      login = new Login();
    }
    return login;
  }

  // possible permission levels
  public enum PermissionLevel {
    ADMIN,
    EMPLOYEE,
    USER,
  }

  // only getter that is practically needed for now
  public PermissionLevel getPermissionLevel() {
    return permissionLevel;
  }

  // unsure yet if getters and setters would make sense here since there does not seem to be any use
  // for them outside
  // the context of checkLogin

  public boolean checkLogin(String username, String password)
          throws EmptyLoginCredentialsException, NullPointerException, IncorrectPasswordException, SQLException {
    // check if username and/or password are empty
    if (username.isEmpty() && password.isEmpty()) {
      throw new EmptyLoginCredentialsException("Both username and password are empty");
    } else if (username.isEmpty()) {
      throw new EmptyLoginCredentialsException("Username is empty");
    } else if (password.isEmpty()) {
      throw new EmptyLoginCredentialsException("Password is empty");
    }

    // storing login info in entity
    this.username = username;
    this.password = password;

    //get the user from the database
    User currentUser = (User) Repository.getRepository().getUser(username);
    //make sure the user exists
    if (currentUser == null) {
      throw new NullPointerException("User does not exist");
    }
    //if the user exists, check the password
    if (!currentUser.getPassword().equals(password)) {
      throw new IncorrectPasswordException("Password is incorrect");
    }
    //if the password is correct, set the permission level
    this.permissionLevel = PermissionLevel.values()[currentUser.getPermissionLevel()];
    //return true if the login was successful
    return true;
  }

  /** This method will log out the user by setting all fields to null */
  public void logOut() {
    this.username = null;
    this.password = null;
    this.permissionLevel = null;
    Login.login = null;
  }
}
