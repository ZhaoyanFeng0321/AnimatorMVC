package cs5004.animator.controller;

import java.util.Objects;

import javax.swing.Timer;

import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.view.IFeature;
import cs5004.animator.view.IGUIView;
import cs5004.animator.view.IView;
import cs5004.animator.view.TypeOfView;

/**
 * This is a Controller implement IController interface and IFeature interface. It has
 * IAnimatorModel and IView as components and control the flow of animation production. For
 * GUIView, it uses a {@link Timer} to control the speed and display of the animation and can
 * support whatever features required by the view in IFeatures.
 */
public class ControllerImpl implements IController, IFeature {
  private final IAnimatorModel model;
  private final IView view;
  private Timer timer;
  private int tick;
  private int initTempo;
  private boolean repeat = false;

  /**
   * Create a controller with valid model and view.
   * @param model valid instance of IAnimatorModel
   * @param view  valid instance of IView
   */
  public ControllerImpl(IAnimatorModel model, IView view) {
    this.model = Objects.requireNonNull(model);
    this.view = Objects.requireNonNull(view);
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
      ((IGUIView) view).setListener(this);
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
    }
  }

  @Override
  public void start() {
    if (tick > model.getLength()) {
      tick = 0;
    }
    timer.start();
  }

  @Override
  public void pause() {
    timer.stop();
  }

  @Override
  public void resume() {
    if (tick > model.getLength()) {
      tick = 0;
    }
    timer.start();
  }

  @Override
  public void restart() {
    tick = 0;
  }

  @Override
  public void setLoop(boolean value) {
    repeat = value;
  }

  @Override
  public void changeSpeed(double factor) {
    if (Math.abs(factor - 1) < 1e-6) {
      // set to initial speed
      timer.setDelay(initTempo);
    }
    // speed up or slow down, compute the new delay value with factor and the initial speed of
    // the animation.
    if (factor > 0) {
      timer.setDelay((int) (initTempo / factor));
    }
  }
}
