package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class E06_TrashCompactor implements Exercise {

  private static final String INPUT_DAY_SIX = "inputs/day06-2025.txt";
  private record Problem(List<Long> numbers, char operation) { }

  @Override
  public String id() {
    return "AOC25-06";
  }

  @Override
  public String title() {
    return "Trash Compactor - Cephalopod Math";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_SIX);

    try {
      String rawInput = Files.readString(inputPath);
      List<String> lines = parseWorkSheet(rawInput);

      long grandTotal = solvePart1(lines);
      System.out.println("Part 1 grand total: " + grandTotal);

      long grandTotalPart2 = solvePart2(lines);
      System.out.println("Part 2 grand total: " + grandTotalPart2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  // read and normalize
  static List<String> parseWorkSheet(String rawInput) {
    List<String> rawLines = rawInput
      .lines()
      .filter(line -> !line.isBlank())
      .toList();

    int width = rawLines.stream()
      .mapToInt(String::length)
      .max()
      .orElse(0); // max line

    List<String> lines = new ArrayList<>(rawLines.size());

    for (String line : rawLines) {
      if (line.length() < width) {
        // pad with spaces to reach max width
        StringBuilder stringBuilder = new StringBuilder(line);
        while (stringBuilder.length() < width) {
          stringBuilder.append(' ');
        }
        lines.add(stringBuilder.toString());
      } else {
        lines.add(line);
      }
    }

    return lines;
  }

  // parse all problems form input
  static List<Problem> parseProblems(List<String> lines) {
    int cols = lines.get(0).length();

    List<Problem> problems = new ArrayList<>();

    Integer currentStart = null; // start column for current problem

    for (int column = 0; column < cols; column++) {
      boolean emptyColumn = true;

      // check if is empty
      for (String line : lines) {
        char character = line.charAt(column);
        if (character != ' ') {
          emptyColumn = false;
          break;
        }
      }

      if (!emptyColumn && currentStart == null) {
        // new problem block
        currentStart = column;
      } else if (emptyColumn && currentStart != null) {
        // leaving problem block
        int endCol = column - 1;
        problems.add(parseProblem(lines, currentStart, endCol));
        currentStart = null;
      }
    }
    // if reach last column, close it
    if (currentStart != null) {
      problems.add(parseProblem(lines, currentStart, cols - 1));
    }

    return problems;
  }

  // same as 1 but calling new method
  static List<Problem> parseProblemsPart2(List<String> lines) {
    int cols = lines.get(0).length();
    List<Problem> problems = new ArrayList<>();

    Integer start = null;

    for (int c = 0; c < cols; c++) {
      boolean empty = true;
      for (String line : lines) {
        if (line.charAt(c) != ' ') {
          empty = false;
          break;
        }
      }

      if (!empty && start == null) {
        start = c;
      } else if (empty && start != null) {
        problems.add(parseProblemPart2(lines, start, c - 1));
        start = null;
      }
    }

    if (start != null) {
      problems.add(parseProblemPart2(lines, start, cols - 1));
    }

    return problems;
  }


  // parses a single problem
  static Problem parseProblem(List<String> lines, int startCol, int endCol) {
    int rows = lines.size();
    int lastRow = rows - 1;

    // find operation in the bottom row within
    String opSlice = lines.get(lastRow).substring(startCol, endCol + 1);
    char operation = 0;
    for (int i = 0; i < opSlice.length(); i++) {
      char character = opSlice.charAt(i);
      if (character == '+' || character == '*') {
        operation = character;
        break;
      }
    }

    List<Long> numbers = new ArrayList<>();

    // extract numbers for above
    for (int r = 0; r < lastRow; r++) {
      String rowSlice = lines.get(r).substring(startCol, endCol + 1);
      String trimmed = rowSlice.trim();
      if (!trimmed.isEmpty()) {
        long value = Long.parseLong(trimmed);
        numbers.add(value);
      }
    }

    return new Problem(numbers, operation);
  }

  static long solvePart1(List<String> lines) {
    List<Problem> problems = parseProblems(lines);

    long grandTotal = 0L;

    for (Problem p : problems) {
      long result = (p).operation();
      grandTotal += result;
    }

    return grandTotal;
  }

  static long solvePart2(List<String> lines) {
    List<Problem> problems = parseProblemsPart2(lines);

    long total = 0;
    for (Problem p : problems) {
      total += evaluateProblem(p);
    }

    return total;
  }


  static Problem parseProblemPart2(List<String> lines, int startCol, int endCol) {
    int rows = lines.size();
    int lastRow = rows - 1;

    // last row operation
    String opSlice = lines.get(lastRow).substring(startCol, endCol + 1);
    char operation = 0;
    for (int i = 0; i < opSlice.length(); i++) {
      char ch = opSlice.charAt(i);
      if (ch == '+' || ch == '*') {
        operation = ch;
        break;
      }
    }

    // parse numbers coll by col
    List<Long> numbers = new ArrayList<>();

    for (int c = startCol; c <= endCol; c++) {
      StringBuilder sb = new StringBuilder();

      // collects vertically
      for (int r = 0; r < lastRow; r++) {
        char ch = lines.get(r).charAt(c);
        if (Character.isDigit(ch)) {
          sb.append(ch);
        }
      }

      if (!sb.isEmpty()) {
        long value = Long.parseLong(sb.toString());
        numbers.add(value);
      }
    }

    return new Problem(numbers, operation);
  }


  static long evaluateProblem(Problem problem) {
    long acc = problem.numbers().get(0);

    for (int i = 1; i < problem.numbers().size(); i++) {
      long n = problem.numbers().get(i);
      acc = (problem.operation() == '+') ? acc + n : acc * n;
    }
    return acc;
  }


  public static void main(String[] args) {
    ConsoleRunner.register(new E06_TrashCompactor());
    ConsoleRunner.main(new String[]{"AOC25-06"});
  }
}
