package me.scolastico.tools.console.commands;

import me.scolastico.tools.console.ConsoleManager;
import org.jline.utils.InfoCmp.Capability;
import picocli.CommandLine.Command;

@Command(
    name = "clear",
    aliases = {"cls"},
    mixinStandardHelpOptions = true,
    version = {"1.0.0"},
    description = {"Clears the screen."}
)
public class ClearScreenCommand implements Runnable {

  @Override
  public void run() {
    ConsoleManager.getTerminal().puts(Capability.clear_screen);
  }

}
