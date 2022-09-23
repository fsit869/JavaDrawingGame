package nz.ac.auckland.se206;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nz.ac.auckland.se206.controllers.FxmlSwitcher;

/**
 * This is the entry point of the JavaFX application, while you can change this class, it should
 * remain as the class that runs the JavaFX application.
 */
public class App extends Application {
  public static void main(final String[] args) {
    launch();
  }

  /**
   * This method is invoked when the application starts. It loads and shows the "Canvas" scene.
   *
   * @param stage The primary stage of the application.
   */
  @Override
  public void start(final Stage stage) {
    FxmlSwitcher fxmlSwitcher = FxmlSwitcher.getInstance();
    stage.setScene(fxmlSwitcher.getRootScene());
    stage.setResizable(true);
    // Force close all threads
    stage.setOnCloseRequest(
        new EventHandler<WindowEvent>() {
          @Override
          public void handle(WindowEvent event) {
            System.exit(0);
          }
        });
    stage.show();
  }
}
