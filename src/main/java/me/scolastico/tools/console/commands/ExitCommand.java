package me.scolastico.tools.console.commands;

import picocli.CommandLine.Command;

@Command(
    name = "exit",
    mixinStandardHelpOptions = true,
    version = {"1.0.0"},
    description = {"Exit the application safely."}
)
public class ExitCommand implements Runnable {

  @Override
  public void run() {
    System.exit(0);
  }

}
