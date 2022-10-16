package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.SettingsData;
import nz.ac.auckland.se206.speech.SoundEffect;
import nz.ac.auckland.se206.speech.TextToSpeechTask;

public class GameModeSettingsController implements ControllerInterface {

  private GameModel gameModel;
  private TextToSpeechTask textToSpeechTask;
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

  @FXML private Button gameModeButton;

  /** Init and loads the gamemode settings view */
  public void initialize() {
    doRefresh();
    // init comboboxes items
    this.textToSpeechTask = new TextToSpeechTask();
    confidenceComboBox.getItems().addAll(confidenceDifficulties);
    timeComboBox.getItems().addAll(timeDifficulties);
    accuracyComboBox.getItems().addAll(accuracyDifficulties);
    wordDifficultyComboBox.getItems().addAll(difficultyDifficulties);

    // Setup styling of the boxes
    confidenceComboBox.setStyle("-fx-font: 15px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    timeComboBox.setStyle("-fx-font: 15px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    accuracyComboBox.setStyle("-fx-font: 15px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");
    wordDifficultyComboBox.setStyle(
        "-fx-font: 15px \"Monospac821 BT\";-fx-background-color:  #8ff7a7");

    // Instruction styled
    instructionsTextArea.setStyle("-fx-text-fill: black;");
  }

  @Override
  public void refresh() {
    doRefresh();
  }

  /** On the instance of a refresh this function is run. */
  public void doRefresh() {
    // Create instance of object saving
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");

    // Get game settings
    this.gameModel = GameModel.getInstance();

    // init gameMode
    switch (this.gameModel.getCurrentGameMode()) {
      case CLASSIC -> gameModeButton.setText("Classic Mode");
      case HIDDEN -> gameModeButton.setText("Hidden-Word Mode");
      case ZEN -> gameModeButton.setText("Zen Mode");
      case LEARNING -> gameModeButton.setText("Learning Mode");
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
    disableUiIfLearning();
  }

  /** Sets the game settings into the players profile */
  private void setGameSettings() {
    // sets confidence settings depending on users choice
    switch (confidenceComboBox.getValue()) {
      case "Easy" -> gameModel
          // Easy level
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          // Medium level
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel
          // Hard level
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.HARD);
      case "Master" -> gameModel
          // Master level
          .getProfile()
          .getSettingsData()
          .setConfidence(SettingsData.Levels.MASTER);
    }
    // sets accuracy settings depending on users choice
    switch (accuracyComboBox.getValue()) {
      case "Easy" -> gameModel.getProfile().getSettingsData().setAccuracy(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          // Medium level
          .getProfile()
          .getSettingsData()
          .setAccuracy(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel
          // Hard level
          .getProfile()
          .getSettingsData()
          .setAccuracy(SettingsData.Levels.HARD);
    }
    // sets word difficulty settings depending on users choice
    switch (wordDifficultyComboBox.getValue()) {
      case "Easy" -> gameModel.getProfile().getSettingsData().setSetting(SettingsData.Levels.EASY);
      case "Medium" -> gameModel
          // Medium level
          .getProfile()
          .getSettingsData()
          .setSetting(SettingsData.Levels.MEDIUM);
      case "Hard" -> gameModel.getProfile().getSettingsData().setSetting(SettingsData.Levels.HARD);
      case "Master" -> gameModel
          // Master level
          .getProfile()
          .getSettingsData()
          .setSetting(SettingsData.Levels.MASTER);
    }
    // sets time settings depending on users choice
    switch (timeComboBox.getValue()) {
      case "Easy" -> gameModel
          // Easy level
          .getProfile()
          .getSettingsData()
          .setTime(SettingsData.Levels.EASY);
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
      case "Learning Mode" -> gameModel.setCurrentGameMode(GameModel.GameMode.LEARNING);
    }
  }

  /** This method is called when user presses the start game button */
  @FXML
  private void onStartButton() throws IOException {
    // play sound effect
    SoundEffect.playEffectOverride(SoundEffect.SOUND.CLICK);
    setGameSettings();
    setGameMode();
    // save profile
    this.profileFactory = new ProfileFactory();
    profileFactory.saveProfile(gameModel.getProfile());
    textToSpeechTask.speak("Lesh go");
    gameModel.setCurrentViewState(GameModel.ViewState.CANVAS);
  }

  /** This method is called when user presses the right change gamemode button */
  @FXML
  private void onRightButton() {
    SoundEffect.playEffectOverride(SoundEffect.SOUND.CLICK);
    // Select game mode right
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Hidden-Word Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Zen Mode");
      case "Zen Mode" -> gameModeButton.setText("Learning Mode");
      case "Learning Mode" -> gameModeButton.setText("Classic Mode");
    }
    textToSpeechTask.speak(gameModeButton.getText());
    disableUiIfLearning();
  }

  /** This method is called when user presses the left change gamemode button */
  @FXML
  private void onLeftButton() {
    SoundEffect.playEffectOverride(SoundEffect.SOUND.CLICK);
    // Select game mode left
    switch (gameModeButton.getText()) {
      case "Classic Mode" -> gameModeButton.setText("Learning Mode");
      case "Hidden-Word Mode" -> gameModeButton.setText("Classic Mode");
      case "Zen Mode" -> gameModeButton.setText("Hidden-Word Mode");
      case "Learning Mode" -> gameModeButton.setText("Zen Mode");
    }
    textToSpeechTask.speak(gameModeButton.getText());
    disableUiIfLearning();
  }

  /** This method is called when user mouse enters the confidence info area */
  @FXML
  private void onConfidenceInfoEnter() {
    // Shows info about confidence
    instructionsTextArea.setText(
        "Instructions-Confidence:\n"
            // Below are the different modes
            + "Level EASY: You win the game if the computer thinks your drawing is the word to draw.\n"
            + "Level MEDIUM: You win the game if the computer thinks your drawing is the word to draw and not bad.\n"
            + "Level HARD: You win the game if the computer thinks your drawing is the word to draw and is good.\n"
            + "Level MASTER: You win the game if the computer thinks your drawing is the word to draw and is very good.");
  }

  /** This method is called when user mouse enters the time info area */
  @FXML
  private void onTimeInfoEnter() {
    // Show the info of time setting
    instructionsTextArea.setText(
        "Instructions-Time limit:\n"
            // Has easy to master levels
            + "Level EASY: You gets 60 seconds to draw the picture correctly.\n"
            + "Level MEDIUM: You gets 45 seconds to draw the picture correctly.\n"
            + "Level HARD: You gets 30 seconds to draw the picture correctly.\n"
            + "Level MASTER: You gets 15 seconds to draw the picture correctly.");
  }

  /** This method is called when user mouse enters the accuracy info area */
  @FXML
  private void onAccuracyInfoEnter() {
    // Set the text of accuracy
    instructionsTextArea.setText(
        "Instructions-Accuracy:\n"
            // Below is levels easy to hard
            + "Level EASY: You win the game if the word to draw is in the computer's top 3 guesses.\n"
            + "Level MEDIUM: You win the game if the word to draw is in the computer's top 2 guesses.\n"
            + "Level HARD: You win the game if the word to draw is the computer’s best guess.");
  }

  /** This method is called when user mouse enters the word difficulty info area */
  @FXML
  private void onWordDifficultyInfoEnter() {
    // Set the text for difficulities
    instructionsTextArea.setText(
        "Instructions-Word Difficulty:\n"
            // Below are the modes easy to master
            + "Level EASY: You will only get given EASY words.\n"
            + "Level MEDIUM: You will get given EASY or MEDIUM words.\n"
            + "Level HARD: You will get given EASY or MEDIUM or HARD words\n"
            + "Level MASTER: You will only get given HARD words.");
  }

  /** This method is called when user mouse enters the gamemode info area */
  @FXML
  private void onGameModeInfoEnter() {
    // Determine which gamemode is selected and set it
    switch (this.gameModeButton.getText()) {
      case "Classic Mode" -> instructionsTextArea.setText(
          // Classic. Also show the info about the mode
          "Instructions-Classic Gamemode:\n"
              + "The aim is to draw the image that you are told in the prompt so that the computer is able to guess that what you have drawn is the prompt. You need to achieve this before the timer runs out.");

      case "Hidden-Word Mode" -> instructionsTextArea.setText(
          // Hidden Also show the info about the mode
          "Instructions-Hidden-Word Gamemode:\n"
              + "The word to draw will not be shown to you. Instead you will be provided the definition of the word to draw. If you get stuck and can't figure out the word use the Hint button to show the number of characters in the word.");

      case "Zen Mode" -> instructionsTextArea.setText(
          // Zen Also show the info about the mode
          "Instructions-Zen Gamemode:\n"
              + "This is an endless mode where there is no timer. Spend as much time as you want to perfect your drawing then either save your masterpiece or press next word to generate a new word to draw.");
      case "Learning Mode" -> instructionsTextArea.setText(
          // Learning Also show the info about the mode
          "Instructions-Zen Gamemode:\n"
              + "In this mode you can draw whatever you want and you will be shown the definition of the object that the computer thinks your drawing is the closest to.");
    }
  }

  @FXML
  private void onConfidenceInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");
  }

  @FXML
  private void onTimeInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");
  }

  @FXML
  private void onAccuracyInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");
  }

  @FXML
  private void onWordDifficultyInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");
  }

  @FXML
  private void onGameModeInfoExit() {
    instructionsTextArea.setText(
        "Hover over the i to see instructions on how to play certain gamemodes or game difficulties");
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    // play sound effect
    SoundEffect.playEffectOverride(SoundEffect.SOUND.CLICK);
    setGameMode();
    setGameSettings();
    // save current profile
    this.profileFactory = new ProfileFactory();
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }

  /** This method disables the comboboxes if it is in learning mode */
  private void disableUiIfLearning() {
    if (this.gameModeButton.getText().equals("Learning Mode")) {
      // Is learning mode
      this.confidenceComboBox.setDisable(true);
      this.accuracyComboBox.setDisable(true);
      this.timeComboBox.setDisable(true);
      this.wordDifficultyComboBox.setDisable(true);
    } else {
      // Not learning mode
      this.confidenceComboBox.setDisable(false);
      this.accuracyComboBox.setDisable(false);
      this.timeComboBox.setDisable(false);
      this.wordDifficultyComboBox.setDisable(false);
    }
  }
}
