package edu.wpi.teamb.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  LOGIN("views/Login.fxml"),
  MEAL_REQUEST("views/requests/MealRequest.fxml"),
  CONFERENCE_REQUEST("views/requests/ConferenceRequest.fxml"),
  ALL_REQUEST("views/requests/AllRequests.fxml"),
  CREATE_NEW_REQUEST("views/requests/CreateNewRequest.fxml"),
  SETTINGS("views/Settings.fxml"),
  EDIT_USERS("views/requests/EditUsers.fxml"),
  PATHFINDER("views/Pathfinder.fxml"),
  MAP_EDITOR("views/MapEditor.fxml"),
  EXIT("views/Root.fxml"),
  SIGNAGE("views/Signage.fxml"),
  MOVE("views/requests/MoveRequest.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
