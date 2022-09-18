package nz.ac.auckland.se206.controllers;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import nz.ac.auckland.se206.model.GameModel;

/** This class is responsible for creating a new profile */
public class NewProfileController {
  private GameModel gameModel;

  private String profilePicPath;

  // FXML components
  @FXML private TextField firstNameTextField;
  @FXML private TextField lastNameTextField;
  @FXML private ImageView profileImageView;
  @FXML private Button createButton;

  public void initialize() {
    this.gameModel = GameModel.getInstance();
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
    System.out.println("no function");
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open Resource File");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
    File profilePic = fileChooser.showOpenDialog(null);
    if (profilePic != null) {
      profilePicPath = profilePic.getPath();
      Image image = new Image(profilePic.toURI().toString());
      profileImageView.setImage(image);
    }
  }

  /**
   * This method is called when user clicks to create a new profile
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onCreateNewProfile(ActionEvent actionEvent) {
    gameModel.getProfile().setProfilePicturePath(profilePicPath);
    gameModel
        .getProfile()
        .setUsername(firstNameTextField.getText() + " " + lastNameTextField.getText());
  }

  /**
   * This method is called when user clicks to create a new profile
   *
   * @param actionEvent Event of the button press
   */
  @FXML
  private void onBackToMenu(ActionEvent actionEvent) {
    gameModel.setCurrentViewState(GameModel.viewState.CANVAS);
  }
}
