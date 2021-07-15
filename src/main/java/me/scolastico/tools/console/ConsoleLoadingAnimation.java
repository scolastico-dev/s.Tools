package me.scolastico.tools.console;

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
  private static char[] animation = {'⣾','⣽','⣻','⢿','⡿','⣟','⣯','⣷'};
  private static int step = 0;
  private static int speed = 25;

  /**
   * Enable the loading animation.
   * <b>Cant be used with ConsoleManager together.</b>
   */
  public static synchronized void enable() {
    if (!enabled && !ConsoleManager.isEnabled()) {
      if (!AnsiConsole.isInstalled()) AnsiConsole.systemInstall();
      enabled = true;
      thread = new Thread(new Runnable() {
        @Override
        public void run() {
          int currentSpeedStep = 0;
          System.out.print(animation[0]);
          while (enabled) {
            try {
              if (currentSpeedStep >= speed) {
                currentSpeedStep = 0;
                System.out.print((char) 8);
                System.out.print(animation[step]);
                step++;
                if (step >= animation.length) step = 0;
              }
              Thread.sleep(10);
              currentSpeedStep++;
            } catch (InterruptedException e) {
              e.printStackTrace();
            } catch (NullPointerException ignored) {}
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
      int timeOut = 0;
      while (thread != null && thread.isAlive()) {
        Thread.sleep(50);
        timeOut++;
        if (timeOut >= 200) {
          throw new TimeoutException("The output and input threads are still running and not shutting down.");
        }
      }
      System.out.print((char) 8);
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
