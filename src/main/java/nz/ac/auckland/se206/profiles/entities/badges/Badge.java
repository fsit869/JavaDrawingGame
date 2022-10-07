package nz.ac.auckland.se206.profiles.entities.badges;

public class Badge {
  protected String name;
  protected String description;
  protected String imageLocation;
  protected Tier tier;

  protected enum Tier {
    NONE,
    BRONZE,
    SILVER,
    GOLD
  }

  public Badge(String name, Tier tier, String description, String imageLocation) {
    this.name = name;
    this.tier = tier;
    this.description = description;
    this.imageLocation = imageLocation;
  }
}
