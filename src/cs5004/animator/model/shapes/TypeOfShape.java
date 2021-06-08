package cs5004.animator.model.shapes;

/**
 * This Enum represents TypeOfShape support by this animation.
 */
public enum TypeOfShape {
  OVAL("Oval"),
  RECTANGLE("Rectangle");

  private final String name;

  TypeOfShape(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}
