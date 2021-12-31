package me.scolastico.tools.etc;

import org.fusesource.jansi.Ansi;

public class TableGeneratorThemes {

  /**
   * Get a new TableGenerator with no boarder lines.
   * @return The new TableGenerator instance.
   */
  public static TableGenerator NO_BOARDER() {
    return new TableGenerator()
        .setBorderCornerTopLeft(' ')
        .setBorderInterceptionTop(' ')
        .setBorderCornerTopRight(' ')
        .setBorderInterceptionLeft(' ')
        .setBorderInterceptionMiddle(' ')
        .setBorderInterceptionRight(' ')
        .setBorderCornerBottomLeft(' ')
        .setBorderInterceptionBottom(' ')
        .setBorderCornerBottomRight(' ')
        .setBorderHorizontal(' ')
        .setBorderVertical(' ')
        .setBorderSpace(0)
        .setHeader(false)
        .setFooter(false)
        .setLeftWall(false)
        .setRightWall(false);
  }

  /**
   * Get a new TableGenerator with invisible boarder lines.
   * @return The new TableGenerator instance.
   */
  public static TableGenerator INVISIBLE_BOARDER() {
    return new TableGenerator()
        .setBorderCornerTopLeft(' ')
        .setBorderInterceptionTop(' ')
        .setBorderCornerTopRight(' ')
        .setBorderInterceptionLeft(' ')
        .setBorderInterceptionMiddle(' ')
        .setBorderInterceptionRight(' ')
        .setBorderCornerBottomLeft(' ')
        .setBorderInterceptionBottom(' ')
        .setBorderCornerBottomRight(' ')
        .setBorderHorizontal(' ')
        .setBorderVertical(' ');
  }

  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   *  ┌──┬──┐
   *  │  │  │
   *  ├──┼──┤
   *  │  │  │
   *  └──┴──┘
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator FANCY_BOARDER() {
    return new TableGenerator()
        .setBorderCornerTopLeft('┌')
        .setBorderInterceptionTop('┬')
        .setBorderCornerTopRight('┐')
        .setBorderInterceptionLeft('├')
        .setBorderInterceptionMiddle('┼')
        .setBorderInterceptionRight('┤')
        .setBorderCornerBottomLeft('└')
        .setBorderInterceptionBottom('┴')
        .setBorderCornerBottomRight('┘')
        .setBorderHorizontal('─')
        .setBorderVertical('│');
  }

  /**
   * Get a new TableGenerator with gray boarders and with this theme:
   * <pre>
   *  ┌──┬──┐
   *  │  │  │
   *  ├──┼──┤
   *  │  │  │
   *  └──┴──┘
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator FANCY_BOARDER_GRAY() {
    return new TableGenerator()
        .setBorderCornerTopLeft('┌')
        .setBorderInterceptionTop('┬')
        .setBorderCornerTopRight('┐')
        .setBorderInterceptionLeft('├')
        .setBorderInterceptionMiddle('┼')
        .setBorderInterceptionRight('┤')
        .setBorderCornerBottomLeft('└')
        .setBorderInterceptionBottom('┴')
        .setBorderCornerBottomRight('┘')
        .setBorderHorizontal('─')
        .setBorderVertical('│')
        .setBoarderColorPrefix(Ansi.ansi().fgBrightBlack().toString())
        .setBoarderColorSuffix(Ansi.ansi().reset().toString());
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   * ╔══╦══╗
   * ║  ║  ║
   * ╠══╬══╣
   * ║  ║  ║
   * ╚══╩══╝
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator FANCY_DOUBLE_BOARDER() {
    return new TableGenerator()
        .setBorderCornerTopLeft('╔')
        .setBorderInterceptionTop('╦')
        .setBorderCornerTopRight('╗')
        .setBorderInterceptionLeft('╠')
        .setBorderInterceptionMiddle('╬')
        .setBorderInterceptionRight('╣')
        .setBorderCornerBottomLeft('╚')
        .setBorderInterceptionBottom('╩')
        .setBorderCornerBottomRight('╝')
        .setBorderHorizontal('═')
        .setBorderVertical('║');
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   * +--+--+
   * |  |  |
   * +--+--+
   * |  |  |
   * +--+--+
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator MYSQL() {
    return new TableGenerator()
        .setBorderCornerTopLeft('+')
        .setBorderInterceptionTop('+')
        .setBorderCornerTopRight('+')
        .setBorderInterceptionLeft('+')
        .setBorderInterceptionMiddle('+')
        .setBorderInterceptionRight('+')
        .setBorderCornerBottomLeft('+')
        .setBorderInterceptionBottom('+')
        .setBorderCornerBottomRight('+')
        .setBorderHorizontal('-')
        .setBorderVertical('|');
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   * .......
   * :  :  :
   * :..:..:
   * :  :  :
   * :..:..:
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator DOTS() {
    return new TableGenerator()
        .setBorderCornerTopLeft('.')
        .setBorderInterceptionTop('.')
        .setBorderCornerTopRight('.')
        .setBorderInterceptionLeft(':')
        .setBorderInterceptionMiddle(':')
        .setBorderInterceptionRight(':')
        .setBorderCornerBottomLeft(':')
        .setBorderInterceptionBottom(':')
        .setBorderCornerBottomRight(':')
        .setBorderHorizontal('.')
        .setBorderVertical(':');
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   * =======
   *
   * =======
   *
   * =======
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator RST_SIMPLE() {
    return new TableGenerator()
        .setBorderCornerTopLeft(' ')
        .setBorderInterceptionTop(' ')
        .setBorderCornerTopRight(' ')
        .setBorderInterceptionLeft(' ')
        .setBorderInterceptionMiddle(' ')
        .setBorderInterceptionRight(' ')
        .setBorderCornerBottomLeft(' ')
        .setBorderInterceptionBottom(' ')
        .setBorderCornerBottomRight(' ')
        .setBorderHorizontal('=')
        .setBorderVertical(' ');
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   *
   *    |
   *  --|--
   *    |
   *
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator REDDIT() {
    return new TableGenerator()
        .setBorderCornerTopLeft(' ')
        .setBorderInterceptionTop(' ')
        .setBorderCornerTopRight(' ')
        .setBorderInterceptionLeft(' ')
        .setBorderInterceptionMiddle('|')
        .setBorderInterceptionRight(' ')
        .setBorderCornerBottomLeft(' ')
        .setBorderInterceptionBottom(' ')
        .setBorderCornerBottomRight(' ')
        .setBorderHorizontal('-')
        .setBorderVertical('|')
        .setRightWall(false)
        .setLeftWall(false)
        .setHeader(false)
        .setFooter(false);
  }


  /**
   * Get a new TableGenerator with this theme:
   * <pre>
   *
   *  |  |  |
   *  |--|--|
   *  |  |  |
   *
   * </pre>
   * @return The new TableGenerator instance.
   */
  public static TableGenerator GITHUB() {
    return new TableGenerator()
        .setBorderCornerTopLeft(' ')
        .setBorderInterceptionTop(' ')
        .setBorderCornerTopRight(' ')
        .setBorderInterceptionLeft('|')
        .setBorderInterceptionMiddle('|')
        .setBorderInterceptionRight('|')
        .setBorderCornerBottomLeft(' ')
        .setBorderInterceptionBottom(' ')
        .setBorderCornerBottomRight(' ')
        .setBorderHorizontal('-')
        .setBorderVertical('|')
        .setHeader(false)
        .setFooter(false);
  }

}
