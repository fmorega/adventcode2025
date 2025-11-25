package com.fran.practice.strings;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

import java.util.Arrays;

public class E03_isAnagram implements Exercise {

  public static boolean solve(String paramA, String paramB){
    if (paramA == null || paramB == null) return false;

    char[] charA = paramA.replaceAll("\\s+", "").toLowerCase().toCharArray();
    char[] charB = paramB.replaceAll("\\s+", "").toLowerCase().toCharArray();

    Arrays.sort(charA);
    Arrays.sort(charB);
    return Arrays.equals(charA, charB);
  }


  @Override
  public String id() {
    return "STR-03";
  }

  @Override
  public String title() {
    return "¿Son anagramas?";
  }

  @Override
  public void run() {

    System.out.println(solve("Romar", "amor"));
    System.out.println(solve("Listen", "Silent"));

  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E03_isAnagram());
    ConsoleRunner.main(new String[]{"STR-03"});
  }
}
