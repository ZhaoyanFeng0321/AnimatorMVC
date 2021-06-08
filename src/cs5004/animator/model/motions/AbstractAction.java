package cs5004.animator.model.motions;

import java.util.Objects;

import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.IShape2D;

/**
 * This is an abstract class implement Action interface operates on IRegularShapes. It share
 * commons of the method operations on all IRegularShapes.
 */
public abstract class AbstractAction implements Action {
  protected TimePeriod actionPeriod;
  protected IMutableShape2D oldShape;
  protected IMutableShape2D newShape;

  /**
   * Create action with given actionPeriod, shape data before and after transformation. All
   * arguments requireNonNull, otherwise exception thrown.
   * @param actionPeriod time period to take action
   * @param oldShape     former state of shape before motion
   * @param newShape     expected state of shape after motion
   */
  public AbstractAction(TimePeriod actionPeriod, IMutableShape2D oldShape,
                        IMutableShape2D newShape) {
    this.actionPeriod = Objects.requireNonNull(actionPeriod, "invalid time period to act");
    this.oldShape = Objects.requireNonNull(oldShape, "invalid former shape");
    this.newShape = Objects.requireNonNull(newShape, "invalid changed shape");
  }

  @Override
  public TimePeriod getActionTime() {
    return actionPeriod;
  }

  @Override
  public boolean isActingAtTick(int tick) {
    return tick > actionPeriod.getStart() && tick <= actionPeriod.getEnd();
  }

  @Override
  public IShape2D getNewShape() {
    return newShape.copy();
  }

  @Override
  public IShape2D getShapeAtTick(int tick) throws IllegalStateException {
    if (!isActingAtTick(tick)) {
      return null;
    }
    return oldShape.tween(newShape, actionPeriod, tick);
  }

  @Override
  public boolean isConsistent(IShape2D prevShape) {
    Objects.requireNonNull(prevShape);
    return this.oldShape.toString().equals(prevShape.toString());
  }

  @Override
  public boolean isMotionless() {
    return false;
  }

  @Override
  public boolean isSynchronous(Action action) {
    Objects.requireNonNull(action);
    if (action.getClass() == this.getClass()) {
      return false;
    }
    return action.getActionTime().compareTo(actionPeriod) == 0
        && action.getNewShape().toString().equals(this.newShape.toString());
  }
}
