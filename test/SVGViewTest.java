import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.view.IView;
import cs5004.animator.view.SVGView;
import cs5004.animator.view.ViewFactory;
import cs5004.animator.model.AnimatorModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * Test if SVGView work well and produce correct output with different speed. Also test the
 * ViewFactory work properly.
 */
public class SVGViewTest {
  IAnimatorModel model;
  Appendable ap;
  ViewFactory vf;

  @Before
  public void setUp() {
    model = new AnimatorModel.Builder().setBounds(140, 100, 100, 100)
        .declareShape("R", "rectangle")
        //change color rgb
        .addMotion("R", 1, 2, 0, 3, 4, 55, 255, 0,
            30, 2, 0, 3, 4, 255, 30, 20)
        .declareShape("O", "OVAL")
        //change width and height
        .addMotion("O", 1, 10, 20, 3, 4, 100, 200, 180,
            30, 10, 20, 300, 400, 100, 200, 180)
        // stand still
        .addMotion("O", 30, 10, 20, 300, 400, 100, 200, 180,
            40, 10, 20, 300, 400, 100, 200, 180)
        //move x
        .addMotion("O", 40, 10, 20, 300, 400, 100, 200, 180,
            50, 15, 20, 300, 400, 100, 200, 180)
        .build();
    ap = new StringBuilder();
    vf = new ViewFactory();
  }

  @Test
  public void testNormalSpeed() throws IOException {
    IView svgView = vf.create("svg", model, ap);
    svgView.render(1);
    assertEquals(
        "<svg viewBox=\"140 100 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"2\" y=\"0\" width=\"3\" height=\"4\" fill=\"rgb(55,255,0)\" "
            + "visibility=\"visible\" >\n"
            + "\t<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"29000.0ms\""
            + " attributeName=\"fill\" from=\"RGB(55,255,0)\" to=\"RGB(255,30,20)\" "
            + "fill=\"freeze\" />\n" + "\n" + "</rect>\n" + "\n"
            + "<ellipse id=\"O\" cx=\"10\" cy=\"20\" rx=\"1\" ry=\"2\" fill=\"rgb(100,200,180)\""
            + " visibility=\"visible\" >\n"
            + "\t<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"29000.0ms\" "
            + "attributeName=\"rx\" from=\"1.5\" to=\"150.0\" fill=\"freeze\" />\n"
            + "\t<animate attributeType=\"xml\" begin=\"1000.0ms\" dur=\"29000.0ms\" "
            + "attributeName=\"ry\" from=\"2.0\" to=\"200.0\" fill=\"freeze\" />\n"
            + "\t<animate attributeType=\"xml\" begin=\"40000.0ms\" dur=\"10000.0ms\" "
            + "attributeName=\"cx\" from=\"10.0\" to=\"15.0\" fill=\"freeze\" />\n"
            + "\n"
            + "</ellipse>\n"
            + "\n"
            + "</svg>\n", ap.toString());
  }

  @Test
  public void testSpeedUp() {
    IView svgView = new SVGView(model, ap);
    svgView.render(20);
    assertEquals(
        "<svg viewBox=\"140 100 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n"
            + "<rect id=\"R\" x=\"2\" y=\"0\" width=\"3\" height=\"4\" fill=\"rgb(55,255,0)\" "
            + "visibility=\"visible\" >\n"
            + "\t<animate attributeType=\"xml\" begin=\"50.0ms\" dur=\"1450.0ms\" attributeName=\""
            + "fill\" from=\"RGB(55,255,0)\" to=\"RGB(255,30,20)\" fill=\"freeze\" />\n"
            + "\n"
            + "</rect>\n"
            + "\n"
            + "<ellipse id=\"O\" cx=\"10\" cy=\"20\" rx=\"1\" ry=\"2\" fill=\"rgb(100,200,180)\" "
            + "visibility=\"visible\" >\n"
            + "\t<animate attributeType=\"xml\" begin=\"50.0ms\" dur=\"1450.0ms\" "
            + "attributeName=\"rx\" from=\"1.5\" to=\"150.0\" fill=\"freeze\" />\n"
            + "\t<animate attributeType=\"xml\" begin=\"50.0ms\" dur=\"1450.0ms\" "
            + "attributeName=\"ry\" from=\"2.0\" to=\"200.0\" fill=\"freeze\" />\n"
            + "\t<animate attributeType=\"xml\" begin=\"2000.0ms\" dur=\"500.0ms\""
            + " attributeName=\"cx\" from=\"10.0\" to=\"15.0\" fill=\"freeze\" />\n"
            + "\n" + "</ellipse>\n" + "\n" + "</svg>\n", ap.toString());
  }

  @Test
  public void testFactory() {
    IView view1 = new SVGView(model, ap);
    IView view2 = vf.create("SVG", model, ap);
    assertSame(view1.getClass(), view2.getClass());
  }

  @Test
  public void testMotionless() {
    IView view1;
    //no shape, default canvas
    model = new AnimatorModel.Builder().build();
    view1 = new SVGView(model, ap);
    view1.render(20);
    assertEquals("<svg viewBox=\"100 100 100 100\" xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "</svg>\n", ap.toString());

    // no "concrete" shape
    model = new AnimatorModel.Builder().setBounds(0, 0, 300, 20)
        .declareShape("R", "rectangle")
        .declareShape("O", "OVAL").build();
    ap = new StringBuilder();
    view1 = new SVGView(model, ap);
    view1.render(20);
    assertEquals("<svg viewBox=\"0 0 300 20\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
        "</svg>\n", ap.toString());

    // motionless shape, just appear
    model = new AnimatorModel.Builder().setBounds(0, 0, 300, 20)
        .declareShape("R", "rectangle")
        .declareShape("O", "OVAL")
        .addMotion("O", 30, 10, 20, 300, 400, 100, 200, 180,
            40, 10, 20, 300, 400, 100, 200, 180)
        .build();
    ap = new StringBuilder();
    view1 = new SVGView(model, ap);
    view1.render(20);
    assertEquals("<svg viewBox=\"0 0 300 20\" xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<ellipse id=\"O\" cx=\"10\" cy=\"20\" rx=\"150\" ry=\"200\" "
        + "fill=\"rgb(100,200,180)\" visibility=\"visible\" >\n"
        + "\n" + "</ellipse>\n" + "\n" + "</svg>\n", ap.toString());
  }
}
