package nz.ac.auckland.se206.model;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import nz.ac.auckland.se206.ml.DoodlePrediction;
import nz.ac.auckland.se206.words.CategorySelector;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/** Game model represents all logic related to a game. */
public class GameModel {
  private CategorySelector categorySelector;
  private DoodlePrediction doodlePrediction;

  private StringProperty currentWordToGuess;
  private ObjectProperty<State> currentGameState;
  private boolean playerWon;

  /** Represents the different states a game could have */
  public enum State {
    READY,
    INGAME,
    FINISHED
  }

  /**
   * Initialize objects and anything needed for a game
   *
   * @param doodlePrediction The object used to predict doodles
   * @throws URISyntaxException Occurs states invalid
   * @throws IOException Occurs states invalid
   */
  public GameModel(DoodlePrediction doodlePrediction) throws URISyntaxException, IOException {
    this.categorySelector = new CategorySelector();
    this.currentWordToGuess = new SimpleStringProperty();
    this.currentGameState = new SimpleObjectProperty<>(State.READY);
    this.doodlePrediction = doodlePrediction;
    this.playerWon = false;
  }

  /**
   * Generate a new word for the game
   *
   * @param difficulty Difficulty of word
   * @return Word that needs to be drawn
   */
  public String generateWord(CategorySelector.Difficulty difficulty) {
    setCurrentWordToGuess(categorySelector.getRandomCategory(difficulty));
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

  public void setCurrentGameState(State currentGameState) {
    this.currentGameState.set(currentGameState);
  }

  public ObjectProperty<State> getCurrentGameStateProperty() {
    return this.currentGameState;
  }

  public boolean isPlayerWon() {
    return playerWon;
  }

  public void setPlayerWon(boolean playerWon) {
    this.playerWon = playerWon;
  }
}
