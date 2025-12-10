package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class E07_Laboratories implements Exercise {

  private static final String INPUT_DAY_SEVEN = "inputs/day07-2025.txt";
  private record BeamState(int rowIndex, int columnIndex) { }


  @Override
  public String id() {
    return "AOC25-07";
  }

  @Override
  public String title() {
    return "Laboratories - Tachyon Manifold";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_SEVEN);

    try {
      String rawInput = Files.readString(inputPath);
      char[][] manifoldGrid = parseManifold(rawInput);

      long splitCountPart1 = countClassicalSplits(manifoldGrid);
      System.out.println("Part 1 - Total splits: " + splitCountPart1);

      long totalTimelinesPart2 = countQuantumTimelines(manifoldGrid);
      System.out.println("Part 2 - Total quantum timelines: " + totalTimelinesPart2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static char[][] parseManifold(String rawInput) {
    List<String> nonEmptyLines = rawInput
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .toList();

    int rowCount = nonEmptyLines.size();
    int columnCount = nonEmptyLines.get(0).length();
    char[][] grid = new char[rowCount][columnCount];

    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      String line = nonEmptyLines.get(rowIndex);
      if (line.length() != columnCount) {
        throw new IllegalArgumentException("Non-rectangular grid at row " + rowIndex);
      }
      grid[rowIndex] = line.toCharArray();
    }

    return grid;
  }

  static BeamState findStart(char[][] grid) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;

    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        if (grid[rowIndex][columnIndex] == 'S') {
          return new BeamState(rowIndex, columnIndex);
        }
      }
    }

    throw new IllegalStateException("No starting point 'S' found in manifold");
  }

  static long countClassicalSplits(char[][] grid) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;

    BeamState startPosition = findStart(grid);

    boolean[][] visitedCellByBeam = new boolean[rowCount][columnCount]; // tracks cells already reached by at least one beam
    Queue<BeamState> pendingBeamsQueue = new ArrayDeque<>();

    visitedCellByBeam[startPosition.rowIndex()][startPosition.columnIndex()] = true;
    pendingBeamsQueue.add(startPosition);

    long totalSplitEvents = 0L;

    while (!pendingBeamsQueue.isEmpty()) {
      BeamState currentBeam = pendingBeamsQueue.poll();
      int currentRowIndex = currentBeam.rowIndex();
      int currentColumnIndex = currentBeam.columnIndex();

      int nextRowIndex = currentRowIndex + 1;                 // beams always move downward
      if (nextRowIndex >= rowCount) {
        continue;                                             // beam exits the manifold
      }

      char cellBelow = grid[nextRowIndex][currentColumnIndex];

      if (cellBelow == '.' || cellBelow == 'S') {
        // Beam continues straight down through empty space or S
        if (!visitedCellByBeam[nextRowIndex][currentColumnIndex]) {
          visitedCellByBeam[nextRowIndex][currentColumnIndex] = true;
          pendingBeamsQueue.add(new BeamState(nextRowIndex, currentColumnIndex));
        }
      } else if (cellBelow == '^') {
        // Beam hits a splitter: classical split event
        totalSplitEvents++;

        int leftColumnIndex = currentColumnIndex - 1;
        if (leftColumnIndex >= 0) {
          if (!visitedCellByBeam[nextRowIndex][leftColumnIndex]) {
            visitedCellByBeam[nextRowIndex][leftColumnIndex] = true;
            pendingBeamsQueue.add(new BeamState(nextRowIndex, leftColumnIndex));
          }
        }

        int rightColumnIndex = currentColumnIndex + 1;
        if (rightColumnIndex < columnCount) {
          if (!visitedCellByBeam[nextRowIndex][rightColumnIndex]) {
            visitedCellByBeam[nextRowIndex][rightColumnIndex] = true;
            pendingBeamsQueue.add(new BeamState(nextRowIndex, rightColumnIndex));
          }
        }
      }
    }

    return totalSplitEvents;
  }

  // Part 2: quantum manifold, count how many different timelines exist
  static long countQuantumTimelines(char[][] grid) {
    int rowCount = grid.length;
    int columnCount = grid[0].length;

    BeamState startPosition = findStart(grid);

    long[][] timelineCounts = new long[rowCount][columnCount]; // number of timelines passing through each cell

    timelineCounts[startPosition.rowIndex()][startPosition.columnIndex()] = 1L; // single particle at start

    int startRowIndex = startPosition.rowIndex();

    // Propagate timelines from the starting row down to the last row
    for (int rowIndex = startRowIndex; rowIndex < rowCount - 1; rowIndex++) {
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        long timelinesHere = timelineCounts[rowIndex][columnIndex];
        if (timelinesHere == 0L) {
          continue;                                           // no timelines at this cell
        }

        int nextRowIndex = rowIndex + 1;
        char cellBelow = grid[nextRowIndex][columnIndex];

        if (cellBelow == '.' || cellBelow == 'S') {
          // straight down
          timelineCounts[nextRowIndex][columnIndex] += timelinesHere;
        } else if (cellBelow == '^') {
          // At a splitter, each existing timeline splits into two branches

          int leftColumnIndex = columnIndex - 1;
          if (leftColumnIndex >= 0) {
            timelineCounts[nextRowIndex][leftColumnIndex] += timelinesHere;
          }

          int rightColumnIndex = columnIndex + 1;
          if (rightColumnIndex < columnCount) {
            timelineCounts[nextRowIndex][rightColumnIndex] += timelinesHere;
          }
        }
      }
    }

    long totalTimelines = 0L;
    int lastRowIndex = rowCount - 1;

    // sum all timelines
    for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
      totalTimelines += timelineCounts[lastRowIndex][columnIndex];
    }

    return totalTimelines;
  }


  public static void main(String[] args) {
    ConsoleRunner.register(new E07_Laboratories());
    ConsoleRunner.main(new String[]{"AOC25-07"});
  }
}
