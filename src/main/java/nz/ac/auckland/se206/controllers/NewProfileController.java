package nz.ac.auckland.se206.controllers;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;

/** This class is responsible for creating a new profile */
public class NewProfileController implements ControllerInterface {
  private GameModel gameModel;

  private ProfileFactory profileFactory;

  private String profilePicPath;

  // FXML components
  @FXML private TextField usernameTextField;

  @FXML private ImageView profileImageView;

  /** Init and loads the create new profile view */
  public void initialize() {
    this.gameModel = GameModel.getInstance();
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void refresh() {
    // todo Clear profile from prev
  }
  /////////////////////
  // Button handlers //
  /////////////////////

  /** This method is called when user clicks to choose a profile picture */
  @FXML
  private void onChooseProfilePicture() {
    // Open file load dialogue
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser
        .getExtensionFilters()
        .addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    File profilePic = fileChooser.showOpenDialog(null);

    // Load image if valid
    if (profilePic != null) {
      profilePicPath = profilePic.getPath();
      Image image = new Image(profilePic.toURI().toString());
      profileImageView.setImage(image);
    }
  }

  /** This method is called when user clicks to create a new profile */
  @FXML
  private void onCreateNewProfile() throws IOException {
    String username = usernameTextField.getText().trim();
    if (verifyUsername(username)) {
      // Obtain the profile path
      if (profilePicPath == null) {
        Image image = new Image(profileImageView.getImage().getUrl());
        profileImageView.setImage(image);
        profilePicPath = profileImageView.getImage().getUrl();
      }
      // Save new profile
      profileFactory.createProfile(username, profilePicPath);
      gameModel.setProfile(profileFactory.selectProfile(username));
      gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
    }
  }

  /**
   * Verifies if the new username is valid to be created into another profile
   *
   * @param create string of username
   * @return validity of username
   */
  private boolean verifyUsername(String create) {
    if (create.equals("")) {
      // Show dialogue if no username entered
      Alert a = new Alert(Alert.AlertType.NONE);
      a.setAlertType(Alert.AlertType.INFORMATION);
      a.setHeaderText("");
      a.setContentText("Please enter a username");
      a.showAndWait();
      return false;
    }
    if (create.length() > 20) {
      // Show dialogue if username is too long
      Alert a = new Alert(Alert.AlertType.NONE);
      a.setAlertType(Alert.AlertType.INFORMATION);
      a.setHeaderText("");
      a.setContentText("Username is too long!");
      a.showAndWait();
      return false;
    }
    try {
      // loops through existing profiles and checks if usernames are taken
      for (Profile profile : profileFactory.getAllProfiles()) {
        if (profile.getUsername().equalsIgnoreCase(create)) {
          // Sends an alert if there is already existing a profile.
          Alert a = new Alert(Alert.AlertType.NONE);
          a.setAlertType(Alert.AlertType.INFORMATION);
          a.setHeaderText("");
          a.setContentText("Username already taken!");
          a.showAndWait();
          return false;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Returns true if there are no problems, and the profile can be saved.
    return true;
  }

  /** This method is called when user clicks to create a new profile */
  @FXML
  private void onBackToProfiles() {
    gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
  }


}
