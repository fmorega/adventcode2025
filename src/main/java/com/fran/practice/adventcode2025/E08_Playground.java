package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.*;

public class E08_Playground implements Exercise {

  private static final String INPUT_DAY_EIGHT = "inputs/day08-2025.txt";

  private record Point3D(long x, long y, long z) { }

  private record Edge(int fromIndex, int toIndex, long squaredDistance) { }

  @Override
  public String id() {
    return "AOC25-08";
  }

  @Override
  public String title() {
    return "Playground - Junction Box Circuits";
  }

  @Override
  public void run() {
    Path inputPath = Path.of(INPUT_DAY_EIGHT);

    try {
      String rawInput = Files.readString(inputPath);
      List<Point3D> junctionBoxes = parseJunctionBoxes(rawInput);

      long result = solvePart1(junctionBoxes, 1000);
      System.out.println("Part 1 - Product of three largest circuits: " + result);

      long resultPart2 = solvePart2(junctionBoxes);
      System.out.println("Part 2 - Product of X coordinates of the final connection: " + resultPart2);

    } catch (IOException e) {
      System.out.println("Error reading file: " + e.getMessage());
    }
  }

  static List<Point3D> parseJunctionBoxes(String rawInput) {
    return rawInput
      .lines()
      .map(String::trim)
      .filter(line -> !line.isEmpty())
      .map(E08_Playground::parsePoint3D)
      .toList();
  }

  private static Point3D parsePoint3D(String line) {
    String[] parts = line.split(",");
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid coordinate line: " + line);
    }
    long x = Long.parseLong(parts[0].trim());
    long y = Long.parseLong(parts[1].trim());
    long z = Long.parseLong(parts[2].trim());
    return new Point3D(x, y, z);
  }

  static long solvePart1(List<Point3D> junctionBoxes, int numberOfPairsToConnect) {
    int n = junctionBoxes.size();

    List<Edge> allEdges = generateAllEdges(junctionBoxes);

    allEdges.sort(Comparator.comparingLong(Edge::squaredDistance));

    int pairsToProcess = Math.min(numberOfPairsToConnect, allEdges.size());
    DisjointSetUnion dsu = new DisjointSetUnion(n);

    for (int i = 0; i < pairsToProcess; i++) {
      Edge edge = allEdges.get(i);
      dsu.union(edge.fromIndex(), edge.toIndex());
    }

    Map<Integer, Integer> componentSizes = new HashMap<>();
    for (int i = 0; i < n; i++) {
      int root = dsu.find(i);
      componentSizes.merge(root, 1, Integer::sum);
    }

    List<Integer> sizes = new ArrayList<>(componentSizes.values());
    sizes.sort(Comparator.reverseOrder());

    long a = sizes.get(0);
    long b = sizes.get(1);
    long c = sizes.get(2);

    return a * b * c;
  }

  static long solvePart2(List<Point3D> junctionBoxes) {
    int n = junctionBoxes.size();

    List<Edge> allEdges = generateAllEdges(junctionBoxes);
    allEdges.sort(Comparator.comparingLong(Edge::squaredDistance));

    DisjointSetUnion dsu = new DisjointSetUnion(n);
    int components = n;

    Edge lastEdgeUsed = null;

    for (Edge edge : allEdges) {
      int u = edge.fromIndex();
      int v = edge.toIndex();

      int rootU = dsu.find(u);
      int rootV = dsu.find(v);
      if (rootU == rootV) {
        continue;
      }

      dsu.union(rootU, rootV);
      components--;
      lastEdgeUsed = edge;

      if (components == 1) {
        break;
      }
    }

    if (lastEdgeUsed == null) {
      throw new IllegalStateException("Could not connect all junction boxes into a single circuit.");
    }

    Point3D p1 = junctionBoxes.get(lastEdgeUsed.fromIndex());
    Point3D p2 = junctionBoxes.get(lastEdgeUsed.toIndex());

    return p1.x() * p2.x();
  }


  static List<Edge> generateAllEdges(List<Point3D> junctionBoxes) {
    int n = junctionBoxes.size();
    List<Edge> edges = new ArrayList<>(n * (n - 1) / 2);

    for (int i = 0; i < n; i++) {
      Point3D p1 = junctionBoxes.get(i);
      for (int j = i + 1; j < n; j++) {
        Point3D p2 = junctionBoxes.get(j);
        long dist2 = squaredDistance(p1, p2);
        edges.add(new Edge(i, j, dist2));
      }
    }

    return edges;
  }

  static long squaredDistance(Point3D p1, Point3D p2) {
    long dx = p1.x() - p2.x();
    long dy = p1.y() - p2.y();
    long dz = p1.z() - p2.z();
    return dx * dx + dy * dy + dz * dz;
  }

  static class DisjointSetUnion {
    private final int[] parent;
    private final int[] rank;

    DisjointSetUnion(int size) {
      this.parent = new int[size];
      this.rank = new int[size];
      for (int i = 0; i < size; i++) {
        parent[i] = i;
        rank[i] = 0;
      }
    }

    int find(int x) {
      if (parent[x] != x) {
        parent[x] = find(parent[x]);
      }
      return parent[x];
    }

    void union(int x, int y) {
      int rootX = find(x);
      int rootY = find(y);
      if (rootX == rootY) {
        return;
      }
      if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
      } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
      } else {
        parent[rootY] = rootX;
        rank[rootX]++;
      }
    }
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E08_Playground());
    ConsoleRunner.main(new String[]{"AOC25-08"});
  }
}
