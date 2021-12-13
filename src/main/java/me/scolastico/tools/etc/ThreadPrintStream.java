package me.scolastico.tools.etc;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * A ThreadPrintStream replaces the normal System.out and ensures
 * that output to System.out goes to a different PrintStream for
 * each thread.  It does this by using ThreadLocal to maintain a
 * PrintStream for each thread.
 * @author https://stackoverflow.com/a/15526391/10536157
 */
public class ThreadPrintStream extends PrintStream {

  private static PrintStream defaultOut = null;

  public static PrintStream getDefaultOut() {
    return defaultOut;
  }

  public static void setDefaultOut(PrintStream defaultOut) {
    ThreadPrintStream.defaultOut = defaultOut;
  }

  /**
   * Changes System.out to a ThreadPrintStream which will
   * send output to a separate file for each thread.
   */
  public static void replaceSystemOut() {
    PrintStream console = System.out;
    ThreadPrintStream threadOut = new ThreadPrintStream();
    System.setOut(threadOut);
    threadOut.setThreadOut(console);
  }

  /**
   * Thread specific storage to hold a PrintStream for each thread
   */
  private ThreadLocal<PrintStream> out;

  private ThreadPrintStream() {
    super(new ByteArrayOutputStream(0));
    out = new ThreadLocal<PrintStream>();
  }

  /**
   * Sets the PrintStream for the currently executing thread.
   */
  public void setThreadOut(PrintStream out) {
    this.out.set(out);
  }

  /**
   * Returns the PrintStream for the currently executing thread.
   */
  public PrintStream getThreadOut() {
    PrintStream ret = this.out.get();
    if (ret == null) {
      ret = defaultOut;
    }
    return ret;
  }

  @Override public boolean checkError() {
    return getThreadOut().checkError();
  }

  @Override public void write(byte[] buf, int off, int len) {
    getThreadOut().write(buf, off, len);
  }

  @Override public void write(int b) {
    getThreadOut().write(b);
  }

  @Override public void flush() {
    getThreadOut().flush();
  }

  @Override public void close() {
    getThreadOut().close();
  }
}
