package me.scolastico.tools.console;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.scolastico.tools.console.commands.ClearScreenCommand;
import me.scolastico.tools.console.commands.ExitCommand;
import me.scolastico.tools.console.commands.HeadCommand;
import me.scolastico.tools.console.commands.HelpCommand;
import me.scolastico.tools.console.commands.StatusCommand;
import me.scolastico.tools.etc.StackTraceRedirectionPrintStream;
import me.scolastico.tools.etc.StringEventOutputStream;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineManager;
import org.fusesource.jansi.Ansi;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.console.impl.SystemRegistryImpl.UnknownCommandException;
import org.jline.keymap.KeyMap;
import org.jline.reader.Binding;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.Parser;
import org.jline.reader.Reference;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.widget.TailTipWidgets;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.UnmatchedArgumentException;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;

public class ConsoleManager {

  private static String INPUT_PREFIX = Ansi.ansi().fgGreen().a("prompt> ").reset().toString();
  private static int MAX_TAB_COMPLETE_SIZE = 50;
  private static Callable<String> OUTPUT_PREFIX = null;
  private static boolean OUTPUT_PREFIX_TIME = true;
  private static SystemRegistry SYSTEM_REGISTRY = null;
  private static CommandLine COMMAND_LINE = null;
  private static Terminal TERMINAL = null;
  private static final List<String> CONSOLE_LOG_LINES = new ArrayList<>();
  private static Integer MAX_CONSOLE_LOG_LINES = 4096;
  private static boolean ENABLED = false;
  private static LineReader READER = null;
  private static Consumer<Object> HELP_PAGE_RENDERER = new HelpCommand();
  private static final ArrayList<Routine> NEW_LOG_LINE_ROUTINES = new ArrayList<>();

  private static final List<Object> COMMANDS = new ArrayList<>(){{
    add(HELP_PAGE_RENDERER);
    add(new ExitCommand());
    add(new ClearScreenCommand());
    add(new StatusCommand());
  }};

  public static synchronized void enable() throws IOException {
    if (!ENABLED) {

      // Initialize CommandLine
      Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));
      HeadCommand commands = new HeadCommand();
      PicocliCommandsFactory factory = new PicocliCommandsFactory();
      COMMAND_LINE = new CommandLine(commands, factory);
      PicocliCommands picocliCommands = new PicocliCommands(COMMAND_LINE);
      Parser parser = new DefaultParser();
      TERMINAL = TerminalBuilder.builder().build();
      SYSTEM_REGISTRY = new SystemRegistryImpl(parser, TERMINAL, workDir, null);
      SYSTEM_REGISTRY.setCommandRegistries(picocliCommands);
      READER = LineReaderBuilder.builder()
          .terminal(TERMINAL)
          .completer(SYSTEM_REGISTRY.completer())
          .parser(parser)
          .variable(LineReader.LIST_MAX, MAX_TAB_COMPLETE_SIZE)
          .build();
      factory.setTerminal(TERMINAL);
      TailTipWidgets widgets = new TailTipWidgets(READER, SYSTEM_REGISTRY::commandDescription, 5, TailTipWidgets.TipType.COMPLETER);
      widgets.enable();
      KeyMap<Binding> keyMap = READER.getKeyMaps().get("main");
      keyMap.bind(new Reference("tailtip-toggle"), KeyMap.alt("s"));

      // Add commands
      for (Object o : COMMANDS) {
        COMMAND_LINE.addSubcommand(o);
      }

      // Generate new System.out
      StringEventOutputStream newOut = new StringEventOutputStream(new Consumer<>() {
        String currentLine = "";
        @Override
        public void accept(String s) {
          currentLine += s;
          if (currentLine.endsWith("\n") || currentLine.endsWith("\r") || currentLine.endsWith("\r\n")) {
            print(currentLine, READER);
            currentLine = "";
          }
        }
      });

      PrintStream inStream = new StackTraceRedirectionPrintStream(
          new PrintStream(newOut, true),
          System.out,
          "org.jline.reader.LineReader"
      );

      PrintStream errStream = new StackTraceRedirectionPrintStream(
          new PrintStream(newOut, true),
          System.err,
          "org.jline.reader.LineReader"
      );

      // Override PrintStreams
      System.setOut(inStream);
      System.setErr(errStream);

