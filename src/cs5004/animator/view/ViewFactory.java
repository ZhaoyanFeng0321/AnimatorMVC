package cs5004.animator.view;

import java.util.Objects;

import cs5004.animator.model.IReadableModel;

/**
 * This is a ViewFactory to help create View with given args.
 */
public class ViewFactory {

  /**
   * Create View with given data.
   * @param view  type of view
   * @param model IReadableModel contains ead-only shapes data
   * @param ap    way to output
   * @return view instance
   */
  public IView create(String view, IReadableModel model, Appendable ap) {
    Objects.requireNonNull(view);
    try {
      if (view.equalsIgnoreCase("visual")) {
        return new VisualView(model);
      } else if (view.equalsIgnoreCase("svg")) {
        return new SVGView(model, ap);
      } else if (view.equalsIgnoreCase("text")) {
        return new TextView(model, ap);
      } else if (view.equalsIgnoreCase("playback")) {
        return new GUIView(new VisualView(model));
      }
      throw new IllegalArgumentException("invalid type of view");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }
}
