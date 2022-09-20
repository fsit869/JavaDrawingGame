package nz.ac.auckland.se206.controllers;

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
    this.arrButtons = new Button[5];
    this.gameModel = GameModel.getInstance();
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
    throw new UnsupportedOperationException("Not yet implemented");
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
