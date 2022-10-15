package nz.ac.auckland.se206;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import nz.ac.auckland.se206.controllers.FxmlSwitcher;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.profiles.ProfileFactory;

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
    // set stage title and icon
    stage.setTitle("Quick Draw 206 version!");
    stage.getIcons().add(new Image("images/pencil.png"));
    stage.setResizable(false);

    stage.show();
  }

  /** Called when app is closed. Ensures profile is saved before closing */
  @Override
  public void stop() {
    try {
      // Save the profile
      ProfileFactory profileFactory = new ProfileFactory();
      profileFactory.saveProfile(GameModel.getInstance().getProfile());
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Exit app
    System.exit(0);
  }
}
