package cs5004.animator.model;

import cs5004.animator.model.shapes.IShape2D;

/**
 * This interface specifies a series of basic operations on a 2D Animator, It identifies shape
 * object by its unique identifier. It store the data of shape and a series of motions it performs
 * in the animator. It allows user to mutate the transformations of a specific shape and get the
 * data of the shape according to the identifier of the shape. Various animator model may contain
 * various transformations it can support.
 */
public interface IAnimatorModel extends IReadableModel {

  /**
   * Add a shape to the map of animation under a given identifier.
   * @param identifier represents identifier of the shape to be added
   * @param shape      represents the shape to be added
   * @throws IllegalArgumentException if the identifier is used. More constrains may specify while
   *                                  implemented.
   */
  void addShape(String identifier, IShape2D shape) throws IllegalArgumentException;

  /**
   * Remove the shape and its data according to the given identifier. The storage of will add one
   * back if remove successfully. More constrains may specify while implemented.
   * @param identifier the identifier of shape data to be removed
   * @return ture if remove successfully, false otherwise
   */
  boolean removeShape(String identifier);

  /**
   * Adds a transformation to model.
   * @param identifier The name of the shape
   * @param t1         The start time of this transformation
   * @param x1         The initial x-position of the shape
   * @param y1         The initial y-position of the shape
   * @param w1         The initial width of the shape
   * @param h1         The initial height of the shape
   * @param r1         The initial red color-value of the shape
   * @param g1         The initial green color-value of the shape
   * @param b1         The initial blue color-value of the shape
   * @param t2         The end time of this transformation
   * @param x2         The final x-position of the shape
   * @param y2         The final y-position of the shape
   * @param w2         The final width of the shape
   * @param h2         The final height of the shape
   * @param r2         The final red color-value of the shape
   * @param g2         The final green color-value of the shape
   * @param b2         The final blue color-value of the shape
   */
  void addAction(String identifier, int t1, int x1, int y1, int w1,
                 int h1, int r1, int g1, int b1, int t2,
                 int x2, int y2, int w2, int h2, int r2,
                 int g2, int b2);

  /**
   * Specify the bounding box to be used for the animation.
   * @param x      The leftmost x value
   * @param y      The topmost y value
   * @param width  The width of the bounding box
   * @param height The height of the bounding box
   */
  void setCanvas(int x, int y, int width, int height);
}
