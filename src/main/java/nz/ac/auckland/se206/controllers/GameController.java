package nz.ac.auckland.se206.controllers;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import nz.ac.auckland.se206.model.GameModel;
import nz.ac.auckland.se206.model.TimerTask;
import nz.ac.auckland.se206.profiles.ProfileFactory;
import nz.ac.auckland.se206.profiles.entities.StatsData;
import nz.ac.auckland.se206.profiles.entities.WordsData;
import nz.ac.auckland.se206.speech.TextToSpeechTask;

/** This is the controller for the game. */
public class GameController {
  private static final int TIMER_MAX = 60;
  // FXML Components
  @FXML private AnchorPane winLoseDialogue;
  @FXML private Text winLoseText;
  @FXML private Label timerLabel;
  @FXML private Label wordLabel;
  @FXML private Canvas canvas;
  @FXML private TabPane readyPaneMenu;
  @FXML private TabPane inGamePaneMenu;
  @FXML private TabPane endGamePaneMenu;
  @FXML private TextArea predictionTextArea;

  private GraphicsContext graphic;

  // Model layer objects
  private GameModel gameModel;
  private TextToSpeechTask textToSpeech;
  private Service<Void> timerService;
  private ProfileFactory profileFactory;

  // mouse coordinates
  private double currentX;
  private double currentY;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   *
   * @throws ModelException If there is an error in reading the input/output of the DL model.
   * @throws IOException If the model cannot be found on the file system.
   */
  public void initialize() {
    graphic = canvas.getGraphicsContext2D();
    this.gameModel = GameModel.getInstance();
    this.textToSpeech = new TextToSpeechTask();

    try {
      this.profileFactory = new ProfileFactory();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Configure game settings
    this.timerService =
        new Service<Void>() {
          @Override
          protected Task<Void> createTask() {
            return new TimerTask(
                TIMER_MAX, timerLabel, predictionTextArea, gameModel, GameController.this);
          }
        };
    this.setupStateBindings();
    this.setTimerBindings();
    this.setupBrush(false);
    onReadyState();
  }

  /** Handles bindings for the timer thread. */
  private void setTimerBindings() {
    // Occurs when player wins
    this.timerService.setOnCancelled(
        e -> {
          this.gameModel.setCurrentGameState(GameModel.State.FINISHED);
        });

    // Occurs when player loses
    this.timerService.setOnSucceeded(
        e -> {
          this.timerLabel.setText("Out of time");
          this.gameModel.setCurrentGameState(GameModel.State.FINISHED);
        });
  }

  /**
   * State transition method. Handles which state to transfer next.
   *
   * @param actionEvent Event from button
   */
  @FXML
  private void onStateChangeButton(ActionEvent actionEvent) {
    switch (gameModel.getCurrentGameState()) {
      case READY:
        gameModel.setCurrentGameState(GameModel.State.INGAME);
        break;
      case INGAME:
        gameModel.setCurrentGameState(GameModel.State.FINISHED);
        break;
      case FINISHED:
        gameModel.setCurrentGameState(GameModel.State.READY);
        break;
    }
  }

  /////////////////////
  // Button handlers //
  /////////////////////

  /** This method is called when the ready state is started */
  private void onReadyState() {
    // Set UI
    this.canvas.setDisable(true);
    this.onClear();
    this.timerLabel.setText(String.valueOf(TIMER_MAX));
    this.predictionTextArea.setText("Your predictions will show up here");
    //        this.statusLabel.setText("Ready");
    this.readyPaneMenu.setVisible(true);
    this.endGamePaneMenu.setVisible(false);
    this.winLoseDialogue.setVisible(false);

    // Set game variables
    this.gameModel.generateWord(WordsData.Difficulty.E);
    this.gameModel.setPlayerWon(false);
  }

  /** This method is called when the ingame state is started */
  private void onInGameState() {
    // Set UI and timer
    this.canvas.setDisable(false);
    this.textToSpeech.speak(String.format("Start drawing a %s", gameModel.getCurrentWordToGuess()));
    this.readyPaneMenu.setVisible(false);
    this.inGamePaneMenu.setVisible(true);
    this.timerService.start();
  }

  /** This method is called when the finished state is started */
  private void onFinishedState() {
    // Set UI settings
    this.canvas.setDisable(true);
    this.inGamePaneMenu.setVisible(false);
    this.endGamePaneMenu.setVisible(true);

    // TTS the end game
    if (this.gameModel.isPlayerWon()) {
      this.textToSpeech.speak("Winner");
      this.winLoseText.setText("You Win!");
    } else {
      this.textToSpeech.speak("Haha loser");
      this.winLoseText.setText("You Lose");
    }
    this.winLoseDialogue.setVisible(true);

    // Disable timer
    this.timerService.cancel();
    this.timerService.reset();

    // Handle saving user profile stats
    try {
      this.saveProfileStats();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (TranslateException e) {
      e.printStackTrace();
    }
  }

  /** Saves the profile information and win/loss */
  private void saveProfileStats() throws IOException, TranslateException {
    StatsData statsData = this.gameModel.getProfile().getStatsData();
    // Save fastest time only if won.
    if (gameModel.isPlayerWon()) {
      statsData.setBestTime(TIMER_MAX - Integer.parseInt(this.timerLabel.getText()));
    } else {
      // When player loses they run out of time.
      statsData.setBestTime(TIMER_MAX);
    }

    // Save win/loss and also stats accuracy
    if (gameModel.isPlayerWon()) {
      statsData.addWins();
      statsData.setBestAccuracy(determineAccuracy());
    } else {
      statsData.addLosses();
      statsData.setBestAccuracy(0);
    }

    this.profileFactory.saveProfile(gameModel.getProfile());
  }

  /**
   * This method determines the accuracy of the guess and rounds it up
   *
   * @return
   */
  private int determineAccuracy() throws TranslateException {
    List<Classifications.Classification> predictions =
        gameModel.getPredictions(this.getCurrentSnapshot(), TimerTask.TOTAL_PREDICTIONS);

    // Find the best probability and round up
    for (final Classifications.Classification classification : predictions) {
      if (classification
          .getClassName()
          .replace("_", " ")
          .equals(gameModel.getCurrentWordToGuess())) {
        return (int) Math.ceil(classification.getProbability() * 100);
      }
    }
    throw new IllegalAccessError(
        String.format(
            "Could not find the word to guess in top %d predictions! Unknown accuracy",
            TimerTask.TOTAL_PREDICTIONS));
  }

  /**
   * Get the current snapshot of the canvas.
   *
   * @return The BufferedImage corresponding to the current canvas content.
   */
  public BufferedImage getCurrentSnapshot() {
    final Image snapshot = canvas.snapshot(null, null);
    final BufferedImage image = SwingFXUtils.fromFXImage(snapshot, null);

    // Convert into a binary image.
    final BufferedImage imageBinary =
        new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

    final Graphics2D graphics = imageBinary.createGraphics();

    graphics.drawImage(image, 0, 0, null);

    // To release memory we dispose.
    graphics.dispose();
    return imageBinary;
  }

  /**
   * Setup the brush mode or eraser mode. Eraser mode currently sets all pixels to white and brush
   * mode sets all pixels to black.
   *
   * @param eraserMode True to turn on eraser.
   */
  private void setupBrush(boolean eraserMode) {
    // save coordinates when mouse is pressed on the canvas
    canvas.setOnMousePressed(
        e -> {
          currentX = e.getX();
          currentY = e.getY();
        });

    canvas.setOnMouseDragged(
        e -> {
          // Brush size (you can change this, it should not be too small or too large).
          double size = 6;

          final double x = e.getX() - size / 2;
          final double y = e.getY() - size / 2;

          // This is the colour of the brush.
          if (eraserMode) {
            graphic.setFill(javafx.scene.paint.Color.WHITE);
            graphic.setStroke(javafx.scene.paint.Color.WHITE);
            size = 18;
          } else {
            graphic.setStroke(javafx.scene.paint.Color.BLACK);
            graphic.setFill(Color.BLACK);
            size = 6;
          }
          graphic.setLineWidth(size);

          // Create a line that goes from the point (currentX, currentY) and (x,y)
          graphic.strokeLine(currentX, currentY, x, y);

          // update the coordinates
          currentX = x;
          currentY = y;
        });
  }

  /**
   * Handle bindings of state changes. IE, when this state occurs, what methods should be called.
   * Also handles binding of the word label.
   */
  private void setupStateBindings() {
    // Bind word label
    wordLabel.textProperty().bind(gameModel.getCurrentWordToGuessProperty());

    // Bindings for when game state changed
    gameModel
        .getCurrentGameStateProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              switch (newValue) {
                case READY:
                  this.onReadyState();
                  break;
                case INGAME:
                  this.onInGameState();
                  break;
                case FINISHED:
                  this.onFinishedState();
                  break;
                default:
                  // Unknown state that is not handled.
                  throw new IllegalStateException(
                      String.format(
                          "Unknown state [%s]\n Transitioned from [%s] state", oldValue, newValue));
              }
            });
  }

