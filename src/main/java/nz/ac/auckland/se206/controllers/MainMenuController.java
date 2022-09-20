package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

/** This is the controller for the main menu */
public class MainMenuController {
  @FXML private Button profileButton;
  @FXML private ImageView profileImageView;

  private GameModel gameModel;

  private ProfileFactory profileFactory;

  public void initialize() throws IOException {
    //    profileButton.setText(gameModel.getProfile().getUsername());
    this.gameModel = GameModel.getInstance();
    profileFactory = new ProfileFactory();
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * This method is called when user clicks start to switch to the canvas view
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onStartButton(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.viewState.CANVAS);
  }

  /**
   * This method is called when user clicks the settings button to go to settings
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onSettingsButton(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.viewState.SETTINGS);
  }

  /**
   * This method is called when user clicks to change profile picture
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onChangeProfileButton(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.viewState.SELECTPROFILES);
  }

  /**
   * This method is called when user clicks to see their profile
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onProfileButton(ActionEvent actionEvent) {
    System.out.println("no function");
  }

  /**
   * This method is called when user clicks to exit the game
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onExitButton(ActionEvent actionEvent) {

    Platform.exit();
  }
}
