package nz.ac.auckland.se206.profiles.entities;

public class SettingsData extends Data {
  private boolean tts;

  public SettingsData(Boolean tts) {
    this.tts = tts;
  }

  @Override
  public void eraseData() {
    this.tts = true; // default tts is on
  }
}
