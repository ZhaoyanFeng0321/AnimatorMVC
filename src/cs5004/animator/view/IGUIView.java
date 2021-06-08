package cs5004.animator.view;

import java.util.List;

import cs5004.animator.model.shapes.IShape2D;

/**
 * This is a IGUIView interface represent views that allow user to interact with. It extends IView
 * interface and mandate additional operations that should be implemented by a GUIView.
 */
public interface IGUIView extends IView {
  /**
   * Provide the view with a controller that implement IFeature, which contains all operations
   * expected to make in the GUIView.
   * @param feature controller
   */
  void setListener(IFeature feature);

  /**
   * Draw given shapes on panel. Therefore the speed of "paint" can be controlled.
   * @param shapes list of shapes to draw
   */
  void draw(List<IShape2D> shapes);
}
