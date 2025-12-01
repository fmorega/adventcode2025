package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class E01_SecretEntrance implements Exercise {

  private static final char LEFT_ROTATION = 'L'; // can be right too
  private static final int DIAL_SIZE = 100;
  private static final int START_POSITION = 50;

  @Override
  public String id() {
    return "AOC25-01";
  }

  @Override
  public String title() {
    return "Secret Entrance - Safe Dial";
  }

  @Override
  public void run() {

    //testExample();

    Path inputPath = Path.of("inputs/day01-2025.txt");
    try {
      List<String> lines = Files.readAllLines(inputPath);
      int password = computePassword(lines);
      int passwordSecondPart = computePasswordWithClick(lines);
      System.out.println("Password = " + password);
      System.out.println("Password part two: " + passwordSecondPart);
    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static int computePasswordWithClick(List<String> lines) {
    int position = START_POSITION;
    int zeroCount = 0;

    for (String rawData : lines) {
      String line = rawData.trim();
      if (line.isEmpty()) continue;

      char direction = line.charAt(0);
      int steps = Integer.parseInt(line.substring(1));

      for (int i = 0; i < steps; i++) {
        position = rotate(position, direction, 1);
        if (position == 0) zeroCount++;
      }
    }
    return zeroCount;
  }

  static int computePassword(List<String> lines) {
    int position = START_POSITION;
    int zeroCount = 0;

    for (String rawData : lines) {
      String line = rawData.trim();
      if (line.isEmpty()) continue;

      char direction = line.charAt(0);
      int steps = Integer.parseInt(line.substring(1));

      position = rotate(position, direction, steps);
      if (position == 0) zeroCount++;
    }
    return zeroCount;
  }

  static int rotate(int current, char dir, int steps) {
    int delta = (dir == LEFT_ROTATION) ? -steps : steps;
    return Math.floorMod(current + delta, DIAL_SIZE);
  }

  static void testExample() {
    List<String> example = List.of("L68", "L30", "R48", "L5", "R60", "L55", "L1", "L99", "R14", "L82");

    int position = 50;
    int zeroCount = 0;

    for (String line : example) {
      char dir = line.charAt(0);
      int steps = Integer.parseInt(line.substring(1));

      position = rotate(position, dir, steps);
      System.out.println("After " + line + " -> " + position);

      if (position == 0) {
        zeroCount++;
      }
    }
    System.out.println("Total times at 0 (example) = " + zeroCount);
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E01_SecretEntrance());
    ConsoleRunner.main(new String[]{"AOC25-01"});
  }
}
