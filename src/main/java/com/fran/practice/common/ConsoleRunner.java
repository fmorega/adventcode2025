package com.fran.practice.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class ConsoleRunner {

  private static final Map<String, Exercise> REGISTRY = new LinkedHashMap<>();
  public static void register(Exercise exercise){
    REGISTRY.put(exercise.id(), exercise);
  }

  public static void main(String[] args) {
    if (args.length == 0){
      System.out.println("Ejercicios disponibles: ");
      REGISTRY.values().forEach(
        exercise -> System.out.println(" " + exercise.id() + " - " + exercise.title())
      );
      System.out.println("\nEjecuta: java ... ConsoleRunner<ID>");
      return;
    }
    Exercise exercise = REGISTRY.get(args[0]);
    if (exercise == null){
      System.out.println("No existe el ejercicio " + args[0]);
      return;
    }
    System.out.println("== " + exercise.id() + " :: " + exercise.title() + " ==");
    exercise.run();
  }


}
