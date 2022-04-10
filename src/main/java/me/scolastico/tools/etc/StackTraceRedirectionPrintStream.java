package me.scolastico.tools.etc;

import java.io.PrintStream;
import org.jetbrains.annotations.NotNull;

public class StackTraceRedirectionPrintStream extends PrintStream {

  private final PrintStream normalPrintStream;
  private final PrintStream redirectedPrintStream;
  private final String lookForClassName;

  public StackTraceRedirectionPrintStream(@NotNull PrintStream normalPrintStream, @NotNull PrintStream redirectedPrintStream, @NotNull String lookForClassName) {
    super(normalPrintStream);
    this.normalPrintStream = normalPrintStream;
    this.redirectedPrintStream = redirectedPrintStream;
    this.lookForClassName = lookForClassName;
  }

  @Override public boolean checkError() {
    if (shouldBeRedirected()) {
      return redirectedPrintStream.checkError();
    } else {
      return normalPrintStream.checkError();
    }
  }

  @Override public void write(@NotNull byte[] buf, int off, int len) {
    if (shouldBeRedirected()) {
      redirectedPrintStream.write(buf, off, len);
    } else {
      normalPrintStream.write(buf, off, len);
    }
  }

  @Override public void write(int b) {
    if (shouldBeRedirected()) {
      redirectedPrintStream.write(b);
    } else {
      normalPrintStream.write(b);
    }
  }

  @Override public void flush() {
    if (shouldBeRedirected()) {
      redirectedPrintStream.flush();
    } else {
      normalPrintStream.flush();
    }
  }

  @Override public void close() {
    redirectedPrintStream.close();
    normalPrintStream.close();
  }

  private boolean shouldBeRedirected() {
    for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
      if (element.getClassName().equals(lookForClassName)) {
        return true;
      }
    }
    return false;
  }

}
