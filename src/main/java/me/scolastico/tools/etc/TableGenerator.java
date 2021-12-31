package me.scolastico.tools.etc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class TableGenerator {

  //ToDo: Add documentation.

  public static final String CENTER_ALIGNMENT = "<!-- CENTER -->";
  public static final String RIGHT_ALIGNMENT = "<!-- RIGHT -->";

  private final ArrayList<ArrayList<String>> content = new ArrayList<>();
  private Character borderVertical = '|';
  private Character borderHorizontal = '-';
  private Character borderInterceptionTop = '-';
  private Character borderInterceptionBottom = '-';
  private Character borderInterceptionLeft = '|';
  private Character borderInterceptionMiddle = '|';
  private Character borderInterceptionRight = '|';
  private Character borderCornerTopLeft = '-';
  private Character borderCornerTopRight = '-';
  private Character borderCornerBottomLeft = '-';
  private Character borderCornerBottomRight = '-';
  private String boarderColorPrefix = "";
  private String boarderColorSuffix = "";
  private int borderSpace = 1;
  private Character space = ' ';
  private int autoLineBreak = 0;
  private boolean header = true;
  private boolean footer = true;
  private boolean leftWall = true;
  private boolean rightWall = true;

  /**
   *
   * @param content The content which should be added.
   *                The content can be aligned by prefixing the content
   *                by an alignment String like 'CENTER_ALIGNMENT'.
   * @return
   */
  public TableGenerator addContent(final String... content) {
    this.content.add(new ArrayList<>(List.of(content)));
    return this;
  }

  public TableGenerator addEmptyLine() {
    this.content.add(new ArrayList<>());
    return this;
  }

  public TableGenerator addTableLineSeparator() {
    this.content.add(new ArrayList<>(List.of("<!-- NEW LINE -->")));
    return this;
  }

  @Override
  public String toString() {
    return generate();
  }

  public String generate() {
    StringBuilder builder = new StringBuilder();
    Map<Integer, Integer> length = new HashMap<>();
    ArrayList<ArrayList<String>> brokenContent = new ArrayList<>();
    for (List<String> c:content) {
      Map<Integer, ArrayList<String>> brokenFields = new HashMap<>();
      int lines = 1;
      for (int i = 0; i < c.size(); i++) {
        ArrayList<String> newLines = brokenFields.getOrDefault(i, new ArrayList<>());
        String s = c.get(i);
        if (s.equals("<!-- NEW LINE -->")) {
          brokenFields = new HashMap<>();
          brokenFields.put(0, new ArrayList<>(){{
            add("<!-- NEW LINE -->");
          }});
          continue;
        }
        int l = s.length();
        if (s.startsWith(CENTER_ALIGNMENT)) l -= CENTER_ALIGNMENT.length();
        if (s.startsWith(RIGHT_ALIGNMENT)) l -= RIGHT_ALIGNMENT.length();
        if (autoLineBreak > 0 && l > autoLineBreak) {
          boolean center = s.startsWith(CENTER_ALIGNMENT);
          boolean right = s.startsWith(RIGHT_ALIGNMENT);
          s = s.replaceAll(CENTER_ALIGNMENT, "").replaceAll(RIGHT_ALIGNMENT, "");
          for (String line : s.split("\r\n|\r|\n")) {
            while (line.length() > autoLineBreak) {
              int location = 0;
              for (char ch : line.substring(autoLineBreak-10, autoLineBreak).toCharArray()) {
                if (ch == ' ') break;
                location++;
              }
              location = autoLineBreak-10+location;
              String newLine = line.substring(0, location);
              l = newLine.length();
              if (l > length.getOrDefault(i, 0)) {
                length.put(i, l);
              }
              newLine = (center ? CENTER_ALIGNMENT : "") + (right ? RIGHT_ALIGNMENT : "") + newLine;
              line = line.substring(location);
              if (newLine.startsWith(" ")) newLine = newLine.substring(1);
              newLines.add(newLine);
              brokenFields.put(i, newLines);
              if (lines < newLines.size()) lines = newLines.size();
            }
            if (!line.isEmpty()) {
              line = (center ? CENTER_ALIGNMENT : "")
                  + (right ? RIGHT_ALIGNMENT : "")
                  + ((line.startsWith(" ") ? line.substring(1) : line));
              newLines.add(line);
              if (lines < newLines.size()) lines = newLines.size();
            }
          }
        } else {
          newLines.addAll(Arrays.asList(s.split("\r\n|\r|\n")));
          if (lines < newLines.size()) lines = newLines.size();
        }
        brokenFields.put(i, newLines);
        if (l > length.getOrDefault(i, 0)) {
          length.put(i, l);
        }
      }
      for (int l = 0; l < lines; l++) {
        ArrayList<String> brokenLine = new ArrayList<>();
        for (int i = 0; i < brokenFields.size(); i++) {
          ArrayList<String> brokenLines = brokenFields.get(i);
          if (brokenLines.size() > l) {
            brokenLine.add(brokenLines.get(l));
          } else {
            brokenLine.add("");
          }
        }
        brokenContent.add(brokenLine);
      }
    }
    if (header) {
      addSeparator(builder, length, borderCornerTopLeft, borderInterceptionTop, borderCornerTopRight);
    }
    String space = StringUtils.repeat(this.space, borderSpace);
    for (ArrayList<String> c : brokenContent) {
      while (c.size() < length.size())
        c.add("");
      if (c.get(0).equals("<!-- NEW LINE -->")) {
        addSeparator(builder, length, borderInterceptionLeft, borderInterceptionMiddle, borderInterceptionRight);
        continue;
      }
      if (leftWall) {
        builder
            .append(boarderColorPrefix)
            .append(borderVertical)
            .append(boarderColorSuffix);
      } else {
        builder.append(" ");
      }
      for (int i = 0; i < c.size(); i++) {
        String s = c.get(i);
        boolean center = s.startsWith(CENTER_ALIGNMENT);
        boolean right = s.startsWith(RIGHT_ALIGNMENT);
        s = s.replaceAll(CENTER_ALIGNMENT, "").replaceAll(RIGHT_ALIGNMENT, "");
        String extraSpaces = StringUtils.repeat(this.space, length.get(i) - s.length());
        builder
            .append(space)
            .append(center ? extraSpaces.substring(0, extraSpaces.length()/2) : (right ? extraSpaces : ""))
            .append(s)
            .append(center ? extraSpaces.substring(extraSpaces.length()/2) : (right ? "" : extraSpaces))
            .append(space);
        if (i < c.size() - 1) {
          builder
              .append(boarderColorPrefix)
              .append(borderVertical)
              .append(boarderColorSuffix);
        }
      }
      if (rightWall) {
        builder
            .append(boarderColorPrefix)
            .append(borderVertical)
            .append(boarderColorSuffix);
      } else {
        builder.append(" ");
      }
      builder.append(System.lineSeparator());
    }
    if (footer) {
      addSeparator(builder, length, borderCornerBottomLeft, borderInterceptionBottom, borderCornerBottomRight);
    }
    String result = builder.toString();
    return result.substring(0, result.length() - System.lineSeparator().length());
  }

  private void addSeparator(StringBuilder builder, Map<Integer, Integer> length, Character cornerLeft, Character interception, Character cornerRight) {
    builder
        .append(boarderColorPrefix)
        .append(cornerLeft);
    for (int i = 0; i < length.size(); i++) {
      builder.append(StringUtils.repeat(borderHorizontal, length.get(i) + (borderSpace * 2)));
      if (i < length.size() - 1) {
        builder.append(interception);
      }
    }
    builder
        .append(cornerRight)
        .append(boarderColorSuffix)
        .append(System.lineSeparator());
  }

  public Character getBorderVertical() {
    return borderVertical;
  }

  public TableGenerator setBorderVertical(Character borderVertical) {
    this.borderVertical = borderVertical;
    return this;
  }

  public Character getBorderHorizontal() {
    return borderHorizontal;
  }

  public TableGenerator setBorderHorizontal(Character borderHorizontal) {
    this.borderHorizontal = borderHorizontal;
    return this;
  }

  public Character getBorderInterceptionTop() {
    return borderInterceptionTop;
  }

  public TableGenerator setBorderInterceptionTop(Character borderInterceptionTop) {
    this.borderInterceptionTop = borderInterceptionTop;
    return this;
  }

  public Character getBorderInterceptionBottom() {
    return borderInterceptionBottom;
  }

  public TableGenerator setBorderInterceptionBottom(Character borderInterceptionBottom) {
    this.borderInterceptionBottom = borderInterceptionBottom;
    return this;
  }

  public Character getBorderInterceptionLeft() {
    return borderInterceptionLeft;
  }

  public TableGenerator setBorderInterceptionLeft(Character borderInterceptionLeft) {
    this.borderInterceptionLeft = borderInterceptionLeft;
    return this;
  }

  public Character getBorderInterceptionMiddle() {
    return borderInterceptionMiddle;
  }

  public TableGenerator setBorderInterceptionMiddle(Character borderInterceptionMiddle) {
    this.borderInterceptionMiddle = borderInterceptionMiddle;
    return this;
  }

  public Character getBorderInterceptionRight() {
    return borderInterceptionRight;
  }

  public TableGenerator setBorderInterceptionRight(Character borderInterceptionRight) {
    this.borderInterceptionRight = borderInterceptionRight;
    return this;
  }

  public Character getBorderCornerTopLeft() {
    return borderCornerTopLeft;
  }

  public TableGenerator setBorderCornerTopLeft(Character borderCornerTopLeft) {
    this.borderCornerTopLeft = borderCornerTopLeft;
    return this;
  }

  public Character getBorderCornerTopRight() {
    return borderCornerTopRight;
  }

  public TableGenerator setBorderCornerTopRight(Character borderCornerTopRight) {
    this.borderCornerTopRight = borderCornerTopRight;
    return this;
  }

  public Character getBorderCornerBottomLeft() {
    return borderCornerBottomLeft;
  }

  public TableGenerator setBorderCornerBottomLeft(Character borderCornerBottomLeft) {
    this.borderCornerBottomLeft = borderCornerBottomLeft;
    return this;
  }

  public Character getBorderCornerBottomRight() {
    return borderCornerBottomRight;
  }

  public TableGenerator setBorderCornerBottomRight(Character borderCornerBottomRight) {
    this.borderCornerBottomRight = borderCornerBottomRight;
    return this;
  }

  public int getBorderSpace() {
    return borderSpace;
  }

  public TableGenerator setBorderSpace(int borderSpace) {
    this.borderSpace = borderSpace;
    return this;
  }

  public Character getSpace() {
    return space;
  }

  public TableGenerator setSpace(Character space) {
    this.space = space;
    return this;
  }

  public String getBoarderColorPrefix() {
    return boarderColorPrefix;
  }

  public TableGenerator setBoarderColorPrefix(String boarderColorPrefix) {
    this.boarderColorPrefix = boarderColorPrefix;
    return this;
  }

  public String getBoarderColorSuffix() {
    return boarderColorSuffix;
  }

  public TableGenerator setBoarderColorSuffix(String boarderColorSuffix) {
    this.boarderColorSuffix = boarderColorSuffix;
    return this;
  }

  public int getAutoLineBreak() {
    return autoLineBreak;
  }

  public TableGenerator setAutoLineBreak(int autoLineBreak) {
    this.autoLineBreak = autoLineBreak;
    return this;
  }

  public boolean isHeader() {
    return header;
  }

  public TableGenerator setHeader(boolean header) {
    this.header = header;
    return this;
  }

  public boolean isFooter() {
    return footer;
  }

  public TableGenerator setFooter(boolean footer) {
    this.footer = footer;
    return this;
  }

  public boolean isLeftWall() {
    return leftWall;
  }

  public TableGenerator setLeftWall(boolean leftWall) {
    this.leftWall = leftWall;
    return this;
  }

  public boolean isRightWall() {
    return rightWall;
  }

  public TableGenerator setRightWall(boolean rightWall) {
    this.rightWall = rightWall;
    return this;
  }
}
