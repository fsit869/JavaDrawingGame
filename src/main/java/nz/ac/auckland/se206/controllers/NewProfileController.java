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

/** This class is responsible for creating a new profile */
public class NewProfileController {
  private GameModel gameModel;

  private ProfileFactory profileFactory;

  private String profilePicPath;

  // FXML components
  @FXML private TextField firstNameTextField;
  @FXML private TextField lastNameTextField;
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
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
    File profilePic = fileChooser.showOpenDialog(null);
    if (profilePic != null) {
      profilePicPath = profilePic.getPath();
      Image image = new Image(profilePic.toURI().toString());
      profileImageView.setImage(image);
    }
  }

  /** This method is called when user clicks to create a new profile */
  @FXML
  private void onCreateNewProfile() throws IOException {
    if (firstNameTextField.getText() == "") {
      Alert a = new Alert(Alert.AlertType.NONE);
      a.setAlertType(Alert.AlertType.INFORMATION);
      a.setHeaderText("");
      a.setContentText("Please enter a first name");
      a.show();
    } else {
      if (profilePicPath == null) {
        Image image = new Image(profileImageView.getImage().getUrl());
        profileImageView.setImage(image);
        profilePicPath = profileImageView.getImage().getUrl();
      }
      String username = firstNameTextField.getText() + " " + lastNameTextField.getText();
      profileFactory.createProfile(username, profilePicPath);
      gameModel.setProfile(profileFactory.selectProfile(username));
    }
  }

  /** This method is called when user clicks to create a new profile */
  @FXML
  private void onBackToProfiles() {
    gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
  }
}
