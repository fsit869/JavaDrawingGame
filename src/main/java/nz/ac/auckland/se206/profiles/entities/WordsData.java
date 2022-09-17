package nz.ac.auckland.se206.profiles.entities;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class WordsData extends Data {
  private Map<Difficulty, ArrayList<String>> wordsNotPlayed;
  private List<String> wordsPlayed;

  public enum Difficulty {
    E,
    M,
    H
  }

  public List<String> getWordsPlayed() {
    return wordsPlayed;
  }

  /**
   * Grabs a random word from the specified difficulty, and appends it to words played.
   *
   * @param difficulty of the words to choose from
   * @return A random word
   * @throws URISyntaxException Syntax
   * @throws IOException IO
   */
  public String getRandomWord(Difficulty difficulty) throws URISyntaxException, IOException {
    String random;
    List<String> wordsList = this.wordsNotPlayed.get(difficulty);

    // Check if wordsList has elements to pop
    if (wordsList.isEmpty()) {

      // Loops through all the words in the CSV file, appends all the right difficulty words into
      // file.
      for (String[] line : selector()) {

        // Append words in the right difficulty into list
        if (Difficulty.valueOf(line[1]) == difficulty) {
          wordsList.add(line[0]);
        }
      }
    }

    // Removes the word from the wordslist,
    random = wordsList.remove(new Random().nextInt(0, wordsList.size()));
    wordsPlayed.add(random);
    return random;
  }

  /**
   * Selects from the CSV file all the words possible.
   *
   * @return List of all the words
   * @throws IOException IO
   * @throws URISyntaxException Syntax
   */
  public List<String[]> selector() throws IOException, URISyntaxException {
    File file = new File(WordsData.class.getResource("/category_difficulty.csv").toURI());
    CSVReader reader = new CSVReader(new FileReader(file));
    return reader.readAll();
  }

  @Override
  public void resetData() {
    this.wordsPlayed.clear();
    this.wordsNotPlayed.get(Difficulty.E).clear();
    this.wordsNotPlayed.get(Difficulty.M).clear();
    this.wordsNotPlayed.get(Difficulty.H).clear();
  }
}
