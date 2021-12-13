package me.scolastico.tools.console;

import me.scolastico.tools.handler.ErrorHandler;

public class ConsoleTestApplication {

  public static void main(String[] args) throws Exception {

    System.out.println("starting");

    ErrorHandler.enableErrorLogFile();

    ConsoleManager.enableWithLanterna(() -> {
      while (true) {
        try {
          System.out.println("test1");
          Thread.sleep(10);
        } catch (InterruptedException ignored) {
          break;
        }
      }
    });

    System.out.println("ending");

  }

}
