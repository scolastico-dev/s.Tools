package me.scolastico.tools.console.commands;


import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.NumberFormat;
import java.util.Date;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.etc.TableGenerator;
import me.scolastico.tools.etc.TableGeneratorThemes;
import org.apache.commons.lang3.ArrayUtils;
import org.fusesource.jansi.Ansi;
import picocli.CommandLine.Command;

@Command(
    name = "status",
    aliases = {"top", "sysload"},
    description = {"Get the current status of the application with its usage of the system resources."},
    mixinStandardHelpOptions = true,
    version = {"1.0.0"}
)
public class StatusCommand implements Runnable {

  @Override
  public void run() {
    Runtime runtime = Runtime.getRuntime();
    NumberFormat format = NumberFormat.getInstance();
    long maxMemory = runtime.maxMemory();
    long allocatedMemory = runtime.totalMemory();
    long freeMemory = runtime.freeMemory();
    System.out.println();
    System.out.println(TableGeneratorThemes.FANCY_BOARDER_GRAY()
        .setBorderSpace(2)
        .addContent(TableGenerator.CENTER_ALIGNMENT + "Memory Statistics")
            .addTableLineSeparator()
        .addContent(
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgMagenta().a("Max memory:").fgDefault().toString(),
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgRed().a("Allocated memory:").fgDefault().toString(),
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgYellow().a("Free memory:").fgDefault().toString(),
            Ansi.ansi().a(TableGenerator.CENTER_ALIGNMENT).fgGreen().a("Total free memory:").fgDefault().toString()
        )
        .addContent(
            TableGenerator.CENTER_ALIGNMENT + format.format(maxMemory / 1024) + "/kb",
            TableGenerator.CENTER_ALIGNMENT + format.format(allocatedMemory / 1024) + "/kb",
            TableGenerator.CENTER_ALIGNMENT + format.format(freeMemory / 1024) + "/kb",
            TableGenerator.CENTER_ALIGNMENT + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024) + "/kb"
        )
    );
    TableGenerator generator = TableGeneratorThemes.FANCY_BOARDER_GRAY()
        .addContent(
            "ID",
            "Name",
            TableGenerator.CENTER_ALIGNMENT + "State",
            "Origin"
        )
        .addTableLineSeparator();
    int maxLineLength = ConsoleManager.getTerminal().getSize().getColumns();
    boolean limitedLength = maxLineLength > 0 && maxLineLength < 100;
    if (limitedLength) generator.addEmptyLine().setAutoLineBreak(30);
    for (Thread thread : Thread.getAllStackTraces().keySet()) {
      StackTraceElement[] stackTraceElements = Thread.getAllStackTraces().get(thread);
      ArrayUtils.reverse(stackTraceElements);
      String origin = "";
      for (StackTraceElement element:stackTraceElements) {
        if (origin.equals("")) origin = element.getClassName();
        if (!element.getClassName().startsWith("java")) {
          origin = element.getClassName();
          break;
        }
      }
      String state = thread.getState().toString();
      if (state.contains("WAITING")) state = Ansi.ansi().fgYellow().a(state).fgDefault().toString();
      else if (state.contains("RUNNABLE")) state = Ansi.ansi().fgGreen().a(state).fgDefault().toString();
      else state = Ansi.ansi().fgRed().a(state).fgDefault().toString();
      generator.addContent(
          Long.toString(thread.getId()),
          thread.getName(),
          TableGenerator.CENTER_ALIGNMENT + state,
          origin
      );
      if (limitedLength) generator.addEmptyLine();
    }
    System.out.println(generator);
    RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
    long startTime = bean.getStartTime();
    long running = (System.currentTimeMillis()-startTime)/1000;
    long hours = running / 3600;
    long minutes = (running % 3600) / 60;
    long seconds = running % 60;
    System.out.println(
        TableGeneratorThemes.FANCY_BOARDER_GRAY()
            .addContent("JVM Starting Time:", Ansi.ansi().fgMagenta().a(new Date(startTime).toString()).fgDefault().toString())
            .addContent("JVM Running Time:", Ansi.ansi().fgGreen().a(hours).a(":").a(minutes).a(":").a(seconds).fgDefault().toString())
    );
    System.out.println();
  }
}
