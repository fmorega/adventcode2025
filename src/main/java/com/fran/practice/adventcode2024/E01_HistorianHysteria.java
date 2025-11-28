package com.fran.practice.adventcode2024;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class E01_HistorianHysteria implements Exercise {
  @Override
  public String id() {
    return "AOC24-01";
  }

  @Override
  public String title() {
    return "Historian Hysteria - Total Distance";
  }

  @Override
  public void run() {
    // ejemplo enunciado
    List<String> exampleLines = List.of(
      "3   4",
      "4   3",
      "2   5",
      "1   3",
      "3   9",
      "3   3"
    );

    LocationLists exampleList = parseInput(exampleLines);
    long exampleResult = computeTotalDistance(exampleList.left(), exampleList.right());
    System.out.println("Example result = " + exampleResult);

    Path inputPath = Path.of("inputs/day01.txt");

    if (Files.exists(inputPath)) {
      try {
        List<String> inputLines = Files.readAllLines(inputPath);
        LocationLists realLists = parseInput(inputLines);
        long realResult = computeTotalDistance(realLists.left(), realLists.right());

        System.out.println("Real input: " + realResult);
      } catch (IOException e) {
        System.out.println("Error reading input file: " + e.getMessage());
      }
    } else {
      System.out.println();
      System.out.println("The file could not be found inputs/day01.txt.");
    }
  }

  // record for aggrupation purposes
  public record LocationLists(List<Integer> left, List<Integer> right) {}

  // parse input to both lists

  public static LocationLists parseInput(List<String> lines) {
    List<Integer> left = new ArrayList<>();
    List<Integer> right = new ArrayList<>();

    for (String rawLine : lines) {
      String line = rawLine.trim();
      if (line.isEmpty()){
        continue;
      }

      // divide by spaces
      String[] parts = line.split("\\s+");
      if (parts.length < 2) {
        throw new IllegalArgumentException(
          "Invalid line" + line
        );
      }

      int leftValue = Integer.parseInt(parts[0]);
      int rightValue = Integer.parseInt(parts[1]);

      left.add(leftValue);
      right.add(rightValue);
    }

    if (left.size() != right.size()) {
      throw new IllegalArgumentException(
        "Different size " + left.size() + " vs " + right.size()
      );
    }
    return new LocationLists(left, right);
  }

  // calculate total
  public static long computeTotalDistance(List<Integer> left, List<Integer> right) {
    if (left.size() != right.size()) {
      throw new IllegalArgumentException("Both lists must be equals in size");
    }

    // new ones for no modify the originals
    List<Integer> leftSorted = new ArrayList<>(left);
    List<Integer> rightSorted = new ArrayList<>(right);

    Collections.sort(leftSorted);
    Collections.sort(rightSorted);

    long totalDistance = 0L;

    // sum
    for (int i = 0; i < leftSorted.size(); i++) {
      long leftValue = leftSorted.get(i);
      long rightValue = rightSorted.get(i);
      long distance = Math.abs(leftValue - rightValue);
      totalDistance += distance;
    }
    return totalDistance;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E01_HistorianHysteria());
    ConsoleRunner.main(new String[]{"AOC24-01"});
  }
}
