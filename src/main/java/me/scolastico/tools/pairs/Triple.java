package me.scolastico.tools.pairs;

/**
 * A generic data holder for two data types.
 * @param <X> Type one you want to store.
 * @param <Y> Type two you want to store.
 * @param <Z> Type three you want to store.
 */
public class Triple<X, Y, Z> {

  private final X x;
  private final Y y;
  private final Z z;

  /**
   * Construct a new pair with two data types.
   * @param x Value for data type one.
   * @param y Value for data type two.
   * @param z Value for data type three.
   */
  public Triple(X x, Y y, Z z) {
    this.x = x;
    this.y = y;
    this.z = z;
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

  /**
   * Get the value of the data with the type three.
   * @return data three
   */
  public Z getZ() {
    return z;
  }

}
