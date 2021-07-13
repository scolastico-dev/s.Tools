package me.scolastico.tools.console;

import static org.fusesource.jansi.Ansi.ansi;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * ConsoleManager for easy creating of console application which looks like a normal console window.
 * Also protects input from being overwritten by the output.
 * Only works with LINUX and WINDOWS. Known problems with IDE consoles! https://go.scolasti.co/ansiproblems
 */
public class ConsoleManager {

  private static boolean enabled = false;
  private static ArrayList<String> lastOutput = new ArrayList<>();
  private static int storeLineNumber = 1024;
  private static PrintStream defaultStream = null;
  private static Thread outputThread = null;
  private static Thread inputThread = null;
  private static PipedInputStream in = null;
  private static String prefix = "> ";
  private static final HashMap<String, Callable<Integer>> commands = new HashMap<>();
  private static final char backspace = (char) 8;
  private static Terminal terminal = null;
  private static boolean registerDefaults = true;
  private static StringBuilder currentInputLine = new StringBuilder();
  private static String notFoundMessage = "Command '%' not found! Try 'list-commands' to get a list of all commands!";
  private static boolean appendTime = true;
  private static ArrayList<ConsolePreOutputModificatorInterface> preOutputModifyList = new ArrayList<>();

  /**
   * Stop the ConsoleManager. This function also resets the most internal values including the last output array.
   * @throws InterruptedException InterruptedException from Thread.sleep() while waiting for the thread shutdown.
   * @throws TimeoutException If the threads are not stopping this exception will be triggered after 10 seconds.
   * @throws IOException IOExceptions from the System.out and System.in read/write actions.
   */
  public static synchronized void disable() throws InterruptedException, TimeoutException, IOException {
    if (enabled) {

      enabled = false;
      System.setOut(defaultStream);
      AnsiConsole.systemUninstall();

      int timeOut = 0;
      while (
          outputThread != null &&
          inputThread != null &&
          outputThread.isAlive() &&
          inputThread.isAlive()
      ) {
        Thread.sleep(50);
        timeOut++;
        if (timeOut >= 200) {
          throw new TimeoutException("The output and input threads are still running and not shutting down.");
        }
      }

      terminal.close();
      lastOutput = new ArrayList<>();

    }
  }

  /**
   * Start the ConsoleManager with daemon threads.
   * @throws IOException IOExceptions from the System.out and System.in read/write actions.
   */
  public static void enable() throws IOException {
    enable(true);
  }

