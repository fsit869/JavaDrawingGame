package nz.ac.auckland.se206.model;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.profiles.entities.Profile;
import nz.ac.auckland.se206.profiles.entities.WordsData;

/** Game model represents all logic related to a game. */
public class GameModel {
  // Instance of the game. Uses singleton pattern
  private static GameModel gameModelInstance = new GameModel();

  private Profile profile;

  private DoodlePrediction doodlePrediction;

  private StringProperty currentWordToGuess;
  private ObjectProperty<State> currentGameState;

  private ObjectProperty<viewState> currentViewState;

  //  private Profile currentProfile;
  private boolean playerWon;

  /** Represents the different states a game could have */
  public enum State {
    READY,
    INGAME,
    FINISHED
  }

  /** Represents the different view states */
  public enum viewState {
    CANVAS,
    SELECTPROFILES,
    NEWPROFILE,
    SETTINGS,
    MAINMENU,
    PROFILESTATS,
  }

  /**
   * Initialize objects and anything needed for a game
   *
   * @throws URISyntaxException Occurs states invalid
   * @throws IOException Occurs states invalid
   */
  private GameModel() {
    try {
      this.currentWordToGuess = new SimpleStringProperty();
      this.currentGameState = new SimpleObjectProperty<>(State.READY);
      this.currentViewState = new SimpleObjectProperty<>(viewState.MAINMENU);
      this.doodlePrediction = new DoodlePrediction();
      this.playerWon = false;
    } catch (Exception e) {
      System.err.println("FAILED TO INITIALIZE GAME MODEL.");
      e.printStackTrace();
    }
  }

  /**
   * Generate a new word for the game
   *
   * @param difficulty Difficulty of word
   * @return Word that needs to be drawn
   */
  public String generateWord(WordsData.Difficulty difficulty) {
    try {
      setCurrentWordToGuess(this.profile.getWordsData().getRandomWord(difficulty));
    } catch (URISyntaxException | IOException e) {
      System.err.println("Failed to generate words");
      throw new RuntimeException(e);
    }
    return getCurrentWordToGuess();
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

  public viewState getCurrentViewState() {
    return currentViewState.get();
  }

  public void setCurrentGameState(State currentGameState) {
    this.currentGameState.set(currentGameState);
  }

  public void setCurrentViewState(viewState currentViewState) {
    this.currentViewState.set(currentViewState);
  }

  public ObjectProperty<State> getCurrentGameStateProperty() {
    return this.currentGameState;
  }

  public ObjectProperty<viewState> getCurrentViewStateProperty() {
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

  ///////////////////////
  // Singleton pattern //
  ///////////////////////

  /**
   * Get the instance of the game model. Using the singleton pattern.
   *
   * @return The game model.
   */
  public static GameModel getInstance() {
    return gameModelInstance;
  }
}
