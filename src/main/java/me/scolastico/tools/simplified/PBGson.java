package me.scolastico.tools.simplified;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Pre- Build Gson
 * is an object holder for some gson objects which are often build.
 */
public class PBGson {

  private static final Gson prettyPrint = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
  private static final Gson small = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
  private static final Gson smallNoNull = new GsonBuilder().disableHtmlEscaping().create();

  /**
   * <pre>
   * Returns a pre- build gson object with the settings:
   *  - Pretty printing
   *  - Disabled html escaping
   *  - Serializing nulls
   * </pre>
   * @return The pre- build gson object.
   */
  public static Gson getPrettyPrint() {
    return prettyPrint;
  }

  /**
   * Short version of getPrettyPrint.
   * @return The pre- build gson object.
   */
  public static Gson pp() {
    return prettyPrint;
  }

  /**
   * <pre>
   * Returns a pre- build gson object with the settings:
   *  - Disabled html escaping
   *  - Serializing nulls
   * </pre>
   * @return The pre- build gson object.
   */
  public static Gson getSmall() {
    return small;
  }

  /**
   * Short version of getSmall.
   * @return The pre- build gson object.
   */
  public static Gson s() {
    return small;
  }

  /**
   * <pre>
   * Returns a pre- build gson object with the settings:
   *  - Disabled html escaping
   * </pre>
   * @return The pre- build gson object.
   */
  public static Gson getSmallNoNull() {
    return smallNoNull;
  }

  /**
   * Short version of getSmallNoNull.
   * @return The pre- build gson object.
   */
  public static Gson snn() {
    return smallNoNull;
  }

}
