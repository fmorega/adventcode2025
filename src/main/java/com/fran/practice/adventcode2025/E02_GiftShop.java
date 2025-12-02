package com.fran.practice.adventcode2025;

import com.fran.practice.common.ConsoleRunner;
import com.fran.practice.common.Exercise;

public class E02_GiftShop implements Exercise {
  @Override
  public String id() {
    return "AOC25-02";
  }

  @Override
  public String title() {
    return "Gift Shop - Invalid IDs Sum";
  }

  @Override
  public void run() {
    String example = """
      11-22,95-115,998-1012,1188511880-1188511890,222220-222224,
      1698522-1698528,446443-446449,38593856-38593862,565653-565659,
      824824821-824824827,2121212118-2121212124""";

    String[] exampleParts = example.split(",");
    long sum = 0;

    for (String range : exampleParts) {
      range = range.trim();
      String[] bounds = range.split("-");
      long startExample = Long.parseLong(bounds[0]);
      long endExample = Long.parseLong(bounds[1]);

      //System.out.println("Range: " + startExample + " - " + endExample);

      for (long id = startExample; id <= endExample; id++) {
        if (isInvalidId(id)) {
          sum += id;
        }
      }
    }
    //System.out.println(Arrays.toString(exampleParts));
    System.out.println("Total sums : " + sum);
  }

  static boolean isInvalidId(long id) {
    String input = Long.toString(id);

    if (input.length() % 2 != 0) {
      return false;
    }

    int half = input.length() / 2;
    String left = input.substring(0, half);
    String right = input.substring(half);

    if (left.equals(right)) {
      System.out.println("Invalid: " + id + " (" + left + " + " + right + ")");
    }

    return left.equals(right);
  }

  public static void main(String[] args) {
    ConsoleRunner.register(new E02_GiftShop());
    ConsoleRunner.main(new String[]{"AOC25-02"});
  }
}
