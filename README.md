# adventcode2025 🧩☕

Repositorio personal para practicar Java a base de retos:

- **Advent of Code** (2024/2025).
- Ejercicios y katas varios (strings, grids, etc.).
- Todo organizado en pequeños **ejercicios autocontenidos** que se ejecutan con un `ConsoleRunner`.

La idea es tener un “laboratorio” de Java donde ir añadiendo ejercicios cortos, limpios y bien estructurados.

---

## 🧱 Tecnologías

- Java (>= 17 recomendado)
- Maven
- IntelliJ IDEA (opcional, pero es el IDE con el que está pensado el proyecto)

---

## 📁 Estructura del proyecto

Ejemplo de estructura (irá creciendo):

```text
src/
└── main/
    └── java/
        └── com/
            └── fran/
                └── practice/
                    ├── common/
                    │   ├── ConsoleRunner.java   # Pequeño runner de consola
                    │   └── Exercise.java        # Interfaz común para los ejercicios
                    ├── strings/
                    │   └── E03_isAnagram.java   # Ejercicio: comprobar si dos cadenas son anagramas
                    └── adventcode2024/
                        └── E10_HoffIt.java      # Ejercicio de Advent of Code (ejemplo)
                    └── adventcode2025/
                        └── ...                  # Próximos ejercicios de AoC
