package cs5004.animator.model.shapes;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.Point2D;
import cs5004.animator.model.components.TimePeriod;

/**
 * This is a package-private interface extends IShape2D interface which contains setter methods to
 * allow one to mutate shape data. This interface should only be used within the model package.
 */
public interface IMutableShape2D extends IShape2D {

  /**
   * set color for shape with given color.
   * @param color color to be set, must be not null
   */
  void setColor(Color color);

  /**
   * set position for shape with given position.
   * @param position position to be set, required not null
   */
  void setPosition(Point2D position);

  /**
   * set size shape with given width and height, width height should be positive.
   * @param width  width to be set, must be positive
   * @param height height to be set, must be positive
   */
  void setSize(double width, double height);

  /**
   * Returns shape tween state in specific tick. More constrains may specify while implemented.
   * @param newS     end state of shape, required not null
   * @param duration period of change, required not null
   * @param tick     tick that tween happen, must be positive
   * @return tween shape of motion
   */
  IMutableShape2D tween(IShape2D newS, TimePeriod duration, int tick);
}
