package nz.ac.auckland.se206.dictionary;

import java.io.IOException;
import javafx.concurrent.Task;

public class DictionaryThread {

  private String wordToDefine;
  Task<Void> backgroundTask =
      new Task<Void>() {

        @Override
        protected Void call() throws Exception {

          String query = wordToDefine;
          try {
            WordInfo wordResult = DictionaryLookUp.searchWordInfo(query);
            System.out.println(
                "\""
                    + wordResult.getWord()
                    + "\" has "
                    + wordResult.getNumberOfEntries()
                    + " dictionary entries.");
            for (int x = 0; x < wordResult.getWordEntries().get(0).getDefinitions().size(); x++) {
              System.out.println(
                  "\""
                      + wordResult.getWord()
                      + "\" means "
                      + wordResult.getWordEntries().get(0).getDefinitions().get(x));
            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (WordNotFoundException e) {
            System.out.println("\"" + e.getWord() + "\" has problems: " + e.getMessage());
          }

          return null;
        }
      };

  public void startDefining() {
    try {
      Thread backgroundPerson = new Thread(backgroundTask);
      backgroundPerson.start();
    } catch (Exception e) {
      System.out.println("no word set to define");
    }
  }

  public void setWordToDefine(String wordToDefine) {
    this.wordToDefine = wordToDefine;
  }
}
