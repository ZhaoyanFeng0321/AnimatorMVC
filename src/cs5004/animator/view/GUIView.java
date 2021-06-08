package cs5004.animator.view;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cs5004.animator.model.shapes.IShape2D;

/**
 * This is the class of GUIView represents a view that allow user interaction. It allow user to
 * actually "play" the animation, operations including start, pause, resume, restart, loop/stop
 * loop, and speed change. It has-a Visual view as a component to delegate the "paint" operations
 * to Visual View and develop a user interface based on it.
 */
public class GUIView implements IGUIView {
  JComboBox<String> speedOptions;
  private final VisualView component;
  private JButton startButton;
  private JButton pauseButton;
  private JButton loopButton;
  private double tempoFactor;
  private final JLabel message;
  private IFeature feature;
  private JPanel listPanel;

  /**
   * Creates a GUIView object with the given visual view.
   * @param view a visual view as a component
   */
  public GUIView(VisualView view) {
    this.component = Objects.requireNonNull(view);

    this.makeButtons();
    this.makeSpeedList();

    // tell user we receive the command
    message = new JLabel("Welcome, please press 'start'", SwingConstants.CENTER);
    Font myFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    message.setFont(myFont);

    // contain buttons and speed list
    JPanel featurePanel = new JPanel();
    featurePanel.add(startButton);
    featurePanel.add(pauseButton);
    featurePanel.add(loopButton);
    featurePanel.add(listPanel);

    JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
    bottomPanel.add(message);
    bottomPanel.add(featurePanel);
    component.add(bottomPanel, BorderLayout.SOUTH);
    component.pack();
  }

  @Override
  public void makeVisible() {
    component.makeVisible();
  }

  @Override
  public void setListener(IFeature feature) {
    this.feature = Objects.requireNonNull(feature);
  }

  @Override
  public void draw(List<IShape2D> shapes) {
    Objects.requireNonNull(shapes);
    component.draw(shapes);
  }

  @Override
  public TypeOfView getViewType() {
    return TypeOfView.GUI;
  }

  @Override
  public void render(int tempo) {
    // do nothing
  }

  private void makeSpeedList() {
    speedOptions = new JComboBox<>();
    speedOptions.addItem("standard");
    speedOptions.addItem("0.5x");
    speedOptions.addItem("0.75x");
    speedOptions.addItem("1.25x");
    speedOptions.addItem("1.5x");
    speedOptions.addItem("1.75x");
    speedOptions.addItem("2.0x");

    speedOptions.addActionListener(e -> {
      String temp = Objects.requireNonNull(speedOptions.getSelectedItem()).toString();
      if (temp.equals("standard")) {
        feature.changeSpeed(1);
      } else {
        tempoFactor = Double.parseDouble(temp.substring(0, temp.length() - 1));
        feature.changeSpeed(tempoFactor);
      }
      message.setText("change speed to " + temp);
    });

    listPanel = new JPanel();
    listPanel.add(new JLabel("Speed: "));
    listPanel.add(speedOptions);
  }

  private void makeButtons() {
    // start button
    startButton = new JButton("start");
    startButton.setActionCommand("start");
    startButton.addActionListener(e -> {
      String cmd = startButton.getText();
      if (cmd.equals("start")) {
        feature.start();
        startButton.setText("restart");
      } else if (cmd.equals("restart")) {
        feature.restart();
      }
      message.setText("animation " + cmd);
    });

    // pause button
    pauseButton = new JButton("pause");
    pauseButton.setActionCommand("pause");
    pauseButton.addActionListener(e -> {
      String cmd = pauseButton.getText();
      if (cmd.equals("pause")) {
        feature.pause();
        pauseButton.setText("resume");
      } else if (cmd.equals("resume")) {
        feature.resume();
        pauseButton.setText("pause");
      }
      message.setText("animation " + cmd);
    });

    // loop button
    loopButton = new JButton("loop");
    loopButton.setActionCommand("loop");
    loopButton.addActionListener(e -> {
      String cmd = loopButton.getText();
      if (cmd.equals("loop")) {
        feature.setLoop(true);
        loopButton.setText("stop loop");
        message.setText("enable loop");
      } else if (cmd.equals("stop loop")) {
        feature.setLoop(false);
        loopButton.setText("loop");
        message.setText("disable loop");
      }
    });
  }
}