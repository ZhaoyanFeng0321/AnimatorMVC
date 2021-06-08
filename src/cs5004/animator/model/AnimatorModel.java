package cs5004.animator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cs5004.animator.model.components.AnimateShape;
import cs5004.animator.model.components.Canvas;
import cs5004.animator.model.components.IAnimateShape;
import cs5004.animator.model.components.TimePeriod;
import cs5004.animator.model.motions.Action;
import cs5004.animator.model.shapes.IMutableShape2D;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.ShapeFactory;
import cs5004.animator.model.shapes.TypeOfShape;
import cs5004.animator.util.AnimationBuilder;

/**
 * This class represent a 2D AnimatorModel implements all methods in IAnimatorModel. It delegates
 * operation to {@link IAnimateShape}, which store the shape state before and after transformation,
 * as well as the animation data of that specific shape. It uses {@link Map} to connect data with
 * its identifier, which is easy to access and mutate. There are specific features of this
 * animation: 1. Able to sort data in timeline, allow input not in time order, but shapes will
 * "perform" transformation in time order. 2. It allows a shape (distinguished by identifier) to
 * perform changing of various attributes at the same time period, for example, moving while
 * scaling. 3. It not allow a shape to perform inconsistent motions or add motion in overlap time.
 * 4. The length of animation depends on the disappear time of the last shape.
 */
public class AnimatorModel implements IAnimatorModel {
  private final Map<String, IAnimateShape> shapes;
  private Canvas canvas;
  private int length = 0;


  /**
   * It initializes model with no argument.
   */
  public AnimatorModel() {
    this.shapes = new LinkedHashMap<>();
    this.canvas = new Canvas(100, 100, 100, 100);
  }

  @Override
  public void addShape(String identifier, IShape2D shape)
      throws IllegalArgumentException {
    if (identifier == null || identifier.equals("")) {
      throw new IllegalArgumentException("empty shapeName");
    }
    if (shapes.containsKey(identifier)) {
      throw new IllegalArgumentException("shape name already used");
    }
    try {
      IMutableShape2D s = (IMutableShape2D) shape;
      IAnimateShape newShape = new AnimateShape(s);
      shapes.put(identifier, newShape);
    } catch (ClassCastException e) {
      throw new IllegalArgumentException("Shape type not support in this model");
    }
  }

  @Override
  public boolean removeShape(String identifier) {
    return shapes.remove(identifier) != null;
  }

