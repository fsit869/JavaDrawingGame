package nz.ac.auckland.se206.controllers;
import ai.djl.ModelException;
import nz.ac.auckland.se206.model.GameModel;

import java.io.IOException;
import java.net.URISyntaxException;

public class FxmlSwitcher {

    private GameModel gameModel;

    /**
     * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
     * the drawing, and we load the ML model.
     *
     * @throws ModelException If there is an error in reading the input/output of the DL model.
     * @throws IOException If the model cannot be found on the file system.
     */
    public void initialize() throws ModelException, IOException, URISyntaxException {
        // Initialize objects
        System.out.println("iniatialising");
        this.gameModel = GameModel.getInstance();
        this.setupViewStateBindings();
        gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
        gameModel.setCurrentViewState(GameModel.viewState.SETTINGS);
        gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
    }

    /**
     * Handle bindings of state changes. IE, when this state occurs, what methods should be called.
     * Also handles binding of the word label.
     */
    private void setupViewStateBindings() {
        // Bind word label
//        wordLabel.textProperty().bind(gameModel.getCurrentWordToGuessProperty());
//        gameModel.getCurrentViewStateProperty();
        // Bindings for when game state changed
        gameModel
                .getCurrentViewStateProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            switch (newValue) {
                                case CANVAS:
                                    System.out.println("Canvas");
                                    break;
                                case SELECTPROFILES:
                                    System.out.println("SelectProfiles");
                                    break;
                                case NEWPROFILE:
                                    System.out.println("NewProfile");
                                    break;
                                case SETTINGS:
                                    System.out.println("Settings");
                                    break;
                                case MAINMENU:
                                    System.out.println("MainMenu");
                                    break;
                                case PROFILESTATS:
                                    System.out.println("ProfileStats");
                                    break;
                                default:
                                    // Unknown state that is not handled.
                                    throw new IllegalStateException(
                                            String.format(
                                                    "Unknown state [%s]\n Transitioned from [%s] state", oldValue, newValue));
                            }
                        });
    }
}
