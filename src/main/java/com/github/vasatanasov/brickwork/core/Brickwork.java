package com.github.vasatanasov.brickwork.core;

import com.github.vasatanasov.brickwork.models.Layer;

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

  public static Brickwork init(int rows, int cols) {
    return new Brickwork(rows, cols);
  }

  public Layer getSecondLayer() {
    return secondLayer;
  }

  public void setFirstLayer(int[][] input) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        firstLayer.setValue(row, col, input[row][col]);
        checkBrickSpan(row, col, firstLayer);
      }
    }
  }

  public void setSecondLayer() {
    while (!secondLayer.isComplete()) {
      if (!placeBricks()) {
        System.err.println("Could not find solution");
        throw new IllegalArgumentException();
      }
    }
  }

  private void checkBrickSpan(int row, int col, Layer layer) {
    int currentValue = layer.getValue(row, col);
    for (int i = 0; i < 4; i++) {
      visitCell(row, rowOffset[i], col, colOffset[i], currentValue, 1, layer);
    }
  }

  private void visitCell(
      int row, int rowOffset, int col, int colOffset, int value, int occurrences, Layer layer) {
    int nextRow = row + rowOffset;
    int nextCol = col + colOffset;
    if (outOfRange(row, col) || outOfRange(nextRow, nextCol)) {
      return;
    }
    int nextValue = layer.getValue(nextRow, nextCol);
    if (nextValue != value) {
      return;
    }
    if (++occurrences > 2) {
      System.err.println("Brick with span more than 2 cells");
      throw new IllegalArgumentException();
    }
    visitCell(nextRow, rowOffset, nextCol, colOffset, value, occurrences, layer);
  }

  private boolean outOfRange(int row, int col) {
    return (row < 0 || row >= rows) || (col < 0 || col >= cols);
  }

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
}
