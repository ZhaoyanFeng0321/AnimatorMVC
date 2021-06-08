package cs5004.animator.view;

/**
 * An interface representing a way to view an animation. These Views process data from
 * IReadableModel and produce different output depends on its implementation. Views can also
 * display the model's animation at a certain speed (ticks per second).
 */
public interface IView {

  /**
   * Render the animation in specific speed. Different view will have different way to "show" the
   * view. TextView and SVGView output depends on its {@link Appendable}, VisualView and GUIView
   * will be given a signal to repaint itself.
   * @param tempo represents number of ticks to animate per second, which should be positive.
   */
  void render(int tempo);

  /**
   * Make the visual view and GUI view visible. Usually called after the view set up. SVG and Text
   * view do nothing if not support.
   */
  void makeVisible();

  /**
   * Returns view type.
   * @return view type
   */
  TypeOfView getViewType();
}