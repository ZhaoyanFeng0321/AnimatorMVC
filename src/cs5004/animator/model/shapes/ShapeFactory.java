package cs5004.animator.model.shapes;

import cs5004.animator.model.components.Point2D;

/**
 * This is a ShapeFactory to help create Shape with given shape data.
 */
public class ShapeFactory {

  /**
   * Create Shape with given shape data. Only support {@link TypeOfShape}. It will throw
   * IllegalArgumentException is illegal arguments provided.
   * @param shape  TypeOfShape
   * @param x      x-position of the shape
   * @param y      y-position of the shape
   * @param width  xDiameter of shape
   * @param height yDiameter of shape
   * @param r      red color-value of the shape
   * @param g      green color-value of the shape
   * @param b      blue color-value of the shape
   * @return shape created
   * @throws IllegalArgumentException if shape cannot be constructed because of illegal value
   */
  public IMutableShape2D create(TypeOfShape shape, int x, int y, int width, int height,
                                int r, int g, int b) throws IllegalArgumentException {
    try {
      switch (shape) {
        case OVAL:
          return new Oval(new Point2D(x, y), width, height, r, g, b);
        case RECTANGLE:
          return new Rectangle(new Point2D(x, y), width, height, r, g, b);
        default:
          return null;
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
