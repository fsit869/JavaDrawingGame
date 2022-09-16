package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;

/** This is the controller for the game. */
public class GameController {
  // FXML Components
  @FXML private Label timerLabel;
  @FXML private Label wordLabel;
  @FXML private Canvas canvas;
  @FXML private TabPane readyPaneMenu;
  @FXML private TabPane inGamePaneMenu;
  @FXML private TabPane endGamePaneMenu;
  @FXML private TextArea predictionTextArea;

  public void initialize() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * Handles the start game button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onStartGameButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles selecting the brush
   *
   * @param actionEvent Event of radio button
   */
  @FXML
  private void onBrushToggle(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles selecting the eraser
   *
   * @param actionEvent Event of radio button
   */
  @FXML
  private void onEraserToggle(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles the clear canvas button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onClear(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles the give up button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onGiveUpButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles save canvas button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onSaveCanvas(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles the goto menu button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onMenuButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Handles the new game button
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onNewGameButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
