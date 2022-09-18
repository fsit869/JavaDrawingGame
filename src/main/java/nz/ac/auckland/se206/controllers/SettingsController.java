package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import nz.ac.auckland.se206.model.GameModel;

/** This class is responsible for selecting settings */
public class SettingsController {

  private GameModel gameModel;
  @FXML private Button ttsButton;

  public void initialize() {
    this.gameModel = GameModel.getInstance();
    if (gameModel.getProfile().getSettingsData().getTts() == true) {
      ttsButton.setText("ON");
      // need to enable tts
    } else {
      ttsButton.setText("OFF");
      // need to disable tts
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

  /**
   * This method is called when the text to speech button is pressed
   *
   * @param actionEvent Event type of button
   */
  public void onTtsButton(ActionEvent actionEvent) {
    switchTts();
  }

  /**
   * This method is called when user clicks the main menu button to go back to main menu
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onBackToMenuButton(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.viewState.CANVAS);
  }
}
