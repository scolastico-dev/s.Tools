package me.scolastico.tools.pairs;

/**
 * A generic data holder for two data types.
 * @param <X> Type one you want to store.
 * @param <Y> Type two you want to store.
 */
public class Pair<X, Y> {

  private final X x;
  private final Y y;

  /**
   * Construct a new pair with two data types.
   * @param x Value for data type one.
   * @param y Value for data type two.
   */
  public Pair(X x, Y y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Get the value of the data with the type one.
   * @return data one
   */
  public X getX() {
    return x;
  }

  /**
   * Get the value of the data with the type two.
   * @return data two
   */
  public Y getY() {
    return y;
  }

}
