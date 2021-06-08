package cs5004.animator.model;

import java.util.List;

import cs5004.animator.model.components.Canvas;
import cs5004.animator.model.motions.Action;
import cs5004.animator.model.shapes.IShape2D;

/**
 * This model interface specifies a series of read-only operations on a 2D Animator. It is an
 * readable model which only support getter method to allow access to animation data, but not allow
 * outsider to mutate the data.
 */
public interface IReadableModel {

  /**
   * Return list of identifiers of all shapes in this animation. The identifiers are the keys to
   * access specific shape data.
   * @return list of identifiers of all shapes in this animation
   */
  List<String> getShapeIDs();

  /**
   * Return an unmodifiable List containing all transformations of a specific shape with given
   * identifier. More constrains may specify while implemented.
   * @param identifier key to access specific shape data
   * @return a transformations list of a specific shape
   */
  List<Action> getMotions(String identifier);

  /**
   * Return the copy of the shape at given tick.
   * @param tick represents the tick to return the shape data
   * @return the copy of the shape at given tick
   * @throws IllegalArgumentException if the tick is negative. More constrains may specify while
   *                                  implemented.
   */
  List<IShape2D> getShapeAtTick(int tick) throws IllegalArgumentException;

  /**
   * Return the copy of the beginning state of shape retrieved by the given identifier. More
   * constrains may specify while implemented.
   * @param identifier represents the identifier of the shape data
   * @return the copy of the shape under the given identifier.
   */
  IShape2D getInitialShape(String identifier);

  /**
   * Return the total time length of the animation.
   * @return the total time length of the animation.
   */
  int getLength();

  /**
   * Returns canvas size of the animation.
   * @return canvas size of the animation
   */
  Canvas getCanvas();

  /**
   * Outputs the whole data of in the animator. Includes the original data of shape and list of
   * action sorted by appear time in ascending order.
   * @return animation state in string
   */
  String getState();
}
