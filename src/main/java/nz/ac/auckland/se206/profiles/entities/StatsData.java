package nz.ac.auckland.se206.profiles.entities;

// model of what will be stored as user data in a profile


public class StatsData extends Data {
  private int best_time;
  private int best_accuracy;

  public StatsData(int best_accuracy, int best_time) {
    this.best_time = best_time;
    this.best_accuracy = best_accuracy;
  }

  public boolean isBestTime() {
    return false;
  }

  public boolean isBestAccuracy() {
    return false;
  }

  @Override
  public void eraseData() {
    this.best_accuracy = -1;
    this.best_time = -1;
  }
}
