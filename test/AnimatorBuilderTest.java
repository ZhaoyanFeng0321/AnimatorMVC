import org.junit.Test;

import cs5004.animator.model.AnimatorModel;
import cs5004.animator.model.IAnimatorModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This is test for AnimatorBuilder.
 */
public class AnimatorBuilderTest {
  private IAnimatorModel model;

  @Test
  public void testInitial() {
    model = new AnimatorModel.Builder().build();
    assertEquals("", model.getState());
    assertEquals(0, model.getLength());
    assertEquals(100, model.getCanvas().getHeight());
    assertEquals(100, model.getCanvas().getWidth());
    assertEquals(100, model.getCanvas().getX());
    assertEquals(100, model.getCanvas().getY());
  }

  @Test
  public void testSetBound() {
    model = new AnimatorModel.Builder().setBounds(0, 3, 40, 50).build();
    assertEquals(0, model.getLength());
    assertEquals(50, model.getCanvas().getHeight());
    assertEquals(40, model.getCanvas().getWidth());
    assertEquals(0, model.getCanvas().getX());
    assertEquals(3, model.getCanvas().getY());

    try {
      model = new AnimatorModel.Builder().setBounds(0, 3, -40, -50).build();
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testDeclareShape() {
    model = new AnimatorModel.Builder().setBounds(0, 3, 40, 50)
        .declareShape("R", "rectangle").declareShape("O", "OVAL").build();
    assertEquals(null, model.getInitialShape("O")); // shape is 'null' state
    assertEquals("Shapes:\n"
        + "Name: R\n"
        + "Type: Rectangle\n"
        + "Name: O\n"
        + "Type: Oval\n", model.getState());
    assertEquals(null, model.getInitialShape("O")); // shape is 'null' state
    assertEquals(50, model.getCanvas().getHeight());
    assertEquals(40, model.getCanvas().getWidth());
    assertEquals(0, model.getCanvas().getX());
    assertEquals(3, model.getCanvas().getY());

    // declare shape with same name
    try {
      model = new AnimatorModel.Builder().declareShape("R", "rectangle")
          .declareShape("R", "OVAL").build();
    } catch (IllegalArgumentException e) {
      //test passes
    }
  }

  @Test
  public void testAddMotion() {
    model = new AnimatorModel.Builder().setBounds(0, 3, 40, 50)
        .declareShape("R", "rectangle").declareShape("O", "OVAL")
        .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
            6, 2, 0, 3, 4, 55, 255, 0)
        .build();
    assertEquals(null, model.getInitialShape("O")); // shape is 'null' state
    assertEquals("Shapes:\n"
            + "Name: O\n"
            + "Type: Oval\n"
            + "Name: R\n"
            + "Type: Rectangle\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (55, 255, 0)\n"
            + "Appears at t=1s, disappears at t=6s\n",
        model.getState());
    assertEquals("Type: Rectangle\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (55, 255, 0)",
        model.getInitialShape("R").toString());

    model = new AnimatorModel.Builder().setBounds(0, 3, 40, 50)
        .declareShape("R", "rectangle")
        .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
            6, 2, 0, 3, 4, 55, 255, 0)
        .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
            10, 6, 9, 3, 4, 55, 255, 0)
        .declareShape("O", "OVAL")
        .addMotion("O", 1, 2, 0, 3, 4, 10, 255, 0,
            3, 6, 9, 3, 4, 10, 255, 0)
        .addMotion("R", 11, 6, 9, 3, 4, 55, 255, 0,
            12, 6, 9, 3, 4, 55, 255, 0)
        .addMotion("R", 13, 6, 9, 3, 4, 55, 255, 0,
            14, 6, 9, 4, 10, 55, 255, 0)
        .build();
    assertEquals("Shapes:\n"
            + "Name: O\n" + "Type: Oval\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (10, 255, 0)\n"
            + "Appears at t=1s, disappears at t=3s\n"
            + "Name: R\n"
            + "Type: Rectangle\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (55, 255, 0)\n"
            + "Appears at t=1s, disappears at t=14s" + "\n\n"
            + "Shape O move from (2.0, 0.0) to (6.0, 9.0), from t=1s to t=3s\n"
            + "Shape R move from (2.0, 0.0) to (6.0, 9.0), from t=1s to t=10s\n"
            + "Shape R scales from Width: 3.0, Height: 4.0 to Width: 4.0, Height: 10.0,"
            + " from t=13s to t=14s",
        model.getState());
    assertEquals(0, model.getShapeAtTick(0).size());
    assertEquals(2, model.getShapeAtTick(2).size());
    assertEquals("Type: Oval\n"
            + "Reference: (4.0, 4.5), Width: 3.0, Height: 4.0, Color: (10, 255, 0)",
        model.getShapeAtTick(2).get(1).toString());
    assertEquals("(2.0, 0.0)", model.getShapeAtTick(2).get(0).getReference().toString());
    assertEquals(1, model.getShapeAtTick(12).size());
    assertEquals("Type: Rectangle\n"
            + "Reference: (6.0, 9.0), Width: 3.0, Height: 4.0, Color: (55, 255, 0)",
        model.getShapeAtTick(12).get(0).toString());
    try {
      model.getShapeAtTick(15);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    try {
      model.getShapeAtTick(-1);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
  }

  @Test
  public void illegalMotion() {
    // add Motion before declare shape
    try {
      model = new AnimatorModel.Builder()
          .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
              6, 2, 0, 3, 4, 55, 255, 0)
          .build();
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    // Overlap time to add motion (e.g. inconsistent move)
    try {
      model = new AnimatorModel.Builder().declareShape("R", "rectangle")
          .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
              6, 4, 2, 3, 4, 55, 255, 0)
          .addMotion("R", 2, 2, 0, 3, 4, 55, 255, 0,
              6, -2, 0, 3, 4, 55, 255, 0)
          .build();
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
    try {
      model = new AnimatorModel.Builder().declareShape("R", "rectangle")
          .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
              6, 4, 2, 3, 4, 55, 255, 0)
          .addMotion("R", 5, 2, 0, 3, 4, 55, 255, 0,
              7, -2, 0, 3, 4, 55, 255, 0)
          .build();
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test passes
    }
  }
}
