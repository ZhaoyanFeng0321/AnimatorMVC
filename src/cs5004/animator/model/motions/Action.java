package cs5004.animator.model.motions;

import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IShape2D;

/**
 * This interface represents the Action that can be made in the Animator. It contains the motion
 * data of a shape in the animation. It consists of the time period, old shape state, new shape
 * state of this action.
 */
public interface Action {

  /**
   * Return time period of the action.
   * @return the action time period
   */
  TimePeriod getActionTime();

  /**
   * Report if a given tick is in the action time period.
   * @param tick represents the given tick
   * @return true if the tick is in the action time period, false otherwise.
   */
  boolean isActingAtTick(int tick);

  /**
   * Return the copy of shape changed after this motion.
   * @return the copy of new shape
   */
  IShape2D getNewShape();

  /**
   * Return the copy of the shape at a given tick.
   * @param tick represents the given tick
   * @return the copy of the shape at a given tick
   */
  IShape2D getShapeAtTick(int tick);

  /**
   * Check if other action can happen with this action at the same time. Return true if two
   * motions: 1. not the same type of motion 2. happen at exactly same time 3. end up with same
   * state of shape otherwise return false.
   * @param action other action to be compared
   * @return true if both can happen at the same time, false otherwise
   */
  boolean isSynchronous(Action action);

  /**
   * Checks if the previous action is consistent with this one. It is consistent if shape produced
   * by last action is the one to be changed in this action.
   * @param prevShape shape produced by previous one action
   * @return ture if is consistent, false otherwise
   */
  boolean isConsistent(IShape2D prevShape);

  /**
   * Return svg format of the transformation.
   * @return svg format of the transformation
   */
  String toSVG();

  /**
   * Returns true if just appear, false otherwise.
   * @return true if just appear, false otherwise
   */
  boolean isMotionless();

}
