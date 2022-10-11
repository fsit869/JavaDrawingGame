package nz.ac.auckland.se206.dictionary;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.controllers.GameController;
import nz.ac.auckland.se206.model.GameModel;

public class DictionaryThread {
  private GameModel gameModel;
  private TextArea definitionTextArea;
  private Service<Void> backgroundService;

  /**
   * Constructor which creates the searching algorithm
   *
   * @param definitionTextArea
   * @param gameController
   */
  public DictionaryThread(TextArea definitionTextArea, GameController gameController) {
    this.gameModel = GameModel.getInstance();
    this.definitionTextArea = definitionTextArea;

    // Create the reusable service to search dictonary.
    this.backgroundService =
        new Service<Void>() {
          @Override
          protected Task<Void> createTask() {
            return new Task<Void>() {
              @Override
              protected Void call() {
                updateProgress(0, 1);
                String query = gameModel.getCurrentWordToGuess();

                String textToDisplay;
                try {
                  // Search for word and update to label
                  WordInfo wordResult = DictionaryLookUp.searchWordInfo(query);
                  textToDisplay = wordResult.getWordEntries().get(0).getDefinitions().get(0);
                  gameController.disablePlayButton(false);
                } catch (WordNotFoundException e) {
                  // Word not found
                  e.printStackTrace();
                  textToDisplay = "No definitions found for " + query;
                } catch (Exception e) {
                  // Unknown error
                  e.printStackTrace();
                  textToDisplay = "Unexpected err querying  " + query;
                }

                String finalTextToDisplay = textToDisplay;
                Platform.runLater(
                    () -> {
                      definitionTextArea.setText(finalTextToDisplay);
                    });

                updateProgress(1, 1);
                return null;
              }
            };
          }
        };
  }

  /** Method that is called to start searching definition of a word and upate it to the textArea */
  public void startDefining() {
    this.backgroundService.cancel();
    this.backgroundService.reset();
    this.backgroundService.start();
  }
}
