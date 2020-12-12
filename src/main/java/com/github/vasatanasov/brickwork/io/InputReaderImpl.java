package com.github.vasatanasov.brickwork.io;

import java.io.BufferedReader;
import java.io.IOException;

public class InputReaderImpl implements InputReader {

  private final BufferedReader bufferedReader;

  public InputReaderImpl(BufferedReader bufferedReader) {
    this.bufferedReader = bufferedReader;
  }

  @Override
  public String readLine() throws IOException {
    return bufferedReader.readLine();
  }
}
