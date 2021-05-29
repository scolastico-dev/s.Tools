package me.scolastico.tools.simplified;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

public class SimplifiedResourceFileReader {

  private static SimplifiedResourceFileReader instance = null;

  public static SimplifiedResourceFileReader getInstance() {
    if (instance == null) {
      instance = new SimplifiedResourceFileReader();
    }
    return instance;
  }

  private SimplifiedResourceFileReader() {}

  /**
   * Get the content of a file from the resources.
   * @param path The path to the file.
   * @return The content of the file.
   */
  public byte[] getByteArrayFromResources(final String path) throws IOException, NullPointerException {
    return getResourceAsStream(path).readAllBytes();
  }

  /**
   * Get the content of a file from the resources.
   * @param path The path to the file.
   * @return The content of the file or NULL if the file cant be read.
   */
  public String getStringFromResources(final String path) {
    try {
      return IOUtils.toString(getResourceAsStream(path), StandardCharsets.UTF_8);
    } catch (IOException|NullPointerException ignored) {}
    return null;
  }

  private InputStream getResourceAsStream(String resource) {
    final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    return in == null ? getClass().getResourceAsStream(resource) : in;
  }

}
