package cs5004.animator.view;

/**
 * This is a IFeature interface that encapsulate each of the features in a {@link GUIView} as a
 * callback function.
 */
public interface IFeature {
  /**
   * Start the animation.
   */
  void start();

  /**
   * Pauses the animation.
   */
  void pause();

  /**
   * Resume the animation from where it stops.
   */
  void resume();

  /**
   * Restart the animation from the beginning.
   */
  void restart();

  /**
   * Enable or disable the loop of animation playing.
   * @param value true if want a loop, false to disable the loop.
   */
  void setLoop(boolean value);

  /**
   * Change the speed of the animation display with given factor. It will change based on the
   * initial speed.
   * @param factor it should be positive. If factor less than 1 is to decrease speed, bigger than 1
   *               is to increase speed and 1 is to set to the initial speed.
   */
  void changeSpeed(double factor);
}
