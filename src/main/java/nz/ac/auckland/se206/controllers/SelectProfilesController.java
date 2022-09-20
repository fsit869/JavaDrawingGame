package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;

/** This class is responsible for selecting which profile to play */
public class SelectProfilesController {
  @FXML private Button profileOne;
  @FXML private Button profileTwo;
  @FXML private Button profileThree;
  @FXML private Button profileFour;
  @FXML private Button profileFive;
  @FXML private ButtonBar buttonBar;

  private List<Profile> profiles;

  private Button[] arrButtons;

  private ProfileFactory factory;
  private GameModel gameModel;

  public void initialize() {
    setButtonsArray();
    this.gameModel = GameModel.getInstance();
    // Catches any error thrown inside the factory
    try {
      this.profiles = factory.getAllProfiles();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    setButtons();
  }

  public void setButtons() {
    for (int i = 0; i < this.profiles.size(); i++) {
      if (profiles.get(i).getUsername() != "") {
        this.arrButtons[i].setText(profiles.get(i).getUsername());
      } else {
        this.arrButtons[i].setText("New Profile");
      }
    }
  }

  /** Configures the buttons into an array */
  public void setButtonsArray() {
    this.arrButtons = new Button[5];
    // All the buttons are put into this array
    this.arrButtons[0] = profileOne;
    this.arrButtons[1] = profileTwo;
    this.arrButtons[2] = profileThree;
    this.arrButtons[3] = profileFour;
    this.arrButtons[4] = profileFive;
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * This method is called when the new profile button is pressed
   *
   * @param actionEvent Event type of button
   */
  public void onNewProfileButton(ActionEvent actionEvent) {
    Button current = (Button) actionEvent.getTarget();
    // Check if button is creating new profile
    if (current.getText().equals("New Profile")) {
      gameModel.setCurrentViewState(GameModel.viewState.NEWPROFILE);
    } else {
      // In the case that profile already exists
      gameModel.setProfile(factory.selectProfile(current.getText()));
      gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
    }
  }

  /**
   * This method is called when the onguest button is pressed
   *
   * @param actionEvent Event type of button
   */
  public void onGuestButton(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
