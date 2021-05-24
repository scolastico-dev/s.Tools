package me.scolastico.tools.handler;

import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class OutputHandlerTest {

  @Test
  void outputHandler() {
    OutputHandler.clearOutput();
    OutputHandler.enable();
    System.out.println("Test1");
    System.out.println("Test2");
    System.out.println("Test3");
    OutputHandler.disable();
    String[] output = OutputHandler.getOutput();
    Assertions.assertEquals("Test1", output[0]);
    Assertions.assertEquals("Test2", output[1]);
    Assertions.assertEquals("Test3", output[2]);
    Assertions.assertEquals(output.length, 3);
  }

  @Test
  void outputHandlerOverflow() {
    OutputHandler.clearOutput();
    OutputHandler.setStoreRows(9);
    OutputHandler.enable();
    for (int i = 1; i <= 10; i++) {
      System.out.println(i);
    }
    OutputHandler.disable();
    String[] output = OutputHandler.getOutput();
    Assertions.assertEquals(9, output.length);
    Assertions.assertFalse(ArrayUtils.contains(output, "1"));
    for (int i = 2; i <= 10; i++) {
      Assertions.assertEquals(Integer.toString(i), output[i-2]);
    }
    OutputHandler.setStoreRows(1024);
  }

}