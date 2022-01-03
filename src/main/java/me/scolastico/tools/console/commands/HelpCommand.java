package me.scolastico.tools.console.commands;

import java.util.function.Consumer;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.etc.TableGenerator;
import me.scolastico.tools.etc.TableGeneratorThemes;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import picocli.CommandLine.Command;

@Command(
    name = "help",
    description = {"List all available commands or shows the help page of a specific command."},
    mixinStandardHelpOptions = true,
    helpCommand = true,
    version = {"1.0.0"}
)
public class HelpCommand implements Runnable, Consumer<Object> {

  private static final String BG_BLACK = Ansi.ansi().bgBright(Color.BLACK).fgBlack().toString();
  private static final String BG_DEFAULT = Ansi.ansi().bgDefault().toString();
  private static final String FG_CYAN = Ansi.ansi().fgBrightCyan().toString();
  private static final String FG_MAGENTA = Ansi.ansi().fgMagenta().toString();
  private static final String FG_GRAY = Ansi.ansi().fgBrightBlack().toString();
  private static final String FG_DEFAULT = Ansi.ansi().fgDefault().toString();

  @Override
  public void run() {
    TableGenerator generator = TableGeneratorThemes.FANCY_BOARDER()
        .setRightWall(false)
        .setLeftWall(false)
        .setFooter(false)
        .setHeader(false)
        .setBorderInterceptionLeft(' ')
        .setBorderInterceptionRight(' ')
        .setBorderInterceptionMiddle('+')
        .setBorderVertical(' ')
        .setBoarderColorPrefix(FG_GRAY)
        .setBoarderColorSuffix(FG_DEFAULT)
        .setAutoLineBreak(100)
        .addContent(
            Ansi.ansi().fgYellow().a("Command").reset().toString(),
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgYellow().a("Description").reset().toString(),
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgYellow().a("Aliases").reset().toString()
        )
        .addTableLineSeparator();
    int maxLineLength = ConsoleManager.getTerminal().getSize().getColumns();
    if (maxLineLength > 0 && maxLineLength < 100) generator.setAutoLineBreak(60);
    boolean bg = false;
    for (Object o : ConsoleManager.getCommands()) {
      Command annotation = o.getClass().getAnnotation(Command.class);
      generator.addContent(
          (bg ? BG_BLACK : "") + FG_CYAN + annotation.name() + FG_DEFAULT,
          String.join(System.lineSeparator(), annotation.description()),
          TableGenerator.RIGHT_ALIGNMENT + FG_MAGENTA
              + String.join(FG_DEFAULT + ", " + FG_MAGENTA, annotation.aliases())
              + FG_DEFAULT + (bg ? BG_DEFAULT : "")
      );
      bg = !bg;
    }
    System.out.println();
    System.out.println(generator);
    System.out.println();
  }

  public static String[] generateHelpPage(Object command) {
    return new String[]{"help page"};
  }

  @Override
  public void accept(Object o) {
    System.out.println(String.join(System.lineSeparator(), generateHelpPage(o)));
  }

}
