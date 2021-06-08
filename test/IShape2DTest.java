import org.junit.Before;
import org.junit.Test;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.shapes.Oval;
import cs5004.animator.model.components.Point2D;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

/**
 * This contains test for all methods in IShape2D interface.
 */
public class IShape2DTest {
  IShape2D oval;
  IShape2D rect;

  @Before
  public void setUp() {
    oval = new Oval(new Point2D(0, 0), 6, 3, new Color(0, 1, 2));
    rect = new Rectangle(new Point2D(-1, 9), 7, 3, 255, 1, 255);
  }

  @Test
  public void testInvalidShape() {
    //invalid color: test all the edge cases
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, 256, 1, 255);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, 255, 0, 256);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, -1, 0, 255);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, 0, -1, 255);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, 0, 255, -1);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 3, 0, 256, 0);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(null, 5, 6, 0, 1, 2);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 0, 0, 0, 1, 2);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), -1, 3, 0, 1, 2);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 8, -3, new Color(0, 1, 2));
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      oval = new Oval(new Point2D(0, 0), 6, 7, null);
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void getReference() {
    assertEquals("(0.0, 0.0)", oval.getReference().toString());
    assertEquals("(-1.0, 9.0)", rect.getReference().toString());
  }

  @Test
  public void getWidth() {
    assertEquals(6, oval.getWidth(), 0.1);
    assertEquals(7, rect.getWidth(), 0.1);

  }

  @Test
  public void getHeight() {
    assertEquals(3, oval.getHeight(), 0.1);
    assertEquals(3, rect.getHeight(), 0.1);
  }

  @Test
  public void getColor() {
    assertEquals("(0, 1, 2)", oval.getColor().toString());
    assertEquals("(255, 1, 255)", rect.getColor().toString());
    assertEquals(0, oval.getR());
    assertEquals(1, oval.getG());
    assertEquals(2, oval.getB());
  }

  @Test
  public void copy() {
    String expected = oval.toString();
    // same data
    assertEquals(expected, oval.copy().toString());
    // not the same object
    assertNotEquals(oval, oval.copy());
  }
}