  /**
   * Start the ConsoleManager.
   * @param daemon Start the threads as daemon's? Default is true.
   * @throws IOException IOExceptions from the System.out and System.in read/write actions.
   */
  public static synchronized void enable(boolean daemon) throws IOException {
    if (!enabled && !ConsoleLoadingAnimation.isEnabled()) {

      if (registerDefaults) {
        if (!commands.containsKey("list-commands")) {
          registerCommand(new ListCommandsCommand());
        }
        if (!commands.containsKey("exit")) {
          registerCommand(new ExitCommand());
        }
      }

      enabled = true;

      if (!AnsiConsole.isInstalled()) AnsiConsole.systemInstall();
      defaultStream = System.out;
      PipedOutputStream out = new PipedOutputStream();
      in = new PipedInputStream(out, 2048);

      System.setOut(null);
      terminal = TerminalBuilder.builder().jna(true).system(true).build();
      terminal.enterRawMode();
      System.setOut(new PrintStream(out));

      defaultStream.print(ansi().bold().a(prefix).reset());

      outputThread = new Thread(new Runnable() {
        @Override
        public void run() {
          Scanner scanner = new Scanner(in);
          while (enabled) {
            String output = scanner.nextLine();
            for (ConsolePreOutputModificatorInterface modificator:preOutputModifyList) {
              output = modificator.modifyOutput(output);
            }
            if (appendTime) output = "[" + getTimeString() + "] " + output;
            defaultStream.println();
            defaultStream.print(ansi().cursorUpLine().a("\033[1L").a(output).cursorDownLine().a(prefix).a(currentInputLine.toString()));
            lastOutput.add(output);
            while (lastOutput.size() > storeLineNumber) {
              lastOutput.remove(0);
            }
          }
        }
      });

      inputThread = new Thread(new Runnable() {
        @Override
        public void run() {
          while (enabled) {
            try {
              char c = (char) terminal.reader().read();
              if (!String.valueOf(c).matches(".")) {
                ArrayList<String> args = new ArrayList<>(Arrays.asList(currentInputLine.toString().split(" ")));
                String commandName = args.get(0);
                args.remove(0);
                defaultStream.println();
                defaultStream.print(prefix);
                if (commands.containsKey(commandName)) {
                  int result;
                  if (args.size() > 0) {
                    result = new CommandLine(commands.get(commandName).getClass()).execute(args.toArray(new String[0]));
                  } else {
                    result = new CommandLine(commands.get(commandName).getClass()).execute();
                  }
                } else if (notFoundMessage != null) {
                  System.out.println(ansi().fgRed().a(notFoundMessage.replaceAll("%", commandName)).reset());
                }
                currentInputLine = new StringBuilder();
              } else {
                int currentLength = prefix.length()+currentInputLine.length();
                if (c == backspace) {
                  if (currentInputLine.length() > 0) {
                    currentInputLine.deleteCharAt(currentInputLine.length()-1);
                    defaultStream.print(backspace + " ");
                  }
                } else {
                  currentInputLine.append(c);
                }
                defaultStream.print(Character.toString(backspace).repeat(currentLength) + prefix + currentInputLine.toString());
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      });

      outputThread.setDaemon(daemon);
      outputThread.start();

      inputThread.setDaemon(daemon);
      inputThread.start();
    }
  }

  /**
   * Execute a command manually.
   * @param args Arguments with command at position 0.
   * @return Status code from command or 404 if command not found.
   */
  public int executeCommand(String[] args) {
    ArrayList<String> argsList = new ArrayList<>(Arrays.asList(currentInputLine.toString().split(" ")));
    String command = argsList.get(0);
    argsList.remove(0);
    if (argsList.size() > 0) {
      return executeCommand(command, argsList.toArray(new String[0]));
    } else {
      return executeCommand(command, new String[]{});
    }
  }

  /**
   * Execute a command manually.
   * @param command The command name.
   * @param args Arguments for the command.
   * @return Status code from command or 404 if command not found.
   */
  public int executeCommand(String command, String[] args) {
    boolean isAnsiInstalled = AnsiConsole.isInstalled();
    if (!isAnsiInstalled) AnsiConsole.systemInstall();
    if (commands.containsKey(command)) {
      int tmp = new CommandLine(commands.get(command).getClass()).execute(args);
      if (!isAnsiInstalled) AnsiConsole.systemUninstall();
      return tmp;
    } else if (notFoundMessage != null) {
      System.out.println(ansi().fgRed().a(notFoundMessage.replaceAll("%", command)).reset());
    }
    if (!isAnsiInstalled) AnsiConsole.systemUninstall();
    return 404;
  }

  /**
   * Is the ConsoleManager enabled?
   * @return True if the ConsoleManager is enabled.
   */
  public static boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the last outputted lines.
   * @return The last lines from System.out.
   */
  public static String[] getLastOutput() {
    return lastOutput.toArray(new String[0]);
  }

  /**
   * Set how much lines from the output should be stored in an string array. If the max is reached old lines will be overwritten.
   * @param storeLineNumber Default is 1024.
   */
  public static void setStoreLineNumber(int storeLineNumber) {
    ConsoleManager.storeLineNumber = storeLineNumber;
  }

  /**
   * Should the default commands are registered if no command with the same name is already registered?
   * @param registerDefaults Default is true.
   */
  public static void setRegisterDefaults(boolean registerDefaults) {
    ConsoleManager.registerDefaults = registerDefaults;
  }

  /**
   * Set the prefix for the input.
   * @param prefix Default is "&gt; "
   */
  public static void setPrefix(String prefix) {
    ConsoleManager.prefix = prefix;
  }

  /**
   * If an command is not found will respond with this message. To disable set to NULL.
   * @param notFoundMessage Default is "Command '%' not found! Try 'list-commands' to get a list of all commands!".
   */
  public static void setNotFoundMessage(String notFoundMessage) {
    ConsoleManager.notFoundMessage = notFoundMessage;
  }

  /**
   * Should the current time stamp be added before an output? Will use "yyyy/MM/dd HH:mm:ss" format.
   * @param appendTime Default is true.
   */
  public static void setAppendTime(boolean appendTime) {
    ConsoleManager.appendTime = appendTime;
  }

  /**
   * Register a command. If the same command is registered twice it will overwrite the old one.
   * The command will be defined though the Command annotation ("name" value).
   * This guide show you how to create a command: https://picocli.info/quick-guide.html
   * @param command Picocli callable object which will be registered as a valid command. All picocli features are available.
   */
  public static void registerCommand(Callable<Integer> command) {
    String name = command.getClass().getAnnotation(Command.class).name();
    assert !name.isEmpty();
    commands.remove(name);
    commands.put(name, command);
  }

  /**
   * Default command for listing all available commands.
   * Will be added automatically if registerDefaults is true.
   */
  @Command(name = "list-commands", description = "List all available commands.", version = "1.0.0")
  public static class ListCommandsCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      System.out.println("Available commands:");
      for (String command : commands.keySet()) {
        System.out.println(command);
      }
      return 0;
    }
  }

  /**
   * Default command for exiting the software through System.exit(0).
   * Will be added automatically if registerDefaults is true.
   */
  @Command(name = "exit", mixinStandardHelpOptions = true, description = "Exit the software.")
  public static class ExitCommand implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
      System.exit(0);
      return 0;
    }
  }

  /**
   * Add a console pre output modificator to change the output before its send to the console.
   * @param modificator The modificator to register.
   */
  public static void registerPreOutputModificator(ConsolePreOutputModificatorInterface modificator) {
    preOutputModifyList.add(modificator);
  }

  private static String getTimeString() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }

}
