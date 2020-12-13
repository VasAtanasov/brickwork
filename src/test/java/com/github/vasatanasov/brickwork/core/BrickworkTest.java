package com.github.vasatanasov.brickwork.core;

import com.github.vasatanasov.brickwork.error.NoSolutionException;
import com.github.vasatanasov.brickwork.io.InputReader;
import com.github.vasatanasov.brickwork.io.InputReaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Stream;

class BrickworkTest {

  @ParameterizedTest
  @MethodSource("provideValidInputs")
  public void when_validInput_ShouldPrintResult(String dimension, String[] input, String[] output)
      throws IOException {
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn(dimension, input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
    String result = manager.toStringSecondLayer();
    Assertions.assertEquals(result, String.join(System.lineSeparator(), output));
  }

  private static Stream<Arguments> provideValidInputs() {
    return Stream.of(
        Arguments.of(
            "2 4",
            new String[] {
              "1 1 2 2", //
              "3 3 4 4", //
              ""
            },
            new String[] {
              "2 1 1 4", //
              "2 3 3 4", //
            }),
        Arguments.of(
            "2 8",
            new String[] {
              "1 1 2 2 3 3 4 4", //
              "5 5 6 6 7 7 8 8", //
              ""
            },
            new String[] {
              "4 1 1 2 2 3 3 8", //
              "4 5 5 6 6 7 7 8", //
            }),
        Arguments.of(
            "2 8",
            new String[] {
              "4 1 1 2 2 3 3 8", //
              "4 5 5 6 6 7 7 8", //
              ""
            },
            new String[] {
              "1 1 2 2 3 3 4 4", //
              "5 5 6 6 7 7 8 8", //
            }),
        Arguments.of(
            "2 10",
            new String[] {
              "1 2 3 4 5 6 7 8 9 10", //
              "1 2 3 4 5 6 7 8 9 10", //
              ""
            },
            new String[] {
              "1 1 2 2 3 3 4 4 5 5", //
              "6 6 7 7 8 8 9 9 10 10", //
            }),
        Arguments.of(
            "4 10",
            new String[] {
              "1 2 3 4 5 6 7 8 9 10",
              "1 2 3 4 5 6 7 8 9 10",
              "11 12 13 13 14 14 15 16 16 20",
              "11 12 17 17 18 18 15 19 19 20",
              ""
            },
            new String[] {
              "1 1 2 2 3 3 4 4 5 5", //
              "6 6 7 7 8 8 9 9 10 10", //
              "11 11 16 12 12 13 13 19 14 14", //
              "15 15 16 17 17 18 18 19 20 20", //
            }),
        Arguments.of(
            "4 2",
            new String[] {
              "1 1", //
              "2 2", //
              "3 3", //
              "4 4", //
              ""
            },
            new String[] {
              "1 2", //
              "1 2", //
              "3 4", //
              "3 4", //
            }));
  }

  @ParameterizedTest
  @MethodSource("provideInvalidInputs")
  public void when_invalidInput_ShouldThrow(String dimension, String[] input) throws IOException {
    BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
    Mockito.when(bufferedReader.readLine()).thenReturn(dimension, input);
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    Assertions.assertThrows(NoSolutionException.class, manager::run);
  }

  private static Stream<Arguments> provideInvalidInputs() {
    return Stream.of(
        Arguments.of(
            "2 4",
            new String[] {
              "1 1 2 2", // Invalid number of rows passed (more than the stated)
              "3 3 4 4", //
              "5 5 6 6", //
            }),
        Arguments.of(
            "4 4",
            new String[] {
              "1 1 2 2", // Invalid number of rows passed (less than the stated)
              "3 3 4 4", //
            }),
        Arguments.of(
            "2 4",
            new String[] {
              "1 1 2 ", // Invalid number of cols passed (less than the stated)
              "3 3 4 4", //
              ""
            }),
        Arguments.of("3 4", new String[] {}), // Odd number of rows
        Arguments.of("2 5", new String[] {}), // Odd number of cols
        Arguments.of("-12 10", new String[] {}), // Invalid number of rows
        Arguments.of("2 100", new String[] {}), // Invalid number of cols
        Arguments.of(
            "2 4",
            new String[] {
              "1 1 2 2", // Invalid bricks with less than 2 rows/cols span
              "3 3 4 5", //
              ""
            }),
        Arguments.of(
            "2 4",
            new String[] {
              "1 1 1 2", // Invalid bricks with more than 2 rows/cols span
              "3 3 3 2", //
              ""
            }),
        Arguments.of(
            "2 4",
            new String[] {
              "0 1 1 2", // Brick is with invalid value
              "0 3 3 2", //
              ""
            }),
        Arguments.of(
            "2 4",
            new String[] {
              "4 1 1 2", // Brick is with invalid value
              "d 3 3 2", //
              ""
            }));
  }
}
