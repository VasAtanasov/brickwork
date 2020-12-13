package com.github.vasatanasov.brickwork.core;

import com.github.vasatanasov.brickwork.models.Layer;

import java.util.ArrayList;
import java.util.List;

/** Main application class contains the business logic for building the brick layers. */
public class Brickwork {

  // Integer arrays representing offsets by row and col for moving up, down, left and right in a
  // matrix.
  private static final int[] rowOffset = new int[] {0, -1, 0, 1};
  private static final int[] colOffset = new int[] {1, 0, -1, 0};

  private final int rows, cols;
  private final Layer firstLayer;
  private final Layer secondLayer;

  private Brickwork(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    firstLayer = Layer.of(rows, cols);
    secondLayer = Layer.of(rows, cols);
  }

  /**
   * Static factory method for instantiating the class
   *
   * @param rows
   * @param cols
   * @return
   */
  public static Brickwork init(int rows, int cols) {
    return new Brickwork(rows, cols);
  }

  public Layer getSecondLayer() {
    return secondLayer;
  }

  /**
   * Builds the first layer of bricks if valid bricks.
   *
   * @param input int array representing layer's layout.
   */
  public void setFirstLayer(int[][] input) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        isValidNumber(input[row][col]);
        checkBrickSpan(row, col, input);
        firstLayer.setValue(row, col, input[row][col]);
      }
    }
  }

  /**
   * Sets the second layer of bricks.
   *
   * @throws IllegalArgumentException if there is no chance to complete the layer.
   */
  public void setSecondLayer() {
    while (!secondLayer.isComplete()) {
      if (!placeBricks()) {
        System.err.println("Could not find solution");
        throw new IllegalArgumentException();
      }
    }
  }

  /**
   * Makes sure brick's value is positive integer
   *
   * @param value
   * @throws IllegalArgumentException if value is less than 1
   */
  private void isValidNumber(int value) {
    if (value <= 0) {
      System.err.println("Brick's value must be more than or equal to 1.");
      throw new IllegalArgumentException();
    }
  }

  /**
   * Helper method to make sure that the brick has exactly span of 2 row/col. First if there is a
   * brick in any direction and second if there are no bricks with more than 2 row/col span.
   *
   * @param row
   * @param col
   * @param layer
   * @throws IllegalArgumentException when no brick with 1x2 or 2x1 found.
   */
  private void checkBrickSpan(int row, int col, int[][] layer) {
    int currentValue = layer[row][col];
    isBrick(row, col, layer);
    for (int i = 0; i < 4; i++) {
      visitCell(row, rowOffset[i], col, colOffset[i], currentValue, 1, layer);
    }
  }

  /**
   * Helper method to make sure that there are two equal adjacent halves.
   *
   * @param row
   * @param col
   * @param layer
   * @throws IllegalArgumentException when there is no adjacent half
   */
  private void isBrick(int row, int col, int[][] layer) {
    for (int i = 0; i < 4; i++) {
      int otherRow = row + rowOffset[i];
      int otherCol = col + colOffset[i];
      boolean isInRange = !outOfRange(otherRow, otherCol);
      if (isInRange && layer[row][col] == layer[otherRow][otherCol]) {
        return;
      }
    }

    System.err.println("Invalid brick. It must have 2 equal halves");
    throw new IllegalArgumentException();
  }

  /**
   * Recursive method for visiting adjacent cells to find the span of the brick.
   *
   * @param row
   * @param rowOffset
   * @param col
   * @param colOffset
   * @param value
   * @param occurrences
   * @param layer
   * @throws IllegalArgumentException when no brick with 1x2 or 2x1 found.
   */
  private void visitCell(
      int row, int rowOffset, int col, int colOffset, int value, int occurrences, int[][] layer) {
    int nextRow = row + rowOffset;
    int nextCol = col + colOffset;
    if (outOfRange(row, col) || outOfRange(nextRow, nextCol)) {
      return;
    }
    int nextValue = layer[nextRow][nextCol];
    if (nextValue != value) {
      return;
    }
    if (++occurrences > 2) {
      System.err.println("Invalid brick with span of more than 2 cells");
      throw new IllegalArgumentException();
    }
    visitCell(nextRow, rowOffset, nextCol, colOffset, value, occurrences, layer);
  }

  private boolean outOfRange(int row, int col) {
    return (row < 0 || row >= rows) || (col < 0 || col >= cols);
  }

  /**
   * Method responsible for building the second layer. First it checks if brick can bi placed
   * horizontally; first half is at layer[row][col], second half is at layer[row][col+1]. Then if
   * not possible horizontally it tries to place it vertically; first half at layer[row][col],
   * second half at layer[row-1][col].
   *
   * @return false if there is no possibility to place the brick in the second layer.
   */
  public boolean placeBricks() {
    boolean isBrickPlaced = false;

    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        if (firstLayer.canPlaceBrick(row, col, rowOffset[0], colOffset[0])) {
          secondLayer.placeBrick(row, col, rowOffset[0], colOffset[0]);
          firstLayer.markAsPlaced(row, col, rowOffset[0], colOffset[0]);
          isBrickPlaced = true;
          break;
        } else if (firstLayer.canPlaceBrick(row, col, rowOffset[1], colOffset[1])) {
          secondLayer.placeBrick(row, col, rowOffset[1], colOffset[1]);
          firstLayer.markAsPlaced(row, col, rowOffset[1], colOffset[1]);
          isBrickPlaced = true;
          break;
        }
      }

      if (isBrickPlaced) break;
    }

    return isBrickPlaced;
  }

  private static final String ASTERISK = "*";
  private static final String SPACE = " ";

  public static String toPrettyString(Layer layer) {
    StringBuilder sb = new StringBuilder();

    for (int row = 0; row < layer.getRows(); row++) {
      List<String> brickLine = new ArrayList<>();
      StringBuilder separation = new StringBuilder();
      brickLine.add(ASTERISK);
      separation.append(ASTERISK);

      for (int col = 0; col < layer.getCols() - 1; col++) {
        int currentValue = layer.getValue(row, col);
        int nextValue = layer.getValue(row, col + 1);

        if (currentValue != nextValue) {
          brickLine.add(String.valueOf(currentValue));
          separation.append(SPACE);
          if (currentValue >= 10) {
            separation.append(SPACE);
          }
        } else {
          brickLine.add(formatBrickValues(currentValue));
          separation.append(ASTERISK.repeat(5));
          col++;
          if (col < layer.getCols() - 1) {
            nextValue = layer.getValue(row, col + 1);
          }
        }
        brickLine.add(ASTERISK);
        separation.append(ASTERISK);

        if (col == layer.getCols() - 2) {
          brickLine.add(String.valueOf(nextValue));
          brickLine.add(ASTERISK);
          if (currentValue != nextValue) {
            separation.append(SPACE);
          }
          separation.append(ASTERISK);
        }
      }
      String brickLineString = String.join("", brickLine);

      if (row == 0) {
        sb.append(ASTERISK.repeat(brickLineString.length()));
        sb.append(System.lineSeparator());
      }

      sb.append(brickLineString);
      sb.append(System.lineSeparator());
      if (row < layer.getRows() - 1) {
        sb.append(separation).append(System.lineSeparator());
      }

      if (row == layer.getRows() - 1) {
        sb.append(ASTERISK.repeat(brickLineString.length()));
        sb.append(System.lineSeparator());
      }
    }

    return sb.toString().trim();
  }

  private static String formatBrickValues(int value) {
    if (value >= 10) return "" + value + " " + value;
    return " " + value + " " + value + " ";
  }
}
