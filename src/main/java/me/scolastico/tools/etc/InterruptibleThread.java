package me.scolastico.tools.etc;

import java.util.concurrent.Callable;
import me.scolastico.tools.handler.ErrorHandler;

public class InterruptibleThread implements Runnable {
  private final Thread thread;
  private final Callable<Boolean> callable;

  public InterruptibleThread(Callable<Boolean> callable, boolean daemon) {
    this.callable = callable;
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    try {
      while (!thread.isInterrupted()) {
        if (!callable.call()) break;
      }
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }

  public Thread getThread() {
    return thread;
  }
}
