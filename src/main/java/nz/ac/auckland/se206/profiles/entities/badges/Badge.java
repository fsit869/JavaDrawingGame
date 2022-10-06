package nz.ac.auckland.se206.profiles.entities.badges;


public abstract class Badge {
  protected boolean status;

  protected Tier tier;

  protected enum Tier {
    NONE,
    ONE,
    TWO,
    THREE
  }
}
