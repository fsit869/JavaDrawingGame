package nz.ac.auckland.se206.profiles.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class WordsData extends Data {
  private Map<Difficulty, ArrayList<String>> wordsNotPlayed = new HashMap<>();
  private ArrayList<String> wordsPlayed = new ArrayList<>();

  public WordsData() {
    wordsNotPlayed.put(Difficulty.H, new ArrayList<>());
    wordsNotPlayed.put(Difficulty.M, new ArrayList<>());
    wordsNotPlayed.put(Difficulty.E, new ArrayList<>());
  }

  @Override
  public void eraseData() {
    this.wordsPlayed.clear();
    this.wordsNotPlayed.get(Difficulty.E).clear();
    this.wordsNotPlayed.get(Difficulty.M).clear();
    this.wordsNotPlayed.get(Difficulty.H).clear();
  }
}
