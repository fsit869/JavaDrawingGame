package nz.ac.auckland.se206.profiles.entities.badges;

import java.util.HashMap;

public class BadgeFactory {
  public enum BadgeEnum {
    TIME_GOLD,
    TIME_SILVER,
    TIME_BRONZE,

    GAMES_PLAYED_GOLD,
    GAMED_PLAYED_SILVER,
    GAME_PLAYED_BRONZE,

    WIN_STREAK_GOLD,
    WIN_STREAK_SILVER,
    WIN_STEAK_BRONZE
  }

  private HashMap<BadgeEnum, Badge> badgeObjects;

  /** Initialises the factory of the badge factory. Puts in the badges into it */
  public BadgeFactory() {
    this.badgeObjects = new HashMap<>();

    // Create time badges
    badgeObjects.put(
        BadgeEnum.TIME_GOLD,
        new Badge("Best Time", Badge.Tier.GOLD, "Finished drawing in less than 5 secs", null));
    badgeObjects.put(
        BadgeEnum.TIME_SILVER,
        new Badge("Best Time", Badge.Tier.SILVER, "Finished drawing in less than 20 secs", null));
    badgeObjects.put(
        BadgeEnum.TIME_BRONZE,
        new Badge("Best Time", Badge.Tier.BRONZE, "Finished drawing in less than 30 secs", null));

    // Create games played badges
    badgeObjects.put(
        BadgeEnum.GAMES_PLAYED_GOLD,
        new Badge("Games Played", Badge.Tier.GOLD, "20+ Games played", null));
    badgeObjects.put(
        BadgeEnum.GAMED_PLAYED_SILVER,
        new Badge("Games Played", Badge.Tier.SILVER, "10-19 Games played", null));
    badgeObjects.put(
        BadgeEnum.GAME_PLAYED_BRONZE,
        new Badge("Games Played", Badge.Tier.BRONZE, "3-9 games played", null));

    // Create win streak badges
    badgeObjects.put(
        BadgeEnum.WIN_STREAK_GOLD, new Badge("Win Streak", Badge.Tier.GOLD, "7+ win streak", null));
    badgeObjects.put(
        BadgeEnum.WIN_STREAK_SILVER,
        new Badge("Win Streak", Badge.Tier.SILVER, "5-6 win streak", null));
    badgeObjects.put(
        BadgeEnum.WIN_STEAK_BRONZE,
        new Badge("Win Streak", Badge.Tier.BRONZE, "3-4 win streak", null));
  }

  public Badge createBadge(BadgeEnum badge) {
    return badgeObjects.get(badge);
  }
}
