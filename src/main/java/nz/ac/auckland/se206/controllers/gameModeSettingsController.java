package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

public class gameModeSettingsController implements ControllerInterface {

  private GameModel gameModel;

  private ProfileFactory profileFactory;
  private String[] confidenceDifficulties = {"Easy", "Medium", "Hard", "Master"};

  private String[] timeDifficulties = {"Easy", "Medium", "Hard", "Master"};

  private String[] accuracyDifficulties = {"Easy", "Medium", "Hard"};

  private String[] difficultyDifficulties = {"Easy", "Medium", "Hard", "Master"};

  @FXML private ComboBox<String> confidenceComboBox;

  @FXML private ComboBox<String> timeComboBox;

  @FXML private ComboBox<String> accuracyComboBox;

  @FXML private ComboBox<String> difficultyComboBox;

  public void initialize() {

    // Create instance of object saving
    try {
      profileFactory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Get game settings
    this.gameModel = GameModel.getInstance();

    confidenceComboBox.getItems().addAll(confidenceDifficulties);
    timeComboBox.getItems().addAll(timeDifficulties);
    accuracyComboBox.getItems().addAll(accuracyDifficulties);
    difficultyComboBox.getItems().addAll(difficultyDifficulties);

    //    timeComboBox.setItems(FXCollections.observableArrayList(timeDifficulties));
    //    accuracyComboBox.setItems(FXCollections.observableArrayList(accuracyDifficulties));
    //    difficultyComboBox.setItems(FXCollections.observableArrayList(difficultyDifficulties));
  }

  @Override
  public void refresh() {}

  @FXML
  public void onStartButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onGameModeButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onRightButton() {
    System.out.println("Not implemented yet");
  }

  @FXML
  public void onLeftButton() {
    System.out.println("Not implemented yet");
  }

  /** This method is called when user clicks the main menu button to go back to main menu */
  @FXML
  private void onBackToMenuButton() throws IOException {
    profileFactory.saveProfile(gameModel.getProfile());
    gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
  }
}
