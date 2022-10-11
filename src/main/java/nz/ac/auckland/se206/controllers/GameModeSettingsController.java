package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.SettingsData;

public class GameModeSettingsController implements ControllerInterface {

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
  /** Init and loads the gamemode settings view */
  public void initialize() {
    doRefresh();
    // init comboboxes items
    confidenceComboBox.getItems().addAll(confidenceDifficulties);
    timeComboBox.getItems().addAll(timeDifficulties);
    accuracyComboBox.getItems().addAll(accuracyDifficulties);
    wordDifficultyComboBox.getItems().addAll(difficultyDifficulties);
    confidenceComboBox.setStyle("-fx-font: 12px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    timeComboBox.setStyle("-fx-font: 12px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    accuracyComboBox.setStyle("-fx-font: 12px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    wordDifficultyComboBox.setStyle(
        "-fx-font: 12px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    instructionsTextArea.setStyle("-fx-text-fill: white;");
  }

  @Override
  public void refresh() {
    doRefresh();
  }

  public void doRefresh() {
    // Create instance of object saving
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");

    // Get game settings
    this.gameModel = GameModel.getInstance();

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
      case MEDIUM -> confidenceComboBox.setValue("Medium");
      case HARD -> confidenceComboBox.setValue("Hard");
      case MASTER -> confidenceComboBox.setValue("Master");
    }
    // init word difficulty combobox
    switch (this.gameModel.getProfile().getSettingsData().getSetting()) {
      case EASY -> wordDifficultyComboBox.setValue("Easy");
      case MEDIUM -> wordDifficultyComboBox.setValue("Medium");
      case HARD -> wordDifficultyComboBox.setValue("Hard");
      case MASTER -> wordDifficultyComboBox.setValue("Master");
    }
  }

  /** Sets the game settings into the players profile */
  private void setGameSettings() {
    // sets confidence settings depending on users choice
    switch (confidenceComboBox.getValue()) {
      case "Easy" -> gameModel
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.HARD);
      case "Master" -> gameModel
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.MASTER);
    }
    // sets accuracy settings depending on users choice
    switch (accuracyComboBox.getValue()) {
      case "Easy" -> gameModel.getProfile().getSettingsData().setAccuracy(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          .getProfile()
          .getSettingsData()
          .setAccuracy(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel.getProfile().getSettingsData().setAccuracy(SettingsData.Levels.HARD);
    }
    // sets word difficulty settings depending on users choice
    switch (wordDifficultyComboBox.getValue()) {
      case "Easy" -> gameModel.getProfile().getSettingsData().setSetting(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          .getProfile()
          .getSettingsData()
          .setSetting(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel.getProfile().getSettingsData().setSetting(SettingsData.Levels.HARD);
      case "Master" -> gameModel
          .getProfile()
          .getSettingsData()
          .setSetting(SettingsData.Levels.MASTER);
    }
    // sets time settings depending on users choice
    switch (timeComboBox.getValue()) {
      case "Easy" -> gameModel.getProfile().getSettingsData().setTime(SettingsData.Levels.EASY);
      case "Medium" -> gameModel.getProfile().getSettingsData().setTime(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel.getProfile().getSettingsData().setTime(SettingsData.Levels.HARD);
      case "Master" -> gameModel.getProfile().getSettingsData().setTime(SettingsData.Levels.MASTER);
    }
  }

  /** Sets the game mode into the game model */
  private void setGameMode() {
    // depending on text of button set gamemode
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModel.setCurrentGameMode(GameModel.GameMode.CLASSIC);
      case "Hidden-Word Mode" -> gameModel.setCurrentGameMode(GameModel.GameMode.HIDDEN);
      case "Zen Mode" -> gameModel.setCurrentGameMode(GameModel.GameMode.ZEN);
    }
  }

  /** This method is called when user presses the start game button */
  @FXML
  public void onStartButton() throws IOException {
    setGameSettings();
    setGameMode();
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.CANVAS);
  }
  /** This method is called when user presses the right change gamemode button */
  @FXML
  public void onRightButton() {
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Hidden-Word Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Zen Mode");
      case "Zen Mode" -> gameModeButton.setText("Classic Mode");
    }
  }
  /** This method is called when user presses the left change gamemode button */
  @FXML
  public void onLeftButton() {
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Zen Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Classic Mode");
      case "Zen Mode" -> gameModeButton.setText("Hidden-Word Mode");
    }
  }
  /** This method is called when user mouse enters the confidence info area */
  @FXML
  public void onConfidenceInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Confidence:\n"
            + "Level easy: You win the game if the computer is at least 1% confident that your image is the word to guess.\n"
            + "Level medium: You win the game if the computer is at least 10% confident that your image is the word to guess.\n"
            + "Level hard: You win the game if the computer is at least 25% confident that your image is the word to guess.\n"
            + "Level master: You win the game if the computer is at least 50% confident that your image is the word to guess.");
  }
  /** This method is called when user mouse enters the time info area */
  @FXML
  public void onTimeInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Time limit:\n"
            + "Level easy: You gets 60 seconds to draw the picture correctly.\n"
            + "Level medium: You gets 45 seconds to draw the picture correctly.\n"
            + "Level hard: You gets 30 seconds to draw the picture correctly.\n"
            + "Level master: You gets 15 seconds to draw the picture correctly.");
  }
  /** This method is called when user mouse enters the accuracy info area */
  @FXML
  public void onAccuracyInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Accuracy:\n"
            + "Level easy: You win the game if the word to draw is in the computer's top 3 guesses.\n"
            + "Level medium: You win the game if the word to draw is in the computer's top 2 guesses.\n"
            + "Level hard: You win the game if the word to draw is the computer’s best guess.");
  }
  /** This method is called when user mouse enters the word difficulty info area */
  @FXML
  public void onWordDifficultyInfoEnter() {
    instructionsTextArea.setText(
        "Instructions-Word Difficulty:\n"
            + "Level easy: You will only get given EASY words.\n"
            + "Level medium: You will get given EASY or MEDIUM words.\n"
            + "Level hard: You will get given EASY or MEDIUM or HARD words\n"
            + "Level master: You will only get given HARD words.");
  }
  /** This method is called when user mouse enters the gamemode info area */
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
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");
  }

  @FXML
  public void onTimeInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");
  }

  @FXML
  public void onAccuracyInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");
  }

  @FXML
  public void onWordDifficultyInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");
  }

  @FXML
  public void onGameModeInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions for certain gamemodes or game difficulties");
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    setGameMode();
    setGameSettings();
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }
}
