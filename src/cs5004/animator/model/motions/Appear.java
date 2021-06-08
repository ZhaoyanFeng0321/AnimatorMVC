package cs5004.animator.model.motions;

import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.IShape2D;

/**
 * Action represents a shape just appear and motionless. same.
 */
public class Appear extends AbstractAction {

  /**
   * Create action with given actionPeriod, shape data before and after transformation. All
   * arguments requireNonNull, otherwise exception thrown.
   * @param actionPeriod time period to take action
   * @param shape        state of shape before motion
   */
  public Appear(TimePeriod actionPeriod, IMutableShape2D shape) {
    super(actionPeriod, shape, shape);
  }

  @Override
  public IShape2D getShapeAtTick(int tick) throws IllegalStateException {
    if (!isActingAtTick(tick)) {
      return null;
    }
    return oldShape;
  }

  @Override
  public boolean isMotionless() {
    return true;
  }

  @Override
  public String toSVG() {
    return "";
  }

  @Override
  public String toString() {
    return "";
  }
}
