package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class E04_PrintingDepartment implements Exercise {

  private static final String INPUT_DAY_FOUR = "inputs/day04-2025.txt";

  @Override
  public String id() {
    return "AOC25-04";
  }

  @Override
  public String title() {
    return "Printing Department - Accessible Paper Rolls";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_FOUR);

    try {
      String rawInput = Files.readString(inputPath);
      char[][] grid = parseGrid(rawInput);

      long accessibleCount = solvePart1(grid);
      long removedCountPart2 = solvePart2(grid);

      System.out.println("Part 1 - Accessible paper rolls: " + accessibleCount);
      System.out.println("Part 2 - Total removed paper rolls: " + removedCountPart2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static char[][] parseGrid(String rawInput) {
    List<String> lines = rawInput
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .toList();

    int rows = lines.size();
    int cols = lines.get(0).length();
    char[][] grid = new char[rows][cols];

    for (int r = 0; r < rows; r++) {
      String line = lines.get(r);
      if (line.length() != cols) {
        throw new IllegalArgumentException("Non-rectangular grid at row " + r);
      }
      grid[r] = line.toCharArray();
    }

    return grid;
  }

  static long solvePart1(char[][] grid) {
    int rows = grid.length;
    int cols = grid[0].length;

    long accessibleCount = 0L;

    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (grid[r][c] == '@' && isAccessible(grid, r, c)) {
          accessibleCount++;
        }
      }
    }

    return accessibleCount;
  }

  static long solvePart2(char[][] originalGrid) {
    int rows = originalGrid.length;
    int cols = originalGrid[0].length;

    // not the original
    char[][] grid = copyGrid(originalGrid);

    long totalRemoved = 0L;

    while (true) {
      boolean[][] toRemove = new boolean[rows][cols];
      int removedThisWave = 0;

      // accesible '@'s'
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          if (grid[r][c] == '@' && isAccessible(grid, r, c)) {
            toRemove[r][c] = true;
            removedThisWave++;
          }
        }
      }

      if (removedThisWave == 0) {
        break;
      }

      // remove all accesibles in that wave
      for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
          if (toRemove[r][c]) {
            grid[r][c] = '.';
          }
        }
      }

      totalRemoved += removedThisWave;
    }

    return totalRemoved;
  }

  static boolean isAccessible(char[][] grid, int row, int col) {
    int rows = grid.length;
    int cols = grid[0].length;

    // 8 neighbors (n, w, s...)
    int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
    int[] dc = {0, 1, 1, 1, 0, -1, -1, -1};

    int neighboursWithRolls = 0;

    for (int i = 0; i < 8; i++) {
      int nr = row + dr[i];
      int nc = col + dc[i];

      if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) {
        continue; // out of limits
      }

      if (grid[nr][nc] == '@') {
        neighboursWithRolls++;
        // already +4, no need to keep looking
        if (neighboursWithRolls >= 4) {
          return false;
        }
      }
    }
    // fewer 4
    return true;
  }

  // deep copy
  static char[][] copyGrid(char[][] source) {
    int rows = source.length;
    char[][] copy = new char[rows][];
    for (int r = 0; r < rows; r++) {
      copy[r] = source[r].clone();
    }
    return copy;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E04_PrintingDepartment());
    ConsoleRunner.main(new String[]{"AOC25-04"});
  }
}
