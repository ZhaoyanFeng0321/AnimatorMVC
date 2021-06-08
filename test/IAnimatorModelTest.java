import org.junit.Before;
import org.junit.Test;

import cs5004.animator.model.AnimatorModel;
import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.ShapeFactory;
import cs5004.animator.model.shapes.TypeOfShape;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * This contains tests for all methods in IAnimatorModel and additional methods in AnimatorModel.
 */
public class IAnimatorModelTest {
  IAnimatorModel test1;
  IShape2D s1;
  IShape2D s2;
  ShapeFactory shapeFactory;

  @Before
  public void setUp() {
    test1 = new AnimatorModel();
    shapeFactory = new ShapeFactory();
    s1 = shapeFactory.create(TypeOfShape.OVAL, 2, 4, 6, 3, 0, 255, 8);
    s2 = shapeFactory.create(TypeOfShape.RECTANGLE, -4, 6, 2, 5,
        255, 255, 255);
  }

  @Test
  public void testModelSetUp() {
    assertEquals("", test1.getState());
    assertEquals("100 100 100 100\n", test1.getCanvas().toString());
    try {
      test1.setCanvas(0, 0, -10, 10);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
  }

  @Test
  public void testAdd() {
    // check proper add and will not change length of animation but storage left
    test1.addShape("s1", s1);
    assertEquals(0, test1.getLength());
    assertEquals("Shapes:\n"
            + "Name: s1\n"
            + "Type: Oval\n"
            + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n\n",
        test1.getState());

    // able to add same shape with different name
    test1.addShape("s2", s1);

    // cannot add different shape with existing name
    try {
      test1.addShape("s1", s2);
    } catch (IllegalArgumentException e) {
      // test pass
    }

    // able to add shape in same appear period
    test1.addShape("s3", s2);
    // able to add shape appear whole animation (edge cases)
    test1.addShape("s4", s2);
    //assertEquals("", test1.getState());
    test1.addAction("s1", 3, 2, 4, 6, 3, 0, 255, 8,
        10, 2, 4, 6, 3, 0, 255, 8);
    String[] lines = test1.getState().split("Name: ");
    assertEquals("s1\n"
        + "Type: Oval\n"
        + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n"
        + "Appears at t=3s, disappears at t=10s\n", lines[lines.length - 1]);
  }

  @Test
  public void testIllegalAdd() {
    test1.addShape("s3", s1);
    try {
      test1.addShape("s3", s2);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    // invalid name of shape, "" or null
    try {
      test1.addShape("", s2);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    try {
      test1.addShape(null, s2);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    // one of parameters == null
    try {
      test1.addShape("s3", null);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
  }

  @Test
  public void testRemoveShape() {
    // Case: remove from empty model, return false and storage not change
    test1 = new AnimatorModel();
    assertFalse(test1.removeShape("nothing"));

    // Case: legal remove, return true
    test1.addShape("s1", s1);
    assertTrue(test1.removeShape("s1"));
    try {
      test1.getInitialShape("s1");
      fail("remove s1 failed");
    } catch (IllegalArgumentException e) {
      // test passes
    }

    // Case: identifier == null or empty
    assertFalse(test1.removeShape(""));
  }

  @Test
  public void testMove() {
    // regular move
    test1.addShape("s1", s1);
    test1.addAction("s1", 1, 2, 4, 6, 3, 0, 255, 8,
        2, -4, 4, 6, 3, 0, 255, 8);
    test1.addAction("s1", 2, -4, 4, 6, 3, 0, 255, 8,
        3, 3, 3, 6, 3, 0, 255, 8);
    assertEquals(2, test1.getMotions("s1").size());
    // move to same position again
    test1.addAction("s1", 5, 3, 3, 6, 3, 0, 255, 8,
        9, 3, 3, 6, 3, 0, 255, 8);
    assertEquals(3, test1.getMotions("s1").size());
    // move other shape in same time
    test1.addShape("s2", s2);
    test1.addAction("s2", 5, -4, 6, 2, 5, 255, 255, 255,
        9, 3, 3, 2, 5, 255, 255, 255);
    assertEquals("(-4.0, 6.0)", test1.getInitialShape("s2").getReference().toString());

    // invalid identifier: "" / null/ shape not exist
    try {
      test1.addAction("", 5, -4, 6, 2, 5, 255, 255, 255,
          9, 3, 3, 2, 5, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction(null, 5, -4, 6, 2, 5, 255, 255, 255,
          9, 3, 3, 2, 5, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // invalid position (null)
    try {
      test1.addAction("not exist", 5, -4, 6, 2, 5, 255, 255, 255,
          9, 3, 3, 2, 5, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testIllegalMoveTime() {
    test1.addShape("s1", s1);
    // move in overlap time (another move or motion is making)
    test1.addAction("s1", 2, 2, 4, 6, 3, 0, 255, 8,
        5, 8, 7, 6, 3, 0, 255, 8);
    try {
      test1.addAction("s1", 2, 2, 4, 6, 3, 0, 255, 8,
          5, -8, 7, 6, 3, 0, 255, 8); // same time
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s1", 2, 2, 4, 6, 3, 0, 255, 8,
          3, -8, 7, 6, 3, 0, 255, 8); // overlap
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s1", 4, 2, 4, 6, 3, 0, 255, 8,
          6, -8, 7, 6, 3, 0, 255, 8); // overlap
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // move at illegal time:
    // 1. start < 0
    try {
      test1.addAction("s1", -1, 2, 4, 6, 3, 0, 255, 8,
          3, 8, 7, 6, 3, 0, 255, 8);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // 2. start < end && end < 0
    try {
      test1.addAction("s1", -1, 2, 4, 6, 3, 0, 255, 8,
          -3, 8, 7, 6, 3, 0, 255, 8);
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // 3. start >= end
    try {
      test1.addAction("s1", 4, 2, 4, 6, 3, 0, 255, 8,
          2, 8, 7, 6, 3, 0, 255, 8);
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s1", 4, 2, 4, 6, 3, 0, 255, 8,
          4, 8, 7, 6, 3, 0, 255, 8);
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testScale() {
    test1.addShape("s1", s1); //w = 6, h = 3
    // + width, - height
    test1.addAction("s1", 4, 2, 4, 6, 3, 0, 255, 8,
        6, -4, 4, 8, 2, 0, 255, 8);
    assertEquals(8, test1.getShapeAtTick(6).get(0).getWidth(), 0.1);
    assertEquals(2, test1.getShapeAtTick(6).get(0).getHeight(), 0.1);
    test1.addShape("s2", s2);// w = 2, h = 5
    // - width, + height
    test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
        6, -4, 6, 1, 9, 255, 255, 255);
    // unordered input(s1 then s2), order output(s2 then s1)
    assertEquals("Shapes:\n" + "Name: s2\n"
        + "Type: Rectangle\n"
        + "Reference: (-4.0, 6.0), Width: 2.0, Height: 5.0, Color: (255, 255, 255)\n"
        + "Appears at t=1s, disappears at t=6s\n" + "Name: s1\n" + "Type: Oval\n"
        + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n"
        + "Appears at t=4s, disappears at t=6s\n" + "\n"
        + "Shape s2 scales from Width: 2.0, Height: 5.0 to "
        + "Width: 1.0, Height: 9.0, from t=1s to t=6s\n"
        + "Shape s1 move from (2.0, 4.0) to (-4.0, 4.0), from t=4s to t=6s\n"
        + "Shape s1 scales from Width: 6.0, Height: 3.0 to "
        + "Width: 8.0, Height: 2.0, from t=4s to t=6s", test1.getState());

    // change only width
    test1.addAction("s2", 7, -4, 6, 1, 9, 255, 255, 255,
        8, -4, 6, 5, 9, 255, 255, 255);
    assertEquals(5.0, test1.getShapeAtTick(8).get(0).getWidth(), 0.1);
    assertEquals(9.0, test1.getShapeAtTick(8).get(0).getHeight(), 0.1);

    // change only length
    test1.addAction("s2", 12, -4, 6, 5, 9, 255, 255, 255,
        18, -4, 6, 5, 3, 255, 255, 255);
    assertEquals(3.0, test1.getShapeAtTick(18).get(0).getHeight(), 0.1);
  }

  @Test
  public void testIllegalScale() {
    test1.addShape("s2", s2);// w = 2, h = 5
    // invalid width/length (<=0)
    try {
      test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
          6, -4, 6, -1, -9, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
          6, -4, 6, 0, 8, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
          6, -4, 6, 6, 0, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // scale in overlap time (scaling or another motion is making)
    test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
        6, -4, 6, 1, 9, 255, 255, 255);
    try {
      test1.addAction("s2", 2, -4, 6, 2, 5, 255, 255, 255,
          5, -4, 6, 1, 9, 255, 255, 255);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testColor() {
    // regular color
    test1.addShape("s1", s1);
    test1.addShape("s2", s2);
    test1.addAction("s1", 3, 2, 4, 6, 3, 0, 255, 8,
        10, 2, 4, 6, 3, 0, 0, 0);
    assertEquals("(0, 0, 0)", test1.getShapeAtTick(10).get(0).getColor().toString());

    // chang color of other shape in overlap time
    test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
        30, 3, 3, 2, 5, 255, 0, 255);
    assertEquals("(255, 0, 255)", test1.getShapeAtTick(30).get(0).getColor().toString());
    // invalid color
    try {
      test1.addAction("s1", 20, 2, 4, 6, 3, 0, 255, 8,
          60, 2, 4, 6, 3, 256, 256, 256);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    try {
      test1.addAction("s1", 20, 2, 4, 6, 3, 0, 255, 8,
          60, 2, 4, 6, 3, -1, -1, -1);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testGetShape() {
    // get shape from empty model
    test1 = new AnimatorModel();
    try {
      test1.getInitialShape("s1");
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // Verify return the copy of shape
    test1.addShape("s1", s1);
    assertEquals(s1.toString(), test1.getInitialShape("s1").toString());
    assertNotEquals(s1, test1.getInitialShape("s1"));

    // get shape after the shape make motion, still the original shape
    test1.addAction("s1", 3, 2, 4, 6, 3, 0, 255, 8,
        4, 2, -2, 6, 3, 0, 255, 8);
    assertEquals(s1.toString(), test1.getInitialShape("s1").toString());

    // identifier = null
    try {
      test1.getInitialShape(null);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // identifier = ""
    try {
      test1.getInitialShape("");
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testGetTransformations() {
    try {
      test1.getMotions("s1");
      fail("fail to catch error");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    test1.addShape("s1", s1);
    assertEquals(0, test1.getMotions("s1").size());

    test1.addAction("s1", 3, 2, 4, 6, 3, 0, 255, 8,
        4, 2, -2, 6, 3, 0, 255, 8);
    // make multiple transformation within one action
    test1.addAction("s1", 4, 2, -2, 6, 3, 0, 255, 8,
        6, 2, -2, 2, 3, 255, 255, 8);
    test1.addAction("s1", 10, 2, -2, 2, 3, 255, 255, 8,
        20, -2, -2, 9, 3, 0, 0, 8);
    assertEquals(6, test1.getMotions("s1").size());
    // check if sorted
    assertEquals("move from (2.0, 4.0) to (2.0, -2.0), from t=3s to t=4s",
        test1.getMotions("s1").get(0).toString());
    assertEquals("scales from Width: 6.0, Height: 3.0 to Width: 2.0, Height: 3.0,"
            + " from t=4s to t=6s",
        test1.getMotions("s1").get(1).toString());
    assertEquals("changes color from (0, 255, 8) to (255, 255, 8), from t=4s to t=6s",
        test1.getMotions("s1").get(2).toString());
    assertEquals("move from (2.0, -2.0) to (-2.0, -2.0), from t=10s to t=20s",
        test1.getMotions("s1").get(3).toString());

    // identifier = null
    try {
      test1.getMotions(null);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // identifier = ""
    try {
      test1.getMotions("");
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void getShapeAtTick() {
    // Verify return the copy of shape
    test1.addShape("s1", s1);
    //just appear and motionless
    test1.addAction("s1", 3, 2, 4, 6, 3, 0, 255, 8,
        10, 2, 4, 6, 3, 0, 255, 8);
    assertEquals(test1.getShapeAtTick(10).toString(), test1.getShapeAtTick(3).toString());
    // Verify return the copy of shape
    assertNotEquals(s1, test1.getShapeAtTick(4).get(0));
    // tick = 0
    assertEquals(0, test1.getShapeAtTick(0).size());

    // middle tween of action
    test1.addAction("s1", 10, 2, 4, 6, 3, 0, 255, 8,
        20, 2, -2, 6, 3, 255, 0, 8);
    assertEquals("Type: Oval\n"
            + "Reference: (2.0, 1.0), Width: 6.0, Height: 3.0, Color: (127, 127, 8)",
        test1.getShapeAtTick(15).get(0).toString());

    // 3.just appear, break between of two changes
    test1.addAction("s1", 25, 2, -2, 6, 3, 255, 0, 8,
        30, 2, 3, 6, 3, 0, 255, 8);
    assertEquals(test1.getShapeAtTick(20).get(0).toString(),
        test1.getShapeAtTick(23).get(0).toString());

    // invalid tick: < 0
    try {
      test1.getShapeAtTick(-1);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
    // invalid tick: > time length of animation
    try {
      test1.getShapeAtTick(101);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  //Check if the shapes are print in ascending order of appear/action time
  @Test
  public void testGetState() {
    AnimatorModel test1 = new AnimatorModel();
    // no shape
    assertEquals("", test1.getState());
    // has shape but no action to take
    test1.addShape("s1", s1);
    assertEquals("Shapes:\n"
            + "Name: s1\n"
            + "Type: Oval\n"
            + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n\n"
        , test1.getState());
    // has action to take
    test1.addAction("s1", 1, 2, 4, 6, 3, 0, 255, 8,
        3, 0, 0, 6, 3, 0, 255, 8);
    assertEquals("Shapes:\n"
        + "Name: s1\n"
        + "Type: Oval\n"
        + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n"
        + "Appears at t=1s, disappears at t=3s\n\n"
        + "Shape s1 move from (2.0, 4.0) to (0.0, 0.0), from t=1s to t=3s", test1.getState());
    // add action not in time order but able to output sorted data
    test1.addAction("s1", 0, 0, 4, 8, 3, 0, 0, 8,
        1, 2, 4, 6, 3, 0, 255, 8);
    assertEquals("Shapes:\n"
        + "Name: s1\n"
        + "Type: Oval\n"
        + "Reference: (0.0, 4.0), Width: 8.0, Height: 3.0, Color: (0, 0, 8)\n"
        + "Appears at t=0s, disappears at t=3s\n" + "\n"
        + "Shape s1 move from (0.0, 4.0) to (2.0, 4.0), from t=0s to t=1s\n"
        + "Shape s1 scales from Width: 8.0, Height: 3.0 to "
        + "Width: 6.0, Height: 3.0, from t=0s to t=1s\n"
        + "Shape s1 changes color from (0, 0, 8) to (0, 255, 8), from t=0s to t=1s\n"
        + "Shape s1 move from (2.0, 4.0) to (0.0, 0.0), from t=1s to t=3s", test1.getState());
    test1 = new AnimatorModel();
    // unordered input(s1 then s2), order output(s2 then s1)
    test1.addShape("s1", s1); //w = 6, h = 3
    test1.addAction("s1", 4, 2, 4, 6, 3, 0, 255, 8,
        6, 0, 0, 8, 2, 0, 255, 8);
    test1.addShape("s2", s2);// w = 2, h = 5
    test1.addAction("s2", 1, 310, 23, 40, 70, 0, 250, 3,
        20, 4, 5, 6, 7, 8, 9, 20);
    assertEquals("Shapes:\n" + "Name: s2\n"
        + "Type: Rectangle\n"
        + "Reference: (310.0, 23.0), Width: 40.0, Height: 70.0, Color: (0, 250, 3)\n"
        + "Appears at t=1s, disappears at t=20s\n"
        + "Name: s1\n" + "Type: Oval\n"
        + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n"
        + "Appears at t=4s, disappears at t=6s\n" + "\n"
        + "Shape s2 move from (310.0, 23.0) to (4.0, 5.0), from t=1s to t=20s\n"
        + "Shape s2 scales from Width: 40.0, Height: 70.0 to "
        + "Width: 6.0, Height: 7.0, from t=1s to t=20s\n"
        + "Shape s2 changes color from (0, 250, 3) to (8, 9, 20), from t=1s to t=20s\n"
        + "Shape s1 move from (2.0, 4.0) to (0.0, 0.0), from t=4s to t=6s\n"
        + "Shape s1 scales from Width: 6.0, Height: 3.0 to "
        + "Width: 8.0, Height: 2.0, from t=4s to t=6s", test1.getState());
  }

  @Test
  public void testCanvas() {
    // before add
    assertEquals("100 100 100 100\n", test1.getCanvas().toString());
    // after add
    test1.setCanvas(200, 0, 40, 50);
    assertEquals("200 0 40 50\n", test1.getCanvas().toString());
  }

  @Test
  public void testSynchronous() {
    test1.addShape("s1", s1);
    test1.addAction("s1", 1, 2, 4, 6, 3, 0, 255, 8,
        5, -4, 4, 6, 3, 255, 255, 8);
    assertEquals("(2.0, 4.0)", test1.getInitialShape("s1").getReference().toString());
    assertEquals("(0, 255, 8)", test1.getInitialShape("s1").getColor().toString());
    assertEquals("Shapes:\n"
            + "Name: s1\n"
            + "Type: Oval\n"
            + "Reference: (2.0, 4.0), Width: 6.0, Height: 3.0, Color: (0, 255, 8)\n"
            + "Appears at t=1s, disappears at t=5s\n"
            + "\n"
            + "Shape s1 move from (2.0, 4.0) to (-4.0, 4.0), from t=1s to t=5s\n"
            + "Shape s1 changes color from (0, 255, 8) to (255, 255, 8), from t=1s to t=5s",
        test1.getState());
  }

  @Test
  public void testGetShapeIds() {
    assertEquals(0, test1.getShapeIDs().size());
    test1.addShape("s1", s1);
    test1.addShape("s2", s2);
    assertEquals(2, test1.getShapeIDs().size());
    assertTrue(test1.getShapeIDs().contains("s1"));
    assertTrue(test1.getShapeIDs().contains("s2"));
    test1.removeShape("s1");
    assertEquals(1, test1.getShapeIDs().size());
    assertFalse(test1.getShapeIDs().contains("s1"));


  }

  @Test
  public void testGetLength() {
    assertEquals(0, test1.getLength());
    test1.addShape("s2", s2);
    test1.addAction("s2", 1, -4, 6, 2, 5, 255, 255, 255,
        30, 3, 3, 2, 5, 255, 0, 255);
    assertEquals(30, test1.getLength()); // auto extend the length
  }
}