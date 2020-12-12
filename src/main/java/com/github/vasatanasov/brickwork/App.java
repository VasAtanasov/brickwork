package com.github.vasatanasov.brickwork;

import com.github.vasatanasov.brickwork.core.Manager;
import com.github.vasatanasov.brickwork.io.InputReader;
import com.github.vasatanasov.brickwork.io.InputReaderImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

  public static void main(String[] args) {
    final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    InputReader reader = new InputReaderImpl(bufferedReader);
    Manager manager = new Manager(reader);
    manager.run();
  }
}
