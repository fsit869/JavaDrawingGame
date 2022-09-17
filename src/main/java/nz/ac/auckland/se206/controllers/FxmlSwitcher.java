package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nz.ac.auckland.se206.App;
import nz.ac.auckland.se206.model.GameModel;

public class FxmlSwitcher {

  private GameModel gameModel;

  private Scene rootScene;

  private static FxmlSwitcher fxmlSwitcherInstance = new FxmlSwitcher();

  private FxmlSwitcher() {
    try {
      this.rootScene = new Scene(loadFxml("canvas"));
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
  public void initialize() throws ModelException, IOException, URISyntaxException {
    // Initialize objects
    FxmlSwitcher fxmlSwitcher = FxmlSwitcher.getInstance();
    this.gameModel = GameModel.getInstance();
    this.setupViewStateBindings();
    gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   * @throws IOException If the file is not found.
   */
  private static Parent loadFxml(final String fxml) throws IOException {
    return new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml")).load();
  }

  /**
   * Handle bindings of view state changes. IE, when this state occurs, what methods should be
   * called.
   */
  private void setupViewStateBindings() {
    // Bindings for when view state changed
    gameModel
        .getCurrentViewStateProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              try {
                switch (newValue) {
                  case CANVAS:
                    System.out.println("Canvas");
                    rootScene.setRoot(loadFxml("canvas"));
                    break;
                  case SELECTPROFILES:
                    System.out.println("SelectProfiles");
                    rootScene.setRoot(loadFxml("select_profiles"));
                    break;
                  case NEWPROFILE:
                    System.out.println("NewProfile");
                    rootScene.setRoot(loadFxml("new_profile"));
                    break;
                  case SETTINGS:
                    System.out.println("Settings");
                    rootScene.setRoot(loadFxml("settings"));
                    break;
                  case MAINMENU:
                    System.out.println("MainMenu");
                    rootScene.setRoot(loadFxml("main_menu"));
                    break;
                  case PROFILESTATS:
                    System.out.println("ProfileStats");
                    rootScene.setRoot(loadFxml("profile_stats"));
                    break;
                  default:
                    // Unknown state that is not handled.
                    throw new IllegalStateException(
                        String.format(
                            "Unknown state [%s]\n Transitioned from [%s] state",
                            oldValue, newValue));
                }
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
              {
              }
            });
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
