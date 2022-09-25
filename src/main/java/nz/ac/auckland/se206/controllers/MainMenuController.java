package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.model.GameModel;

/** This is the controller for the main menu */
public class MainMenuController {
  @FXML private ImageView profileImageView;

  @FXML private Button startButton;

  @FXML private Button statsButton;

  @FXML private Button changeProfileButton;
  @FXML private Button settingsButton;

  @FXML private Button exitButton;

  private GameModel gameModel;

  public void initialize() {
    startButton.toBack();
    statsButton.toBack();
    changeProfileButton.toBack();
    settingsButton.toBack();
    exitButton.toBack();
    // Setup required objects
    this.gameModel = GameModel.getInstance();
    //    profileNameLabel.setText(gameModel.getProfile().getUsername());
    String defaultUrl = profileImageView.getImage().getUrl();

    // Try load profile picture
    try {
      Image image = new Image(gameModel.getProfile().getProfilePicturePath());
      profileImageView.setImage(image);
    } catch (Exception e) {
      // Unexpected err while loading image. Display default img not found.
      Image image = new Image(defaultUrl);
      profileImageView.setImage(image);
    }
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /** This method is called when user clicks start to switch to the canvas view */
  @FXML
  private void onStartButton() {
    gameModel.setCurrentViewState(GameModel.ViewState.CANVAS);
  }

  /** This method is called when user clicks the settings button to go to settings */
  @FXML
  private void onSettingsButton() {
    gameModel.setCurrentViewState(GameModel.ViewState.SETTINGS);
  }

  /** This method is called when user clicks to change profile picture */
  @FXML
  private void onChangeProfileButton() {
    gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
  }

  /** This method is called when user clicks to see their stats */
  @FXML
  private void onStatsButton() {
    gameModel.setCurrentViewState(GameModel.ViewState.PROFILESTATS);
  }

  /** This method is called when user clicks to exit the game */
  @FXML
  private void onExitButton() {
    System.exit(0);
  }
}
