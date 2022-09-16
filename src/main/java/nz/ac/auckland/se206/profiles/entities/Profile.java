package nz.ac.auckland.se206.profiles.entities;

// model of a user profile

import nz.ac.auckland.se206.words.CategorySelector.Difficulty;

public class Profile {
  private String username;
  // links to the file of the pfp
  private String profilePicture;
  private StatsData statsData;
  private WordsData wordsData;
  private SettingsData settingsData;

  public Profile(
      String username,
      String profilePicture,
      StatsData statsData,
      WordsData wordsData,
      SettingsData settingsData) {
    this.username = username;
    this.profilePicture = profilePicture;
    this.statsData = statsData;
    this.wordsData = wordsData;
    this.settingsData = settingsData;
  }

  public String getRandomWord(Difficulty difficulty) {
    return "";
  }
}
