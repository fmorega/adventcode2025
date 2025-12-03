package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class E03_EscalatorBatteries implements Exercise {

  private static final String INPUT_DAY_THREE = "inputs/day03-2025.txt";
  @Override
  public String id() {
    return "AOC25-03";
  }

  @Override
  public String title() {
    return "Escalator Batteries - Maximun Joltage";
  }

  @Override
  public void run() {

    Path inpuntPath = Path.of(INPUT_DAY_THREE);

    try {
      String rawInput = Files.readString(inpuntPath);
      List<String> banks = parseBanks(rawInput);

      long totalJoltage = solvePartOne(banks);
      System.out.println("Part 1 - Total output joltage: " + totalJoltage);
    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static List<String> parseBanks(String rawInput) {

    return rawInput
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .toList();
  }

  static long solvePartOne(List<String> banks) {

    long total = 0L;

    for (String bank : banks) {
      int maxForBank = maxJoltageForBank(bank);
      total += maxForBank;
    }
    return total;
  }

  static int maxJoltageForBank(String bank) {

    int bestPair = 0;
    int bestTensSoFar = -1; // still no digit

    for (int i = 0; i < bank.length(); i++) {
      int digit = bank.charAt(i) - '0';

      //  first try best previous digit
      if (bestTensSoFar != -1) {
        int candidate = bestTensSoFar * 10 + digit;
        if (candidate > bestPair) {
          bestPair = candidate;
        }
      }

      // update the best one
      if (digit > bestTensSoFar) {
        bestTensSoFar = digit;
      }
    }

    return bestPair;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E03_EscalatorBatteries());
    ConsoleRunner.main(new String[]{"AOC25-03"});
  }
}
