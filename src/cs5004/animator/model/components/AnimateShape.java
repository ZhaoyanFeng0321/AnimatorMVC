package cs5004.animator.model.components;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import cs5004.animator.model.motions.Action;
import cs5004.animator.model.motions.Appear;
import cs5004.animator.model.motions.ChangeColor;
import cs5004.animator.model.motions.Move;
import cs5004.animator.model.motions.Scale;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.TypeOfShape;


/**
 * This class implements all methods in IAnimateShape. It operates on IMutableShape2D and store
 * action data will be taken by IMutableShape2D.
 */
public class AnimateShape implements IAnimateShape {
  private final TypeOfShape shapeType;
  private final List<Action> listOfActs;
  private IMutableShape2D initialShape;
  private IShape2D newShape;
  private TimePeriod appear;

  /**
   * Create AnimateShape and initiate data with given IShape. The appear time of object is null and
   * wait to be set. newShape is set to be the original shape before it gets mutated.
   * @param shape an instance of IMutableShape2D
   */
  public AnimateShape(IMutableShape2D shape) {
    Objects.requireNonNull(shape);
    this.shapeType = shape.getType();
    this.initialShape = shape;
    this.newShape = initialShape.copy();
    this.listOfActs = new ArrayList<>();
    this.appear = new TimePeriod();
  }

  /**
   * Register AnimateShape with only knowledge of shape type represent an abstract shape state.
   * This animate shape will become concrete after shape data initialized.
   * @param shape shape type
   */
  public AnimateShape(TypeOfShape shape) {
    Objects.requireNonNull(shape);
    this.initialShape = null;
    this.newShape = null;
    this.shapeType = shape;
    this.listOfActs = new ArrayList<>();
    this.appear = new TimePeriod();
  }

  @Override
  public List<Action> getActions() {
    process();
    return listOfActs;
  }

  @Override
  public IShape2D getInitialShape() {
    if (initialShape == null) {
      return null;
    }
    return initialShape.copy();
  }

  @Override
  public IShape2D getFinalShape() {
    if (initialShape == null) {
      return null;
    }
    process();
    return newShape.copy();
  }

  @Override
  public TypeOfShape getType() {
    return shapeType;
  }

  @Override
  public TimePeriod getDisplay() {
    return appear;
  }

  // iterate to pass update shape and get desired shape
  @Override
  public IShape2D getShapeAtTick(int tick) {
    if (!appear.isValid() || tick < appear.getStart() || tick > appear.getEnd()) {
      return null;
    }
    IShape2D s = initialShape.copy();
    process();
    for (Action a : listOfActs) {
      if (a.getActionTime().getStart() >= tick) {
        break;
      }
      s = a.getNewShape();
      if (a.isActingAtTick(tick)) {
        s = a.getShapeAtTick(tick);
        break;
      }
    }
    return s.copy();
  }

  @Override
  public void addMotion(TimePeriod period, IMutableShape2D oldShape, IMutableShape2D newShape)
      throws IllegalArgumentException, NullPointerException {
    Objects.requireNonNull(period);
    Objects.requireNonNull(oldShape);
    Objects.requireNonNull(newShape);

    // shape change but no time to make change
    if (period.duration() == 0) {
      return;
    }
    // update earliest state of shape
    if (this.setDisplay(period)) {
      this.initialShape = oldShape;
      this.newShape = initialShape.copy();
    }
    try {
      if (oldShape.toString().equals(newShape.toString())) {
        this.validate(new Appear(period, oldShape));
      } else {
        if (oldShape.getReference().getX() != newShape.getReference().getX()
            || oldShape.getReference().getY() != newShape.getReference().getY()) {
          this.validate(new Move(period, oldShape, newShape));
        }
        if (oldShape.getWidth() != newShape.getWidth()
            || oldShape.getHeight() != newShape.getHeight()) {
          this.validate(new Scale(period, oldShape, newShape));
        }
        if (!oldShape.getColor().toString().equals(newShape.getColor().toString())) {
          this.validate(new ChangeColor(period, oldShape, newShape));
        }
      }
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public String toString() {
    if (initialShape == null) {
      return "Type: " + shapeType;
    }
    String str = initialShape + "\n";
    if (appear.isValid()) {
      str += String.format("Appears at t=%ds, disappears at t=%ds",
          appear.getStart(), appear.getEnd());
    }
    return str;
  }

  // Set the display time for this shape in the animation
  private boolean setDisplay(TimePeriod period) {
    Objects.requireNonNull(period);
    boolean flag = false; // whether need update oldest shape state
    if (!appear.isValid()) {
      this.appear = period;
      flag = true; // earliest shape state need update
    } else {
      if (period.getStart() <= appear.getStart()) {
        flag = true;
      }
      this.appear = new TimePeriod(Math.min(period.getStart(), appear.getStart()),
          Math.max(period.getEnd(), appear.getEnd()));
    }
    return flag;
  }

  // A helper method to check if action is legal to act at this time period
  // if any other motions is taking in overlap time, throw exception, else add motion to list
  private void validate(Action newAct) {
    Objects.requireNonNull(newAct);
    TimePeriod period = newAct.getActionTime();
    // check if overlap with other motion time
    for (Action a : listOfActs) {
      if (a.getActionTime().compareTo(period) == 0 && !a.isSynchronous(newAct)) {
        throw new IllegalArgumentException("Overlap time to make motion");
      }
      if (a.getActionTime().isOverlap(period)) {
        throw new IllegalArgumentException("Overlap time to make motion");
      }
    }
    listOfActs.add(newAct);
  }

  // A helper method to iterates the action list and get the newest shape.
  // It passes the former state of shape to the next action data to produce the newer shape.
  private void process() {
    if (listOfActs.size() > 0) {
      listOfActs.sort(Comparator.comparing(Action::getActionTime));
      Action prev;
      for (int i = 1; i < listOfActs.size(); i++) {
        prev = listOfActs.get(i - 1);
        // if shape from last motion not consistent with this motion
        // and not able to happen at the same time, throw exception
        if (!listOfActs.get(i).isConsistent(prev.getNewShape())
            && !listOfActs.get(i).isSynchronous(prev)) {
          throw new IllegalStateException("Motion is not consistent");
        }
      }
      // nothing wrong, new Shape produce
      this.newShape = listOfActs.get(listOfActs.size() - 1).getNewShape();
    }
  }
}
