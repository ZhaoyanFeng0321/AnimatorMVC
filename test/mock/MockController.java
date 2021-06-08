package mock;

import java.util.Objects;

import javax.swing.Timer;

import cs5004.animator.controller.IController;
import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.view.IFeature;
import cs5004.animator.view.IGUIView;
import cs5004.animator.view.IView;
import cs5004.animator.view.TypeOfView;

/**
 * This is a Mock Controller used to test the interaction correctness between the ControllerImpl
 * and GUIView.
 */
public class MockController implements IController, IFeature {
  private final IAnimatorModel model;
  private final IView view;
  private Timer timer;
  private int tick;
  private int initTempo;
  private StringBuilder react;
  private boolean repeat = false;


  /**
   * Create a mock.MockController with the given list of shapes list and view.
   * @param model valid instance of IAnimatorModel
   * @param view  view, requireNonNull
   * @param react used to track method evoke
   */
  public MockController(IAnimatorModel model, IView view, StringBuilder react) {
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
    this.react = react;
  }

  @Override
  public void run(int speed) throws IllegalArgumentException {
    try {
      view.makeVisible();
    } catch (UnsupportedOperationException e) {
      // ignore it
    }
    try {
      view.render(speed);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }

    // set up control to GUIView
    if (view.getViewType().equals(TypeOfView.GUI)) {
      initTempo = 1000 / speed;
      this.timer = new Timer(initTempo, actionEvent -> {
        if (repeat && tick > model.getLength()) {
          tick = 0;
        } else if (!repeat && tick > model.getLength()) {
          return;
        }
        ((IGUIView) view).draw(model.getShapeAtTick(tick));
        tick++;
      });
      ((IGUIView) view).setListener(this);
    }
  }

  @Override
  public void start() {
    react.append("Start!\n");
    if (tick > model.getLength()) {
      tick = 0;
    }
    timer.start();
  }

  @Override
  public void pause() {
    timer.stop();
    react.append(String.format("Pause at tick %d\n", tick));
  }

  @Override
  public void resume() {
    react.append(String.format("Resume at tick %d\n", tick));
    timer.start();
  }

  @Override
  public void restart() {
    react.append("Restart!\n");
    timer.restart();
  }

  @Override
  public void setLoop(boolean value) {
    repeat = value;
    if (value) {
      react.append("Loop!\n");
    } else {
      react.append("End-Loop!\n");
    }
  }

  @Override
  public void changeSpeed(double factor) {
    if (Math.abs(factor - 0) < 1e-6) {
      // set to initial speed
      timer.setDelay(initTempo);
      react.append((String.format("speed: %d ticks/sec\n", initTempo)));
    }
    // speed up or slow down, compute the new delay value with factor and the initial speed of
    // the animation.
    if (factor > 0) {
      timer.setDelay((int) (initTempo / factor));
      react.append(String.format("speed: %d ticks/sec\n", (int) (initTempo / factor)));
    }
  }
}