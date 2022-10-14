package nz.ac.auckland.se206.model;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;
import nz.ac.auckland.se206.profiles.entities.WordsData;

/** Game model represents all logic related to a game. */
public class GameModel {
  /** Represents the different states a game could have */
  public enum State {
    READY,
    INGAME,
    FINISHED
  }

  /** Represents the different gamemodes */
  public enum GameMode {
    ZEN,
    CLASSIC,
    HIDDEN,
    LEARNING,
  }

  /** Represents the different view states */
  public enum ViewState {
    CANVAS,
    SELECTPROFILES,
    NEWPROFILE,
    SETTINGS,
    MAINMENU,
    PROFILESTATS,

    GAMEMODESETTINGS,
  }

  // Instance of the game. Uses singleton pattern
  private static GameModel gameModelInstance = new GameModel();

  /**
   * Get the instance of the game model. Using the singleton pattern.
   *
   * @return The game model.
   */
  public static GameModel getInstance() {
    return gameModelInstance;
  }

  private Profile profile;

  private DoodlePrediction doodlePrediction;
  private StringProperty currentWordToGuess;

  private ObjectProperty<State> currentGameState;

  private ObjectProperty<ViewState> currentViewState;

  private GameMode currentGameMode;

  private boolean playerWon;

  /** Initialize objects and anything needed for a game */
  private GameModel() {
    try {
      this.currentWordToGuess = new SimpleStringProperty();
      this.currentGameState = new SimpleObjectProperty<>(State.READY);
      this.currentViewState = new SimpleObjectProperty<>(ViewState.MAINMENU);
      this.doodlePrediction = new DoodlePrediction();
      this.playerWon = false;

      // Setup default profile
      this.currentGameMode = GameMode.CLASSIC;
      ProfileFactory profileFactory = new ProfileFactory();
      this.profile = profileFactory.selectProfile("Guest");

    } catch (Exception e) {
      System.err.println("FAILED TO INITIALIZE GAME MODEL.");
      e.printStackTrace();
    }
  }

  /** Generate a new word for the game AND SET IT */
  public void generateWord() {
    // Need a random number for randomisation of difficulty
    Random random = new Random();
    WordsData.Difficulty difficulty;
    int generated = random.nextInt(2);

    // Creates random difficulties depending on the setting of the user
    switch (this.profile.getSettingsData().getSetting()) {
      case EASY -> difficulty = WordsData.Difficulty.E;
      case MEDIUM -> difficulty = generated == 1 ? WordsData.Difficulty.E : WordsData.Difficulty.M;
      case HARD -> {
        // Hard must be able to get letters from all difficulties
        generated = random.nextInt(3);
        if (generated == 1) {
          difficulty = WordsData.Difficulty.E;
        } else if (generated == 2) {
          difficulty = WordsData.Difficulty.M;
        } else {
          difficulty = WordsData.Difficulty.H;
        }
      }
      case MASTER -> difficulty = WordsData.Difficulty.H;
        // In the case that settings data is null
      default -> difficulty = null;
    }
    ;
    try {
      // Get the words from the user profile
      setCurrentWordToGuess(this.profile.getWordsData().getRandomWord(difficulty));
    } catch (URISyntaxException | IOException e) {
      // If unexpected err occurs
      System.err.println("Failed to generate words");
      throw new RuntimeException(e);
    }
    getCurrentWordToGuess();
  }

  /**
   * Returns a list of predictions based on the image
   *
   * @param image The image to predict
   * @param totalPredictions Total predictions in list
   * @return List of predictions.
   * @throws TranslateException Failed to read image
   */
  public List<Classifications.Classification> getPredictions(
      BufferedImage image, int totalPredictions) throws TranslateException {
    return doodlePrediction.getPredictions(image, totalPredictions);
  }

  /////////////////////////
  // Getters and setters //
  /////////////////////////
  public String getCurrentWordToGuess() {
    return currentWordToGuess.get();
  }

  public StringProperty getCurrentWordToGuessProperty() {
    return currentWordToGuess;
  }

  public void setCurrentWordToGuess(String currentWordToGuess) {
    this.currentWordToGuess.setValue(currentWordToGuess);
  }

  public State getCurrentGameState() {
    return currentGameState.get();
  }

  public ViewState getCurrentViewState() {
    return currentViewState.get();
  }

  public GameMode getCurrentGameMode() {
    return this.currentGameMode;
  }

  public void setCurrentGameState(State currentGameState) {
    this.currentGameState.set(currentGameState);
  }

  public void setCurrentViewState(ViewState currentViewState) {
    this.currentViewState.set(currentViewState);
  }

  public void setCurrentGameMode(GameMode gameMode) {
    this.currentGameMode = gameMode;
  }

  public ObjectProperty<State> getCurrentGameStateProperty() {
    return this.currentGameState;
  }

  public ObjectProperty<ViewState> getCurrentViewStateProperty() {
    return this.currentViewState;
  }

  public boolean isPlayerWon() {
    return playerWon;
  }

  public void setPlayerWon(boolean playerWon) {
    this.playerWon = playerWon;
  }

  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }
}
