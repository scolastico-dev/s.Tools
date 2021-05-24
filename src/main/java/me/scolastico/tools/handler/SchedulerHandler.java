package me.scolastico.tools.handler;

import java.util.HashMap;
import me.scolastico.tools.dataholder.SchedulerConfiguration;

/**
 * SchedulerHandler for easy scheduling of tasks!
 */
public class SchedulerHandler {

  private static long counter = 0;
  private static final HashMap<Long, SchedulerConfiguration> configurations = new HashMap<>();

  /**
   * Enable the SchedulerHandler.
   */
  private static void enable() {

  }

  /**
   * Register your SchedulerConfiguration and enable it for automatic scheduling.
   * @param configuration Your SchedulerConfiguration to register
   * @return The id of the configuration to remove it again if wanted.
   */
  public static long registerTask(SchedulerConfiguration configuration) {
    counter++;
    configurations.put(counter, configuration);
    return counter;
  }

  /**
   * Remove a SchedulerConfiguration so it wont run again.
   * @param id The id of the ScheduleConfiguration to remove.
   */
  public static void removeConfiguration(long id) {
    configurations.remove(id);
  }

}
