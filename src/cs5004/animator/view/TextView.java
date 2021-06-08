package cs5004.animator.view;

import java.io.IOException;

import cs5004.animator.model.IReadableModel;

/**
 * This represents TextView implements IView. It displays animation in text format. Output
 * animation state in the order of presenting time. It support output in different speed. For
 * example, appear time from 1 to 100 in speed 20 will be described as "Appears at t=0.1s,
 * disappears at t=5.0s".
 */
public class TextView extends AbstractView {

  /**
   * Initialize view with IReadableModel and Appendable.
   * @param model model with read-only shape data to be displayed
   * @param ap    appendable approach to output text
   */
  public TextView(IReadableModel model, Appendable ap) {
    super(model, ap);
  }

  // All shape will be sorted by the presenting time.
  // All transformations description will be sorted too.
  @Override
  public void render(int tempo) throws IllegalArgumentException {
    super.render(tempo);
    String[] str = this.model.getState().split("\n");
    try {
      this.output.append(String.format("Speed: %d tick(s) / second\n\n", tempo));
      for (String s : str) {
        if (s.contains("from")) {
          // convert 'from time' with current tempo
          String sf = s.substring(s.indexOf("from t="), s.indexOf("s to t="));
          double ft = Double.parseDouble(sf.substring(sf.indexOf("t=") + 2)) / tempo;
          s = s.replace(sf, String.format("from t=%.1f", ft));
          // convert 'to time' with current tempo
          String st = s.substring(s.indexOf("to t="), s.length() - 1);
          double tt = Double.parseDouble(st.substring(st.indexOf("t=") + 2)) / tempo;
          s = s.replace(st, String.format("to t=%.1f", tt));
        }
        if (s.contains("Appears")) {
          // convert appear time with current tempo
          String ap = s.substring(s.indexOf("Appears at"), s.indexOf("s, disappears"));
          double at = Double.parseDouble(ap.substring(ap.indexOf("t=") + 2)) / tempo;
          s = s.replace(ap, String.format("Appears at t=%.1f", at));
        }
        if (s.contains("disappears")) {
          // convert disappear time with current tempo
          String dp = s.substring(s.indexOf("disappears at"), s.length() - 1);
          double dt = Double.parseDouble(dp.substring(dp.indexOf("t=") + 2)) / tempo;
          s = s.replace(dp, String.format("disappears at t=%.1f", dt));
        }
        this.output.append(s).append("\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public TypeOfView getViewType() {
    return TypeOfView.TEXT;
  }

}
