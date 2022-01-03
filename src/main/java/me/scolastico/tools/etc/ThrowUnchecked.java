package me.scolastico.tools.etc;

public class ThrowUnchecked {

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwException(Throwable exception, Object dummy) throws T {
    throw (T) exception;
  }

  public static void throwException(Throwable exception) {
    ThrowUnchecked.<RuntimeException>throwException(exception, null);
  }

}
