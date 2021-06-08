package cs5004.animator.model.shapes;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.Point2D;

/**
 * This represents Oval shape which implements IShape2D. Notices that the width of Oval equivalent
 * to double radius X, and height of Oval equivalent to double radius Y.
 */
public class Oval extends AbstractShape2D {

  /**
   * Create Oval with valid reference, width, height, and value of r, g, b.
   * @param reference center of Oval, must not be null
   * @param width     width of Oval, equivalent to 2 times radius X, must be positive
   * @param height    height of Oval, equivalent to 2 times radius Y, must be positive
   * @param r         r value of color, range: 0~255
   * @param g         g value of color, range: 0~255
   * @param b         b value of color, range: 0~255
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public Oval(Point2D reference, double width, double height, int r, int g, int b)
      throws IllegalArgumentException {
    super(reference, width, height, r, g, b);
  }

  /**
   * Create Oval with valid reference, width, height, and color.
   * @param reference center of Oval, must not be null
   * @param width     width of Oval, equivalent to 2 times radius X, must be positive
   * @param height    height of Oval, equivalent to 2 times radius Y, must be positive
   * @param color     color of Oval, must not be null
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public Oval(Point2D reference, double width, double height, Color color)
      throws IllegalArgumentException {
    super(reference, width, height, color);
  }

  @Override
  public TypeOfShape getType() {
    return TypeOfShape.OVAL;
  }

  @Override
  public IShape2D copy() {
    return new Oval(reference, width, height, color);
  }

  @Override
  public String toSVG() {
    String str = String.format("<ellipse id=\"NAME\" cx=\"%d\" cy=\"%d\" rx=\"%d\" ry=\"%d\" ",
        (int) reference.getX(), (int) reference.getY(),
        (int) width / 2, (int) height / 2);
    str = str + String.format("fill=\"rgb(%d,%d,%d)\" visibility=\"visible\" >",
        color.getR(), color.getG(), color.getB());
    return str;
  }

  @Override
  public String toString() {
    return String.format("Type: Oval\nReference: %s, Width: %.1f, Height: %.1f, Color: %s",
        reference, width, height, color);
  }

  @Override
  protected IMutableShape2D replicate(Point2D p, double width, double height, Color color)
      throws IllegalArgumentException {
    if (p == null || width <= 0 || height <= 0 || color == null) {
      throw new IllegalArgumentException("invalid data to replicate shape");
    }
    return new Oval(p, width, height, color);
  }
}
