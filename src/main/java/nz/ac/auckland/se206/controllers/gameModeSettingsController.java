package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

public class gameModeSettingsController implements ControllerInterface {

  private GameModel gameModel;

  private ProfileFactory profileFactory;
  private String[] confidenceDifficulties = {"Easy", "Medium", "Hard", "Master"};

  private String[] timeDifficulties = {"Easy", "Medium", "Hard", "Master"};

  private String[] accuracyDifficulties = {"Easy", "Medium", "Hard"};

  private String[] difficultyDifficulties = {"Easy", "Medium", "Hard", "Master"};

  @FXML private ComboBox<String> confidenceComboBox;

  @FXML private ComboBox<String> timeComboBox;

  @FXML private ComboBox<String> accuracyComboBox;

  @FXML private ComboBox<String> wordDifficultyComboBox;
  @FXML private TextArea instructionsTextArea;

  public void initialize() {

    // Create instance of object saving
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    instructionsTextArea.setText("Instructions");

    // Get game settings
    this.gameModel = GameModel.getInstance();

    confidenceComboBox.getItems().addAll(confidenceDifficulties);
    timeComboBox.getItems().addAll(timeDifficulties);
    accuracyComboBox.getItems().addAll(accuracyDifficulties);
    wordDifficultyComboBox.getItems().addAll(difficultyDifficulties);
  }

  @Override
  public void refresh() {}

  @FXML
  public void onStartButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onGameModeButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onRightButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onLeftButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onConfidenceInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Confidence:\n"
            + "Level easy: You win the game if the computer is at least 1% confident\n"
            + "that your image is the word to guess.\n"
            + "Level medium: You win the game if the computer is at least 10% confident\n"
            + "that your image is the word to guess.\n"
            + "Level hard: You win the game if the computer is at least 25% confident\n"
            + "that your image is the word to guess.\n"
            + "Level master: You win the game if the computer is at least 50% confident\n"
            + "that your image is the word to guess.");
  }

  @FXML
  public void onTimeInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Time limit:\n"
            + "Level easy: You gets 60 seconds to draw the picture correctly.\n"
            + "Level medium: You gets 45 seconds to draw the picture correctly.\n"
            + "Level hard: You gets 30 seconds to draw the picture correctly.\n"
            + "Level master: You gets 15 seconds to draw the picture correctly.");
  }

  @FXML
  public void onAccuracyInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Accuracy:\n"
            + "Level easy: You win the game if the word to draw is in the computer's top 3\n"
            + "guesses.\n"
            + "Level medium: You win the game if the word to draw is in the computer's top\n"
            + "2 guesses.\n"
            + "Level hard: You win the game if the word to draw is the computer’s best\n"
            + "guess.");
  }

  @FXML
  public void onWordDifficultyInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Word Difficulty:\n"
            + "Level easy: You will only get given EASY words.\n"
            + "Level medium: You will get given EASY or MEDIUM words.\n"
            + "Level hard: You will get given EASY or MEDIUM or HARD words\n"
            + "Level master: You will only get given HARD words.");
  }

  @FXML
  public void onConfidenceInfoExit() {
    instructionsTextArea.setText("Instructions");
  }

  @FXML
  public void onTimeInfoExit() {
    instructionsTextArea.setText("Instructions");
  }

  @FXML
  public void onAccuracyInfoExit() {
    instructionsTextArea.setText("Instructions");
  }

  @FXML
  public void onWordDifficultyInfoExit() {
    instructionsTextArea.setText("Instructions");
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }
}
