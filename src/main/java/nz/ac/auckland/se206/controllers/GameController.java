package nz.ac.auckland.se206.controllers;

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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
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
public class GameController implements ControllerInterface {
  private static final int TIMER_MAX = 60;
  // FXML Components
  @FXML private Button zenNextWordButton;
  @FXML private Button giveUpButton;
  @FXML private ColorPicker colourPicker;
  @FXML private Label confidenceLabel;
  @FXML private Label accuracyLabel;
  @FXML private RadioButton brushRadioButton;
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
  private boolean startedDrawing;

  // mouse coordinates
  private double currentX;
  private double currentY;
  private int accuracyValue = 0;

  /**
   * JavaFX calls this method once the GUI elements are loaded. In our case we create a listener for
   * the drawing, and we load the ML model.
   */
  public void initialize() {
    // Init objects required objects
    graphic = canvas.getGraphicsContext2D();
    this.gameModel = GameModel.getInstance();
    this.textToSpeech = new TextToSpeechTask();

    // Initialize the profile saver
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
    this.startedDrawing = false;

    // Setup colour picker for zen
    this.colourPicker.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        setupBrush(false);
      }
    });
    onReadyState();
  }

  /**
   * Reset the view to ready game state
   */
  @Override
  public void refresh() {
    this.gameModel.setCurrentGameState(GameModel.State.READY);

    // Setup zenmode settings
    if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
      this.giveUpButton.setText("End");
      this.zenNextWordButton.setVisible(true);
      this.colourPicker.setVisible(true);
    } else {
      this.colourPicker.setVisible(false);
      this.zenNextWordButton.setVisible(false);
      this.giveUpButton.setText("Give up");
    }
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

  /////////////////////
  // Button handlers //
  /////////////////////

  /** This method is called when the ready state is started */
  private void onReadyState() {
    // Set UI
    this.canvas.setDisable(true);
    this.onClear();

    // If zen mode dont show timer
    if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
      this.timerLabel.setText("Zen mode!");
    } else {
      this.timerLabel.setText(String.valueOf(TIMER_MAX));
    }
    this.predictionTextArea.setText("Your predictions will show up here");
    this.readyPaneMenu.setVisible(true);
    this.endGamePaneMenu.setVisible(false);
    this.winLoseDialogue.setVisible(false);
    this.setAccuracyLabelMet(false);
    this.setConfidenceLabelMet(false);

    // Set game variables
    this.gameModel.generateWord(WordsData.Difficulty.E);
    this.gameModel.setPlayerWon(false);
    this.startedDrawing = false;
  }

  /** This method is called when the ingame state is started */
  private void onInGameState() {
    // Set UI and timer
    this.canvas.setDisable(false);
    this.textToSpeech.speak(String.format("Start drawing a %s", gameModel.getCurrentWordToGuess()));

    // Set to brush
    this.setupBrush(false);
    this.brushRadioButton.setSelected(true);

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

    // Prevent mouse from continue drawing after round ends
    canvas.setOnMouseDragged(e -> {});

    // TTS the end game and win/lose diagoue only if not zen mode
    if (!this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
      if (this.gameModel.isPlayerWon()) {
        this.textToSpeech.speak("Winner");
        this.winLoseText.setText("You Win!");
      } else {
        this.textToSpeech.speak("Haha loser");
        this.winLoseText.setText("You Lose");
      }
      this.winLoseDialogue.setVisible(true);
    }

    // Disable timer
    this.timerService.cancel();
    this.timerService.reset();

    // Handle saving user profile stats. If zen mode dont save
    if (!this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
      try {
        this.saveProfileStats();
      } catch (IOException | TranslateException e) {
        e.printStackTrace();
      }
    }

  }

  /** Saves the profile information and win/loss */
  private void saveProfileStats() throws IOException, TranslateException {
    StatsData statsData = this.gameModel.getProfile().getStatsData();
    System.out.println("saving");
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
      statsData.setBestAccuracy(accuracyValue);
    } else {
      statsData.addLosses();
      statsData.setBestAccuracy(0);
    }

    this.profileFactory.saveProfile(gameModel.getProfile());
  }

  public void setAccuracyValue(int accuracyValue) {
    this.accuracyValue = accuracyValue;
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

          setBrush(e, eraserMode);
        });

    // Allow for click painting
    canvas.setOnMouseDragged(
        e -> {
          setBrush(e, eraserMode);
        });
  }

  /**
   * Set up brush settings
   *
   * @param e Event of clicky
   * @param eraserMode Toggle eraser
   */
  private void setBrush(MouseEvent e, boolean eraserMode) {
    // Brush size (you can change this, it should not be too small or too large).
    double size = 6;

    final double x = e.getX() - size / 2;
    final double y = e.getY() - size / 2;

    // This is the colour of the brush.
    if (eraserMode) {
      graphic.setFill(Color.WHITE);
      graphic.setStroke(Color.WHITE);
      size = 18;
    } else {
      if (this.gameModel.getCurrentGameMode().equals(GameModel.GameMode.ZEN)) {
        // If zen mode enable colour picker
        graphic.setStroke(this.colourPicker.getValue());
        graphic.setFill(this.colourPicker.getValue());
      } else {
        // If not zen mode only black
        graphic.setStroke(Color.BLACK);
        graphic.setFill(Color.BLACK);
      }
      setStartedDrawing(true);
      size = 6;
    }
    graphic.setLineWidth(size);

    // Create a line that goes from the point (currentX, currentY) and (x,y)
    graphic.strokeLine(currentX, currentY, x, y);

    // update the coordinates
    currentX = x;
    currentY = y;
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
    setStartedDrawing(false);
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
    this.gameModel.setCurrentViewState(GameModel.ViewState.MAINMENU);
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

  @FXML
  private void onWinLoseClose(ActionEvent actionEvent) {
    this.winLoseDialogue.setVisible(false);
  }

  public boolean isStartedDrawing() {
    return startedDrawing;
  }

  public void setStartedDrawing(boolean startedDrawing) {
    this.startedDrawing = startedDrawing;
  }

  /**
   * Set colour of accuracy label if met
   * @param isMet Boolean met
   */
  public void setAccuracyLabelMet(boolean isMet) {
    if (isMet) {
      this.accuracyLabel.setTextFill(Color.GREEN);
    } else {
      this.accuracyLabel.setTextFill(Color.RED);
    }
  }

  /**
   * Set the colour of confidence if met
   * @param isMet Boolean met
   */
  public void setConfidenceLabelMet(boolean isMet) {
    if (isMet) {
      this.confidenceLabel.setTextFill(Color.GREEN);
    } else {
      this.confidenceLabel.setTextFill(Color.RED);
    }
  }

  @FXML
  private void onZenNextWord(ActionEvent actionEvent) {
  }
}
