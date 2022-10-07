package nz.ac.auckland.se206.dictionary;

import java.io.IOException;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.TextArea;
import nz.ac.auckland.se206.model.GameModel;

public class DictionaryThread {
  private GameModel gameModel;
  private TextArea definitionTextArea;
  private Service<Void> backgroundService;

  public DictionaryThread(TextArea definitionTextArea) {
    this.gameModel = GameModel.getInstance();
    this.definitionTextArea = definitionTextArea;

    this.backgroundService = new Service<Void>() {
      @Override
      protected Task<Void> createTask() {
        return new Task<Void>() {
          @Override
          protected Void call() {
            System.out.println("Thread started");
            updateProgress(0, 1);
            String query = gameModel.getCurrentWordToGuess();
            System.out.println("Searching for " + query);
            try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            Platform.runLater(() -> {
              try {
                WordInfo wordResult = DictionaryLookUp.searchWordInfo(query);
                System.out.println(wordResult.getWordEntries().get(0).getDefinitions().get(0));
                System.out.println("ww");


                definitionTextArea.setText(wordResult.getWordEntries().get(0).getDefinitions().get(0));
              } catch (Exception e) {
                e.printStackTrace();
                definitionTextArea.setText("Unable to load word. ");
              }
            });
            updateProgress(1, 1);
            return null;
          }
        };
      }
    };

  }

  public void startDefining() {
    this.backgroundService.cancel();
    this.backgroundService.reset();
    this.backgroundService.start();
  }

}
