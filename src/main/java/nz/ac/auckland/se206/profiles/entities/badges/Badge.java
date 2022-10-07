package nz.ac.auckland.se206.profiles.entities.badges;

public class Badge {
  private String name;
  private String description;
  private String imageLocation;
  private Tier tier;

  public enum Tier {
    NONE,
    BRONZE,
    SILVER,
    GOLD
  }

  public String getName() {
    return name;
  }

  public Badge(String name, Tier tier, String description, String imageLocation) {
    this.name = name;
    this.tier = tier;
    this.description = description;
    this.imageLocation = imageLocation;
  }
}
