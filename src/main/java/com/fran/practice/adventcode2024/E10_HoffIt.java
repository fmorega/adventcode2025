package com.fran.practice.adventcode2024;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.util.List;
import java.util.stream.IntStream;

public class E10_HoffIt implements Exercise {
  @Override
  public String id() {
    return "AOC24-E10";
  }

  @Override
  public String title() {
    return "Topographic Hiking Trails";
  }

  @Override
  public void run() {

    List<String> example = List.of(
      "89010123",
      "78121874",
      "87430965",
      "96549874",
      "45678903",
      "32019012",
      "01329801",
      "10456732"
    );

    int result = solve(example);
    System.out.println("Resultado del ejemplo = " + result + " (esperado: 36)");

    // from file
/*    try {
      List<String> inputLines = Files.readAllLines(Path.of("input.txt"));
      int resultFromFile = solve(inputLines);
      System.out.println("Result from file: " + resultFromFile);
    } catch (IOException e){
      System.err.println("Can't read input.txt: " + e.getMessage());
    }*/
  }

  // main method: from map as a textlines to sum of all trailheads
  public static int solve(List<String> mapLines) {

    int[][] heightMap = parseHeightMap(mapLines);

    int rowCount = heightMap.length;
    int columnCount = heightMap[0].length;

    return IntStream.range(0, rowCount)
      .boxed()
      .flatMap(rowIndex -> IntStream.range(0, columnCount)
        .filter(columnIndex -> heightMap[rowIndex][columnIndex] == 0)
        .mapToObj(columnIndex -> computeTrailheadScore(heightMap, rowIndex, columnIndex)))
      .mapToInt(Integer::intValue)
      .sum();
  }

  // turns each line into a num file
  private static int[][] parseHeightMap(List<String> mapLines) {
    int rowCount = mapLines.size();
    int columnCount = mapLines.get(0).length();

    int[][] heightMap = new int[rowCount][columnCount];

    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      String line = mapLines.get(rowIndex);
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        char cellChar = line.charAt(columnIndex);
        if (cellChar == '.') {
          heightMap[rowIndex][columnIndex] = -1; // just in case
        } else {
          // turns '0'..'9' --> 0..9
          heightMap[rowIndex][columnIndex] = cellChar - '0';
        }
      }
    }

    return heightMap;
  }

  // the score will be the num of diff rows with height 9, achievable 1 by 1
  private static int computeTrailheadScore(int[][] heightMap,
                                           int startRow,
                                           int startColumn) {

    int rowCount = heightMap.length;
    int columnCount = heightMap[0].length;

    // reachableNines[row][column] will be true if that 9 is achievable
    // desde este trailhead siguiendo las reglas del ejercicio.
    boolean[][] reachableNines = new boolean[rowCount][columnCount];

    // deep search
    exploreTrail(heightMap, startRow, startColumn, 0, reachableNines);

    // count diff 9s achievable
    int score = 0;
    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
        if (reachableNines[rowIndex][columnIndex]) {
          score++;
        }
      }
    }

    return score;
  }

  private static void exploreTrail(int[][] heightMap,
                                   int row,
                                   int column,
                                   int expectedHeight,
                                   boolean[][] reachableNines) {

    int rowCount = heightMap.length;
    int columnCount = heightMap[0].length;

    // check limits
    if (row < 0 || row >= rowCount || column < 0 || column >= columnCount) {
      return;
    }

    // check height
    if (heightMap[row][column] != expectedHeight) {
      return;
    }

    if (expectedHeight == 9) {
      reachableNines[row][column] = true;
      return;
    }

    int nextExpectedHeight = expectedHeight + 1;

    exploreTrail(heightMap, row - 1, column, nextExpectedHeight, reachableNines); // up
    exploreTrail(heightMap, row + 1, column, nextExpectedHeight, reachableNines); // down
    exploreTrail(heightMap, row, column - 1, nextExpectedHeight, reachableNines); // left
    exploreTrail(heightMap, row, column + 1, nextExpectedHeight, reachableNines); // right
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E10_HoffIt());
    ConsoleRunner.main(new String[]{"AOC24-E10"});
  }
}