      new Thread(() -> {
        while (true) {
          try {
            runCommand(READER.readLine(INPUT_PREFIX, null, (MaskingCallback) null, null));
          } catch (UserInterruptException ignored) {
          } catch (EndOfFileException e) {
            System.exit(1);
            return;
          } catch (Exception e) {
            ErrorHandler.handleFatal(e);
          }
        }
      }).start();
      ENABLED = true;
    }
  }

  public static void runCommand(String command) throws Exception {
    if (SYSTEM_REGISTRY != null) {
      new Thread(() -> {
        try {
          if (
              command.equalsIgnoreCase("HELP")
                  || command.toUpperCase().startsWith("HELP ")
                  || command.equalsIgnoreCase("EXIT")
                  || command.toUpperCase().startsWith("EXIT ")
          ) {
            COMMAND_LINE.clearExecutionResults();
            COMMAND_LINE.execute(command.split(" "));
          } else {
            SYSTEM_REGISTRY.cleanUp();
            SYSTEM_REGISTRY.execute(command);
          }
        } catch (UserInterruptException ignored) {
        } catch (UnmatchedArgumentException | IllegalArgumentException e) {
          Object c = null;
          for (Object o : COMMANDS) {
            String cmd = command.split(" ")[0];
            Command annotation = o.getClass().getAnnotation(Command.class);
            if (annotation.name().equalsIgnoreCase(cmd)) {
              c = o;
              break;
            }
            for (String alias : annotation.aliases()) {
              if (alias.equalsIgnoreCase(cmd)) {
                c = o;
                break;
              }
            }
            if (c != null) break;
          }
          if (c == null) {
            ErrorHandler.handle(
                new InvalidClassException(
                    "Could not find the help page for the command '" + command + "'."
                )
            );
          } else {
            for (String line : HelpCommand.generateHelpPage(c)) print(line, READER);
          }
        } catch (UnknownCommandException e) {
          print(Ansi.ansi()
                  .fgRed()
                  .a("Command '")
                  .fgBrightRed()
                  .a(command)
                  .fgRed()
                  .a("' not found.")
                  .reset()
                  .toString(),
              READER
          );
        } catch (EndOfFileException e) {
          System.exit(1);
        } catch (Exception e) {
          ErrorHandler.handleFatal(e);
        }
      }, "Command Executer").start();
    } else {
      throw new IllegalAccessException("Cant be accessed before ConsoleManager is enabled!");
    }
  }

  public static void registerCommand(Object command) {
    if (COMMAND_LINE != null) COMMAND_LINE.addSubcommand(command);
    COMMANDS.add(command);
  }

  public static void registerNewLogLineRoutine(Routine routine) {
    NEW_LOG_LINE_ROUTINES.add(routine);
  }

  private static void print(String line, LineReader reader) {
    String prefix = "";
    if (OUTPUT_PREFIX_TIME) {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
      LocalDateTime now = LocalDateTime.now();
      prefix += Ansi.ansi().a("[").fgCyan().a(dtf.format(now)).reset().a("] ");
    }
    if (OUTPUT_PREFIX != null) {
      try {
        prefix += OUTPUT_PREFIX.call() + " ";
      } catch (Exception e) {
        ErrorHandler.handle(e);
      }
    }
    addLineToLog(prefix + line);
    reader.printAbove(prefix + line);
  }

  private static void addLineToLog(String line) {
    if (NEW_LOG_LINE_ROUTINES.size() > 0) {
      RoutineManager manager = new RoutineManager(NEW_LOG_LINE_ROUTINES);
      HashMap<String, Object> map = new HashMap<>();
      map.put("LINE", line);
      map.put("line", line);
      map.put("Line", line);
      manager.startNotAsynchronously(map);
    }
    CONSOLE_LOG_LINES.add(line);
    if (CONSOLE_LOG_LINES.size() > MAX_CONSOLE_LOG_LINES) {
      CONSOLE_LOG_LINES.remove(0);
    }
  }

  public static Consumer<Object> getHelpPageRenderer() {
    return HELP_PAGE_RENDERER;
  }

  public static void setHelpPageRenderer(Consumer<Object> helpPageRenderer) {
    HELP_PAGE_RENDERER = helpPageRenderer;
  }

  public static Object[] getCommands() {
    return COMMANDS.toArray(new Object[0]);
  }

  public static String getInputPrefix() {
    return INPUT_PREFIX;
  }

  public static void setInputPrefix(String inputPrefix) {
    INPUT_PREFIX = inputPrefix;
  }

  public static int getMaxTabCompleteSize() {
    return MAX_TAB_COMPLETE_SIZE;
  }

  public static void setMaxTabCompleteSize(int maxTabCompleteSize) {
    MAX_TAB_COMPLETE_SIZE = maxTabCompleteSize;
  }

  public static Callable<String> getOutputPrefix() {
    return OUTPUT_PREFIX;
  }

  public static void setOutputPrefix(Callable<String> outputPrefix) {
    OUTPUT_PREFIX = outputPrefix;
  }

  public static boolean isOutputPrefixTime() {
    return OUTPUT_PREFIX_TIME;
  }

  public static void setOutputPrefixTime(boolean outputPrefixTime) {
    OUTPUT_PREFIX_TIME = outputPrefixTime;
  }

  public static LineReader getReader() {
    return READER;
  }

  public static Integer getMaxConsoleLogLines() {
    return MAX_CONSOLE_LOG_LINES;
  }

  public static void setMaxConsoleLogLines(Integer maxConsoleLogLines) {
    MAX_CONSOLE_LOG_LINES = maxConsoleLogLines;
  }

  public static Terminal getTerminal() {
    return TERMINAL;
  }

  public static String[] getLastLogLines() {
    return CONSOLE_LOG_LINES.toArray(new String[0]);
  }

  public static boolean isEnabled() {
    return ENABLED;
  }

}
