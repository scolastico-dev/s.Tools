package me.scolastico.tools.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import javax.validation.constraints.NotNull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Config handler for easy managing of config files with the json format.
 * @param <Config> A simple getter and setter class containing all your config values.
 */
public class ConfigHandler<Config> {

  private static final Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

  private final String path;
  private final File file;
  private final Config defaultConfig;

  /**
   * Create a config handler.
   * Writes automatically the default config if not existing.
   * @param defaultConfig The default config.
   * @param path The path to the file with the file name and extension.
   *             In normal cases should be an ".json" file.
   * @throws IOException If file is not read- or writeable an IOException is thrown.
   */
  public ConfigHandler(Config defaultConfig, String path) throws IOException {
    this.defaultConfig = defaultConfig;
    this.path = path;
    this.file = new File(path);
    if (file.exists()) {
      if (!file.canRead()) {
        throw new IOException("Cannot read file '" + path + "'!");
      } else if (!file.canWrite()) {
        throw new IOException("Cannot write file '" + path + "'!");
      }
    }
  }

  /**
   * Create a config handler.
   * @param defaultConfig The default config.
   * @param path The path to the file with the file name and extension.
   *             In normal cases should be an ".json" file.
   * @param writeDefaultConfigAutomatically If true the default config will be written automatically if no config exists.
   *                                        If this is enabled the config will also be updated every time the handler is created.
   * @throws IOException If file is not read- or writeable an IOException is thrown.
   */
  public ConfigHandler(Config defaultConfig, String path, Boolean writeDefaultConfigAutomatically) throws IOException {
    this.defaultConfig = defaultConfig;
    this.path = path;
    this.file = new File(path);
    if (file.exists()) {
      if (!file.canRead()) {
        throw new IOException("Cannot read file '" + path + "'!");
      } else if (!file.canWrite()) {
        throw new IOException("Cannot write file '" + path + "'!");
      }
    }
    if (writeDefaultConfigAutomatically) {
      if (file.exists()) {
        saveDefaultConfig();
      } else {
        storeConfig(loadConfig());
      }
    }
  }

  /**
   * Check if the config file is already existing.
   * @return Returns true if the file is existing.
   */
  public boolean checkIfExists() {
    return file.exists();
  }

  /**
   * Delete the file.
   * @return Returns true if the file was deleted successfully.
   */
  public boolean delete() {
    if (!file.delete()) {
      if (checkIfExists()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Save the default config in the config file.
   * WARNING: this also overrides normal configs.
   * You should check before this if the config exists.
   * @throws IOException If the writing to the file fails an IOException will be thrown.
   */
  public void saveDefaultConfig() throws IOException {
    FileUtils.writeStringToFile(file, gson.toJson(defaultConfig), StandardCharsets.UTF_8);
  }

  /**
   * Load the config.
   * @return The config that was loaded from the file.
   * @throws IOException If the reading of the file fails an IOException will be thrown.
   */
  public Config loadConfig() throws IOException {
    return gson.fromJson(FileUtils.readFileToString(file, StandardCharsets.UTF_8), (Type) defaultConfig.getClass());
  }

  /**
   * Save the config object as an JSON string in the config file.
   * @param config Config object to save.
   * @throws IOException If the writing to the file fails an IOException will be thrown.
   */
  public void storeConfig(Config config) throws IOException {
    FileUtils.writeStringToFile(file, gson.toJson(config), StandardCharsets.UTF_8);
  }

  /**
   * Get the path to the config file.
   * @return Returns the path to the config file.
   */
  public String getPath() {
    return path;
  }

  /**
   * Get the config file as an File object.
   * @return Returns the config file as an File object.
   */
  public File getFile() {
    return file;
  }

  /**
   * Get the default config object.
   * @return Returns the default config object.
   */
  public Config getDefaultConfig() {
    return defaultConfig;
  }

  /**
   * Get an path to an folder where you can store user configurations.
   * This function is OS optimized for Windows, Linux and Mac.
   * @return An path to an folder where you can store user configurations.
   *         It is recommended to create an folder with your application's name in this folder.
   */
  public static String getUserConfigFolder() {
    return getUserConfigFolder(null);
  }

  /**
   * Get an path to an folder where you can store user configurations.
   * This function is OS optimized for Windows, Linux and Mac.
   * @param projectName The project name.
   *                    If this value is not null an folder will also be created
   *                    with the project name in the configurations folder.
   * @return An path to an folder where you can store user configurations.
   */
  public static String getUserConfigFolder(String projectName) {
    String path;
    if (SystemUtils.IS_OS_WINDOWS) {
      path = System.getenv("APPDATA") + "\\" + projectName + "\\";
    } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
      path = SystemUtils.getUserHome().getAbsolutePath() + "/.config/" + projectName + "/";
    } else {
      path = "./.config/";
    }
    File file = new File(path);
    file.mkdirs();
    return path;
  }

  /**
   * Get an path to an folder where you can store system wide configurations.
   * This function is OS optimized for Windows, Linux and Mac.
   * @param projectName The project name.
   *                    An folder will also be created with the project name.
   * @return An path to an folder where you can store system wide configurations.
   */
  public static String getSystemConfigFolder(@NotNull String projectName) {
    String path;
    if (SystemUtils.IS_OS_WINDOWS) {
      path = System.getenv("ProgramData") + "\\" + projectName + "\\";
    } else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
      path = "/etc/" + projectName + "/config/";
    } else {
      path = "./SYSTEM-WIDE-CONFIG-FOLDER-NOT-SUPPORTED/";
    }
    File file = new File(path);
    file.mkdirs();
    return path;
  }

}
