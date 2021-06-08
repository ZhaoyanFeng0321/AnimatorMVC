import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import cs5004.animator.controller.ControllerImpl;
import cs5004.animator.controller.IController;
import cs5004.animator.model.AnimatorModel;
import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.util.AnimationReader;
import cs5004.animator.view.IView;
import cs5004.animator.view.SVGView;
import cs5004.animator.view.TextView;
import mock.MockController;
import mock.MockGUIView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


/**
 * Test all operations contains in ControllerImpl including implementations of IController and
 * IFeature interface. Verify the correctness of GUI operations with the assist of {@link
 * MockController} and {@link MockGUIView}.
 */
public class ControllerTest {
  IAnimatorModel model;
  IView view;
  MockGUIView mockGUIView;
  IController controller;
  IController mockGUIController;
  StringBuilder ap;

  @Before
  public void setUp() throws Exception {
    ap = new StringBuilder();
    InputStream inputStream = new FileInputStream("resources/smalldemo.txt");
    model = AnimationReader.parseFile(new InputStreamReader(inputStream),
        new AnimatorModel.Builder());
  }

  @Test
  public void tesIllegalSetUp() {
    try {
      controller = new ControllerImpl(model, null);
      fail("fail to throw exception");
    } catch (NullPointerException e) {
      // test pass
    }
    try {
      controller = new ControllerImpl(null, view);
      fail("fail to throw exception");
    } catch (NullPointerException e) {
      // test pass
    }
  }

  @Test
  public void tesIllegalRun() {
    view = new TextView(model, ap);
    controller = new ControllerImpl(model, view);
    try {
      controller.run(-10);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test pass
    }
    try {
      controller.run(0);
      fail("fail to throw exception");
    } catch (IllegalArgumentException e) {
      // test pass
    }
  }

  // test if controller work properly and make textual view produce correct output.
  @Test
  public void testTextOperation() {
    view = new TextView(model, ap);
    controller = new ControllerImpl(model, view);
    controller.run(10);
    assertEquals("Speed: 10 tick(s) / second\n"
            + "\n" + "Shapes:\n" + "Name: R\n" + "Type: Rectangle\n"
            + "Reference: (200.0, 200.0), Width: 50.0, Height: 100.0, Color: (255, 0, 0)\n"
            + "Appears at t=0.1s, disappears at t=10.0s\n" + "Name: C\n"
            + "Type: Oval\n"
            + "Reference: (440.0, 70.0), Width: 120.0, Height: 60.0, Color: (0, 0, 255)\n"
            + "Appears at t=0.6s, disappears at t=10.0s\n"
            + "\n" + "Shape R move from (200.0, 200.0) to (300.0, 300.0), from t=1.0s to t=5.0s\n"
            + "Shape C move from (440.0, 70.0) to (440.0, 250.0), from t=2.0s to t=5.0s\n"
            + "Shape C move from (440.0, 250.0) to (440.0, 370.0), from t=5.0s to t=7.0s\n"
            + "Shape C changes color from (0, 0, 255) to (0, 170, 85), from t=5.0s to t=7.0s\n"
            + "Shape R scales from Width: 50.0, Height: 100.0 to Width: 25.0, "
            + "Height: 100.0, from t=5.1s to t=7.0s\n"
            + "Shape C changes color from (0, 170, 85) to (0, 255, 0), from t=7.0s to t=8.0s\n"
            + "Shape R move from (300.0, 300.0) to (200.0, 200.0), from t=7.0s to t=10.0s\n",
        ap.toString());
  }

  @Test
  public void testSVGOperation() {
    view = new SVGView(model, ap);
    controller = new ControllerImpl(model, view);
    controller.run(20);
    String[] test = ap.toString().split("</rect>");
    assertEquals("<svg viewBox=\"200 70 360 360\" "
        + "xmlns=\"http://www.w3.org/2000/svg\">\n"
        + "<rect id=\"R\" x=\"200\" y=\"200\" width=\"50\" height=\"100\" fill=\"rgb(255,0,0)\""
        + " visibility=\"visible\" >\n"
        + "\t<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"2000.0ms\" "
        + "attributeName=\"x\" from=\"200.0\" to=\"300.0\" fill=\"freeze\" />\n"
        + "\t<animate attributeType=\"xml\" begin=\"500.0ms\" dur=\"2000.0ms\" "
        + "attributeName=\"y\" from=\"200.0\" to=\"300.0\" fill=\"freeze\" />\n"
        + "\t<animate attributeType=\"xml\" begin=\"2550.0ms\" dur=\"950.0ms\" "
        + "attributeName=\"width\" from=\"50.0\" to=\"25.0\" fill=\"freeze\" />\n"
        + "\t<animate attributeType=\"xml\" begin=\"3500.0ms\" dur=\"1500.0ms\" "
        + "attributeName=\"x\" from=\"300.0\" to=\"200.0\" fill=\"freeze\" />\n"
        + "\t<animate attributeType=\"xml\" begin=\"3500.0ms\" dur=\"1500.0ms\" "
        + "attributeName=\"y\" from=\"300.0\" to=\"200.0\" fill=\"freeze\" />\n" + "\n", test[0]);
  }

