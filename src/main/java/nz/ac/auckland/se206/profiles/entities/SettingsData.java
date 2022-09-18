package nz.ac.auckland.se206.profiles.entities;

public class SettingsData extends Data {
  private boolean tts;

  public void setTts(boolean tts) {
    this.tts = tts;
  }

  public boolean getTts() {
    return this.tts;
  }

  @Override
  public void resetData() {
    this.tts = true; // default tts is on
  }
}
