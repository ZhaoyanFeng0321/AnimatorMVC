package cs5004.animator.model.components;

/**
 * This is a final class represents color in RGB. The value of r, g, and b is within 0~255.
 */
public class Color {
  private final int r;
  private final int g;
  private final int b;

  /**
   * Set the color with given r, g, b.
   * @param r represents the r of the color
   * @param g represents the g of the color
   * @param b represents the b of the color
   * @throws IllegalArgumentException if r, g or b is negative or larger than 255
   */
  public Color(double r, double g, double b) throws IllegalArgumentException {
    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
      throw new IllegalArgumentException("illegal rgb value to for color");
    }
    this.r = (int) r;
    this.g = (int) g;
    this.b = (int) b;
  }

  /**
   * Return the r of the color.
   * @return the r of the color
   */
  public int getR() {
    return r;
  }

  /**
   * Return the g of the color.
   * @return the g of the color
   */
  public int getG() {
    return g;
  }

  /**
   * Return the b of the color.
   * @return the b of the color
   */
  public int getB() {
    return b;
  }

  /**
   * Return a formatted string of r,g,b.
   * @return a formatted string of r,g,b
   */
  public String toString() {
    return String.format("(%d, %d, %d)", r, g, b);
  }
}
