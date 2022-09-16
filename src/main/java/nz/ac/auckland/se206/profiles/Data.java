package nz.ac.auckland.se206.profiles;

// model of what will be stored as user data in a profile

import java.util.ArrayList;
import java.util.HashMap;
import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class Data {
  private ArrayList<String> wordsPlayed;
  private HashMap<Difficulty, ArrayList<String>> wordsNotPlayed;
  private int best_time;
  private int best_accuracy;

  public boolean isBestTime() {
    return false;
  }

  public boolean isBestAccuracy() {
    return false;
  }
}
