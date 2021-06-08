package cs5004.animator.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import cs5004.animator.model.IReadableModel;
import cs5004.animator.model.shapes.IShape2D;

/**
 * This represents VisualView extends JFrame implements IView. It displays animation in different
 * speed using {@link Timer}.
 */
public class VisualView extends JFrame implements IView {
  private final AnimatorPanel animatorPanel;
  private final IReadableModel model;
  private int tick = 0;

  /**
   * Initialize VisualView with IReadableModel. It will set up the JFrame wait for setVisible.
   * @param model model with read-only shape data to be displayed
   */
  public VisualView(IReadableModel model) {
    super();
    this.model = (Objects.requireNonNull(model));
    this.setTitle("Easy Animator");
    this.setLocation(model.getCanvas().getX(), model.getCanvas().getY());
    this.setPreferredSize(new Dimension(model.getCanvas().getWidth(),
        model.getCanvas().getHeight()));
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setResizable(true);

    animatorPanel = new AnimatorPanel(model.getCanvas());
    JScrollPane scrollBar = new JScrollPane(this.animatorPanel,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollBar.setPreferredSize(new Dimension(800, 400));
    this.add(scrollBar, BorderLayout.CENTER);
    this.pack();
  }

  // Throw IllegalArgumentException if tempo is not positive value.
  @Override
  public void render(int tempo) throws IllegalArgumentException {
    if (tempo <= 0) {
      throw new IllegalArgumentException("tempo must be positive value");
    }
    // Calculate the delay of the timer based on the given tempo.
    int delay = 1000 / tempo;
    Timer timer = new Timer(delay, actionEvent -> {
      if (tick < 0 || tick > model.getLength()) {
        return;
      }
      draw(model.getShapeAtTick(tick));
      tick++;
    });
    timer.start();
  }

  @Override
  public void makeVisible() {
    this.setVisible(true);
  }

  @Override
  public TypeOfView getViewType() {
    return TypeOfView.VISUAL;
  }

  // ask panel to draw given shapes
  protected void draw(List<IShape2D> shapes) {
    Objects.requireNonNull(shapes);
    animatorPanel.drawShapes(shapes);
    animatorPanel.repaint();
  }
}
