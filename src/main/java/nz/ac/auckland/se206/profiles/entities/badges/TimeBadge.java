package nz.ac.auckland.se206.profiles.entities.badges;

public class TimeBadge extends Badge {

  public void condition(int time) {
    if (time < 45) {
      this.tier = Tier.ONE;
      this.status = true;
    }
    if (time < 30) {
      this.tier = Tier.TWO;
    }
    if (time < 10) {
      this.tier = Tier.THREE;
    }
  }

  public Tier outputBadge() {
    if (this.status) {
      return this.tier;
    }
    return Tier.NONE;
  }
}
