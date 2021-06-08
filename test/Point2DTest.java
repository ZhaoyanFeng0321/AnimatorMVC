import org.junit.Before;
import org.junit.Test;

import cs5004.animator.model.components.Point2D;

import static org.junit.Assert.assertEquals;

/**
 * This contains test for all methods in Point2D class.
 */
public class Point2DTest {
  Point2D p1;
  Point2D p2;

  @Before
  public void setUp() {
    p1 = new Point2D(3.30, -1.40);
    p2 = new Point2D(0, 0);

  }

  @Test
  public void getX() {
    assertEquals(3.3, p1.getX(), 0.1);
    assertEquals(0, p2.getX(), 0.1);

  }

  @Test
  public void getY() {
    assertEquals(-1.4, p1.getY(), 0.1);
    assertEquals(0, p2.getY(), 0.1);
  }

  @Test
  public void testToString() {
    assertEquals("(3.3, -1.4)", p1.toString());
    assertEquals("(0.0, 0.0)", p2.toString());
  }
}