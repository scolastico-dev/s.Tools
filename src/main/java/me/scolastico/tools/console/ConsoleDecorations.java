package me.scolastico.tools.console;

/**
 * Console decorations. Can simply added to System.out.print messages.
 */
public class ConsoleDecorations {

  public static final String RESET                      = "\u001b[0m";
  public static final String BOLD                       = "\u001b[1m";
  public static final String UNDERLINE                  = "\u001b[4m";
  public static final String INVERTED_COLOR             = "\u001b[7m";
  public static final String BACKSPACE                  = Character.toString((char) 8);

  public static class Text {

    public static final String RED                      = "\u001b[31;1m";
    public static final String GREEN                    = "\u001b[32;1m";
    public static final String BLUE                     = "\u001b[34;1m";
    public static final String AQUA                     = "\u001b[36;1m";
    public static final String PURPLE                   = "\u001b[35;1m";
    public static final String YELLOW                   = "\u001b[33;1m";
    public static final String GOLD                     = "\u001b[33m";
    public static final String DARK_RED                 = "\u001b[31m";
    public static final String DARK_GREEN               = "\u001b[32m";
    public static final String DARK_BLUE                = "\u001b[34m";
    public static final String DARK_AQUA                = "\u001b[36m";
    public static final String DARK_PURPLE              = "\u001b[35m";
    public static final String DARK_GREY                = "\u001b[30;1m";
    public static final String GREY                     = "\u001b[37m";
    public static final String WHITE                    = "\u001b[37;1m";
    public static final String BLACK                    = "\u001b[30m";
    public static final String RESET                    = "\u001b[0m";

  }

  /**
   * Set the background color of the console.
   */
  public static class Background {

    public static final String RED                      = "\u001b[41;1m";
    public static final String GREEN                    = "\u001b[42;1m";
    public static final String BLUE                     = "\u001b[44;1m";
    public static final String AQUA                     = "\u001b[46;1m";
    public static final String PURPLE                   = "\u001b[45;1m";
    public static final String YELLOW                   = "\u001b[43;1m";
    public static final String GOLD                     = "\u001b[43m";
    public static final String DARK_RED                 = "\u001b[41m";
    public static final String DARK_GREEN               = "\u001b[42m";
    public static final String DARK_BLUE                = "\u001b[44m";
    public static final String DARK_AQUA                = "\u001b[46m";
    public static final String DARK_PURPLE              = "\u001b[45m";
    public static final String DARK_GREY                = "\u001b[40;1m";
    public static final String GREY                     = "\u001b[47m";
    public static final String WHITE                    = "\u001b[47;1m";
    public static final String BLACK                    = "\u001b[40m";

  }

  public static class CursorNavigation {

    public static final String LEFT                     = "\033[D";
    public static final String RIGHT                    = "\033[C";
    public static final String UP                       = "\033[A";
    public static final String DOWN                     = "\033[B";
    public static final String SAVE_POSITION            = "\033[s";
    public static final String RESTORE_POSITION         = "\033[u";
    public static final String ERASE_TO_END_OF_LINE     = "\033[K";
    public static final String BEGINNING_OF_PREVIOUS    = "\033[F";

    public static String LEFT(int i) {return              "\033[" + i + "D";}
    public static String RIGHT(int i) {return             "\033[" + i + "C";}
    public static String UP(int i) {return                "\033[" + i + "A";}
    public static String DOWN(int i) {return              "\033[" + i + "B";}

  }

}
