package nz.ac.auckland.se206.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

/** This class is responsible for creating a new profile */
public class NewProfileController {
  // FXML components
  @FXML private TextField firstNameTextField;
  @FXML private TextField lastNameTextField;
  @FXML private ImageView profileImageView;
  @FXML private Button createButton;

  public void initialize() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /**
   * This method is called when user clicks to choose a profile picture
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onChooseProfilePicture(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * This method is called when user clicks to create a new profile
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onCreateNewProfile(ActionEvent actionEvent) {
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
