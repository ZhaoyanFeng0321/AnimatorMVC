package cs5004.animator.view;

import java.io.IOException;

import cs5004.animator.model.IReadableModel;
import cs5004.animator.model.components.Canvas;
import cs5004.animator.model.motions.Action;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.TypeOfShape;

/**
 * This represents SVGView implements IView. It displays animation in svg format. It support
 * display animation in different speed.
 */
public class SVGView extends AbstractView {

  /**
   * Initialize SVGView with IReadableModel.
   * @param model model with read-only shape data to be displayed
   * @param ap    appendable approach to output svg
   */
  public SVGView(IReadableModel model, Appendable ap) {
    super(model, ap);
  }

  @Override
  public void render(int tempo) throws IllegalArgumentException {
    super.render(tempo);
    IShape2D s;
    Canvas c = model.getCanvas();
    try {
      this.output.append(String.format("<svg viewBox=\"%d %d %d %d\"", c.getX(), c.getY(),
          c.getWidth(), c.getHeight()));
      this.output.append(" xmlns=\"http://www.w3.org/2000/svg\">\n");

      for (String id : model.getShapeIDs()) {
        s = model.getInitialShape(id);
        if (s != null) {
          output.append(s.toSVG().replace("NAME", id)).append("\n");
          for (Action a : model.getMotions(id)) {
            String mSVG = a.toSVG();
            if (mSVG.contains("begin")) {
              // convert 'begin' with current tempo
              String begin = mSVG.substring(mSVG.indexOf("begin="), mSVG.indexOf("ms\" dur=\""));
              double bt = Double.parseDouble(begin.substring(begin.indexOf("begin=") + 7)) / tempo;
              mSVG = mSVG.replace(begin, String.format("begin=\"%.1f", bt));
              // convert 'dur' with current tempo
              String dur = mSVG.substring(mSVG.indexOf("dur="), mSVG.indexOf("ms\" attributeName"));
              double tt = Double.parseDouble(dur.substring(dur.indexOf("dur=") + 5)) / tempo;
              mSVG = mSVG.replace(dur, String.format("dur=\"%.1f", tt));
            }
            output.append(mSVG);
          }
          if (s.getType().equals(TypeOfShape.OVAL)) {
            output.append("\n</ellipse>\n\n");
          } else if (s.getType().equals(TypeOfShape.RECTANGLE)) {
            output.append("\n</rect>\n\n");
          }
        }
      }
      output.append("</svg>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public TypeOfView getViewType() {
    return TypeOfView.SVG;
  }
}