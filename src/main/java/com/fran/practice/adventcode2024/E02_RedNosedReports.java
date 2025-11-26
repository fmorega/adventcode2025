package com.fran.practice.adventcode2024;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class E02_RedNosedReports implements Exercise {

  @Override
  public String id() {
    return "AOC24-02";
  }

  @Override
  public String title() {
    return "Red-Nose Reports - Safe Reports";
  }

  @Override
  public void run() {

    // test
    List<String> exampleLines = List.of(
      "7 6 4 2 1",  // OK
      "1 2 7 8 9",
      "9 7 6 2 1",
      "1 3 2 4 5",
      "8 6 4 4 1",
      "1 3 6 7 9"   // OK
    );

    long exampleSafe = countSafeReports(exampleLines);
    System.out.println("Example safe reports = " + exampleSafe + " (esperado: 2)");
    System.out.println();

    Path inputPath = Path.of("inputs/day02.txt");

    if (Files.exists(inputPath)) {
      try {
        List<String> inputLines = Files.readAllLines(inputPath);
        long safeReports = countSafeReports(inputLines);

        System.out.println("== Resultados para tu input ==");
        System.out.println("Informes totales : " + inputLines.size());
        System.out.println("Informes seguros  : " + safeReports);

      } catch (IOException e) {
        System.out.println("Error reading file: " + e.getMessage());
      }
    } else {
      System.out.println("No se ha encontrado el fichero inputs/day02.txt.");
    }
  }

  public static long countSafeReports(List<String> lines) {
    long safeCount = 0;

    for (String rawLine : lines) {
      List<Integer> levels = parseLevels(rawLine);
      if (levels.isEmpty()) {
        continue;
      }
      if (isReportSafe(levels)) {
        safeCount++;
      }
    }
    return safeCount;
  }

  public static List<Integer> parseLevels(String rawLine) {
    String line = rawLine.trim();
    if (line.isEmpty()) {
      return List.of();
    }

    String[] parts = line.split("\\s+");
    List<Integer> levels = new ArrayList<>(parts.length);

    for (String part : parts) {
      levels.add(Integer.parseInt(part));
    }

    return levels;
  }

  public static boolean isReportSafe(List<Integer> levels) {
    if (levels.size() < 2) {
      return false; // just in case
    }

    Trend trend = null;

    for (int i = 0; i < levels.size() - 1; i++) {
      int current = levels.get(i);
      int next = levels.get(i + 1);
      int diff = next - current;

      // There cannot be two identicals in a row
      if (diff == 0) {
        return false;
      }

      int absDiff = Math.abs(diff);

      // the diff must be between 1 - 3
      if (absDiff < 1 || absDiff > 3) {
        return false;
      }

      // trend up or down
      if (trend == null) {
        trend = (diff > 0) ? Trend.INCREASING : Trend.DECREASING;
      } else {
        // if it is up, cannot be a down step
        if (trend == Trend.INCREASING && diff < 0) {
          return false;
        }
        // if it is down, cannot be a up step
        if (trend == Trend.DECREASING && diff > 0) {
          return false;
        }
      }
    }

    // if all the rules are green
    return true;
  }

  private enum Trend {
    INCREASING,
    DECREASING
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E02_RedNosedReports());
    ConsoleRunner.main(new String[]{"AOC24-02"});
  }
}
