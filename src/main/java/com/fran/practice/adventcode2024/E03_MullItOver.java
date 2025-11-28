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
        long total = sumMulInstructions(memory);
        long totalWithConditions = sumMulInstructionsWithConditions(memory);

        System.out.println("Memory total: " + memory.length());
        System.out.println("Sum total mul(x,y): " + total);
        System.out.println("Sum total with conditions mul(x,y): " + totalWithConditions);

      } catch (IOException e) {
        System.out.println("Error reading input file: " + e.getMessage());
      }
    } else {
      System.out.println();
      System.out.println("The file could not be found inputs/day03.txt.");
    }
  }

  // just to sum all valids (x,y)
  public static long sumMulInstructions(String memory) {
    long total = 0L;

    for (int i = 0; i < memory.length(); i++) {
      Integer product = tryParseMulAt(memory, i);
      if (product != null){
        total += product;
      }
    }
    return total;
  }

  public static long sumMulInstructionsWithConditions(String memory) {
    long total = 0L;
    boolean enabled = true;

    for (int i = 0; i < memory.length(); i++) {

      // check don't()
      if (memory.startsWith("don't()", i)) {
        enabled = false;
        continue;
      }

      // check do()
      if (memory.startsWith("do()", i)) {
        enabled = true;
        continue;
      }

      if (!enabled) {
        continue;
      }

      // parse mul(X,Y) starting i
      Integer product = tryParseMulAt(memory, i);
      if (product != null) {
        total += product;
      }
    }

    return total;
  }

  public static Integer tryParseMulAt(String memory, int index) {

    if (!memory.startsWith("mul(", index)) {
      return null;
    }

    int pos = index + 4;

    // first number x
    int startX = pos;
    while (pos < memory.length() && Character.isDigit(memory.charAt(pos))){
      pos++;
    }
    int lenghtX = pos - startX;

    // between 1-3 digits
    if (lenghtX < 1 || lenghtX > 3){
      return null;
    }

    // comma?
    if (pos >= memory.length() || memory.charAt(pos) != ','){
      return null; // not valid
    }
    pos++; // jump over it

    // second number y
    int startY = pos;
    while (pos < memory.length() && Character.isDigit(memory.charAt(pos))){
      pos++;
    }
    int lenghtY = pos - startY;

    if (lenghtY < 1 || lenghtY > 3){
      return null;
    }

    // check ')'
    if (pos >= memory.length() || memory.charAt(pos) != ')') {
      return null;
    }

    //parse
    int x = Integer.parseInt(memory.substring(startX, startX + lenghtX));
    int y = Integer.parseInt(memory.substring(startY, startY + lenghtY));

    return x * y;
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E03_MullItOver());
    ConsoleRunner.main(new String[]{"AOC24-03"});
  }
}
