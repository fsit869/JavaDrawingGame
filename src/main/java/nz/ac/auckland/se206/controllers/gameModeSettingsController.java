package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

  private String[] gameModes = {"Classic", "Hidden-word Mode", "Zen Mode"};

  @FXML private ComboBox<String> confidenceComboBox;

  @FXML private ComboBox<String> timeComboBox;

  @FXML private ComboBox<String> accuracyComboBox;

  @FXML private ComboBox<String> wordDifficultyComboBox;
  @FXML private TextArea instructionsTextArea;

  @FXML private Button gameModeButton;

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

    // init comboboxes items
    confidenceComboBox.getItems().addAll(confidenceDifficulties);
    timeComboBox.getItems().addAll(timeDifficulties);
    accuracyComboBox.getItems().addAll(accuracyDifficulties);
    wordDifficultyComboBox.getItems().addAll(difficultyDifficulties);

    // init gameMode
    switch (this.gameModel.getCurrentGameMode()) {
      case CLASSIC -> gameModeButton.setText("Classic Mode");
      case HIDDEN -> gameModeButton.setText("Hidden-Word Mode");
      case ZEN -> gameModeButton.setText("Zen Mode");
    }

    // init accuracy combobox
    switch (this.gameModel.getProfile().getSettingsData().getAccuracy()) {
      case EASY -> accuracyComboBox.setValue("Easy");
      case MEDIUM -> accuracyComboBox.setValue("Medium");
      case HARD -> accuracyComboBox.setValue("Hard");
    }
    // init time combobox
    switch (this.gameModel.getProfile().getSettingsData().getTime()) {
      case EASY -> timeComboBox.setValue("Easy");
      case MEDIUM -> timeComboBox.setValue("Medium");
      case HARD -> timeComboBox.setValue("Hard");
      case MASTER -> timeComboBox.setValue("Master");
    }
    // init confidence combobox
    switch (this.gameModel.getProfile().getSettingsData().getConfidence()) {
      case EASY -> confidenceComboBox.setValue("Easy");
      case MEDIUM -> accuracyComboBox.setValue("Medium");
      case HARD -> accuracyComboBox.setValue("Hard");
      case MASTER -> accuracyComboBox.setValue("Master");
    }
    // init word difficulty combobox
    switch (this.gameModel.getProfile().getSettingsData().getSetting()) {
      case EASY -> wordDifficultyComboBox.setValue("Easy");
      case MEDIUM -> wordDifficultyComboBox.setValue("Medium");
      case HARD -> wordDifficultyComboBox.setValue("Hard");
      case MASTER -> wordDifficultyComboBox.setValue("Master");
    }
  }

  @Override
  public void refresh() {
    //    initialize();
  }

  @FXML
  public void onStartButton() {
    gameModel.setCurrentViewState(GameModel.ViewState.CANVAS);
  }

  @FXML
  public void onRightButton() {
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Hidden-Word Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Zen Mode");
      case "Zen Mode" -> gameModeButton.setText("Classic Mode");
    }
  }

  @FXML
  public void onLeftButton() {
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Zen Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Classic Mode");
      case "Zen Mode" -> gameModeButton.setText("Hidden-Word Mode");
    }
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
  public void onGameModeInfoEnter() {

    switch (this.gameModeButton.getText()) {
      case "Classic Mode" -> instructionsTextArea.setText(
          "Instructions-Classic Gamemode:\n" + "The rules are ....");
      case "Hidden-Word Mode" -> instructionsTextArea.setText(
          "Instructions-Hidden-Word Gamemode:\n" + "The rules are ....");
      case "Zen Mode" -> instructionsTextArea.setText(
          "Instructions-Zen Gamemode:\n" + "The rules are ....");
    }
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

  @FXML
  public void onGameModeInfoExit() {
    instructionsTextArea.setText("Instructions");
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }
}
