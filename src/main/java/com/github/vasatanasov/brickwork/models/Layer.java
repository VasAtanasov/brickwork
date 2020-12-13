package com.github.vasatanasov.brickwork.models;

/** Data structure representing area with 1x2 brick slots */
public class Layer {
  private final int totalBricksSlots;
  private int rows;
  private int cols;
  private final int[][] layer;
  private int totalBricks;

  private Layer(int rows, int cols) {
    setRows(rows);
    setCols(cols);
    layer = new int[rows][cols];
    totalBricksSlots = (rows * cols) / 2;
  }

  /**
   * Static factory method of the Layer class
   *
   * @param rows number of layer rows, positive, even
   * @param cols number of layer cols, positive, even
   * @return {@link com.github.vasatanasov.brickwork.models.Layer}
   */
  public static Layer of(int rows, int cols) {
    return new Layer(rows, cols);
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  /**
   * Returns a value representing half of a brick from the layer
   *
   * @param row
   * @param col
   * @return brick half
   */
  public int getValue(int row, int col) {
    checkRange(row, col);
    return layer[row][col];
  }

  /**
   * Sets a value representing half of a brick from the layer
   *
   * @param row
   * @param col
   * @param value
   */
  public void setValue(int row, int col, int value) {
    checkRange(row, col);
    layer[row][col] = value;
  }

  /** @return true if all bricks slots are taken else false */
  public boolean isComplete() {
    return totalBricks == totalBricksSlots;
  }

  /**
   * Sets the two halves on the layer.
   *
   * @param row
   * @param col
   * @param rowOffset
   * @param colOffset
   * @throws IllegalArgumentException if any half is out of layer's range
   */
  public void placeBrick(int row, int col, int rowOffset, int colOffset) {
    checkRange(row, col);
    int otherHalfRow = row + rowOffset;
    int otherHalfCol = col + colOffset;
    checkRange(otherHalfRow, otherHalfCol);
    totalBricks += 1;
    layer[row][col] = totalBricks;
    layer[otherHalfRow][otherHalfCol] = totalBricks;
  }

  /**
   * Checks if the each brick's half to be placed is in range of the layer, there is no brick placed
   * from the second layer yet and that it is not a whole brick.
   *
   * @param row
   * @param col
   * @param rowOffset
   * @param colOffset
   * @return true if there are 2 different halves
   */
  public boolean canPlaceBrick(int row, int col, int rowOffset, int colOffset) {
    checkRange(row, col);
    if (layer[row][col] <= 0) return false;
    int otherHalfRow = row + rowOffset;
    int otherHalfCol = col + colOffset;
    if (outOfRange(otherHalfRow, otherHalfCol)) return false;
    if (layer[otherHalfRow][otherHalfCol] <= 0) return false;
    return layer[row][col] != layer[otherHalfRow][otherHalfCol];
  }

  /**
   * Mark the values as negatives when there is brick placed on top so that we no it is not valid
   * position for other bricks from the same layer
   *
   * @param row
   * @param col
   * @param rowOffset
   * @param colOffset
   * @throws IllegalArgumentException if any half is out of layer's range
   */
  public void markAsPlaced(int row, int col, int rowOffset, int colOffset) {
    checkRange(row, col);
    int otherHalfRow = row + rowOffset;
    int otherHalfCol = col + colOffset;
    checkRange(otherHalfRow, otherHalfCol);
    layer[row][col] = -layer[row][col];
    layer[otherHalfRow][otherHalfCol] = -layer[otherHalfRow][otherHalfCol];
  }

  private void setRows(int rows) {
    if (isInvalidAreaParam(rows) || isOdd(rows)) {
      System.err.println("Rows must be even number and less than 100");
      throw new IllegalArgumentException();
    }
    this.rows = rows;
  }

  private void setCols(int cols) {
    if (isInvalidAreaParam(cols) || isOdd(cols)) {
      System.err.println("Columns must be even number and less than 100");
      throw new IllegalArgumentException();
    }
    this.cols = cols;
  }

  private void checkRange(int row, int col) {
    if (outOfRange(row, col)) {
      throw new IllegalArgumentException();
    }
  }

  private boolean outOfRange(int row, int col) {
    return (row < 0 || row >= rows) || (col < 0 || col >= cols);
  }

  private boolean isOdd(int num) {
    return num % 2 != 0;
  }

  private boolean isInvalidAreaParam(int i) {
    return i < 2 || i >= 100;
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();
    for (int row = 0; row < rows; row++) {
      StringBuilder rowOutput = new StringBuilder();
      for (int col = 0; col < cols; col++) {
        rowOutput.append(layer[row][col]).append(" ");
      }
      output.append(rowOutput.toString().trim()).append(System.lineSeparator());
    }
    return output.toString().trim();
  }
}
