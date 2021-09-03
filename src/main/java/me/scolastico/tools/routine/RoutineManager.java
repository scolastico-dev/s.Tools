package me.scolastico.tools.routine;

import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.handler.ErrorHandler;

/**
 * With the routine manager you can divide long functions into sections which can then also be executed asynchronously.
 * The functions and variables are actually self-explanatory.
 */
public class RoutineManager {

  private final List<Routine> routineList;
  private Routine canceledRoutine = null;
  private boolean running = false;
  private boolean started = false;
  private boolean canceled = false;
  private Thread thread = null;
  private String errorMessage = null;
  private Class<?> lastExecutedClass = null;
  private HashMap<String, Object> lastObjectMap = null;

  public RoutineManager(List<Routine> routineList) {
    this.routineList = routineList;
  }

  public RoutineManager(List<Routine> routineList, Routine canceledRoutine) {
    this.routineList = routineList;
    this.canceledRoutine = canceledRoutine;
  }

  private synchronized void execute(HashMap<String, Object> objectMap) {
    running = true;
    try {
      Routine[] skippedRoutine = null;
      for (Routine routine : routineList) {
        if (skippedRoutine != null) {
          boolean tmp = false;
          for (Routine skipped : skippedRoutine) {
            if (routine.getClass().equals(skipped.getClass())) {
              tmp = true;
              break;
            }
          }
          if (tmp) continue;
        }
        lastExecutedClass = routine.getClass();
        RoutineAnswer answer = routine.execute(objectMap);
        if (answer.isStop()) {
          errorMessage = answer.getErrorMessage();
          if (canceledRoutine != null) {
            lastExecutedClass = canceledRoutine.getClass();
            canceledRoutine.execute(objectMap);
          }
          break;
        }
        lastObjectMap = answer.getObjectMap();
        objectMap = answer.getObjectMap();
        skippedRoutine = answer.getSkippedRoutine();
        Thread.sleep(10);
      }
    } catch (Exception e) {
      if (!(e instanceof InterruptedException)) {
        ErrorHandler.handle(e);
      }
    }
    running = false;
  }

  public void start() {
    start(new HashMap<>());
  }

  public void start(HashMap<String, Object> objectMap) {
    if (!running) {
      thread = new Thread(new Runnable() {
        @Override
        public void run() {
          startNotAsynchronously(objectMap);
        }
      });
      thread.start();
    }
  }

  public void startNotAsynchronously() {
    startNotAsynchronously(new HashMap<>());
  }

  public void startNotAsynchronously(HashMap<String, Object> objectMap) {
    if (!running) {
      started = true;
      execute(objectMap);
    }
  }

  public void stop() {
    if (running) {
      thread.interrupt();
      canceled = true;
      running = false;
    }
  }

  public HashMap<String, Object> getLastObjectMap() {
    return lastObjectMap;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public boolean isRunning() {
    return running;
  }

  public boolean isStarted() {
    return started;
  }

  public boolean isCanceled() {
    return canceled;
  }

  public Class<?> getLastExecutedClass() {
    return lastExecutedClass;
  }

}
