package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;
import nz.ac.auckland.se206.profiles.entities.StatsData;
import nz.ac.auckland.se206.profiles.entities.badges.Badge;
import nz.ac.auckland.se206.profiles.entities.badges.BadgeFactory;

/** This is the controller for the profile stats view */
public class ProfileStatsController implements ControllerInterface {
  public static final int TOTAL_PREDICTIONS_TO_SHOW = 5;

  // Fxml badges
  @FXML private ImageView badgeThreeImage;
  @FXML private ImageView badgeTwoImage;
  @FXML private ImageView badgeOneImage;
  @FXML private Label badgeThreeLabel;
  @FXML private Label badgeOneLabel;
  @FXML private Label badgeTwoLabel;
  // Fxml components
  @FXML private Label profileNameLabel;
  @FXML private ImageView profileImageView;
  @FXML private TextArea profileStatsTextArea;
  @FXML private TextArea bestRecordsTextArea;
  @FXML private TextArea pastWordsTextArea;

  private GameModel gameModel;
  private Profile profile;

  /** Init and load the stats view */
  public void initialize() {
    gameModel = GameModel.getInstance();
    profile = gameModel.getProfile();

    // Set stats view
    profileNameLabel.setText(profile.getUsername());
    profileStatsTextArea.setText(generateProfileWordString());
    bestRecordsTextArea.setText(generateBestRecordsString());

    // Display past words
    int pastWordSize = gameModel.getProfile().getWordsData().getWordsPlayed().size();
    if (pastWordSize == 0) {
      pastWordsTextArea.setText("No past words!");
    } else {
      pastWordsTextArea.setText(generatePastWordString(TOTAL_PREDICTIONS_TO_SHOW));
    }

    // Display profile picture
    try {
      Image image = new Image(profile.getProfilePicturePath());
      if (image.isError()) {
        throw new IllegalArgumentException("Image not valid");
      }
      profileImageView.setImage(image);
    } catch (IllegalArgumentException e) {
      System.err.println("Failed to load image");
      try {
        profileImageView.setImage(
            new Image(
                ProfileStatsController.class
                    .getResource("/images/img_not_found.png")
                    .toURI()
                    .toString()));
      } catch (URISyntaxException ex) {
        System.out.println("Wtf");
        ex.printStackTrace();
      }
    }

    // Load badges
    try {
      loadBadges();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  /** When view is loaded again. Refresh the stats */
  @Override
  public void refresh() {
    this.initialize();
  }

  /**
   * This method loads all the badges
   */
  private void loadBadges() throws URISyntaxException {
    List<Badge> badgesToRender = this.gameModel.getProfile().getStatsData().getBadgeList();
    HashMap<String, ArrayList<Badge>> badgeHashMap = new HashMap<>();


    // Display badge names
    badgeOneLabel.setText("Win streak");
    badgeThreeLabel.setText("Best Time");
    badgeTwoLabel.setText("Games played");

    // Load
    badgeHashMap.put("Best Time", new ArrayList<>());
    badgeHashMap.put("Games Played", new ArrayList<>());
    badgeHashMap.put("Win Streak", new ArrayList<>());

    for (Badge badge : badgesToRender) {
      badgeHashMap.get(badge.getName()).add(badge);
    }

    // Create tool tips
    Tooltip badgeOneTooltip;
    Tooltip badgeTwoTooltip;
    Tooltip badgeThreeTooltip;

    // Load badge for Win streak
    if (checkBadgeAchieved("Win Streak", Badge.Tier.GOLD, badgesToRender)) {
      // Gold achieved
      badgeOneImage.setImage(new Image(ProfileStatsController.class.getResource("/images/gold-cup.png").toURI().toString()));
      badgeOneTooltip = new Tooltip("7+ win streaks achieved.");
    } else if (checkBadgeAchieved("Win Streak", Badge.Tier.SILVER, badgesToRender)) {
      // Silver achieved
      badgeOneImage.setImage(new Image(ProfileStatsController.class.getResource("/images/silver-cup.png").toURI().toString()));
      badgeOneTooltip = new Tooltip("5+ win streaks achieved. Get 7+ win streak for next cup!");
    } else if (checkBadgeAchieved("Win Streak", Badge.Tier.BRONZE, badgesToRender)) {
      // Bronze achieved
      badgeOneImage.setImage(new Image(ProfileStatsController.class.getResource("/images/bronze-cup.png").toURI().toString()));
      badgeOneTooltip = new Tooltip("3+ win streaks achieved. Get 5+ win streak to get next cup!");
    } else {
      // Badge not achieved
      badgeOneImage.setImage(new Image(ProfileStatsController.class.getResource("/images/blank-cup.png").toURI().toString()));
      badgeOneTooltip = new Tooltip("Get 3+ win streaks to get the bronze cup!");
    }

    // Load badge for Games played
    if (checkBadgeAchieved("Games Played", Badge.Tier.GOLD, badgesToRender)) {
      // Gold achieved
     badgeTwoImage.setImage(new Image(ProfileStatsController.class.getResource("/images/gold-cup.png").toURI().toString()));
      badgeTwoTooltip = new Tooltip("20+ Games played!");
      System.out.println("A");
    } else if (checkBadgeAchieved("Games Played", Badge.Tier.SILVER, badgesToRender)) {
      // Silver achieved
      badgeTwoImage.setImage(new Image(ProfileStatsController.class.getResource("/images/silver-cup.png").toURI().toString()));
      badgeTwoTooltip = new Tooltip("10+ games played! Play 20+ games to get next cup!");
      System.out.println("B");
    } else if (checkBadgeAchieved("Games Played", Badge.Tier.BRONZE, badgesToRender)) {
      // Bronze achieved
      badgeTwoImage.setImage(new Image(ProfileStatsController.class.getResource("/images/bronze-cup.png").toURI().toString()));
      badgeTwoTooltip = new Tooltip("5+ games played! Get 10+ games to get next cup");
      System.out.println("C");
    } else {
      // Badge not achieved
      badgeTwoImage.setImage(new Image(ProfileStatsController.class.getResource("/images/blank-cup.png").toURI().toString()));
      badgeTwoTooltip = new Tooltip("Play 5 games to get the next cup!");
      System.out.println("D");
    }

    // Load boadge for games played
    if (checkBadgeAchieved("Best Time", Badge.Tier.GOLD, badgesToRender)) {
      // Gold achieved
      badgeThreeImage.setImage(new Image(ProfileStatsController.class.getResource("/images/gold-cup.png").toURI().toString()));
      badgeThreeTooltip = new Tooltip("<5 seconds to win");
    } else if (checkBadgeAchieved("Best Time", Badge.Tier.SILVER, badgesToRender)) {
      // Silver achieved
      badgeThreeImage.setImage(new Image(ProfileStatsController.class.getResource("/images/silver-cup.png").toURI().toString()));
      badgeThreeTooltip = new Tooltip("<20 secs to win. Get <5 secs to get next cup");
    } else if (checkBadgeAchieved("Best Time", Badge.Tier.BRONZE, badgesToRender)) {
      // Bronze achieved
      badgeThreeImage.setImage(new Image(ProfileStatsController.class.getResource("/images/bronze-cup.png").toURI().toString()));
      badgeThreeTooltip = new Tooltip("<30 secs to win. Get <20 secs to get next cup!");
    } else {
      // Badge not achieved
      badgeThreeImage.setImage(new Image(ProfileStatsController.class.getResource("/images/blank-cup.png").toURI().toString()));
      badgeThreeTooltip = new Tooltip("Get <30 seconds to win to get the first cup!");
    }

    // Bind tool tips
    Tooltip.install(badgeOneImage, badgeOneTooltip);
    Tooltip.install(badgeTwoImage, badgeTwoTooltip);
    Tooltip.install(badgeThreeImage, badgeThreeTooltip);

    badgeOneTooltip.setShowDelay(Duration.seconds(0));
    badgeTwoTooltip.setShowDelay(Duration.seconds(0));
    badgeThreeTooltip.setShowDelay(Duration.seconds(0));
  }

  /**
   * Checks whether the badge exists in the list
   * @param badgeName Name of the badge. Must be exact
   * @param tier Teir of the badge
   * @param badgesAchieved The list of badges to search thorough
   * @return boolean if found
   */
  private boolean checkBadgeAchieved(String badgeName, Badge.Tier tier, List<Badge> badgesAchieved) {
    for (Badge badge : badgesAchieved) {
      if (badge.getTier().equals(tier) && badge.getName().equals(badgeName)) {
        return true;
      }
    }
    return false;
  }


  /////////////////////
  // String builders //
  /////////////////////

  /**
   * Generate a list of past words
   *
   * @param totalToDisplay Total past words to display
   * @return Multiline string of past words
   */
  private String generatePastWordString(int totalToDisplay) {
    // Get list of words to display
    List<String> wordsPlayed = this.profile.getWordsData().getWordsPlayed();
    if (wordsPlayed.size() > totalToDisplay) {
      wordsPlayed = wordsPlayed.subList(0, totalToDisplay);
    }

    StringBuilder stringBuilder = new StringBuilder();
    // Generate String
    for (String word : wordsPlayed) {
      stringBuilder.append(word + System.getProperty("line.separator"));
    }
    return stringBuilder.toString();
  }

  /**
   * Generate profile statistic string
   *
   * @return Multiline string of past words
   */
  private String generateProfileWordString() {
    StringBuilder stringBuilder = new StringBuilder();
    StatsData statsData = this.gameModel.getProfile().getStatsData();
    // Show the respective stat
    stringBuilder.append(String.format("%-15s %5d\n", "Games Won:", statsData.getWins()));
    stringBuilder.append(String.format("%-15s %5d\n", "Games Lost:", statsData.getLosses()));
    stringBuilder.append(String.format("%-15s %5d\n", "Total Games:", statsData.getTotalGames()));
    return stringBuilder.toString();
  }

  /**
   * Generate best record string
   *
   * @return Multi line string
   */
  private String generateBestRecordsString() {
    // Get required objects
    StringBuilder stringBuilder = new StringBuilder();
    StatsData statsData = this.gameModel.getProfile().getStatsData();

    // Best win streak
    stringBuilder.append(
        String.format("%-20s %3d\n", "Highest Win Streak:", statsData.getBestStreak()));
    // Current win streak
    stringBuilder.append(
        String.format("%-20s %3d\n", "Current Win Streak:", statsData.getCurrentStreak()));
    // Accuracy
    stringBuilder.append(
        String.format("%-20s %3d\n", "Best Accuracy:", statsData.getBestAccuracy()));
    stringBuilder.append(String.format("%-20s %3d\n", "Best Time:", statsData.getBestTime()));
    return stringBuilder.toString();
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * This method is called when user clicks the main menu button to go back to main menu
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onBackToMenuButton(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }

  /**
   * This method is called when requesting to see all words
   *
   * @param actionEvent Event of button
   */
  @FXML
  private void onFullListButton(ActionEvent actionEvent) {
    // Box info
    Alert fullWordDialogue = new Alert(Alert.AlertType.INFORMATION);
    fullWordDialogue.setTitle("Past words");
    fullWordDialogue.setHeaderText("Below is your full past word history from newest to oldest");

    // Config textarea settings. Influenced from
    // https://code.makery.ch/blog/javafx-dialogs-official/
    TextArea textArea = new TextArea();
    textArea.setEditable(false);
    textArea.setWrapText(true);
    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    // Show past words
    int pastWordSize = gameModel.getProfile().getWordsData().getWordsPlayed().size();
    if (pastWordSize == 0) {
      textArea.setText("No past words!");
    } else {
      textArea.setText(generatePastWordString(pastWordSize));
    }

    // Display
    fullWordDialogue.getDialogPane().setContent(textArea);
    fullWordDialogue.showAndWait();
  }
}
