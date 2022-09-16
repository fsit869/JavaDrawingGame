package nz.ac.auckland.se206.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

/** This is the controller for the profile stats view */
public class ProfileStatsController {

    ObservableList<String> fullWordHistory =
            FXCollections.observableArrayList("Option 1", "Option 2", "Option 3");
    @FXML private Label profileNameLabel;
    @FXML private ImageView profileImageView;

    @FXML private TextArea profileStatsTextArea;

    @FXML private TextArea bestRecordsTextArea;

    @FXML private TextArea pastWordsTextArea;

    @FXML private ComboBox<String> fullListComboBox;

    public void initialize() {
        fullListComboBox.setItems(fullWordHistory);
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
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
