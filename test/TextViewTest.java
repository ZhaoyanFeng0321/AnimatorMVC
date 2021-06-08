import org.junit.Before;
import org.junit.Test;

import cs5004.animator.model.AnimatorModel;
import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.view.IView;
import cs5004.animator.view.TextView;
import cs5004.animator.view.ViewFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test if TextView work well and produce correct output with different speed. Also test the
 * ViewFactory work properly.
 */
public class TextViewTest {
  IAnimatorModel model;
  Appendable ap;
  ViewFactory vf;

  @Before
  public void setUp() {
    model = new AnimatorModel.Builder().setBounds(0, 0, 100, 100)
        .declareShape("R", "rectangle")
        //change color rgb
        .addMotion("R", 0, 2, 0, 3, 4, 0, 255, 255,
            30, 2, 0, 3, 4, 255, 30, 20)
        .declareShape("O", "OVAL")
        //change width and height
        .addMotion("O", 1, 10, 20, 3, 4, 100, 200, 180,
            30, 10, 20, 300, 400, 100, 200, 180)
        //move x
        .addMotion("O", 40, 10, 20, 300, 400, 100, 200, 180,
            50, 15, 20, 300, 400, 100, 200, 180)
        .build();
    ap = new StringBuilder();
    vf = new ViewFactory();
  }

  @Test
  public void testNormalSpeed() {
    IView textView = vf.create("text", model, ap);
    textView.render(1);
    assertEquals("Speed: 1 tick(s) / second\n"
            + "\n" + "Shapes:\n" + "Name: R\n" + "Type: Rectangle\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (0, 255, 255)\n"
            + "Appears at t=0.0s, disappears at t=30.0s\n"
            + "Name: O\n" + "Type: Oval\n"
            + "Reference: (10.0, 20.0), Width: 3.0, Height: 4.0, Color: (100, 200, 180)\n"
            + "Appears at t=1.0s, disappears at t=50.0s\n" + "\n"
            + "Shape R changes color from (0, 255, 255) to (255, 30, 20), from t=0.0s to t=30.0s\n"
            + "Shape O scales from Width: 3.0, Height: 4.0 to Width: 300.0, Height: 400.0,"
            + " from t=1.0s to t=30.0s\n"
            + "Shape O move from (10.0, 20.0) to (15.0, 20.0), from t=40.0s to t=50.0s\n",
        ap.toString());
  }

  @Test
  public void testSpeedUp() {
    IView textView = vf.create("TEXT", model, ap);
    textView.render(20);
    assertEquals(
        "Speed: 20 tick(s) / second\n\n" + "Shapes:\n" + "Name: R\n"
            + "Type: Rectangle\n"
            + "Reference: (2.0, 0.0), Width: 3.0, Height: 4.0, Color: (0, 255, 255)\n"
            + "Appears at t=0.0s, disappears at t=1.5s\n"
            + "Name: O\n" + "Type: Oval\n"
            + "Reference: (10.0, 20.0), Width: 3.0, Height: 4.0, Color: (100, 200, 180)\n"
            + "Appears at t=0.1s, disappears at t=2.5s\n" + "\n"
            + "Shape R changes color from (0, 255, 255) to (255, 30, 20), from t=0.0s to t=1.5s\n"
            + "Shape O scales from Width: 3.0, Height: 4.0 to Width: 300.0, Height: 400.0,"
            + " from t=0.1s to t=1.5s\n"
            + "Shape O move from (10.0, 20.0) to (15.0, 20.0), from t=2.0s to t=2.5s\n",
        ap.toString());
  }

  @Test
  public void testFactory() {
    IView view1 = new TextView(model, ap);
    IView view2 = vf.create("text", model, ap);
    assertSame(view1.getClass(), view2.getClass());
  }

  @Test
  public void testMotionless() {
    IView view1;
    //no shape, default canvas
    model = new AnimatorModel.Builder().build();
    view1 = new TextView(model, ap);
    view1.render(20);
    assertEquals("Speed: 20 tick(s) / second", ap.toString().trim());

    // no "concrete" shape
    model = new AnimatorModel.Builder().setBounds(140, -100, 300, 20)
        .declareShape("R", "rectangle")
        .declareShape("O", "OVAL").build();
    ap = new StringBuilder();
    view1 = new TextView(model, ap);
    view1.render(20);
    assertEquals("Speed: 20 tick(s) / second\n\n"
            + "Shapes:\n" + "Name: R\n" + "Type: Rectangle\n" + "Name: O\n" + "Type: Oval",
        ap.toString().trim());

    // motionless shape, just appear
    model = new AnimatorModel.Builder().setBounds(140, -100, 300, 20)
        .declareShape("R", "rectangle")
        .declareShape("O", "OVAL")
        .addMotion("O", 30, 10, 20, 300, 400, 100, 200, 180,
            40, 10, 20, 300, 400, 100, 200, 180)
        .build();
    ap = new StringBuilder();
    view1 = new TextView(model, ap);
    view1.render(20);
    assertEquals("Speed: 20 tick(s) / second\n" + "\n" + "Shapes:\n" + "Name: R\n"
        + "Type: Rectangle\n" + "Name: O\n" + "Type: Oval\n"
        + "Reference: (10.0, 20.0), Width: 300.0, Height: 400.0, Color: (100, 200, 180)\n"
        + "Appears at t=1.5s, disappears at t=2.0s\n", ap.toString());
  }
}
