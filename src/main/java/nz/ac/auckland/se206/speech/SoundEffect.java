package nz.ac.auckland.se206.speech;

import java.net.URISyntaxException;
import javafx.scene.media.AudioClip;

public class SoundEffect {
  /** Register sound effects as enums with values */
  public enum EFFECT {
    TEST("/sounds/mixkit-alert-quick-chime-766.wav"),
    TEST_TWO("/sounds/mixkit-security-facility-breach-alarm-994.wav");

    private final String SOUND_LOCATION;

    /**
     * Get enum value (Sound location) as string
     *
     * @return
     */
    private String getSoundLocation() {
      return SOUND_LOCATION;
    }

    /**
     * Enum constructor to init sound effect location
     *
     * @param soundLocation
     */
    EFFECT(String soundLocation) {
      try {
        this.SOUND_LOCATION = SoundEffect.class.getResource(soundLocation).toURI().toString();
      } catch (URISyntaxException e) {
        throw new RuntimeException("Unexpected error with sound effect " + soundLocation);
      } catch (NullPointerException e) {
        throw new RuntimeException(soundLocation + " Sound effect not found");
      }
    }
  }

  private static AudioClip audioClip = new AudioClip(EFFECT.TEST.getSoundLocation());

  /**
   * Stop previous sound effect and play new sound effect. Differs from playEffectParallel as this
   * one only allows one sound to be played at a time. This effect can be stopped. Use
   * stopSoundEffect()
   *
   * @param effectToPlay Enum effect to play
   */
  public static void playEffectOverride(EFFECT effectToPlay) {
    audioClip.stop();
    audioClip = new AudioClip(effectToPlay.getSoundLocation());
    audioClip.play();
  }

  /** Stops any playing sound effects DOES NOT stop the parallelSoundEffect method. */
  public static void stopSoundEffect() {
    audioClip.stop();
  }

  /**
   * Play a sound effect, alongside any existing sound effects. IN parallel NOTE: There is no way to
   * stop this sound effect until it finishes.
   *
   * @param effectToPlay
   */
  public static void playEffectParallel(EFFECT effectToPlay) {
    AudioClip parallelClip = new AudioClip(effectToPlay.getSoundLocation());
    parallelClip.play();
  }
}
