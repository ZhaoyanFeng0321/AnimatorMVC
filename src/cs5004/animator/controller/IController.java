package cs5004.animator.controller;

/**
 * This is an IController interface of the whole MVC model. It will control the flow and data
 * operation in the middle between model and view.
 */
public interface IController {

  /**
   * Start the program, i.e. give control to the controller
   * @param speed the speed of animation
   */
  void run(int speed);
}
