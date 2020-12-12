package com.github.vasatanasov.brickwork.core;

import com.github.vasatanasov.brickwork.io.InputReader;
import com.github.vasatanasov.brickwork.io.InputReaderImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;

class BrickworkTest {

  @Test
  void shouldPlaceWhenValidInput01() throws IOException {
    final String[] input = {
      "1 1 2 2", //
      "3 3 4 4", //
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("2 4", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }

  @Test
  void shouldPlaceWhenValidInput02() throws IOException {
    final String[] input = {
      "1 1 2 2 3 3 4 4", //
      "5 5 6 6 7 7 8 8", //
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("2 8", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }

  @Test
  void shouldPlaceWhenValidInput03() throws IOException {
    final String[] input = {
      "4 1 1 2 2 3 3 8", //
      "4 5 5 6 6 7 7 8" //
      ,
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("2 8", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }

  @Test
  void shouldPlaceWhenValidInput04() throws IOException {
    final String[] input = {
      "1 2 3 4 5 6 7 8 9 10", //
      "1 2 3 4 5 6 7 8 9 10", //
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("2 10", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }

  @Test
  void shouldPlaceWhenValidInput05() throws IOException {
    final String[] input = {
      "1 2 3 4 5 6 7 8 9 10",
      "1 2 3 4 5 6 7 8 9 10",
      "11 12 13 13 14 14 15 16 16 20",
      "11 12 17 17 18 18 15 19 19 20",
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("4 10", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }

  @Test
  void shouldPlaceWhenValidInput06() throws IOException {
    final String[] input = {
      "1 1", //
      "2 2", //
      "3 3", //
      "4 4", //
      ""
    };
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn("4 2", input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }
}
