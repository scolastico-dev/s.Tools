package me.scolastico.tools.routine;

import java.util.HashMap;

public class RoutineA implements Routine {

  @Override
  public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
    System.out.println("Running [A]");
    objectMap.put("test", 123);
    return new RoutineAnswer(objectMap, new Routine[]{new RoutineB()});
  }

}
