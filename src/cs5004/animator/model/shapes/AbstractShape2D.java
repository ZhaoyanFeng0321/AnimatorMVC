package cs5004.animator.model.shapes;

import java.util.Objects;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.Point2D;
import cs5004.animator.model.components.TimePeriod;

/**
 * This abstract class implements part of methods from IMutableShape2D and IShape2D which are
 * common operations to shape class extend this class.
 */
public abstract class AbstractShape2D implements IMutableShape2D {

  protected Point2D reference;
  protected double width;
  protected double height;
  protected Color color;

  /**
   * Create shape with valid reference, width, height, and value of r, g, b.
   * @param reference position of shape, must not be null
   * @param width     width of shape, must be positive
   * @param height    height of shape, must be positive
   * @param r         r value of color, range: 0~255
   * @param g         g value of color, range: 0~255
   * @param b         b value of color, range: 0~255
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public AbstractShape2D(Point2D reference, double width, double height, int r, int g, int b)
      throws IllegalArgumentException {
    if (reference == null || width <= 0 || height <= 0) {
      throw new IllegalArgumentException("invalid data to create shape");
    }
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("illegal value of RGB");
    }
    this.reference = reference;
    this.width = width;
    this.height = height;
    this.color = new Color(r, g, b);
  }

  /**
   * Create shape with valid reference, width, height, and color.
   * @param reference position of shape, must not be null
   * @param width     width of shape, must be positive
   * @param height    height of shape, must be positive
   * @param color     color of shape, must not be null
   * @throws IllegalArgumentException if invalid arguments provided.
   */
  public AbstractShape2D(Point2D reference, double width, double height, Color color)
      throws IllegalArgumentException {
    if (reference == null || width <= 0 || height <= 0 || color == null) {
      throw new IllegalArgumentException("invalid data to create shape");
    }
    this.reference = reference;
    this.width = width;
    this.height = height;
    this.color = color;
  }

  @Override
  public Point2D getReference() {
    return reference;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public void setColor(Color color) throws IllegalArgumentException {
    if (color == null) {
      throw new IllegalArgumentException("illegal color");
    }
    this.color = color;
  }

  @Override
  public int getR() {
    return color.getR();
  }

  @Override
  public int getG() {
    return color.getG();
  }

  @Override
  public int getB() {
    return color.getB();
  }

  @Override
  public void setSize(double width, double height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height should be positive");
    }
    this.width = width;
    this.height = height;
  }

  @Override
  public IMutableShape2D tween(IShape2D newS, TimePeriod duration, int tick)
      throws IllegalArgumentException {
    Objects.requireNonNull(newS);
    Objects.requireNonNull(duration);
    if (tick <= 0) {
      throw new IllegalArgumentException("invalid time to create tween shape");
    }
    int ta = duration.getStart();
    int tb = duration.getEnd();
    Point2D newPosition = newS.getReference();
    double x = interval(ta, tb, tick, reference.getX(), newPosition.getX());
    double y = interval(ta, tb, tick, reference.getY(), newPosition.getY());
    double h = interval(ta, tb, tick, height, newS.getHeight());
    double w = interval(ta, tb, tick, width, newS.getWidth());
    int r = (int) interval(ta, tb, tick, color.getR(), newS.getColor().getR());
    int g = (int) interval(ta, tb, tick, color.getG(), newS.getColor().getG());
    int b = (int) interval(ta, tb, tick, color.getB(), newS.getColor().getB());

    return this.replicate(new Point2D(x, y), w, h, new Color(r, g, b));
  }

  // helper method to compute tween value of shape attributes.
  private double interval(int ta, int tb, int tick, double va, double vb) {
    return va * (tb - tick) / (tb - ta) + vb * (tick - ta) / (tb - ta);
  }

  @Override
  public void setPosition(Point2D position) throws IllegalArgumentException {
    if (position == null) {
      throw new IllegalArgumentException("invalid position");
    }
    this.reference = position;
  }

  // a helper method to return an instance of Shape copy with given data.
  protected abstract IMutableShape2D replicate(Point2D p, double width, double height, Color color);
}