  // test interactive operations between controller and GUIView.
  // check controller does not change input data while convey to GUIView
  @Test
  public void testInputToGUI() throws InterruptedException {
    StringBuilder input = new StringBuilder(); // initial data input to controller
    for (int i = 0; i <= model.getLength(); i++) {
      for (IShape2D s : model.getShapeAtTick(i)) {
        input.append(String.format("Input: %s\n", s.toString()));
      }
    }
    StringBuilder viewLog = new StringBuilder();
    StringBuilder controllerLog = new StringBuilder();
    // action command as controller to start timer
    mockGUIView = new MockGUIView(viewLog, "start");
    controller = new MockController(model, mockGUIView, controllerLog);
    controller.run(100);
    // test if view receive proper shapes data
    Thread.sleep(2000);
    assertEquals(input.toString(), viewLog.toString());
  }

  // Check if make proper reaction to command from GUIView
  @Test
  public void testProcessCommand() throws InterruptedException {
    // Case 1 : test pause and resume at same tick
    StringBuilder viewLog = new StringBuilder();
    StringBuilder controllerLog = new StringBuilder();
    mockGUIView = new MockGUIView(viewLog, "start pause resume");
    mockGUIController = new MockController(model, mockGUIView, controllerLog);
    mockGUIController.run(10);
    Thread.sleep(3000); // wait for processing
    String[] test = controllerLog.toString().split("\n");
    assertEquals(3, test.length);
    assertEquals("Start!", test[0]);
    // check if it is same tick
    assertEquals(test[1].substring(test[1].indexOf("at") + 3),
        test[2].substring(test[2].indexOf("at") + 3));

    controllerLog = new StringBuilder();
    mockGUIView = new MockGUIView(viewLog, "start loop end-loop");
    mockGUIController = new MockController(model, mockGUIView, controllerLog);
    mockGUIController.run(1);
    // test loop command
    assertEquals("Start!\nLoop!\nEnd-Loop!\n", controllerLog.toString());

    controllerLog = new StringBuilder();
    mockGUIView = new MockGUIView(viewLog, "start change-speed 0.5");
    mockGUIController = new MockController(model, mockGUIView, controllerLog);
    mockGUIController.run(10);
    // test if slow down speed to expected value
    assertEquals("Start!\nspeed: 200 ticks/sec\n", controllerLog.toString());

    controllerLog = new StringBuilder();
    mockGUIView = new MockGUIView(viewLog, "start change-speed 2");
    mockGUIController = new MockController(model, mockGUIView, controllerLog);
    mockGUIController.run(10);
    // test if speed up to expected value
    assertEquals("Start!\nspeed: 50 ticks/sec\n", controllerLog.toString());
  }

  // View will receive no shapes if not asked to "start"
  @Test
  public void testNotStart() throws InterruptedException {
    StringBuilder viewLog = new StringBuilder();
    StringBuilder controllerLog = new StringBuilder();
    // action command as controller to start timer
    mockGUIView = new MockGUIView(viewLog, "");
    controller = new MockController(model, mockGUIView, controllerLog);
    controller.run(100);
    // test if view receive proper shapes data
    Thread.sleep(2000); // wait for processing
    assertEquals("", viewLog.toString());
    assertEquals("", controllerLog.toString()); // no command receive


    viewLog = new StringBuilder();
    controllerLog = new StringBuilder();
    // action command as controller to start timer
    mockGUIView = new MockGUIView(viewLog, "pause");
    controller = new MockController(model, mockGUIView, controllerLog);
    controller.run(100);
    // test if view receive proper shapes data
    Thread.sleep(2000);
    assertEquals("", viewLog.toString());
    assertEquals("Pause at tick 0\n", controllerLog.toString());


    viewLog = new StringBuilder();
    controllerLog = new StringBuilder();
    // action command as controller to start timer
    mockGUIView = new MockGUIView(viewLog, "resume restart");
    controller = new MockController(model, mockGUIView, controllerLog);
    controller.run(100);
    // test if view receive proper shapes data
    Thread.sleep(2000);
    assertTrue(viewLog.length() > 0); // shapes data receive
  }
}