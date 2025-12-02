package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class E02_GiftShop implements Exercise {

  private static final String HYPHEN_SPLIT = "-";
  private static final String COMMA_SPLIT = ",";
  private static final String INPUT_DAY_TWO = "inputs/day02-2025.txt";

  @Override
  public String id() {
    return "AOC25-02";
  }

  @Override
  public String title() {
    return "Gift Shop - Invalid IDs Sum";
  }

  @Override
  public void run() {

    Path inputPath = Path.of(INPUT_DAY_TWO);
    try {
      String rawInput = Files.readString(inputPath);
      long totalSum = sumInvalidIdsInInput(rawInput);
      System.out.println("Total sums: " + totalSum);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static long sumInvalidIdsInInput(String rawInput) {

    long totalSum = 0L;
    String[] partsInput = rawInput.split(COMMA_SPLIT);

    for (String range : partsInput) {
      range = range.trim();
      String[] bounds = range.split(HYPHEN_SPLIT);
      long startBound = Long.parseLong(bounds[0]);
      long endBound = Long.parseLong(bounds[1]);

      for (long id = startBound; id <= endBound; id++) {
        if (isInvalidId(id)) {
          totalSum += id;
        }
      }
    }
    return totalSum;
  }

  static boolean isInvalidId(long id) {
    String input = Long.toString(id);

    if (input.length() % 2 != 0) {
      return false;
    }

    int half = input.length() / 2;
    String left = input.substring(0, half);
    String right = input.substring(half);

    // just for debug purposes
    if (left.equals(right)) {
      System.out.println("Invalid: " + id + " (" + left + " + " + right + ")");
    }

    return left.equals(right);
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E02_GiftShop());
    ConsoleRunner.main(new String[]{"AOC25-02"});
  }
}
