package nz.ac.auckland.se206.controllers;

import java.net.URISyntaxException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.entities.Profile;

/** This is the controller for the profile stats view */
public class ProfileStatsController {
  public static final int TOTAL_PREDICTIONS_TO_SHOW = 5;
  // Fxml components
  @FXML private Label profileNameLabel;
  @FXML private ImageView profileImageView;
  @FXML private TextArea profileStatsTextArea;
  @FXML private TextArea bestRecordsTextArea;
  @FXML private TextArea pastWordsTextArea;
  @FXML private ComboBox<String> fullListComboBox;

  private ObservableList<String> fullWordHistory =
      FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");
  private GameModel gameModel;
  private Profile profile;

  public void initialize() throws URISyntaxException {
    fullListComboBox.setItems(fullWordHistory);
    gameModel = GameModel.getInstance();
    profile = gameModel.getProfile();

    profileNameLabel.setText(profile.getUsername());
    pastWordsTextArea.setText(generatePastWordString(TOTAL_PREDICTIONS_TO_SHOW));

    try {
      profileImageView.setImage(new Image(profile.getProfilePicturePath()));
    } catch (IllegalArgumentException e) {
      System.err.println("Failed to load image");
      profileImageView.setImage(
          new Image(
              ProfileStatsController.class
                  .getResource("/images/img_not_found.png")
                  .toURI()
                  .toString()));
    }
  }
  /////////////////////
  // String builders //
  /////////////////////
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
    gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
  }
}
