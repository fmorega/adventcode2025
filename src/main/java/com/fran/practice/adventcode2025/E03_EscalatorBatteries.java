package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class E03_EscalatorBatteries implements Exercise {

  private static final String INPUT_DAY_THREE = "inputs/day03-2025.txt";
  private static final int PART2_DIGITS = 12;

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
      long totalPart2 = solvePartTwo(banks);

      System.out.println("Part 1 - Total output joltage: " + totalJoltage);
      System.out.println("Part 2 - Total output joltage: " + totalPart2);
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
      int maxForBank = maxJoltageForBankTwoDigits(bank);
      total += maxForBank;
    }
    return total;
  }

  static long solvePartTwo(List<String> banks) {
    long total = 0L;

    for (String bank : banks) {
      String best = maxJoltageForBankWithLength(bank);
      long value = Long.parseLong(best);
      total += value;
    }

    return total;
  }

  static int maxJoltageForBankTwoDigits(String bank) {

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

  static String maxJoltageForBankWithLength(String bank) {

    int n = bank.length();
    int toRemove = n - E03_EscalatorBatteries.PART2_DIGITS;
    StringBuilder stack = new StringBuilder(n); // stack

    for (int i = 0; i < n; i++) {
      char current = bank.charAt(i);

      while (!stack.isEmpty()
        && toRemove > 0 // still open to delete
        && stack.charAt(stack.length() - 1) < current) { // last digit added to stack < current digit

        stack.deleteCharAt(stack.length() - 1); // delete "blocking" digit
        toRemove--;
      }

      stack.append(current);
    }

    if (stack.length() > E03_EscalatorBatteries.PART2_DIGITS) {
      stack.setLength(E03_EscalatorBatteries.PART2_DIGITS);
    }

    return stack.toString();
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E03_EscalatorBatteries());
    ConsoleRunner.main(new String[]{"AOC25-03"});
  }
}
