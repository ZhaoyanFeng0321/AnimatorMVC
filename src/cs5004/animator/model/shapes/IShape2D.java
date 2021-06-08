package cs5004.animator.model.shapes;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.Point2D;

/**
 * This interface represents all 2 dimensions shapes. All data of IShape2D is read-only. It
 * specifies the operations can be performed by all shape 2D. A 2D shape is characterized by
 * position, width, height and color.
 */
public interface IShape2D {

  /**
   * Return the Enum Type of this Shape.
   * @return the Enum Type of this Shape
   */
  TypeOfShape getType();

  /**
   * Return the reference point of the shape.
   * @return the reference point of the shape
   */
  Point2D getReference();

  /**
   * Return the width of the shape.
   * @return the width point of the shape
   */
  double getWidth();

  /**
   * Return the height of the shape.
   * @return the height of the shape
   */
  double getHeight();

  /**
   * Return the color of the shape.
   * @return the color of the shape
   */
  Color getColor();

  /**
   * Return the R value of RGB color of the shape.
   * @return the R value of RGB color of the shape
   */
  int getR();

  /**
   * Return the G value of RGB color of the shape.
   * @return the G value of RGB color of the shape
   */
  int getG();

  /**
   * Return the B value of RGB color of the shape.
   * @return the B value of RGB color of the shape
   */
  int getB();

  /**
   * Return the copy of the shape.
   * @return the copy of the shape
   */
  IShape2D copy();

  /**
   * Return SVG format of shape data.
   * @return SVG format of shape data
   */
  String toSVG();
}
