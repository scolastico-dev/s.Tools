package me.scolastico.tools.console;

public class ConsoleTestApplication {

  public static void main(String[] args) throws Exception {

    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            System.out.println("test");
            Thread.sleep(10000);
          } catch (InterruptedException e) {}
        }
      }
    }).start();

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {}
    ConsoleManager.enable(false, true);

  }

}
