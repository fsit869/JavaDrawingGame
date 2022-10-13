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

  /**
   * Creates a badge object for the user to display in their profile.
   *
   * @param name of the badge
   * @param tier of the badge
   * @param description about the badge
   * @param imageLocation of the badge
   */
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
