package com.fran.practice.adventcode2024;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class E03_MullItOver implements Exercise {
  @Override
  public String id() {
    return "AOC24-03";
  }

  @Override
  public String title() {
    return "Mull it over - Total Value";
  }

  @Override
  public void run() {

    Path inputPath = Path.of("inputs/day03.txt");

    if (Files.exists(inputPath)) {
      try { // singleton
        String memory = Files.readString(inputPath);
        long total = scanFile(memory);

        System.out.println("Memory total: " + memory.length());
        System.out.println("Sum total mul(x,y): " + total);

      } catch (IOException e) {
        System.out.println("Error reading input file: " + e.getMessage());
      }
    } else {
      System.out.println();
      System.out.println("The file could not be found inputs/day03.txt.");
    }
  }

  public static long scanFile(String memory) {

    int pos;
    long total = 0L;

    for (int i = 0; i <= memory.length() - 4; i++){
      if (
        memory.charAt(i) == 'm' &&
          memory.charAt(i + 1) == 'u' &&
          memory.charAt(i + 2) == 'l' &&
          memory.charAt(i + 3) == '('

      ) {
        pos = i + 4;

        // first number x
        int startX = pos;
        while (pos < memory.length() && Character.isDigit(memory.charAt(pos))){
          pos++;
        }

        int lenghtX = pos - startX;

        // between 1-3 digits
        if (lenghtX < 1 || lenghtX > 3){
          continue;
        }

        String xText = memory.substring(startX, pos);
        int x = Integer.parseInt(xText);

        // comma?
        if (pos >= memory.length() || memory.charAt(pos) != ','){
          continue; // not valid
        }
        pos++; // jump over it

        // second number y
        int startY = pos;
        while (pos < memory.length() && Character.isDigit(memory.charAt(pos))){
          pos++;
        }
        int lenghtY = pos - startY;

        if (lenghtY < 1 || lenghtY > 3){
          continue;
        }

        String yText = memory.substring(startY, pos);
        int y = Integer.parseInt(yText);

        // check ')'
        if (pos >= memory.length() || memory.charAt(pos) != ')') {
          continue;
        }

        int finalResult = x * y;
        total += finalResult;
      }
    }
    return total;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E03_MullItOver());
    ConsoleRunner.main(new String[]{"AOC24-03"});
  }
}
