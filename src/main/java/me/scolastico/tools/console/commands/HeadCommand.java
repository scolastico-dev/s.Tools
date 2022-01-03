package me.scolastico.tools.console.commands;

import picocli.CommandLine.Command;

@Command(name = "",
    description = {
        "Hit @|magenta <TAB>|@ to see available commands.",
        "Hit @|magenta ALT-S|@ to toggle tailtips."
    },
    footer = {
        "Press Ctrl-D to exit."
    }
)
public class HeadCommand implements Runnable {

  public void run() {}

}
