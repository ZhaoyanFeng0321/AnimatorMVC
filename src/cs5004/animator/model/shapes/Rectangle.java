package cs5004.animator.model.shapes;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.Point2D;

/**
 * This represents Rectangle which implements RegularShape and IShape2D.
 */
public class Rectangle extends AbstractShape2D {

  /**
   * Create Rectangle with valid reference, width, height, and value of r, g, b.
   * @param reference Min corner of Rectangle, must not be null
   * @param width     width of Rectangle, must be positive
   * @param height    height of Rectangle, must be positive
   * @param r         r value of color, range: 0~255
   * @param g         g value of color, range: 0~255
   * @param b         b value of color, range: 0~255
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public Rectangle(Point2D reference, double width, double height, int r, int g, int b)
      throws IllegalArgumentException {
    super(reference, width, height, r, g, b);
  }

  /**
   * Create Rectangle with valid reference, width, height, and color.
   * @param reference Min corner of Rectangle, must not be null
   * @param width     width of Rectangle, must be positive
   * @param height    height of Rectangle, must be positive
   * @param color     color of Rectangle, must not be null
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public Rectangle(Point2D reference, double width, double height, Color color)
      throws IllegalArgumentException {
    super(reference, width, height, color);
  }

  @Override
  public TypeOfShape getType() {
    return TypeOfShape.RECTANGLE;
  }

  @Override
  public IShape2D copy() {
    return new Rectangle(reference, width, height, color);
  }

  @Override
  public String toSVG() {
    String str = String.format("<rect id=\"NAME\" x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" ",
        (int) reference.getX(), (int) reference.getY(),
        (int) width, (int) height);
    str = str + String.format("fill=\"rgb(%d,%d,%d)\" visibility=\"visible\" >",
        color.getR(), color.getG(), color.getB());
    return str;
  }

  @Override
  protected IMutableShape2D replicate(Point2D p, double width, double height, Color color)
      throws IllegalArgumentException {
    if (p == null || width <= 0 || height <= 0 || color == null) {
      throw new IllegalArgumentException("invalid data to replicate shape");
    }
    return new Rectangle(p, width, height, color);
  }

  @Override
  public String toString() {
    return String.format("Type: Rectangle\nReference: %s, Width: %.1f, Height: %.1f, Color: %s",
        reference, width, height, color);
  }
}
