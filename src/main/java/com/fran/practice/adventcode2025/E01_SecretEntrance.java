package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.util.List;

public class E01_SecretEntrance implements Exercise {
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

    testExample();
  }

  static void testExample() {
    List<String> example = List.of(
      "L68",
      "L30",
      "R48",
      "L5",
      "R60",
      "L55",
      "L1",
      "L99",
      "R14",
      "L82"
    );

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

  static int rotate(int current, char dir, int steps) {
    int delta = (dir == 'L') ? -steps : steps;
    return Math.floorMod(current + delta, 100);
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E01_SecretEntrance());
    ConsoleRunner.main(new String[]{"AOC25-01"});
  }
}
