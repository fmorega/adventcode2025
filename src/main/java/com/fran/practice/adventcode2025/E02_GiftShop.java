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
    String[] rawRanges = rawInput.split(COMMA_SPLIT);

    for (String range : rawRanges) {
      range = range.trim();
      // separate each range
      String[] rangeBounds = range.split(HYPHEN_SPLIT);
      long startBound = Long.parseLong(rangeBounds[0]);
      long endBound = Long.parseLong(rangeBounds[1]);

      for (long id = startBound; id <= endBound; id++) {
        if (isInvalidId(id)) {
          totalSum += id;
        }
      }
    }
    return totalSum;
  }

  static boolean isInvalidId(long id) {

    // cast the id into a string
    String input = Long.toString(id);
    int digitsCount = input.length();

    // from 1 to half lenght
    for (int patternLength = 1; patternLength <= digitsCount/2;patternLength++ ) {
      // discard if the lenght is not a mult of the pattern
      if (digitsCount % patternLength != 0) {
        continue;
      }

      // candidate pattern
      String pattern = input.substring(0, patternLength);
      boolean allMatch = true;

      // check each consecutive segment with same size
      for (int position = patternLength; position < digitsCount; position += patternLength) {
        // check if the substr matches with the pattern
        if (!input.startsWith(pattern, position)) {
          allMatch = false;
          break;
        }
      }

      if (allMatch) {
        // debug
        System.out.println("Invalid: " + id + " (pattern = " + pattern + ")");
        return true; // invalid
      }
    }
    return false;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E02_GiftShop());
    ConsoleRunner.main(new String[]{"AOC25-02"});
  }
}
