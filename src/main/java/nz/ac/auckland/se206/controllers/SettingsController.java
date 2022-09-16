package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/** This class is responsible for selecting settings */
public class SettingsController {

  @FXML private Button ttsButton;

  public void initialize() {
    throw new UnsupportedOperationException("Not yet implemented");
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
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * This method is called when user clicks the main menu button to go back to main menu
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onBackToMenuButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
