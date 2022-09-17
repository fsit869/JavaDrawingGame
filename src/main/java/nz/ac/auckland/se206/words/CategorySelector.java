package nz.ac.auckland.se206.words;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/** Class to extract words from category_difficulty.csv */
@Deprecated
public class CategorySelector {
  private HashMap<Difficulty, ArrayList<String>> words;

  public enum Difficulty {
    E,
    M,
    H
  }

  /**
   * Object to get words
   *
   * @throws URISyntaxException Error by invalid file location
   * @throws IOException Error by IO
   */
  @Deprecated
  public CategorySelector() throws URISyntaxException, IOException {
    words = new HashMap<>();

    // Load Difficulties into Hashmap
    for (Difficulty difficulty : Difficulty.values()) {
      words.put(difficulty, new ArrayList<String>());
    }

    // Load words into hashmap
    for (String[] line : getLines()) {
      words.get(Difficulty.valueOf(line[1])).add(line[0]);
    }
  }

  /**
   * Get a random word based on difficulty from CSV file
   *
   * @param difficulty Enum difficulty/
   * @return A random word based on the difficulty.
   */
  @Deprecated
  public String getRandomCategory(Difficulty difficulty) {
    return words.get(difficulty).get(new Random().nextInt(words.get(difficulty).size()));
  }

  /**
   * Read all lines of the csv file.
   *
   * @return Array of all the lines in the file
   * @throws URISyntaxException File not found
   * @throws IOException IO error.
   */
  @Deprecated
  protected List<String[]> getLines() throws URISyntaxException, IOException {
    // Read files line by line and return
    File file = new File(CategorySelector.class.getResource("/category_difficulty.csv").toURI());
    CSVReader reader = new CSVReader(new FileReader(file));
    return reader.readAll();
  }
}
