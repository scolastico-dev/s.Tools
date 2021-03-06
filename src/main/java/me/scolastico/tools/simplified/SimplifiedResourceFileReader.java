package me.scolastico.tools.simplified;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 * Simple functions to read a byte array or a string out of a file in the resources.
 */
public class SimplifiedResourceFileReader {

  private static SimplifiedResourceFileReader instance = null;

  /**
   * Get the instance. This class is a singleton.
   * @return The instance.
   */
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
   * @throws IOException If read process is not successful an IOException is triggered.
   * @throws NullPointerException If not found an NullPointerException is triggered.
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

  /**
   * Get all files recursively in a folder from the resources.
   * @param path The path to the folder.
   * @return A list of all paths to the files.
   */
  public String[] getAllResourcesInFolder(final String path) {
    Reflections reflections = new Reflections(path, new ResourcesScanner());
    Set<String> resourceList = reflections.getResources(x -> true);
    return resourceList.toArray(new String[0]);
  }

  private InputStream getResourceAsStream(String resource) {
    final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
    return in == null ? getClass().getResourceAsStream(resource) : in;
  }

}
