package me.scolastico.tools.routine;

import java.util.HashMap;
import org.junit.jupiter.api.Assertions;

public class RoutineC implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    System.out.println("Running [C]");
    Assertions.assertEquals(123, objectMap.get("test"));
    objectMap.put("test2", 456);
    return new RoutineAnswer(objectMap);
  }

}
