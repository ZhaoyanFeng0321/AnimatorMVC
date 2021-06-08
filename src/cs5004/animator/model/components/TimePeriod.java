package cs5004.animator.model.components;

/**
 * This is the class of time period. It consists of the start and end units to represent a time
 * period.
 */
public final class TimePeriod implements Comparable<TimePeriod> {
  private final int start;
  private final int end;
  private final boolean valid;

  /**
   * create a TimePeriod with start and end tick.
   * @param start start tick
   * @param end   end tick
   * @throws IllegalArgumentException if tick is not positive
   */
  public TimePeriod(int start, int end) throws IllegalArgumentException {
    if (start < 0 || end < 0 || start > end) {
      throw new IllegalArgumentException("invalid timeline");
    }
    this.start = start;
    this.end = end;
    this.valid = true;
  }

  /**
   * create a 'empty' TimePeriod represent 'not happen'.
   */
  public TimePeriod() {
    this.start = 0;
    this.end = 0;
    this.valid = false;
  }

  /**
   * Check is this is a valid time period.
   * @return whether is valid time period
   */
  public boolean isValid() {
    return valid;
  }

  /**
   * returns the duration of this time period.
   * @return the duration of this time period
   */
  public int duration() {
    return end - start;
  }

  /**
   * return the starting time.
   * @return the starting time
   */
  public int getStart() {
    return start;
  }

  /**
   * return the ending time.
   * @return the ending time
   */
  public int getEnd() {
    return end;
  }

  /**
   * return true if the given time period overlaps with this one.
   * @param d other time period
   * @return true if overlaps
   */
  public boolean isOverlap(TimePeriod d) {
    if (d == null) {
      throw new IllegalArgumentException("invalid parameter provided");
    }
    return (d.end > start && d.end < end) || (d.start > start && d.start < end)
        || (d.start < start && d.end > end) || (d.start > start && d.end < end);
  }

  @Override
  public String toString() {
    return String.format("from t=%ds to t=%ds", start, end);
  }

  // compare the start unit, if same, compare end unit
  @Override
  public int compareTo(TimePeriod t) {
    if (t == null) {
      throw new IllegalArgumentException("invalid parameter provided");
    }
    if (start < t.start || (start == t.start && end < t.end)) {
      return -1;
    } else if (start > t.start || end > t.end) {
      return 1;
    } else {
      return 0;
    }
  }
}
