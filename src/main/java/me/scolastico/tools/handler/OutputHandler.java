package me.scolastico.tools.handler;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import me.scolastico.tools.dataholder.SchedulerConfiguration;

/**
 * The output handler is to handle System.out.print in the
 * way so you can save all outputs to a string and read it!
 * @deprecated Moved this function to the new ConsoleManager.
 */
@Deprecated
public class OutputHandler {

  private static ByteArrayOutputStream out = null;
  private static PrintStream defaultStream = null;
  private static Long schedulerId = null;
  private static int storeRows = 1024;
  private static final ArrayList<String> lastCalls = new ArrayList<>();
  private static final SchedulerConfiguration scheduler = new SchedulerConfiguration(1, new Runnable() {
    @Override
    public void run() {
      checkOutput();
    }
  });

  /**
   * To enable the output recorder call this function!
   */
  public static void enable() {
    if (out == null) {
      out = new ByteArrayOutputStream();
      defaultStream = System.out;
      System.setOut(new PrintStream(out));
      schedulerId = SchedulerHandler.registerTask(scheduler);
    }
  }

  /**
   * To disable the output recorder call this function!
   */
  public static void disable() {
    if (out != null) {
      SchedulerHandler.removeConfiguration(schedulerId);
      checkOutput();
      System.setOut(defaultStream);
      defaultStream = null;
      out = null;
    }
  }

  /**
   * Get the last 1024 lines from the output.
   * @return Array list with the last 1024 calls from System.out.print!
   */
  public static String[] getOutput() {
    if (out != null) {
      checkOutput();
    }
    return lastCalls.toArray(new String[0]);
  }

  /**
   * Get the current configuration of how much rows should be stored. Default is 1024
   * @return How much rows can be stored before the oldest will be deleted for making more space.
   */
  public static int getStoreRows() {
    return storeRows;
  }

  /**
   * Set how much rows should be stored. Default is 1024.
   * @param storeRows How much rows can be stored before the oldest will be deleted for making more space.
   */
  public static void setStoreRows(int storeRows) {
    OutputHandler.storeRows = storeRows;
  }

  /**
   * Clear the output and remove all lines.
   */
  public static void clearOutput() {
    lastCalls.clear();
  }

  private static void checkOutput() {
    String output = out.toString();
    if (!output.isEmpty()) {
      defaultStream.print(output);
      out = new ByteArrayOutputStream();
      System.setOut(new PrintStream(out));
      String[] lines = output.split("\r\n|\r|\n");
      lastCalls.addAll(Arrays.asList(lines));
      while (lastCalls.size() > storeRows) {
        lastCalls.remove(0);
      }
    }
  }

}
