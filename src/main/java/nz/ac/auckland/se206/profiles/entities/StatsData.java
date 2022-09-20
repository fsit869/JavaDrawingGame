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

  public int getCurrentStreak() {
    return currentStreak;
  }

  public int getLosses() {
    return losses;
  }

  public int getWins() {
    return wins;
  }

  public int getTotalGames() {
    return totalGames;
  }

  public int getBestAccuracy() {
    return bestAccuracy;
  }

  public int getBestTime() {
    return bestTime;
  }

  public int getBestStreak() {
    return bestStreak;
  }

  public void addGames() {
    this.totalGames += 1;
  }

  /** Will reset the current streak when called. */
  public void addLosses() {
    this.losses += 1;
    this.currentStreak = 0;
  }

  /**
   * This method will automatically add to best streak if it is found to be higher than current
   * streak.
   */
  public void addWins() {
    this.wins += 1;
    this.currentStreak += 1;
    // Check if currentStreak is better than best streak
    if (this.currentStreak > this.bestStreak) {
      this.bestStreak = this.currentStreak;
    }
  }

  /**
   * Checks if the time elapsed is less than current fastest time. i.e. 60 - time remaining
   *
   * @param roundTime current round's time
   */
  public void setBestTime(int roundTime) {
    if (this.bestTime > roundTime) {
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
    this.bestAccuracy = 0;
    this.bestTime = 60;
    this.totalGames = 0;
    this.wins = 0;
    this.losses = 0;
    this.currentStreak = 0;
    this.bestStreak = 0;
  }
}
