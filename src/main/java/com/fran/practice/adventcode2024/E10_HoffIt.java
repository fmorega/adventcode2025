package com.fran.practice.adventcode2024;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.util.List;

public class E10_HoffIt implements Exercise {
  @Override
  public String id() {
    return "E10";
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
  }

  public static int solve(List<String> lines) {

    int[][] map = parseMap(lines);
    int rows = map.length;
    int cols = map[0].length;

    int totalScore = 0;

    for (int r = 0; r < rows; r++){
      for (int c = 0; c < cols; c++){
        if (map[r][c] == 0){
          totalScore += scoreTrailhead(map, r, c);
        }
      }
    }

    return totalScore;
  }

  private static int[][] parseMap(List<String> lines) {
    int rows = lines.size();
    int cols = lines.get(0).length();
    int[][] map = new int[rows][cols];

    for (int r = 0; r < rows; r++) {
      String line = lines.get(r);
      for (int c = 0; c < cols; c++) {
        char ch = line.charAt(c);
        if (ch == '.') {
          map[r][c] = -1; // casilla impasable
        } else {
          map[r][c] = ch - '0';
        }
      }
    }

    return map;
  }

  /**
   * Calcula la puntuación de un trailhead concreto (celda con altura 0):
   * número de posiciones distintas con altura 9 alcanzables desde él
   * subiendo siempre de +1 en +1.
   */
  private static int scoreTrailhead(int[][] map, int startR, int startC) {
    int rows = map.length;
    int cols = map[0].length;

    // reachable9[r][c] = true si ese 9 es alcanzable desde este trailhead
    boolean[][] reachable9 = new boolean[rows][cols];

    dfs(map, startR, startC, 0, reachable9);

    int score = 0;
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        if (reachable9[r][c]) {
          score++;
        }
      }
    }
    return score;
  }

  /**
   * DFS que sigue caminos que aumentan exactamente en 1 la altura.
   *
   * @param expectedHeight altura que esperamos en esta celda (0..9)
   */
  private static void dfs(int[][] map, int r, int c, int expectedHeight, boolean[][] reachable9) {
    int rows = map.length;
    int cols = map[0].length;

    // Fuera de límites
    if (r < 0 || r >= rows || c < 0 || c >= cols) {
      return;
    }

    // Casilla impasable o altura distinta a la esperada
    if (map[r][c] != expectedHeight) {
      return;
    }

    // Si llegamos a un 9, lo marcamos y no seguimos más desde aquí
    if (expectedHeight == 9) {
      reachable9[r][c] = true;
      return;
    }

    int nextHeight = expectedHeight + 1;

    // Arriba
    dfs(map, r - 1, c, nextHeight, reachable9);
    // Abajo
    dfs(map, r + 1, c, nextHeight, reachable9);
    // Izquierda
    dfs(map, r, c - 1, nextHeight, reachable9);
    // Derecha
    dfs(map, r, c + 1, nextHeight, reachable9);
  }

  /**
   * Main para integrarlo con tu ConsoleRunner.
   */
  public static void main(String[] args) {
    ConsoleRunner.register(new E10_HoffIt());
    ConsoleRunner.main(new String[]{"E10"});
  }





















}
