package mock;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.view.IFeature;
import cs5004.animator.view.IGUIView;
import cs5004.animator.view.TypeOfView;

/**
 * Mock GUI view to test the data correctness and help test controller operation in GUIView. Using
 * textual format to visualize the operation correctness.
 */
public class MockGUIView implements IGUIView {
  private StringBuilder log;
  private String cmdInput;
  private IFeature feature;

  /**
   * Create a mock GUIView with demo of command that designed to to test the operation between this
   * View and Controller.
   * @param log used to track whether input is correct
   * @param cmdInput demo of command input
   */
  public MockGUIView(StringBuilder log, String cmdInput) {
    this.cmdInput = cmdInput;
    this.log = log;
  }

  @Override
  public void setListener(IFeature feature) {
    feature = Objects.requireNonNull(feature);
    String[] cmds = cmdInput.split(" ");
    Iterator<String> iter = Arrays.stream(cmds).iterator();
    while (iter.hasNext()) {
      String str = iter.next();
      switch (str) {
        case "start":
          feature.start();
          break;
        case "pause":
          feature.pause();
          break;
        case "loop":
          feature.setLoop(true);
          break;
        case "end-loop":
          feature.setLoop(false);
          break;
        case "restart":
          feature.restart();
          break;
        case "resume":
          feature.resume();
          break;
        case "change-speed":
          feature.changeSpeed(Double.parseDouble(iter.next()));
          break;
        default:
          break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // do nothing
      }
    }
  }

  @Override
  public void draw(List<IShape2D> shapes) {
    for (IShape2D s : shapes) {
      log.append(String.format("Input: %s\n", s.toString()));
    }
  }

  @Override
  public void render(int tempo) {
    // do nothing in this mock
  }

  @Override
  public void makeVisible() {
    // do nothing in this mock
  }

  @Override
  public TypeOfView getViewType() {
    return TypeOfView.GUI;
  }

}
