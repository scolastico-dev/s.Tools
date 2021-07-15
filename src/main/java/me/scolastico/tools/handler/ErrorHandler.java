package me.scolastico.tools.handler;

import io.sentry.Sentry;
import io.sentry.SentryEvent;
import io.sentry.SentryLevel;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;

/**
 * Handle your errors automatically.
 * Send them to sentry, output them to the console and/or write them to the error log!
 */
public class ErrorHandler implements UncaughtExceptionHandler {

  private static boolean sentry = false;
  private static File errorLog = null;
  private static boolean createdNewLog = false;

  /**
   * Its not recommended to use the instance.
   * Use the static functions!
   */
  public ErrorHandler() {}

  /**
   * To enable sentry reporting past the sentry dns string to this function!
   * @param sentryDns The sentry dns string for error reporting to sentry.io!
   */
  public static void enableSentry(String sentryDns) {
    enableSentry(sentryDns, 1.0, null);
  }

  /**
   * To enable sentry reporting past the sentry dns string to this function!
   * @param sentryDns The sentry dns string for error reporting to sentry.io!
   * @param tracesSampleRate https://docs.sentry.io/platforms/java/configuration/sampling/
   * @param release https://docs.sentry.io/platforms/java/configuration/releases/
   */
  public static void enableSentry(String sentryDns, double tracesSampleRate, String release) {
    if (!sentry) {
      Sentry.init(options -> {
        options.setDsn(sentryDns);
        options.setTracesSampleRate(tracesSampleRate);
        if (release != null) options.setRelease(release);
      });
      sentry = true;
    }
  }

  /**
   * Enable the exception handling for uncaught exceptions.
   * Uncaught exceptions will trigger a fatal exception!
   */
  public static void enableCatchUncaughtException() {
    Thread.setDefaultUncaughtExceptionHandler(new ErrorHandler());
  }

  /**
   * Enable the 'error.log' file to which the stack traces will also be written.
   */
  public static void enableErrorLogFile() {
    try {
      errorLog = new File("error.log");
      if (errorLog.exists()) {
        int counter = 2;
        File destination = new File("error." + counter + ".log");
        while (destination.exists()) {
          counter++;
          destination = new File("error." + counter + ".log");
        }
        FileUtils.moveFile(errorLog, destination);
      }
    } catch (Exception e) {
      handle(e);
    }
  }

  /**
   * Handle an exception.
   * @param e The exception to handle.
   */
  public static void handle(Throwable e) {
    handle(e, SentryLevel.ERROR);
  }

  /**
   * Handle an exception and send more data to Sentry.
   * @param e The exception to handle.
   * @param level The error level for Sentry.
   * @param debugInformation The debug infos.
   */
  public static void handle(Throwable e, SentryLevel level, String... debugInformation) {
    if (sentry) {
      SentryEvent event = new SentryEvent();
      event.setLevel(level);
      event.setThrowable(e);
      int i = 1;
      for (String info:debugInformation) {
        event.setExtra("Debug Information [" + i + "]", info);
        i++;
      }
    }
    StringWriter stringWriter = new StringWriter();
    e.printStackTrace(new PrintWriter(stringWriter));
    String exceptionAsString = stringWriter.toString();
    System.err.println("Message: " + e.getMessage());
    System.err.println("StackTrace:");
    System.err.println(exceptionAsString);
    if (errorLog != null) {
      try {
        if (!createdNewLog) {
          createdNewLog = true;
          FileUtils.writeStringToFile(errorLog, "Error log created at (UNIX) " + System.currentTimeMillis()/1000 + "\n\n", StandardCharsets.UTF_8, false);
        }
        FileUtils.writeStringToFile(
            errorLog,
            "Time (UNIX): " + System.currentTimeMillis()/1000 + "\nMessage: " + e.getMessage() + "\nStack Trace:\n" + exceptionAsString+"\n\n",
            StandardCharsets.UTF_8,
            true
        );
      } catch (IOException exception) {
        System.err.println("WARNING: Can't write stacktrace to the 'error.log' file!");
      }
    }
  }

  /**
   * Handle an exception and exit the program with exit a non zero exit code.
   * @param e The exception to handle.
   */
  public static void handleFatal(Throwable e) {
    handleFatal(e,1);
  }

  /**
   * Handle an exception and exit the program with exit a non zero exit code.
   * @param e The exception to handle.
   * @param exitCode The exit code to report.
   */
  public static void handleFatal(Throwable e, int exitCode) {
    handle(e);
    System.out.println();
    System.out.println("FATAL ERROR SHUTTING DOWN!");
    System.out.println();
    System.exit(exitCode);
  }

  /**
   * Internal function from the UncaughtExceptionHandler implementation.
   * @param t The current thread.
   * @param e The uncaught exception.
   */
  @Override
  public void uncaughtException(Thread t, Throwable e) {
    handleFatal(e);
  }

}
