package me.scolastico.tools.handler;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import me.scolastico.tools.dataholder.SchedulerConfiguration;

/**
 * SchedulerHandler for easy scheduling of tasks!
 */
public class SchedulerHandler extends TimerTask {

  private static long counter = 0;
  private static final HashMap<Long, SchedulerConfiguration> configurations = new HashMap<>();
  private static final Timer timer = new Timer();
  private static boolean enabled = false;

  /**
   * Enable the SchedulerHandler.
   */
  public static void enable() {
    if (!enabled) {
      enabled = true;
      timer.scheduleAtFixedRate(new SchedulerHandler(), 50, 50);
    }
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

  @Override
  public void run() {
    for (SchedulerConfiguration configuration:configurations.values()) {
      configuration.executeTick();
    }
  }

  private SchedulerHandler() {}

}
