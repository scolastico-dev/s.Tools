package me.scolastico.tools.routine;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;

public class RoutineB implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    System.out.println("Running [B]");
    Assertions.fail("Executed routine which should be ignored!");
    return new RoutineAnswer();
  }

}
