package com.github.vasatanasov.brickwork.core;

import com.github.vasatanasov.brickwork.io.InputReader;

import java.io.IOException;
import java.util.Arrays;

/**
 * Class used for reading and passing the input to main application class {@link
 * com.github.vasatanasov.brickwork.core.Brickwork}.
 *
 * Writes the output to the console.
 */
public class Manager {

  private final InputReader reader;
  private Brickwork brickwork;
  private int rows;
  private int cols;

  /**
   * @param reader Interface containing the readLine() method. Relays on dependency injection.
   *     Suitable for mock testing
   */
  public Manager(InputReader reader) {
    this.reader = reader;
  }

  /**
   * The main flow of the application runs her. Handles exceptions thrown during execution of the
   * brickwork algorithm.
   */
  public void run() {
    try {
      String[] tokens = reader.readLine().split("\\s+");
      rows = Integer.parseInt(tokens[0]);
      cols = Integer.parseInt(tokens[1]);
      brickwork = Brickwork.init(rows, cols);
      int[][] input = readInput();
      brickwork.setFirstLayer(input);
      brickwork.setSecondLayer();
      System.out.println(brickwork.getSecondLayer());
    } catch (IllegalArgumentException | IOException | ArrayIndexOutOfBoundsException i) {
      System.err.println("-1: No solution");
      System.exit(-1);
    }
  }

  public String toStringSecondLayer() {
    return brickwork.getSecondLayer().toString();
  }

  /**
   * Reads the first layer form the console and validates if the input number of rows and cols
   * matches the one given on the first line.
   *
   * @throws IOException
   * @throws IllegalArgumentException
   */
  private int[][] readInput() throws IOException, IllegalArgumentException {
    int[][] input = new int[rows][];
    int row = 0;
    while (true) {

      int[] numbers =
          Arrays.stream(reader.readLine().split("\\s+"))
              .map(String::trim)
              .filter(s -> !s.isEmpty())
              .mapToInt(Integer::parseInt)
              .toArray();

      if (row == rows) {
        if (numbers.length > 0) {
          System.err.println("Invalid number of rows");
          throw new IllegalArgumentException();
        }
        break;
      }

      if (numbers.length != cols) {
        System.err.println("Invalid number of cols");
        throw new IllegalArgumentException();
      }
      input[row] = numbers;
      ++row;
    }

    return input;
  }
}
