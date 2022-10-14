package nz.ac.auckland.se206.model;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.controllers.GameController;
import nz.ac.auckland.se206.speech.SoundEffect;
import nz.ac.auckland.se206.dictionary.DictionaryThread;

/** Represents a timer on a different task and any actions to be done on it. */
public class TimerTask extends Task<Void> {
  public static final int TOTAL_PREDICTIONS = 10;

  private int accuracy;
  private int confidence;

  private int timerTotal;
  private int counter;
  private Label timerLabel;
  private TextArea predicationTextArea;
  private StringBuilder stringBuilder;
  private GameModel gameModel;
  private GameController canvasController;
  private DictionaryThread dictonaryThread;
  private int dictonaryCounter;

  /**
   * Initialize a new timer thread and anything related to it
   *
   * @param timerTotal The start of timer value
   * @param label Label the timer should display on
   * @param predictionTextArea TextArea the prediction text should be rendered
   * @param gameModel Model of the game
   * @param canvasController The canvas to get image from
   */
  public TimerTask(
      int timerTotal,
      Label label,
      TextArea predictionTextArea,
      GameModel gameModel,
      GameController canvasController,
      DictionaryThread dictonaryThread) {
    this.timerTotal = timerTotal;
    this.counter = timerTotal;
    this.timerLabel = label;
    this.predicationTextArea = predictionTextArea;
    this.stringBuilder = new StringBuilder();
    this.gameModel = gameModel;
    this.canvasController = canvasController;
    this.dictonaryThread = dictonaryThread;
    this.dictonaryCounter = 0;
  }

