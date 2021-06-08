package cs5004.animator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs5004.animator.controller.ControllerImpl;
import cs5004.animator.controller.IController;
import cs5004.animator.model.AnimatorModel;
import cs5004.animator.model.IAnimatorModel;
import cs5004.animator.util.AnimationReader;
import cs5004.animator.view.IView;
import cs5004.animator.view.ViewFactory;

/**
 * Class that contains main method and runs the animation.
 */
public class EasyAnimator {

  /**
   * The purpose of this method is to parse the user input and establish the types of things the
   * user specified. It should follow rules below: Providing an input file (the -in pair) and a
   * view (the -view pair) are mandatory. If the output set is not specified and the view needs it,
   * the default should be System.out. If the speed is not specified and the view needs it, the
   * default is 1 tick per second.
   * @param args given arguments in the main method.
   */
  public static void main(String[] args) {

    Objects.requireNonNull(args);
    IAnimatorModel model = null;
    IView view;
    ViewFactory viewFactory = new ViewFactory();
    String input = "";
    String viewType = "";
    String out = "";
    int speed = 1;

    JFrame messageFrame = new JFrame();
    messageFrame.setSize(100, 100);

    for (int i = 0; i < args.length; i++) {
      try {
        // reads input file
        if (args[i].equals("-in")) {
          input = args[i + 1];
          try {
            InputStream inputStream = new FileInputStream(input);
            model = AnimationReader.parseFile(new InputStreamReader(inputStream),
                new AnimatorModel.Builder());
          } catch (FileNotFoundException e) {
            // file not found
            JOptionPane.showMessageDialog(messageFrame, "file not found",
                "File Error: ", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
          } catch (NullPointerException | IllegalArgumentException e) {
            // Exception thrown while building the model
            JOptionPane.showMessageDialog(messageFrame, e.getMessage(),
                "Fail to build animation: ", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
          }
        }
        // reads view type the user wants
        if (args[i].equals("-view")) {
          try {
            viewType = args[i + 1];
          } catch (IllegalArgumentException e) {
            // view type provided is invalid
            JOptionPane.showMessageDialog(messageFrame, e.getMessage(),
                "View Error: ", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
          }
        }
        // reads output file path
        if (args[i].equals("-out") || args[i].equals("-o")) {
          out = args[i + 1];
        }
        // reads the speed
        if (args[i].equals("-speed")) {
          speed = Integer.parseInt(args[i + 1]);
        }
      } catch (IndexOutOfBoundsException e) {
        // ask user to specify later
      }
    }

    // if file name or view not be specified
    if (input.equals("")) {
      JOptionPane.showMessageDialog(messageFrame, "Please specify an input file",
          "Warning: ", JOptionPane.WARNING_MESSAGE);
      System.exit(-1);
    }
    if (viewType.equals("")) {
      JOptionPane.showMessageDialog(messageFrame, "Please specify a view",
          "Warning: ", JOptionPane.WARNING_MESSAGE);
      System.exit(-1);
    }

    // render model in view
    try {
      Appendable ap;
      if (out.equals("") || out.equals("out")) {
        ap = System.out;
      } else {
        ap = new FileWriter(out, true);
      }
      view = viewFactory.create(viewType, model, ap);
      IController controller = new ControllerImpl(model, view);
      controller.run(speed);
      // output the non-visual view file
      if ((viewType.equals("text") || viewType.equals("svg"))
          && ap instanceof FileWriter) {
        ((FileWriter) ap).flush();
        ((FileWriter) ap).close();
      }
    } catch (NullPointerException | IOException e) {
      e.printStackTrace();
      System.exit(-1);
    } catch (IllegalArgumentException e) {
      JOptionPane.showMessageDialog(messageFrame, e.getMessage(),
          "Error in view: ", JOptionPane.ERROR_MESSAGE);
      System.exit(-1);
    }
  }
}