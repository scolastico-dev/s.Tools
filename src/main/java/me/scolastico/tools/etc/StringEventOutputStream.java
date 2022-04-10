package me.scolastico.tools.etc;

import java.io.OutputStream;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class StringEventOutputStream extends OutputStream {

  private final Consumer<String> consumer;

  public StringEventOutputStream(Consumer<String> consumer) {
    this.consumer = consumer;
  }

  @Override
  public void write(int b) {
    consumer.accept(String.valueOf((char) b));
  }

  @Override
  public void write(@NotNull byte[] b) {
    consumer.accept(new String(b));
  }

  @Override
  public void write(@NotNull byte[] b, int off, int len) {
    consumer.accept(new String(b, off, len));
  }

}
