import org.junit.Before;
import org.junit.Test;

import cs5004.animator.model.components.Color;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This contains test for all methods in Color.
 */
public class ColorTest {
  private Color c1;
  private Color c2;
  private Color c3;

  @Before
  public void setUp() {
    c1 = new Color(0, 255, 8);
    c2 = new Color(0, 0, 0);
    c3 = new Color(255, 255, 255);
  }

  @Test
  public void testObjectData() {
    assertEquals(0, c1.getR());
    assertEquals(255, c1.getG());
    assertEquals(8, c1.getB());
    assertEquals(0, c2.getR());
    assertEquals(0, c2.getG());
    assertEquals(0, c2.getB());
    assertEquals(255, c3.getR());
    assertEquals(255, c3.getG());
    assertEquals(255, c3.getB());
  }

  @Test
  public void testInvalidColor() {
    //test all the edge cases
    try {
      c1 = new Color(-1, 255, 8); // r<0
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      c1 = new Color(256, 255, 8); // r > 255
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      c1 = new Color(0, 256, 8); // g >255
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      c1 = new Color(0, -1, 8); // g < 0
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      c1 = new Color(255, 255, -1); // b < 0
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      c1 = new Color(255, 255, 256); // b > 255
      fail("fail to throw proper exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testToString() {
    assertEquals("(0, 255, 8)", c1.toString());
  }
}