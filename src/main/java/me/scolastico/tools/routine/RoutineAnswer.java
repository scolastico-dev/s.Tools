package me.scolastico.tools.routine;

import java.util.HashMap;

/**
 * A data holder for the routines module. Is required as a return value in the run function of a routine instance.
 */
public class RoutineAnswer {

  /**
   * The objectMap which is transmitted to the next function in the routine.
   *
   * Default is <code>new HashMap<String, Object>()</code>
   */
  private final HashMap<String, Object> objectMap;

  /**
   * Should the routine be canceled?
   *
   * Default is <code>false</code>
   */
  private final boolean stop;

  /**
   * The routines in this list are skipped until a routine is found that is not on the list.
   * This list is reset between the functions of the routine, so if a function was found that
   * is not in the list and after this function there is a function which was in this list, it is executed.
   *
   * Default is <code>null</code>
   */
  private final Routine[] skippedRoutine;

  /**
   * If the routine was canceled you can define here an error message.
   * Default is <code>null</code>
   */
  private final String errorMessage;

  /**
   * Create a routine answer.
   * The values that were not set were reset to their standard. (See description of the variables.)
   * @param objectMap The objectMap which is transmitted to the next function in the routine.
   */
  public RoutineAnswer(HashMap<String, Object> objectMap) {
    this.objectMap = objectMap;
    skippedRoutine = null;
    errorMessage = null;
    stop = false;
  }

  /**
   * Create a routine answer.
   * The values that were not set were reset to their standard. (See description of the variables.)
   * @param objectMap
   * @param skippedRoutine
   */
  public RoutineAnswer(HashMap<String, Object> objectMap, Routine[] skippedRoutine) {
    this.objectMap = objectMap;
    if (skippedRoutine.length > 0) {
      this.skippedRoutine = skippedRoutine;
    } else {
      this.skippedRoutine = null;
    }
    errorMessage = null;
    stop = false;
  }

  /**
   * Create a routine answer.
   * The values that were not set were reset to their standard. (See description of the variables.)
   * @param stop
   */
  public RoutineAnswer(boolean stop) {
    this.stop = stop;
    errorMessage = null;
    objectMap = new HashMap<>();
    skippedRoutine = null;
  }

  /**
   * Create a routine answer.
   * The values that were not set were reset to their standard. (See description of the variables.)
   * @param stop
   * @param errorMessage
   */
  public RoutineAnswer(boolean stop, String errorMessage) {
    this.stop = stop;
    this.errorMessage = errorMessage;
    objectMap = new HashMap<>();
    skippedRoutine = null;
  }

  /**
   * Create a routine answer.
   * The values will be set to their standard. (See description of the variables.)
   */
  public RoutineAnswer() {
    objectMap = new HashMap<>();
    skippedRoutine = null;
    errorMessage = null;
    stop = false;
  }

  public HashMap<String, Object> getObjectMap() {
    return objectMap;
  }

  public boolean isStop() {
    return stop;
  }

  public Routine[] getSkippedRoutine() {
    return skippedRoutine;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
