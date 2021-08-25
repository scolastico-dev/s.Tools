package me.scolastico.tools.routine;

import java.util.HashMap;
import javax.validation.constraints.NotNull;

/**
 * The routine interface which is needed for all routine jobs.
 */
public interface Routine {

  /**
   * The maim execute function for this routine job.
   * @param objectMap The object map from the answer of the last run. If its the first run its a new and empty HashMap.
   * @return RoutineAnswer which cant be NULL. To create an empty answer do this: <code>new RoutineAnswer();</code>
   * @throws Exception Exceptions will be handled by the ErrorHandler.
   */
  @NotNull RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception;

}
