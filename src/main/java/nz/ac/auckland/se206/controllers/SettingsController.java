package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

/** This class is responsible for selecting settings */
public class SettingsController {

  private GameModel gameModel;

  private ProfileFactory profileFactory = new ProfileFactory();
  @FXML private Button ttsButton;

  public SettingsController() throws IOException {}

  public void initialize() {
    this.gameModel = GameModel.getInstance();
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
  public void onTtsButton() {
    switchTts();
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    if (ttsButton.getText().equals("ON")) {
      gameModel.getProfile().getSettingsData().setTts(true);
    } else {
      gameModel.getProfile().getSettingsData().setTts(false);
    }
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.viewState.CANVAS);
  }
}
