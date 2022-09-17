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

  public String getRandomWord(Difficulty difficulty) throws URISyntaxException, IOException {
    String random = "";
    List<String> wordsList = this.wordsNotPlayed.get(difficulty);
    if (wordsList.isEmpty()) {
      for (String[] line : selector()) {
        if (Difficulty.valueOf(line[1]) == difficulty) {
          wordsList.add(line[0]);
        }
      }
    }
    random = wordsList.remove(new Random().nextInt(0, wordsList.size()));
    wordsPlayed.add(random);
    return random;
  }

  public List<String[]> selector() throws IOException, URISyntaxException {
    File file = new File(WordsData.class.getResource("/category_difficulty.csv").toURI());
    CSVReader reader = new CSVReader(new FileReader(file));
    return reader.readAll();
  }

  @Override
  public void eraseData() {
    this.wordsPlayed.clear();
    this.wordsNotPlayed.get(Difficulty.E).clear();
    this.wordsNotPlayed.get(Difficulty.M).clear();
    this.wordsNotPlayed.get(Difficulty.H).clear();
  }
}
