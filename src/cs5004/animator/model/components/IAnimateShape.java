package cs5004.animator.model.components;

import java.util.List;

import cs5004.animator.model.motions.Action;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.TypeOfShape;

/**
 * This interface specifies operations of an Animate shape. An animate shape represent a shape in
 * the animator, which consist of a IShape2D itself, its appear period in the animation and a list
 * of transformations it is going to make.
 */
public interface IAnimateShape {

  /**
   * Return a list of actions the shape to perform sorted by time.
   * @return a list of actions the shape to perform sorted by time.
   */
  List<Action> getActions();

  /**
   * return the copy of the newest status/data of the shape after all transformation.
   * @return the copy of the newest status/data of the shape after all transformation
   */
  IShape2D getFinalShape();

  /**
   * Returns the copy of original status of the shape before changed.
   * @return the copy of original status of the shape
   */
  IShape2D getInitialShape();

  /**
   * Returns the type of this animate shape.
   * @return the type of this animate shape
   */
  TypeOfShape getType();

  /**
   * return the time period this shape appear in the animation.
   * @return the time period this shape appear in the animation.
   */
  TimePeriod getDisplay();

  /**
   * Return the copy of shape status at a specific tick. Return null if the shape not appear at
   * this tick.
   * @param tick a moment in the animation
   * @return the copy of shape status at a specific tick, null if shape not show up at that tick
   */
  IShape2D getShapeAtTick(int tick);

  /**
   * Add transformation to this shape with given time, former state and after state of shape.
   * @param period   duration of transformation
   * @param oldShape state of shape before transformation
   * @param newShape state of shape after transformation
   */
  void addMotion(TimePeriod period, IMutableShape2D oldShape, IMutableShape2D newShape);
}
