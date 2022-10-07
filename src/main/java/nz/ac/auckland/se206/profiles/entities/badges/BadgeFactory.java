package nz.ac.auckland.se206.profiles.entities.badges;

import java.util.HashMap;

public class BadgeFactory {
  private HashMap<BadgeEnum, Badge> badgeObjects;

  public enum BadgeEnum {
    TIME_GOLD
  }

  public BadgeFactory() {
    this.badgeObjects = new HashMap<>();
    badgeObjects.put(
        BadgeEnum.TIME_GOLD,
        new Badge("Gold Time", Badge.Tier.GOLD, "You achieved time", "the image"));
  }

  public Badge createBadge(BadgeEnum badge) {
    return badgeObjects.get(badge);
  }
}
