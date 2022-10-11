package nz.ac.auckland.se206.profiles.entities.badges;

public class Badge {
  public enum Tier {
    NONE,
    BRONZE,
    SILVER,
    GOLD
  }

  private String name;
  private String description;
  private String imageLocation;
  private Tier tier;

  public Badge(String name, Tier tier, String description, String imageLocation) {
    this.name = name;
    this.tier = tier;
    this.description = description;
    this.imageLocation = imageLocation;
  }

  public String getDescription() {
    return description;
  }

  public String getImageLocation() {
    return imageLocation;
  }

  public Tier getTier() {
    return tier;
  }

  public String getName() {
    return name;
  }
}
