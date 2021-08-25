package me.scolastico.tools.routine;

import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoutineTest {

  @Test
  void routineTest() throws Exception {
    ArrayList<Routine> routines = new ArrayList<>();
    routines.add(new RoutineA());
    routines.add(new RoutineB());
    routines.add(new RoutineC());
    RoutineManager manager = new RoutineManager(routines);
    manager.startNotAsynchronously();
    Assertions.assertFalse(manager.isRunning());
    Assertions.assertFalse(manager.isCanceled());
    Assertions.assertTrue(manager.isStarted());
  }

}
