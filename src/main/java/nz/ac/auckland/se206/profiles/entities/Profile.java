package nz.ac.auckland.se206.profiles.entities;

// model of a user profile

public class Profile {
  private String username;
  // links to the file of the pfp
  private String profilePicturePath;
  private StatsData statsData;
  private WordsData wordsData;
  private SettingsData settingsData;

  public SettingsData getSettingsData() {
    return settingsData;
  }

  public StatsData getStatsData() {
    return statsData;
  }

  public WordsData getWordsData() {
    return wordsData;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getProfilePicturePath() {
    return this.profilePicturePath;
  }

  public void setProfilePicturePath(String profilePicturePath) {
    this.profilePicturePath = profilePicturePath;
  }

  /** Resets ALL attributes of a profile, back into a blank one that can be rewritten. */
  public void resetProfile() {
    this.username = "";
    this.profilePicturePath = "";
    resetData();
  }

  /** Resets the data of a profile, without touching the username of profile picture path. */
  public void resetData() {
    this.statsData.resetData();
    this.wordsData.resetData();
    this.settingsData.resetData();
  }
}
