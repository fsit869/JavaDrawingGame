package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.entities.Profile;
import nz.ac.auckland.se206.profiles.entities.StatsData;

/** This is the controller for the profile stats view */
public class ProfileStatsController implements ControllerInterface {
  public static final int TOTAL_PREDICTIONS_TO_SHOW = 5;
  // Fxml components
  @FXML private Label profileNameLabel;
  @FXML private ImageView profileImageView;
  @FXML private TextArea profileStatsTextArea;
  @FXML private TextArea bestRecordsTextArea;
  @FXML private TextArea pastWordsTextArea;

  private GameModel gameModel;
  private Profile profile;

  /**
   * Init and load the stats view
   *
   */
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
      profileImageView.setImage(new Image(profile.getProfilePicturePath()));
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
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void refresh() {
    this.initialize();
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
