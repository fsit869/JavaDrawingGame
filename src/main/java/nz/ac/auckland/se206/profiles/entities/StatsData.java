package nz.ac.auckland.se206.profiles.entities;

// model of what will be stored as user data in a profile

public class StatsData extends Data {
  private int best_time;
  private int best_accuracy;

  public int getBest_accuracy() {
    return best_accuracy;
  }

  public int getBest_time() {
    return best_time;
  }

  public void setBestTime(int roundTime) {
    if (this.best_time < roundTime) {
      this.best_time = roundTime;
    }
  }

  public void setBestAccuracy(int roundAccuracy) {
    if (this.best_accuracy < roundAccuracy) {
      this.best_accuracy = roundAccuracy;
    }
  }

  @Override
  public void eraseData() {
    this.best_accuracy = -1;
    this.best_time = -1;
  }
}
