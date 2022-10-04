package nz.ac.auckland.se206.profiles.entities;

public class SettingsData extends Data {
  /* Represents the levels of difficulty */
  public enum Levels {
    EASY,
    MEDIUM,
    HARD,
    MASTER
  }

  private boolean tts;
  private Levels accuracy;
  private Levels time;
  private Levels confidence;
  private Levels setting;

  public Levels getAccuracy() {
    return accuracy;
  }

  /**
   * Set accuracy of the difficulty
   *
   * @param accuracy Only easy, medium or hard
   */
  public void setAccuracy(Levels accuracy) {
    // Prevent invalid level of accuracy
    if (accuracy.equals(Levels.MASTER)) {
      throw new UnsupportedOperationException("Master level is not part of accuracy");
    }

    this.accuracy = accuracy;
  }

  public Levels getTime() {
    return time;
  }

  public void setTime(Levels time) {
    this.time = time;
  }

  public Levels getConfidence() {
    return confidence;
  }

  public void setConfidence(Levels confidence) {
    this.confidence = confidence;
  }

  public Levels getSetting() {
    return setting;
  }

  public void setSetting(Levels setting) {
    this.setting = setting;
  }

  public void setTts(boolean tts) {
    this.tts = tts;
  }

  public boolean getTts() {
    return this.tts;
  }

  @Override
  public void resetData() {
    this.tts = true; // default tts is on
    this.accuracy = Levels.EASY;
    this.time = Levels.EASY;
    this.confidence = Levels.EASY;
    this.setting = Levels.EASY;
  }
}
