package me.scolastico.tools.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeoutException;
import org.fusesource.jansi.AnsiConsole;

/**
 * A simple function to create a loading animation in the console.
 * Useful while startup routines to show a user that the software is still working.
 * <b>Cant be used with ConsoleManager together.</b>
 */
public class ConsoleLoadingAnimation {

  private static boolean enabled = false;
  private static Thread thread = null;
  private static char[] animation = {'/','-','\\','|'};
  private static int step = 0;
  private static int speed = 25;
  private static PrintStream defaultStream;
  private static boolean takenOverOutputStream = false;

  /**
   * Enable the loading animation and take over print stream to prevent output while loading.
   * <b>Cant be used with ConsoleManager together.</b>
   */
  public static void enable() {
    enable(true);
  }

  /**
   * Enable the loading animation.
   * <b>Cant be used with ConsoleManager together.</b>
   * @param takeOverSystemOutputStream Take over the output stream from System.out to prevent output while showing loading animation?
   */
  public static synchronized void enable(boolean takeOverSystemOutputStream) {
    if (!enabled && !ConsoleManager.isEnabled()) {
      defaultStream = System.out;
      takenOverOutputStream = takeOverSystemOutputStream;
      if (takeOverSystemOutputStream) System.setOut(new PrintStream(new ByteArrayOutputStream()));
      if (!AnsiConsole.isInstalled()) AnsiConsole.systemInstall();
      enabled = true;
      thread = new Thread(new Runnable() {
        @Override
        public void run() {
          int currentSpeedStep = 0;
          if (System.out != null) defaultStream.print(animation[0]);
          while (enabled) {
            try {
              if (currentSpeedStep >= speed) {
                currentSpeedStep = 0;
                defaultStream.print((char) 8);
                defaultStream.print(animation[step]);
                step++;
                if (step >= animation.length) step = 0;
              }
              Thread.sleep(10);
              currentSpeedStep++;
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      });
      thread.setDaemon(true);
      thread.start();
    }
  }

  /**
   * Disable and remove the loading animation.
   * @throws InterruptedException InterruptedException from Thread.sleep() while waiting for the thread shutdown.
   * @throws TimeoutException If the threads are not stopping this exception will be triggered after 10 seconds.
   */
  public static synchronized void disable() throws InterruptedException, TimeoutException {
    if (enabled) {
      enabled = false;
      if (takenOverOutputStream) System.setOut(defaultStream);
      int timeOut = 0;
      while (thread != null && thread.isAlive()) {
        Thread.sleep(50);
        timeOut++;
        if (timeOut >= 200) {
          throw new TimeoutException("The output and input threads are still running and not shutting down.");
        }
      }
      defaultStream.print((char) 8);
    }
  }

  /**
   * The animation chars in the correct order. Will also reset the animation to start at the beginning.
   * @param animation Default is {'⣾','⣽','⣻','⢿','⡿','⣟','⣯','⣷'}.
   */
  public static void setAnimation(char[] animation) {
    ConsoleLoadingAnimation.animation = animation;
    step = 0;
  }

  /**
   * The speed of the animation. Higher is slower.
   * @param speed Default is 25.
   */
  public static void setSpeed(int speed) {
    ConsoleLoadingAnimation.speed = speed;
  }

  /**
   * Is the animation enabled?
   * @return Returns true if the animation is enabled.
   */
  public static boolean isEnabled() {
    return enabled;
  }

}
