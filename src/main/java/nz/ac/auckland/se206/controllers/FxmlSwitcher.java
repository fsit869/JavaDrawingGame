package nz.ac.auckland.se206.controllers;

import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nz.ac.auckland.se206.model.GameModel;

public class FxmlSwitcher {

  private static FxmlSwitcher fxmlSwitcherInstance = new FxmlSwitcher();

  /**
   * Get the instance of the fxml switcher. Using the singleton pattern.
   *
   * @return The game model.
   */
  public static FxmlSwitcher getInstance() {
    return fxmlSwitcherInstance;
  }

  private GameModel gameModel;

  private Scene rootScene;

  private HashMap<GameModel.ViewState, FxmlLoaderData> viewMap = new HashMap<>();

  private FxmlSwitcher() {

    // Initialize objects and views
    this.gameModel = GameModel.getInstance();

    // Initalize all the views with their view state
    this.addView(GameModel.ViewState.CANVAS, new FxmlLoaderData(loadFxml("game")));
    this.addView(
        GameModel.ViewState.SELECTPROFILES, new FxmlLoaderData(loadFxml("select_profiles")));
    this.addView(GameModel.ViewState.NEWPROFILE, new FxmlLoaderData(loadFxml("new_profile")));
    this.addView(GameModel.ViewState.SETTINGS, new FxmlLoaderData(loadFxml("settings")));
    this.addView(GameModel.ViewState.MAINMENU, new FxmlLoaderData(loadFxml("main_menu")));
    this.addView(GameModel.ViewState.PROFILESTATS, new FxmlLoaderData(loadFxml("profile_stats")));
    this.addView(
        GameModel.ViewState.GAMEMODESETTINGS, new FxmlLoaderData(loadFxml("game_mode_settings")));

    // Setup inital view
    this.rootScene =
        new Scene(this.viewMap.get(GameModel.ViewState.SELECTPROFILES).getRoot(), 642, 750);
    this.gameModel.setCurrentViewState(GameModel.ViewState.SELECTPROFILES);
    this.setupViewStateBindings();
  }

  /**
   * Returns the node associated to the input file. The method expects that the file is located in
   * "src/main/resources/fxml".
   *
   * @param fxml The name of the FXML file (without extension).
   * @return The node of the input file.
   */
  private FXMLLoader loadFxml(final String fxml) {
    try {
      return new FXMLLoader(getClass().getResource("/fxml/" + fxml + ".fxml"));
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
              activateView(newValue);
            });
  }

  public Scene getRootScene() {
    return rootScene;
  }

  /**
   * Add a view to the hashmap
   *
   * @param viewState The key of hashmap
   * @param fxmlLoaderData The value of the hashmap
   */
  private void addView(GameModel.ViewState viewState, FxmlLoaderData fxmlLoaderData) {
    this.viewMap.put(viewState, fxmlLoaderData);
  }

  /**
   * Display the view based on viewState of GameModel
   *
   * @param viewState of the current gameModel
   */
  private void activateView(GameModel.ViewState viewState) {
    FxmlLoaderData fxmlLoaderData = viewMap.get(viewState);
    Parent root = fxmlLoaderData.getRoot();

    // Debug print controller instance
    System.out.println((fxmlLoaderData.getFxmlLoader().getController().toString()));
    // Debug print controller class
    System.out.println((fxmlLoaderData.getFxmlLoader().getController().getClass().toString()));

    // Get view controller and refresh it.
    ControllerInterface controller =
        ((ControllerInterface) fxmlLoaderData.getFxmlLoader().getController());
    controller.refresh();
    this.rootScene.setRoot(root);
  }
}