  @Override
  public void addAction(String identifier, int t1, int x1, int y1, int w1,
                        int h1, int r1, int g1, int b1, int t2,
                        int x2, int y2, int w2, int h2, int r2,
                        int g2, int b2) {
    if (identifier == null || !shapes.containsKey(identifier)) {
      throw new IllegalArgumentException("shape not exist");
    }

    try {
      ShapeFactory s = new ShapeFactory();
      TimePeriod period = new TimePeriod(t1, t2);
      IMutableShape2D former = s.create(shapes.get(identifier).getType(),
          x1, y1, w1, h1, r1, g1, b1);
      IMutableShape2D after = s.create(shapes.get(identifier).getType(),
          x2, y2, w2, h2, r2, g2, b2);
      shapes.get(identifier).addMotion(period, former, after);
      this.length = Math.max(length, t2);
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public void setCanvas(int x, int y, int width, int height) {
    try {
      this.canvas = new Canvas(x, y, width, height);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * throw IllegalArgumentException if the identifier is null or do not exist.
   */
  @Override
  public IShape2D getInitialShape(String identifier) throws IllegalArgumentException {
    if (identifier == null || !shapes.containsKey(identifier)) {
      throw new IllegalArgumentException("shape not exist");
    }
    return shapes.get(identifier).getInitialShape();
  }

  @Override
  public List<String> getShapeIDs() {
    return new ArrayList<>(shapes.keySet());
  }

  @Override
  public List<Action> getMotions(String identifier) {
    if (!shapes.containsKey(identifier)) {
      throw new IllegalArgumentException("shape not exist");
    }
    return new ArrayList<>(shapes.get(identifier).getActions());
  }

  @Override
  public List<IShape2D> getShapeAtTick(int tick) throws IllegalArgumentException {
    int length = getLength();
    if (tick < 0 || tick > length) {
      throw new IllegalArgumentException("Illegal tick in this animator");
    }
    List<IShape2D> shapesAtTick = new ArrayList<>();
    for (Map.Entry<String, IAnimateShape> a : shapes.entrySet()) {
      if (a.getValue().getShapeAtTick(tick) != null) {
        shapesAtTick.add(a.getValue().getShapeAtTick(tick));
      }
    }
    return shapesAtTick;
  }

  /**
   * Return the total time length of the animation.
   * @return the total time length of the animation.
   */
  @Override
  public int getLength() {
    return this.length;
  }

  @Override
  public Canvas getCanvas() {
    return this.canvas;
  }


  @Override
  public String getState() {
    if (shapes.size() == 0) {
      return "";
    }
    // data of all shape
    String str = "Shapes:\n";
    List<Map.Entry<String, IAnimateShape>> list = new ArrayList<>(shapes.entrySet());
    list.sort(Comparator.comparing(o -> o.getValue().getDisplay()));
    for (Map.Entry<String, IAnimateShape> s : list) {
      str += String.format("Name: %s\n%s\n", s.getKey(), s.getValue());
    }
    // data of all motions
    List<ActionData> script = new ArrayList<>();
    for (Map.Entry<String, IAnimateShape> e : shapes.entrySet()) {
      if (e.getValue().getActions().size() > 0) {
        for (Action a : e.getValue().getActions()) {
          if (!a.isMotionless()) {
            script.add(new ActionData(e.getKey(), a));
          }
        }
      }
    }
    Collections.sort(script);
    if (script.size() > 0) {
      str += "\n" + script.stream().map(s -> s.toString())
          .collect(Collectors.joining("\n"));
    }
    return str;
  }

  private void registerShape(String identifier, String type) throws IllegalArgumentException {
    if (identifier == null || identifier.equals("")) {
      throw new IllegalArgumentException("empty shapeName");
    }
    if (shapes.containsKey(identifier)) {
      throw new IllegalArgumentException("shape name already used");
    }
    if (type.equalsIgnoreCase("rectangle")) {
      this.shapes.put(identifier, new AnimateShape(TypeOfShape.RECTANGLE));
    } else if (type.equalsIgnoreCase("oval")
        || type.equalsIgnoreCase("ellipse")) {
      this.shapes.put(identifier, new AnimateShape(TypeOfShape.OVAL));
    } else {
      throw new IllegalArgumentException("invalid shape type");
    }
  }

  /**
   * A builder class that enables the program to read the existing files and convert the
   * information to an Animator model.
   */
  public static final class Builder implements AnimationBuilder<IAnimatorModel> {
    private final AnimatorModel adaptee;

    public Builder() {
      this.adaptee = new AnimatorModel();
    }

    /**
     * Builds an actual Animator model.
     * @return a modified animation model that has all the information converted from the file.
     */
    @Override
    public IAnimatorModel build() {
      return adaptee;
    }

    /**
     * Specify the bounding box to be used for the animation.
     * @param x      The leftmost x value
     * @param y      The topmost y value
     * @param width  The width of the bounding box
     * @param height The height of the bounding box
     * @return convertible builder so that it can be modified again
     */
    @Override
    public AnimationBuilder<IAnimatorModel> setBounds(int x, int y, int width, int height) {
      try {
        this.adaptee.setCanvas(x, y, width, height);
        return this;
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    /**
     * Adds a new shape to the growing document.
     * @param name The unique name of the shape to be added. No shape with this name should already
     *             exist.
     * @param type The type of shape (e.g. "ellipse", "rectangle") to be added. The set of
     *             supported shapes is unspecified, but should include "ellipse" and "rectangle" as
     *             a minimum.
     * @return convertible builder so that it can be modified again
     */
    @Override
    public AnimationBuilder<IAnimatorModel> declareShape(String name, String type) {
      try {
        this.adaptee.registerShape(name, type);
        return this;
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    /**
     * Adds a transformation to the specific animation shape.
     * @param name The name of the shape (added with {@link AnimationBuilder#declareShape})
     * @param t1   The start time of this transformation
     * @param x1   The initial x-position of the shape
     * @param y1   The initial y-position of the shape
     * @param w1   The initial width of the shape
     * @param h1   The initial height of the shape
     * @param r1   The initial red color-value of the shape
     * @param g1   The initial green color-value of the shape
     * @param b1   The initial blue color-value of the shape
     * @param t2   The end time of this transformation
     * @param x2   The final x-position of the shape
     * @param y2   The final y-position of the shape
     * @param w2   The final width of the shape
     * @param h2   The final height of the shape
     * @param r2   The final red color-value of the shape
     * @param g2   The final green color-value of the shape
     * @param b2   The final blue color-value of the shape
     * @return convertible builder so that it can be modified again
     */
    @Override
    public AnimationBuilder<IAnimatorModel> addMotion(String name, int t1, int x1, int y1, int w1,
                                                      int h1, int r1, int g1, int b1, int t2,
                                                      int x2, int y2, int w2, int h2, int r2,
                                                      int g2, int b2) {
      try {
        this.adaptee.addAction(name, t1, x1, y1, w1, h1, r1, g1, b1,
            t2, x2, y2, w2, h2, r2, g2, b2);
        return this;
      } catch (IllegalArgumentException | NullPointerException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
  }

  /*
   This is a helper class sorting all animation data of all shapes. It combine the data of actions
   of each shape and its identifier. ActionData is default to be sorted based on the timeline
   in ascending order.
   */
  class ActionData implements Comparable<ActionData> {
    private final String name;
    private final Action action;

    // It takes name and action as parameter to create Action data.
    public ActionData(String name, Action action) {
      this.name = name;
      this.action = action;
    }

    // ActionData can be sorted as its action time in ascending order.
    @Override
    public int compareTo(ActionData a) {
      return this.action.getActionTime().compareTo(a.action.getActionTime());
    }

    @Override
    public String toString() {
      return "Shape " + name + " " + action.toString();
    }
  }
}
