package cs5004.animator.model.motions;

import cs5004.animator.model.components.Point2D;
import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.TypeOfShape;

/**
 * This is Move class extends AbstractAction and implements Action interface, which is to change
 * position of the shape.
 */
public class Move extends AbstractAction {
  private final Point2D oldPosition;
  private final Point2D newPosition;

  /**
   * Create Move action with given actionPeriod, shape data before and after transformation.
   * @param actionPeriod time period to take action, should not be null
   * @param oldShape     former state of shape before motion, should not be null
   * @param newShape     expected state of shape after motion, should not be null
   */
  public Move(TimePeriod actionPeriod, IMutableShape2D oldShape, IMutableShape2D newShape) {
    super(actionPeriod, oldShape, newShape);
    this.oldPosition = oldShape.getReference();
    this.newPosition = newShape.getReference();
  }

  @Override
  public String toString() {
    //action have not taken
    if (oldShape == null) {
      return "";
    }
    return String.format("move from %s to %s, %s", oldPosition, newPosition, actionPeriod);
  }

  @Override
  public String toSVG() {
    String result = "";
    if (this.oldShape.getType() == TypeOfShape.OVAL) {
      // move horizontal
      if (this.newPosition.getX() != this.oldPosition.getX()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"cx\" from=\"" + this.oldPosition.getX()
            + "\" to=\"" + this.newPosition.getX() + "\" fill=\"freeze\" />\n";
      }
      // move vertical
      if (this.newPosition.getY() != this.oldPosition.getY()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"cy\" from=\"" + this.oldPosition.getY()
            + "\" to=\"" + this.newPosition.getY() + "\" fill=\"freeze\" />\n";
      }
    }
    if (this.oldShape.getType() == TypeOfShape.RECTANGLE) {
      // move horizontal
      if (this.newPosition.getX() != this.oldPosition.getX()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"x\" from=\"" + this.oldPosition.getX()
            + "\" to=\"" + this.newPosition.getX() + "\" fill=\"freeze\" />\n";
      }
      // move vertical
      if (this.newPosition.getY() != this.oldPosition.getY()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"y\" from=\"" + this.oldPosition.getY()
            + "\" to=\"" + this.newPosition.getY() + "\" fill=\"freeze\" />\n";
      }
    }
    return result;
  }
}
