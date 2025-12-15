package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.List;
import java.util.TreeSet;

public class E09_MovieTheater implements Exercise {

  private static final String INPUT_DAY_NINE = "inputs/day09-2025.txt";

  private record Point(int x, int y) {
  }

  @Override
  public String id() {
    return "AOC25-09";
  }

  @Override
  public String title() {
    return "Movie Theater - Largest Rectangle";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_NINE);

    try {
      String rawInput = Files.readString(inputPath);
      List<Point> redTiles = parseRedTiles(rawInput);

      long maxArea = solvePart1(redTiles);
      System.out.println("Part 1 - Max rectangle area: " + maxArea);

      long part2 = solvePart2(redTiles);
      System.out.println("Part 2 - Max rectangle area (only red/green): " + part2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static List<Point> parseRedTiles(String rawInput) {
    return rawInput
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .map(E09_MovieTheater::parsePoint)
      .toList();
  }

  private static Point parsePoint(String line) {
    String[] parts = line.split(",");
    if (parts.length != 2) {
      throw new IllegalArgumentException("Invalid coordinate line: " + line);
    }
    int x = Integer.parseInt(parts[0].trim());
    int y = Integer.parseInt(parts[1].trim());
    return new Point(x, y);
  }

  static long solvePart1(List<Point> redTiles) {
    int tileCount = redTiles.size();
    long maxArea = 0L;

    for (int i = 0; i < tileCount; i++) {
      Point p1 = redTiles.get(i);
      for (int j = i + 1; j < tileCount; j++) {
        Point p2 = redTiles.get(j);

        long width = Math.abs((long) p1.x() - p2.x()) + 1;
        long height = Math.abs((long) p1.y() - p2.y()) + 1;
        long area = width * height;

        if (area > maxArea) {
          maxArea = area;
        }
      }
    }

    return maxArea;
  }

  static long solvePart2(List<Point> redTiles) {
    if (redTiles.size() < 2) {
      return 0L;
    }

    CompressedRegion region = buildCompressedAllowedRegion(redTiles);
    long[][] prefixAllowedWeight = buildWeightedPrefixSum(region.allowed, region.cellWeights);

    long bestArea = 0L;
    int redCount = redTiles.size();

    for (int firstIndex = 0; firstIndex < redCount; firstIndex++) {
      Point firstRed = redTiles.get(firstIndex);

      for (int secondIndex = firstIndex + 1; secondIndex < redCount; secondIndex++) {
        Point secondRed = redTiles.get(secondIndex);

        int minX = Math.min(firstRed.x(), secondRed.x());
        int maxX = Math.max(firstRed.x(), secondRed.x());
        int minY = Math.min(firstRed.y(), secondRed.y());
        int maxY = Math.max(firstRed.y(), secondRed.y());

        long rectangleTileCount = ((long) (maxX - minX) + 1) * ((long) (maxY - minY) + 1);

        int x1Index = lowerBound(region.xCoords, minX);
        int x2IndexExclusive = lowerBound(region.xCoords, maxX + 1);
        int y1Index = lowerBound(region.yCoords, minY);
        int y2IndexExclusive = lowerBound(region.yCoords, maxY + 1);

        if (x1Index < 0 || y1Index < 0 || x2IndexExclusive <= x1Index || y2IndexExclusive <= y1Index) {
          continue;
        }
        if (x2IndexExclusive > region.cellWeights[0].length || y2IndexExclusive > region.cellWeights.length) {
          continue;
        }

        long allowedWeight = queryWeightedPrefixSum(prefixAllowedWeight, x1Index, y1Index, x2IndexExclusive - 1, y2IndexExclusive - 1);
        if (allowedWeight == rectangleTileCount) {
          if (rectangleTileCount > bestArea) {
            bestArea = rectangleTileCount;
          }
        }
      }
    }

    return bestArea;
  }

  private record CompressedRegion(int[] xCoords, int[] yCoords, boolean[][] allowed, long[][] cellWeights) { }

  static CompressedRegion buildCompressedAllowedRegion(List<Point> redTiles) {
    int redCount = redTiles.size();

    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;

    for (Point p : redTiles) {
      minX = Math.min(minX, p.x());
      maxX = Math.max(maxX, p.x());
      minY = Math.min(minY, p.y());
      maxY = Math.max(maxY, p.y());
    }

    TreeSet<Integer> xSet = new TreeSet<>();
    TreeSet<Integer> ySet = new TreeSet<>();

    xSet.add(minX - 1);
    xSet.add(maxX + 2);
    ySet.add(minY - 1);
    ySet.add(maxY + 2);

    for (Point p : redTiles) {
      xSet.add(p.x());
      xSet.add(p.x() + 1);
      ySet.add(p.y());
      ySet.add(p.y() + 1);
    }

    for (int i = 0; i < redCount; i++) {
      Point a = redTiles.get(i);
      Point b = redTiles.get((i + 1) % redCount);

      if (a.x() == b.x()) {
        int x = a.x();
        xSet.add(x);
        xSet.add(x + 1);
        ySet.add(Math.min(a.y(), b.y()));
        ySet.add(Math.max(a.y(), b.y()) + 1);
      } else if (a.y() == b.y()) {
        int y = a.y();
        ySet.add(y);
        ySet.add(y + 1);
        xSet.add(Math.min(a.x(), b.x()));
        xSet.add(Math.max(a.x(), b.x()) + 1);
      } else {
        throw new IllegalArgumentException("must share row or column " + a + " -> " + b);
      }
    }

    int[] xCoords = xSet.stream().mapToInt(Integer::intValue).toArray();
    int[] yCoords = ySet.stream().mapToInt(Integer::intValue).toArray();

    int cellWidthCount = xCoords.length - 1;
    int cellHeightCount = yCoords.length - 1;

    boolean[][] boundaryBlocked = new boolean[cellHeightCount][cellWidthCount];

    for (int i = 0; i < redCount; i++) {
      Point a = redTiles.get(i);
      Point b = redTiles.get((i + 1) % redCount);

      int x1 = a.x();
      int y1 = a.y();
      int x2 = b.x();
      int y2 = b.y();

      if (x1 == x2) {
        int yStart = Math.min(y1, y2);
        int yEndExclusive = Math.max(y1, y2) + 1;

        int xCellIndex = lowerBound(xCoords, x1);
        int yCellStartIndex = lowerBound(yCoords, yStart);
        int yCellEndIndexExclusive = lowerBound(yCoords, yEndExclusive);

        for (int yCellIndex = yCellStartIndex; yCellIndex < yCellEndIndexExclusive; yCellIndex++) {
          boundaryBlocked[yCellIndex][xCellIndex] = true;
        }
      } else {
        int xStart = Math.min(x1, x2);
        int xEndExclusive = Math.max(x1, x2) + 1;

        int yCellIndex = lowerBound(yCoords, y1);
        int xCellStartIndex = lowerBound(xCoords, xStart);
        int xCellEndIndexExclusive = lowerBound(xCoords, xEndExclusive);

        for (int xCellIndex = xCellStartIndex; xCellIndex < xCellEndIndexExclusive; xCellIndex++) {
          boundaryBlocked[yCellIndex][xCellIndex] = true;
        }
      }
    }

    boolean[][] outside = new boolean[cellHeightCount][cellWidthCount];
    ArrayDeque<int[]> queue = new ArrayDeque<>();

    queue.add(new int[]{0, 0});
    outside[0][0] = true;

    int[] dx = {1, -1, 0, 0};
    int[] dy = {0, 0, 1, -1};

    while (!queue.isEmpty()) {
      int[] cell = queue.poll();
      int currentYCell = cell[0];
      int currentXCell = cell[1];

      for (int k = 0; k < 4; k++) {
        int nextYCell = currentYCell + dy[k];
        int nextXCell = currentXCell + dx[k];

        if (nextYCell < 0 || nextYCell >= cellHeightCount || nextXCell < 0 || nextXCell >= cellWidthCount) {
          continue;
        }
        if (outside[nextYCell][nextXCell]) {
          continue;
        }
        if (boundaryBlocked[nextYCell][nextXCell]) {
          continue;
        }

        outside[nextYCell][nextXCell] = true;
        queue.add(new int[]{nextYCell, nextXCell});
      }
    }

    boolean[][] allowed = new boolean[cellHeightCount][cellWidthCount];
    for (int yCellIndex = 0; yCellIndex < cellHeightCount; yCellIndex++) {
      for (int xCellIndex = 0; xCellIndex < cellWidthCount; xCellIndex++) {
        allowed[yCellIndex][xCellIndex] = boundaryBlocked[yCellIndex][xCellIndex] || !outside[yCellIndex][xCellIndex];
      }
    }

    long[][] cellWeights = new long[cellHeightCount][cellWidthCount];
    for (int yCellIndex = 0; yCellIndex < cellHeightCount; yCellIndex++) {
      long cellHeight = (long) yCoords[yCellIndex + 1] - yCoords[yCellIndex];
      for (int xCellIndex = 0; xCellIndex < cellWidthCount; xCellIndex++) {
        long cellWidth = (long) xCoords[xCellIndex + 1] - xCoords[xCellIndex];
        cellWeights[yCellIndex][xCellIndex] = cellWidth * cellHeight;
      }
    }

    return new CompressedRegion(xCoords, yCoords, allowed, cellWeights);
  }

  static long[][] buildWeightedPrefixSum(boolean[][] allowed, long[][] cellWeights) {
    int height = allowed.length;
    int width = allowed[0].length;

    long[][] prefix = new long[height + 1][width + 1];

    for (int y = 1; y <= height; y++) {
      long rowRunningSum = 0L;
      for (int x = 1; x <= width; x++) {
        long contribution = allowed[y - 1][x - 1] ? cellWeights[y - 1][x - 1] : 0L;
        rowRunningSum += contribution;
        prefix[y][x] = prefix[y - 1][x] + rowRunningSum;
      }
    }

    return prefix;
  }

  static long queryWeightedPrefixSum(long[][] prefix, int x1, int y1, int x2, int y2) {
    int ax2 = x2 + 1;
    int ay2 = y2 + 1;

    return prefix[ay2][ax2]
      - prefix[y1][ax2]
      - prefix[ay2][x1]
      + prefix[y1][x1];
  }

  static int lowerBound(int[] sortedValues, int target) {
    int lo = 0;
    int hi = sortedValues.length;
    while (lo < hi) {
      int mid = (lo + hi) >>> 1;
      if (sortedValues[mid] < target) {
        lo = mid + 1;
      } else {
        hi = mid;
      }
    }
    return lo;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E09_MovieTheater());
    ConsoleRunner.main(new String[]{"AOC25-09"});
  }
}
