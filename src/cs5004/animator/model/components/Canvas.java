package cs5004.animator.model.components;

/**
 * A class that represents the leftmost x value, the topmost y value, width, and height of the
 * canvas.
 */
public class Canvas {
  private final int x;
  private final int y;
  private final int width;
  private final int height;

  /**
   * Specify the boundary and size of the animation.
   * @param x      The leftmost x value
   * @param y      The topmost y value
   * @param width  The width of the bounding box
   * @param height The height of the bounding box
   */
  public Canvas(int x, int y, int width, int height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width or height must be positive value");
    }
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Gets this canvas' leftmost x.
   * @return the leftmost x of this canvas
   */
  public int getX() {
    return this.x;
  }

  /**
   * Gets this canvas' topmost y.
   * @return the topmost y of this canvas
   */
  public int getY() {
    return this.y;
  }

  /**
   * Gets this canvas' width.
   * @return the width of this canvas
   */
  public int getWidth() {
    return this.width;
  }

  /**
   * Gets this canvas' height.
   * @return the height of this canvas
   */
  public int getHeight() {
    return this.height;
  }

  /**
   * converts the canvas to a string form.
   * @return string form of the canvas
   */
  @Override
  public String toString() {
    return this.x + " " + this.y + " " + this.width + " " + this.height + "\n";
  }
}