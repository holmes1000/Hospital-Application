package edu.wpi.teamb.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  LOGIN("views/Login.fxml"),
  MEAL_REQUEST("views/requests/MealRequest.fxml"),
  CONFERENCE_REQUEST("views/requests/ConferenceRequest.fxml"),
  ALL_REQUEST("views/requests/SubmittedRequests.fxml"),
  CREATE_NEW_REQUEST("views/requests/CreateNewRequest.fxml"),
  SETTINGS("views/settings/Settings.fxml"),
  EDIT_ACCOUNT("views/settings/EditAccount.fxml"),
  EDIT_USERS("views/settings/EditUsers.fxml"),
  PATHFINDER("views/pathfinder/Pathfinder.fxml"),
  MAP_EDITOR("views/mapeditor/MapEditor.fxml"),
  EXIT("views/Root.fxml"),
  SIGNAGE("views/signage/Signage.fxml"),
  MOVE("views/requests/MoveRequest.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
