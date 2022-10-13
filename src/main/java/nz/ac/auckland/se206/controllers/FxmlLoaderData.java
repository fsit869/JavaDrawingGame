package nz.ac.auckland.se206.controllers;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/** This class is for storing the views and controller */
public class FxmlLoaderData {
  private Parent root;
  private FXMLLoader fxmlLoader;

  /**
   * Create a new data object to be loaded
   *
   * @param fxmlLoader Fxml view loader
   */
  public FxmlLoaderData(FXMLLoader fxmlLoader) {
    // Store fxmlLoader
    this.fxmlLoader = fxmlLoader;
    // Load the root
    try {
      this.root = fxmlLoader.load();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get the root parent view of the root
   *
   * @return Parent root view
   */
  public Parent getRoot() {
    return root;
  }

  /**
   * returns the Fxml Loader obj for
   *
   * @return loader obj
   */
  public FXMLLoader getFxmlLoader() {
    return fxmlLoader;
  }
}
