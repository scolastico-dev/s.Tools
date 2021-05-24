package me.scolastico.tools.dataholder;

/**
 * The SchedulerConfiguration for the SchedulerHandler.
 */
public class SchedulerConfiguration {

  private final long executeEveryTick;
  private final boolean runOnce;
  private final Runnable runnable;
  private long lastExecute = 0;

  /**
   * Create a new SchedulerConfiguration for multiple runs.
   * @param executeEveryTick Execute on every X tick. (20 ticks are 1 second!)
   * @param runnable The runnable to be executed if the scheduler ticks.
   */
  public SchedulerConfiguration(long executeEveryTick, Runnable runnable) {
    this.executeEveryTick = executeEveryTick;
    this.runOnce = false;
    this.runnable = runnable;
  }

  /**
   * Create a new SchedulerConfiguration
   * @param executeEveryTick Execute on every X tick. (20 ticks are 1 second!)
   * @param runOnce Run only once?
   * @param runnable The runnable to be executed if the scheduler ticks.
   */
  public SchedulerConfiguration(long executeEveryTick, boolean runOnce, Runnable runnable) {
    this.executeEveryTick = executeEveryTick;
    this.runOnce = runOnce;
    this.runnable = runnable;
  }

  /**
   * Its not recommended to use this function manually.
   * The function will be automatically called every tick
   * from the SchedulerManager.
   */
  public void executeTick() {
    if (lastExecute >= executeEveryTick) {
      lastExecute = 0;
      runnable.run();
    }
  }

}
