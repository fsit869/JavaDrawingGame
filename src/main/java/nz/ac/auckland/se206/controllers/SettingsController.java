package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

/** This class is responsible for selecting settings */
public class SettingsController {

  private GameModel gameModel;

  private ProfileFactory profileFactory;
  @FXML private Button ttsButton;

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

    // Set initial state of TTS button
    if (gameModel.getProfile().getSettingsData().getTts()) {
      ttsButton.setText("ON");
    } else {
      ttsButton.setText("OFF");
    }
  }

  private void switchTts() {
    if (ttsButton.getText().equals("ON")) {
      ttsButton.setText("OFF");
      // need to disable tts
      gameModel.getProfile().getSettingsData().setTts(false);
    } else {
      ttsButton.setText("ON");
      // need to enable tts
      gameModel.getProfile().getSettingsData().setTts(true);
    }
  }

  ///////////////////
  // Button handlers /
  ///////////////////

  /** This method is called when the text to speech button is pressed */
  @FXML
  private void onTtsButton() {
    switchTts();
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.CANVAS);
  }
}
