package nz.ac.auckland.se206.profiles.entities;

// model of what will be stored as user data in a profile

public class StatsData extends Data {
  private int bestTime;
  private int bestAccuracy;
  private int totalGames;
  private int wins;
  private int losses;
  private int currentStreak;
  private int bestStreak;

  public int getBestAccuracy() {
    return bestAccuracy;
  }

  public int getBestTime() {
    return bestTime;
  }

  /**
   * Checks if the time is greater than current best
   *
   * @param roundTime current round's time
   */
  public void setBestTime(int roundTime) {
    if (this.bestTime < roundTime) {
      this.bestTime = roundTime;
    }
  }

  /**
   * Checks if the accuracy is greater than current best
   *
   * @param roundAccuracy current round's accuracy
   */
  public void setBestAccuracy(int roundAccuracy) {
    if (this.bestAccuracy < roundAccuracy) {
      this.bestAccuracy = roundAccuracy;
    }
  }

  @Override
  public void resetData() {
    this.bestAccuracy = -1;
    this.bestTime = -1;
  }
}
