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
public class NewProfileController {
  private GameModel gameModel;

  private ProfileFactory profileFactory;

  private String profilePicPath;

  // FXML components
  @FXML private TextField usernameTextField;

  @FXML private ImageView profileImageView;

  /** Init and loads the create new profile view */
  public void initialize() throws IOException {
    this.gameModel = GameModel.getInstance();
    profileFactory = new ProfileFactory();
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
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
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
      a.show();
      return false;
    }
    try {
      for (Profile profile : profileFactory.getAllProfiles()) {
        if (profile.getUsername().equals(create)) {
          Alert a = new Alert(Alert.AlertType.NONE);
          a.setAlertType(Alert.AlertType.INFORMATION);
          a.setHeaderText("");
          a.setContentText("Username already taken!");
          a.show();
          return false;
        }
        ;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /** This method is called when user clicks to create a new profile */
  @FXML
  private void onBackToProfiles() {
    gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
  }
}
