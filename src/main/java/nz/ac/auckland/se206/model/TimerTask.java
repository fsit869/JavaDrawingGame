package nz.ac.auckland.se206.model;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.CanvasController;

/** Represents a timer on a different task and any actions to be done on it. */
public class TimerTask extends Task<Void> {
  private static final int TOTAL_PREDICTIONS = 10;

  private int timerTotal;
  private int counter;
  private Label timerLabel;
  private TextArea predicationTextArea;
  private StringBuilder stringBuilder;
  private GameModel gameModel;
  private CanvasController canvasController;

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
      CanvasController canvasController) {
    this.timerTotal = timerTotal;
    this.counter = timerTotal;
    this.timerLabel = label;
    this.predicationTextArea = predictionTextArea;
    this.stringBuilder = new StringBuilder();
    this.gameModel = gameModel;
    this.canvasController = canvasController;
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
            timerLabel.setText(String.valueOf(counter));
            predicationTextArea.setText(stringBuilder.toString());
          });
      counter--;

      try {
        Thread.sleep(1000);
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

    // Check won
    if (getWinCondition(predictions, 3)) {
      this.gameModel.setPlayerWon(true);
      gameModel.setCurrentGameState(GameModel.State.FINISHED);
    }
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
      if (predictedWord.equals(wordToDraw)) {
        return true;
      }

      counter++;
    }
    return false;
  }

  /**
   * Generates the strings of prediction used in the textArea
   *
   * @param predictions List of predictions to generate string of predictions.
   */
  private void generatePredictionString(List<Classifications.Classification> predictions) {
    // Iterate through each prediction and add to string
    for (final Classifications.Classification classification : predictions) {
      // Generate for textarea
      stringBuilder.append(
          String.format(
              "[%3.2f%%] %-20s\n",
              100 * classification.getProbability(),
              classification.getClassName().replace("_", " ")));
    }
  }
}
