package nz.ac.auckland.se206.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import nz.ac.auckland.se206.model.GameModel;

import java.io.IOException;
import java.util.HashMap;

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
    this.addView(GameModel.ViewState.CANVAS, new FxmlLoaderData(loadFxml("game")));
    this.addView(GameModel.ViewState.SELECTPROFILES, new FxmlLoaderData(loadFxml("select_profiles")));
    this.addView(GameModel.ViewState.NEWPROFILE, new FxmlLoaderData(loadFxml("new_profile")));
    this.addView(GameModel.ViewState.SETTINGS, new FxmlLoaderData(loadFxml("settings")));
    this.addView(GameModel.ViewState.MAINMENU, new FxmlLoaderData(loadFxml("main_menu")));
    this.addView(GameModel.ViewState.PROFILESTATS, new FxmlLoaderData(loadFxml("profile_stats")));


    this.rootScene = new Scene(this.viewMap.get(GameModel.ViewState.SELECTPROFILES).getRoot(), 642, 702);
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

  private void addView(GameModel.ViewState viewState, FxmlLoaderData fxmlLoaderData) {
    this.viewMap.put(viewState, fxmlLoaderData);
  }

  private void activateView(GameModel.ViewState viewState) {
    FxmlLoaderData fxmlLoaderData = viewMap.get(viewState);
    Parent root = fxmlLoaderData.getRoot();
    System.out.println((fxmlLoaderData.getFxmlLoader().getController().toString()));
    System.out.println((fxmlLoaderData.getFxmlLoader().getController().getClass().toString()));
//      viewFxmlLoader.setController(viewFxmlLoader.getControllerFactory().call(viewFxmlLoader.getController().getClass()));
    this.rootScene.setRoot(root);
  }
}
