package cs5004.animator.model.motions;

import cs5004.animator.model.components.Color;
import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.shapes.IMutableShape2D;

/**
 * This is ChangeColor class extends AbstractAction and implements Action interface, which is to
 * change color of Shapes.
 */
public class ChangeColor extends AbstractAction {
  private final Color oldColor;
  private final Color newColor;

  /**
   * Create ChangeColor action with given actionPeriod, shape data before and after
   * transformation.
   * @param actionPeriod time period to take action, should not be null
   * @param oldShape     shape data before transformation, should not be null
   * @param newShape     shape data after transformation, should not be null
   */
  public ChangeColor(TimePeriod actionPeriod, IMutableShape2D oldShape, IMutableShape2D newShape) {
    super(actionPeriod, oldShape, newShape);
    this.oldColor = oldShape.getColor();
    this.newColor = newShape.getColor();
  }

  @Override
  public String toString() {
    return String.format("changes color from %s to %s, %s", oldColor, newColor, actionPeriod);
  }

  @Override
  public String toSVG() {
    return "\t<animate attributeType=\"xml\" begin=\""
        + 1000 * this.getActionTime().getStart()
        + "ms\" dur=\"" + 1000 * this.getActionTime().duration() + "ms\""
        + " attributeName=\"fill\"" + " from=\"RGB(" + this.oldColor.getR() + ","
        + this.oldColor.getG() + "," + this.oldColor.getB() + ")\" to=\""
        + "RGB(" + this.newColor.getR() + "," + this.newColor.getG() + ","
        + this.newColor.getB() + ")" + "\" fill=\"freeze\" />\n";
  }
}