  /**
   * Handles when eraser toggle button pressed
   *
   * @param actionEvent Event of toggle button
   */
  @FXML
  private void onEraserToggle(ActionEvent actionEvent) {
    setupBrush(true);
  }

  /**
   * Handles when brush toggle button pressed
   *
   * @param actionEvent Event of toggle button
   */
  @FXML
  private void onBrushToggle(ActionEvent actionEvent) {
    setupBrush(false);
  }

  /** This method is called when the "Clear" button is pressed. */
  @FXML
  private void onClear() {
    graphic.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**
   * This method handles when save method is called
   *
   * @param actionEvent Event of JavaFX button component
   * @throws IOException Occurs when file IO error
   */
  @FXML
  private void onSaveCanvas(ActionEvent actionEvent) throws IOException {
    // Find location to save
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save canvas location");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("BMP (*.bmp)", "*.bmp"));
    fileChooser.setInitialFileName("myImage");
    File selectedFile = fileChooser.showSaveDialog(endGamePaneMenu.getScene().getWindow());

    // Request save only if user did not press cancel
    if (selectedFile != null) {
      ImageIO.write(getCurrentSnapshot(), "bmp", selectedFile);
    }
  }

  /**
   * This button is called when requesting the menu
   *
   * @param actionEvent Button event
   */
  @FXML
  private void onMenuButton(ActionEvent actionEvent) {
    this.gameModel.setCurrentViewState(GameModel.viewState.MAINMENU);
  }

  /**
   * This button is called when starting game
   *
   * @param actionEvent Button event
   */
  @FXML
  private void onStartGameButton(ActionEvent actionEvent) {
    this.gameModel.setCurrentGameState(GameModel.State.INGAME);
  }

  /**
   * This button is called when giving up request
   *
   * @param actionEvent Button event
   */
  @FXML
  private void onGiveUpButton(ActionEvent actionEvent) {
    this.gameModel.setCurrentGameState(GameModel.State.FINISHED);
  }

  /**
   * This button is called when requesting new game
   *
   * @param actionEvent Button event
   */
  @FXML
  private void onNewGameButton(ActionEvent actionEvent) {
    this.gameModel.setCurrentGameState(GameModel.State.READY);
  }

  /**
   * Closes the win/loss menu
   *
   * @param mouseEvent Button event
   */
  @FXML
  private void onWinLoseClose(MouseEvent mouseEvent) {
    this.winLoseDialogue.setVisible(false);
  }
}
