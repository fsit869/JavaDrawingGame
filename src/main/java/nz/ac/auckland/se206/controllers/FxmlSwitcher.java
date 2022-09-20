package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nz.ac.auckland.se206.model.GameModel;

public class FxmlSwitcher {

  private GameModel gameModel;

  private Scene rootScene;

  private static FxmlSwitcher fxmlSwitcherInstance = new FxmlSwitcher();

  private FxmlSwitcher() {
    try {
      initialize();
    } catch (IOException e) {
      System.err.println("Failed to initialize fxml switcher. Possible error with loadFxml()");
      throw new RuntimeException(e);
    } catch (ModelException e) {
      throw new RuntimeException(e);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * constructor calls this function to set up the switcher
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  private void initialize() throws ModelException, IOException, URISyntaxException {
    // Initialize objects
    this.gameModel = GameModel.getInstance();
    this.rootScene = new Scene(loadFxml("select_profiles"));
    this.setupViewStateBindings();

    // IMPORTANT: Upon end of init. The view according to this state will be displayed.
    this.gameModel.setCurrentViewState(GameModel.ViewState.PROFILESTATS);
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   */
  private Parent loadFxml(final String fxml) {
    try {
      return new FXMLLoader(FxmlSwitcher.class.getResource("/fxml/" + fxml + ".fxml")).load();
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException();
    }
  }

  /**
   * Handle bindings of view state changes. IE, when this state occurs, what methods should be
   * called.
   */
  private void setupViewStateBindings() {
    // Bindings for when view state changed
    gameModel
        .getCurrentViewStateProperty()
        // Determine which states goto which page
        .addListener(
            (observable, oldValue, newValue) -> {
              switch (newValue) {
                case CANVAS -> rootScene.setRoot(loadFxml("game"));
                case SELECTPROFILES -> rootScene.setRoot(loadFxml("select_profiles"));
                case NEWPROFILE -> rootScene.setRoot(loadFxml("new_profile"));
                case SETTINGS -> rootScene.setRoot(loadFxml("settings"));
                case MAINMENU -> rootScene.setRoot(loadFxml("main_menu"));
                case PROFILESTATS -> rootScene.setRoot(loadFxml("profile_stats"));
                default ->
                // Unknown state that is not handled.
                throw new IllegalStateException(
                    String.format(
                        "Unknown state [%s]\n Transitioned from [%s] state", oldValue, newValue));
              }
            });
    // This likely should be refactored into a hash map
  }

  ///////////////////////
  // Singleton pattern //
  ///////////////////////

  /**
   * Get the instance of the fxml switcher. Using the singleton pattern.
   *
   * @return The game model.
   */
  public static FxmlSwitcher getInstance() {
    return fxmlSwitcherInstance;
  }

  public Scene getRootScene() {
    return rootScene;
  }
}
