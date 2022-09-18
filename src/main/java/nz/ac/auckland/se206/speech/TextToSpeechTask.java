package nz.ac.auckland.se206.speech;

import javafx.concurrent.Task;

/** Represents a TTS class that is on a different thread */
public class TextToSpeechTask {
  private Task<Void> speechTask;
  private TextToSpeech textToSpeech;
  private String wordToSpeak = "";
  private Thread thread;

  /** Create a new speech thread */
  public TextToSpeechTask() {
    createThread();
  }

  /** Create new speech thread */
  private void createThread() {
    // Voice object on a different thread
    this.textToSpeech = new TextToSpeech();
    this.speechTask =
        new Task<Void>() {
          @Override
          // Voice out word and update progress
          protected Void call() {
            updateProgress(0, 1);
            textToSpeech.speak(wordToSpeak);
            updateProgress(1, 1);
            return null;
          }
        };

    this.thread = new Thread(this.speechTask);
  }

  /**
   * TTS a word. Since it is on a different thread will not impact main app.
   *
   * @param wordToSpeak Word to TTS
   */
  public void speak(String wordToSpeak) {
    this.wordToSpeak = wordToSpeak;
    createThread();
    this.thread.start();
  }
}