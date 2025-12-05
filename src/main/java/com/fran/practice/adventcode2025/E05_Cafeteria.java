package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class E05_Cafeteria implements Exercise {

  private static final String INPUT_DAY_FIVE = "inputs/day05-2025.txt";

  public record Range(long start, long end) { }

  @Override
  public String id() {
    return "AOC25-05";
  }

  @Override
  public String title() {
    return "Cafeteria - Fresh Ingredients";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_FIVE);

    try {
      String rawInput = Files.readString(inputPath);

      ParsedDatabase parsedInputs = parseDatabase(rawInput);
      long freshCount = solvePart1(parsedInputs.ranges(), parsedInputs.ingredientsIds());
      long freshCountPart2 = solvePart2(parsedInputs.ranges);

      System.out.println("Part 1 - Fresh ingredients: " + freshCount);
      System.out.println("Part 2 - Total fresh IDs: " + freshCountPart2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  private record ParsedDatabase(List<Range> ranges, List<Long> ingredientsIds) {}

  static long solvePart1(List<Range> ranges, List<Long> ingredientsIds) {
    long freshCount = 0L;

    for (long id : ingredientsIds) {
      if (isFresh(id, ranges)) {
        freshCount++; // if is inside a valid ranges
      }
    }

    return freshCount;
  }

  static long solvePart2(List<Range> ranges) {
    List<Range> merged = mergeRanges(ranges); //first merge overlapping or adjacent

    long totalFreshIds = 0L;

    for (Range range : merged) {
      long length = range.end() - range.start() + 1;
      totalFreshIds += length; // add computed inclusive length
    }

    return totalFreshIds;
  }

  static List<Range> mergeRanges(List<Range> ranges) {
    if (ranges.isEmpty()) {
      return List.of();
    }

    List<Range> sorted = new ArrayList<>(ranges);
    sorted.sort(Comparator
      .comparingLong(Range::start)
      .thenComparingLong(Range::end));

    List<Range> merged = new ArrayList<>(); // not overlap

    Range current = sorted.get(0);

    for (int i = 1; i < sorted.size(); i++) {
      Range next = sorted.get(i); // next range to consider

      if (next.start() > current.end() + 1) {
        merged.add(current); // if next is after current
        current = next;
      } else {

        long newEnd = Math.max(current.end(), next.end()); // max from both
        current = new Range(current.start(), newEnd);
      }
    }

    merged.add(current);
    return merged;
  }

  static boolean isFresh(long id, List<Range> ranges) {
    for (Range range : ranges) {
      if (id >= range.start() && id <= range.end()) {
        return true;
      }
    }

    return false;
  }

  static ParsedDatabase parseDatabase(String rawInput) {
    // ranges -- ids
    String[] parts = rawInput.split("\\R\\R", 2);
    if (parts.length != 2) {
      throw new IllegalArgumentException("Input does not contain two blocks separated by a black line");
    }

    String rangesBlock = parts[0];
    String idsBlock = parts[1];

    List<Range> ranges = rangesBlock
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .map(E05_Cafeteria::parseRangeLine)
      .toList();

    List<Long> ingredientsIds = idsBlock
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .map(Long::parseLong)
      .toList();

    return new ParsedDatabase(ranges, ingredientsIds);
  }

  private static Range parseRangeLine(String line) {
    String[] parts = line.split("-");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid range line: " + line);
    }

    long start = Long.parseLong(parts[0]);
    long end = Long.parseLong(parts[1]);

    return new Range(start, end);
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E05_Cafeteria());
    ConsoleRunner.main(new String[]{"AOC25-05"});
  }
}
