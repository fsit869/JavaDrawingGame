package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.speech.TextToSpeechTask;

/** This class is responsible for selecting settings */
public class SettingsController implements ControllerInterface {

  private GameModel gameModel;
  private ProfileFactory profileFactory;
  @FXML private Button ttsOnButton;
  @FXML private Button ttsOffButton;

  /** Init and loads the settings view */
  public void initialize() {

    // Create instance of object saving
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Get game settings
    this.gameModel = GameModel.getInstance();

    // Set initial state of TTS buttons
    if (gameModel.getProfile().getSettingsData().getTts()) {
      ttsOnButton.setStyle("-fx-background-color: #00FF00;-fx-background-radius: 2em");
      ttsOffButton.setStyle("-fx-background-color: grey;-fx-background-radius: 2em");
    } else {
      ttsOffButton.setStyle("-fx-background-color: #FF0000;-fx-background-radius: 2em");
      ttsOnButton.setStyle("-fx-background-color: grey;-fx-background-radius: 2em");
    }
  }

  @Override
  public void refresh() {
  }

  private void switchTts() {
    if (gameModel.getProfile().getSettingsData().getTts() == true) {
      ttsOffButton.setStyle("-fx-background-color: #FF0000;-fx-background-radius: 2em");
      ttsOnButton.setStyle("-fx-background-color: grey;-fx-background-radius: 2em");
      // need to disable tts
      gameModel.getProfile().getSettingsData().setTts(false);
    } else {
      ttsOnButton.setStyle("-fx-background-color: #00FF00;-fx-background-radius: 2em");
      ttsOffButton.setStyle("-fx-background-color: grey;-fx-background-radius: 2em");
      // need to enable tts
      gameModel.getProfile().getSettingsData().setTts(true);
    }
  }

  ///////////////////
  // Button handlers /
  ///////////////////

  /** This method is called when the text to speech button is pressed */
  @FXML
  private void onTtsOnButton() {
    if (gameModel.getProfile().getSettingsData().getTts() == false) {
      switchTts();
    }
  }

  @FXML
  private void onTtsOffButton() {
    if (gameModel.getProfile().getSettingsData().getTts() == true) {
      switchTts();
    }
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }
}
