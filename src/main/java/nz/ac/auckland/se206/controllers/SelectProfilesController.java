package nz.ac.auckland.se206.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.Profile;
import nz.ac.auckland.se206.speech.TextToSpeechTask;

/** This class is responsible for selecting which profile to play */
public class SelectProfilesController implements ControllerInterface {
  @FXML private Text promptText;
  @FXML private Circle circleOne;
  @FXML private Circle circleTwo;
  @FXML private Circle circleThree;
  @FXML private Circle circleFour;
  @FXML private Circle circleFive;

  @FXML private Circle circleSix;
  @FXML private ToggleButton deleteButton;
  @FXML private Button guest;
  @FXML private Button profileOne;
  @FXML private Button profileTwo;
  @FXML private Button profileThree;
  @FXML private Button profileFour;
  @FXML private Button profileFive;
  private List<Profile> profiles;
  private Circle[] arrCircles;
  private Button[] arrButtons;
  private boolean deleteMode;
  private ProfileFactory factory;
  private GameModel gameModel;
  private TextToSpeechTask textToSpeechTask;

  /** Initialises the controller, sets the factory and the profiles */
  public void initialize() {
    setButtonsArray();
    setCirclesArray();
    this.deleteMode = false;
    this.textToSpeechTask = new TextToSpeechTask();
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

  @Override
  public void refresh() {
    // todo Fix loading/deleting profiles.
    setButtons();
    setCirclesArray();
    initialize();
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
        try {
          Image image =
              new Image(
                  this.deleteMode
                      ? "file:src/main/resources/images/icons/minus.png"
                      : profiles.get(i).getProfilePicturePath());
          this.arrCircles[i].setFill(new ImagePattern(image));
          if (image.isError()) {
            throw new FileNotFoundException();
          }
        } catch (Exception e) {
          // Unexpected err while loading image. Display default
          Image image = new Image("file:src/main/resources/images/img_not_found.png");
          this.arrCircles[i].setFill(new ImagePattern(image));
        }
      } else {
        // If the profile doesn't exist, default images are shown.
        Image image =
            new Image(
                this.deleteMode
                    ? "file:src/main/resources/images/icons/invalid.png"
                    : "file:src/main/resources/images/icons/add.png");
        this.arrButtons[i].setText(this.deleteMode ? "Empty Slot!" : "New Profile");
        this.arrCircles[i].setFill(new ImagePattern(image));
        this.arrButtons[i].setDisable(this.deleteMode);
      }
    }
    // Configuring the guest profile
    this.arrButtons[5].setText(profiles.get(5).getUsername());
    this.arrButtons[5].setDisable(this.deleteMode);
    this.arrCircles[5].setFill(
        new ImagePattern(
            new Image(
                this.deleteMode
                    ? "file:src/main/resources/images/icons/invalid.png"
                    : "file:src/main/resources/images/default_profile_picture.png")));
  }

  /** Configures the circles for the profiles into an array */
  public void setCirclesArray() {
    this.arrCircles = new Circle[6];
    // All the circles are put into this array
    this.arrCircles[0] = circleOne;
    this.arrCircles[1] = circleTwo;
    this.arrCircles[2] = circleThree;
    this.arrCircles[3] = circleFour;
    this.arrCircles[4] = circleFive;
    this.arrCircles[5] = circleSix;
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
    this.arrButtons[5] = guest;
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
      String[] greetings = {"Ni Hao", "welcome", "Kia Ora"};
      int randomIndex = new Random().nextInt(greetings.length);
      textToSpeechTask.speak(greetings[randomIndex] + " " + gameModel.getProfile().getUsername());
    }
  }

  /**
   * On the instance of a profile to be deleted
   *
   * @param profile name to be deleted
   */
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

  /**
   * When a profile is selected in the menu
   *
   * @param profile to be selected
   */
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

  /** On toggle changes the page into deleteMode or toggles it off */
  @FXML
  private void onDeleteModeToggle() {
    this.deleteMode = !deleteMode;
    promptText.setText(this.deleteMode ? "Delete a Profile!" : "Select a Profile!");
    setButtons();
  }
}
