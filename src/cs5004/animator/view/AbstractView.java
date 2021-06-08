package cs5004.animator.view;

import java.util.Objects;

import cs5004.animator.model.IReadableModel;

/**
 * This is a AbstractView implements part of common methods implements IView. It represents view
 * that output animation in textual format.
 */
public abstract class AbstractView implements IView {
  protected IReadableModel model;
  protected Appendable output;

  /**
   * Initialize view with IReadableModel and Appendable.
   * @param model model with read-only shape data to be displayed
   * @param ap    appendable approach to output textual animation
   */
  public AbstractView(IReadableModel model, Appendable ap) {
    this.model = Objects.requireNonNull(model, "invalid model provided to view");
    this.output = Objects.requireNonNull(ap, "invalid appendable provided to view");
  }

  // Throw IllegalArgumentException if tempo is not positive value.
  @Override
  public void render(int tempo) throws IllegalArgumentException {
    if (tempo <= 0) {
      throw new IllegalArgumentException("tempo must be positive value");
    }
  }

  @Override
  public void makeVisible() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("This view not support this operation");
  }
}