  /**
   * Overrides Task Call() method. Code that will be run on different thread.
   *
   * @return Void, Nothing.
   */
  @Override
  protected Void call() {
    while (true) {
      // Break if asked to cancel
      if (this.isCancelled()) {
        break;
      }

      // Timer finished exit condition
      if (counter == 0) {
        break;
      }

      updateMessage(String.valueOf(counter));
      updateProgress(counter, this.timerTotal);

      // Handle timer and predictions
      Platform.runLater(
          () -> {
            try {
              handlePredictions();
            } catch (TranslateException e) {
              e.printStackTrace();
            }
            generatePredictionRating();
            // If zen mode dont show timer label
            if (gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
              timerLabel.setText("Zen mode!");
            } else if (gameModel.getCurrentGameMode().equals(GameModel.GameMode.LEARNING)) {
              timerLabel.setText("Learning mode!");
            } else {
              timerLabel.setText(String.valueOf(counter));
            }
            predicationTextArea.setText(stringBuilder.toString());
          });

      // If zen mode or learning mode dont decrement timer.
      if (!gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)
          && !gameModel.getCurrentGameMode().equals(GameModel.GameMode.LEARNING)) {
        counter--;
      }
      try {
        Thread.sleep(1000);
        this.dictonaryCounter++;
      } catch (InterruptedException e) {
        if (this.isCancelled()) {
          break;
        }
        e.printStackTrace();
      }
    }
    return null;
  }

  /** This method occurs if task is cancelled. */
  @Override
  protected void cancelled() {
    updateMessage("The task was cancelled.");
    super.cancelled();
  }

  /** This method occurs if task is failed. */
  @Override
  protected void failed() {
    updateMessage("The task failed.");
    super.failed();
  }

  /** This method occurs if task is successful. */
  @Override
  public void succeeded() {
    updateMessage("The task finishedd successfully.");
    super.succeeded();
  }

  /**
   * This method handles the win condition and generates prediction string to the stringbuilder.
   *
   * @throws TranslateException Prediction error
   */
  private void handlePredictions() throws TranslateException {
    // Generate string
    stringBuilder.setLength(0);
    stringBuilder.append("Current Predictions\n");

    List<Classifications.Classification> predictions =
        gameModel.getPredictions(canvasController.getCurrentSnapshot(), TOTAL_PREDICTIONS);
    generatePredictionString(predictions);

    // Setup accuracy based on game
    switch (this.gameModel.getProfile().getSettingsData().getAccuracy()) {
      case EASY -> this.accuracy = 3;
      case MEDIUM -> this.accuracy = 2;
      case HARD -> this.accuracy = 1;
    }

    // Check won
    if (getWinCondition(predictions, this.accuracy)
        && canvasController.isStartedDrawing()
        && !this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.LEARNING)) {
      this.gameModel.setPlayerWon(true);
      // If zen mode dont transition
      if (!this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
        gameModel.setCurrentGameState(GameModel.State.FINISHED);
      }
    }

    // Only do this every 2 seconds. To prevent it from spamming
    if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.LEARNING)
        && this.dictonaryCounter % 2 == 0) {
      // Get top prediction
      String currentTopPredictionWord = predictions.get(0).getClassName().replace("_", " ");
      ;

      // If different from previous prediction change it.
      if (gameModel.getCurrentWordToGuess().equals(currentTopPredictionWord)) {
        // Same word. We ignore it
      } else {
        // Different word. Change it start defining
        if (canvasController.isStartedDrawing()) {
          canvasController.setWordLabel(currentTopPredictionWord);
          canvasController.setDrawLabel(false);
          gameModel.setCurrentWordToGuess(currentTopPredictionWord);
          this.dictonaryThread.startDefining();
        }
      }
    }

    this.gameModel.setPlayerWon(false);
  }

  /**
   * Checks whether the AI has guessed the correct prediction
   *
   * @param predictions List of predictions to check through
   * @param maxPredictions Out of the predictions, it will check only maxPredictions
   * @return Player wins
   */
  private boolean getWinCondition(
      List<Classifications.Classification> predictions, int maxPredictions) {
    int counter = 0;
    String wordToDraw = gameModel.getCurrentWordToGuess();

    // Iterate and check each prediction
    for (final Classifications.Classification classification : predictions) {

      // Prediction string uses _ instead of spaces for words with gaps
      if (counter >= maxPredictions) {
        break;
      }

      String predictedWord = classification.getClassName().replace("_", " ");
      int probabilityValue = (int) Math.ceil(classification.getProbability() * 100);

      // Find confidence required to win
      switch (this.gameModel.getProfile().getSettingsData().getConfidence()) {
        case EASY -> this.confidence = 1;
        case MEDIUM -> this.confidence = 10;
        case HARD -> this.confidence = 25;
        case MASTER -> this.confidence = 50;
      }

      // Check if win
      if (predictedWord.equals(wordToDraw)) {
        // The word is met
        this.canvasController.setAccuracyLabelMet(true);

        // Check if confidence is met
        if (probabilityValue >= this.confidence
            && !this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.LEARNING)) {
          this.canvasController.setConfidenceLabelMet(true);
          this.canvasController.setAccuracyValue(probabilityValue);
          this.canvasController.setCorrectImageVisible(true);
          this.canvasController.setWrongImageVisible(false);
          return true;
        } else {
          this.canvasController.setConfidenceLabelMet(false);
          // If in zen mode show the wrong/correct
          if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
            this.canvasController.setCorrectImageVisible(false);
            this.canvasController.setWrongImageVisible(true);
          }

          return false;
        }
      }

      counter++;
    }
    this.canvasController.setAccuracyLabelMet(false);
    this.canvasController.setConfidenceLabelMet(false);

    // If in zen mode show the wrong/correct
    if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
      this.canvasController.setCorrectImageVisible(false);
      this.canvasController.setWrongImageVisible(true);
    }

    // If zen mode show the wrong/right in game live
    return false;
  }

  /**
   * Generates a rating, 1-10 1 being in the top 10, to how close it to the top 10.
   *
   * @return prediction rating of how close
   */
  private int generatePredictionRating() {
    List<Classifications.Classification> yes;
    // default if not found (above 100)
    int distance = 10;

    try {
      // get the top 100 predictions
      yes = gameModel.getPredictions(canvasController.getCurrentSnapshot(), 100);
    } catch (TranslateException e) {
      throw new RuntimeException(e);
    }

    // loops through the top 100 and sees if word is in it
    for (Classifications.Classification i : yes) {
      if (i.getClassName().equals(gameModel.getCurrentWordToGuess())) {
        return yes.indexOf(i) / 10 + 1;
      }
    }

    return distance;
  }

  /**
   * Generates the strings of prediction used in the textArea
   *
   * @param predictions List of predictions to generate string of predictions.
   */
  private void generatePredictionString(List<Classifications.Classification> predictions) {
    // Stop prediction text if player has not started drawing
    if (!canvasController.isStartedDrawing()) {
      stringBuilder.append("Nothing. Start drawing!");
    } else {
      // Iterate through each prediction and add to string
      for (final Classifications.Classification classification : predictions) {
        // Generate for textarea
        stringBuilder.append(
            String.format(
                "[%d%%] %-20s\n",
                Math.round(100 * classification.getProbability()),
                classification.getClassName().replace("_", " ")));
      }
    }
  }
}
