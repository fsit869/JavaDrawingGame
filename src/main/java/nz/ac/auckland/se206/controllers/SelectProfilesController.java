package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ToggleButton;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;

/** This class is responsible for selecting which profile to play */
public class SelectProfilesController {
  @FXML private ToggleButton deleteButton;
  @FXML private Button Guest;
  @FXML private Button profileOne;
  @FXML private Button profileTwo;
  @FXML private Button profileThree;
  @FXML private Button profileFour;
  @FXML private Button profileFive;
  @FXML private ButtonBar buttonBar;

  private List<Profile> profiles;
  private Button[] arrButtons;
  private boolean deleteMode;
  private ProfileFactory factory;
  private GameModel gameModel;

  /** Initialises the controller, sets the factory and the profiles */
  public void initialize() {
    setButtonsArray();
    this.deleteMode = false;
    // Initialise the profile factory
    try {
      this.factory = new ProfileFactory();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.gameModel = GameModel.getInstance();
    // Catches any error thrown inside the factory
    try {
      this.profiles = factory.getAllProfiles();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    setButtons();
  }

  /** Sets the text of the buttons, grabbing the usernames from the player data */
  public void setButtons() {
    String prepend = "";
    if (this.deleteMode) {
      prepend = "DELETE ";
    }
    for (int i = 0; i < this.profiles.size() - 1; i++) {
      // Check if there is a profile in the spot
      if (!profiles.get(i).getUsername().equals("")) {
        this.arrButtons[i].setText(prepend + profiles.get(i).getUsername());
      } else {
        this.arrButtons[i].setText(this.deleteMode ? "Empty Slot!" : "New Profile");
        this.arrButtons[i].setDisable(this.deleteMode);
      }
    }
    this.arrButtons[5].setText(profiles.get(5).getUsername());
    this.arrButtons[5].setDisable(this.deleteMode);
  }

  /** Configures the buttons into an array */
  public void setButtonsArray() {
    this.arrButtons = new Button[6];
    // All the buttons are put into this array
    this.arrButtons[0] = profileOne;
    this.arrButtons[1] = profileTwo;
    this.arrButtons[2] = profileThree;
    this.arrButtons[3] = profileFour;
    this.arrButtons[4] = profileFive;
    this.arrButtons[5] = Guest;
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * This method is called when the new profile button is pressed
   *
   * @param actionEvent Event type of button
   */
  @FXML
  private void onProfileButton(ActionEvent actionEvent) {
    Button current = (Button) actionEvent.getTarget();
    // Check if button is creating new profile
    if (this.deleteMode) {
      onDeleteProfile(current.getText());
      current.setDisable(true);
    } else {
      onSelectedProfile(current.getText());
    }
  }

  public void onDeleteProfile(String profile) {
    profile = profile.substring(7);
    if (profile.equals("Empty Profile!")) {
      // Cannot delete an empty profile slot
      throw new NoSuchElementException("Profile doesn't exist");
    } else if (profile.equals("Guest")) {
      // Guest profile should not be able to be deleted
      throw new NoSuchElementException("Cannot delete the Guest profile");
    } else {
      // Attempt to delete selected profile to delete
      try {
        factory.deleteProfile(factory.selectProfile(profile));
        this.profiles = factory.getAllProfiles();
        setButtons();
      } catch (IOException e) {
        throw new RuntimeException("Error deleting the profile");
      }
    }
  }

  public void onSelectedProfile(String profile) {
    if (profile.equals("New Profile")) {
      gameModel.setCurrentViewState(GameModel.ViewState.NEWPROFILE);
    } else if (profile.equals("Guest")) {
      // Selects the guest profile, and resets the data to it.
      gameModel.setProfile(factory.selectProfile(profile));
      gameModel.getProfile().resetData();
      gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
    } else {
      // Selects the chosen profile and changes to the page.
      gameModel.setProfile(factory.selectProfile(profile));
      gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
    }
  }

  /** Changes the page into deleteMode or toggles it off */
  public void toggleDeleteMode() {
    this.deleteMode = !deleteMode;
    setButtons();
  }
}
