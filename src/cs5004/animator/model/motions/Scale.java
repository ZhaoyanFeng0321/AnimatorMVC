package cs5004.animator.model.motions;

import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.TypeOfShape;

/**
 * This is Scale class extends ShapeAction and implements Action interface, which is to change
 * width and/or height of the shape.
 */
public class Scale extends AbstractAction {
  private final double newWidth;
  private final double newHeight;

  /**
   * Create Scale action with given actionPeriod, shape data before and after transformation.
   * @param actionPeriod time period to take action, should not be null
   * @param oldShape     shape data before transformation, should not be null
   * @param newShape     shape data after transformation, should not be null
   */
  public Scale(TimePeriod actionPeriod, IMutableShape2D oldShape, IMutableShape2D newShape) {
    super(actionPeriod, oldShape, newShape);
    this.newWidth = newShape.getWidth();
    this.newHeight = newShape.getHeight();
  }

  @Override
  public String toString() {
    return String.format("scales from Width: %.1f, Height: %.1f to Width: %.1f, Height: %.1f, %s",
        oldShape.getWidth(), oldShape.getHeight(), newWidth, newHeight, actionPeriod);
  }

  @Override
  public String toSVG() {
    String result = "";
    if (oldShape.getType() == TypeOfShape.OVAL) {
      // change width/rx
      if (this.newWidth != this.oldShape.getWidth()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"rx\" from=\"" + this.oldShape.getWidth() / 2
            + "\" to=\"" + this.newWidth / 2 + "\" fill=\"freeze\" />\n";
      }
      // change height/ry
      if (this.newHeight != this.oldShape.getHeight()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"" + "ry\" from=\"" + this.oldShape.getHeight() / 2
            + "\" to=\"" + this.newHeight / 2 + "\" fill=\"freeze\" />\n";
      }
    }
    if (oldShape.getType() == TypeOfShape.RECTANGLE) {
      // change width
      if (this.newWidth != this.oldShape.getWidth()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"width\" from=\"" + this.oldShape.getWidth()
            + "\" to=\"" + this.newWidth + "\" fill=\"freeze\" />\n";
      }
      // change height
      if (this.newHeight != this.oldShape.getHeight()) {
        result += "\t<animate attributeType=\"xml\" begin=\""
            + 1000 * this.getActionTime().getStart()
            + "ms\" dur=\"" + 1000 * (this.actionPeriod.duration())
            + "ms\" attributeName=\"height\" from=\"" + this.oldShape.getHeight()
            + "\" to=\"" + this.newHeight + "\" fill=\"freeze\" />\n";
      }
    }
    return result;
  }
}
