package cs5004.animator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;

import cs5004.animator.model.components.Canvas;
import cs5004.animator.model.shapes.IShape2D;
import cs5004.animator.model.shapes.TypeOfShape;

/**
 * A drawing class called by {@link VisualView} to draw all the shapes with their positions,
 * colors, and sizes on a canvas with the given size.
 */
class AnimatorPanel extends JPanel {
  private List<IShape2D> shapes;

  /**
   * A constructor that is called in the VisualView with canvas size to set up this panel.
   * @param canvas canvas of animation
   */
  public AnimatorPanel(Canvas canvas) {
    super();
    Objects.requireNonNull(canvas);
    this.setBackground(Color.WHITE);
    this.setBounds(canvas.getX(), canvas.getY(),
        canvas.getWidth(), canvas.getHeight());
    this.setPreferredSize(new Dimension(canvas.getWidth(),
        canvas.getHeight()));
  }

  /**
   * Draws a shape with corresponding position, color, and size.
   * @param g given graphics
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (shapes != null && shapes.size() > 0) {
      Graphics2D g2d = (Graphics2D) g;
      for (IShape2D shape : shapes) {
        if (shape != null) {
          g2d.setColor(new Color(shape.getColor().getR(), shape.getColor().getG(),
              shape.getColor().getB()));
          if (shape.getType().equals(TypeOfShape.RECTANGLE)) {
            g2d.fillRect((int) shape.getReference().getX(), (int) shape.getReference().getY(),
                (int) shape.getWidth(), (int) shape.getHeight());
          } else if (shape.getType().equals(TypeOfShape.OVAL)) {
            g2d.fillOval((int) shape.getReference().getX(), (int) shape.getReference().getY(),
                (int) shape.getWidth(), (int) shape.getHeight());
          }
        }
      }
    }
  }

  /**
   * Call by Visual view to update the shapes to be drawn.
   * @param shapes list of shape to be drawn
   */
  public void drawShapes(List<IShape2D> shapes) {
    this.shapes = (Objects.requireNonNull(shapes));
  }
}
