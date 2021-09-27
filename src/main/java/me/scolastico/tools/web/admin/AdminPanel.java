package me.scolastico.tools.web.admin;

import static com.kosprov.jargon2.api.Jargon2.Hasher;
import static com.kosprov.jargon2.api.Jargon2.Type;
import static com.kosprov.jargon2.api.Jargon2.Verifier;
import static com.kosprov.jargon2.api.Jargon2.jargon2Hasher;
import static com.kosprov.jargon2.api.Jargon2.jargon2Verifier;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.WebServer;
import me.scolastico.tools.web.admin.commands.AddUser;
import me.scolastico.tools.web.admin.commands.ChangePassword;
import me.scolastico.tools.web.admin.commands.ChangePermissions;
import me.scolastico.tools.web.admin.commands.RemoveUser;
import me.scolastico.tools.web.admin.commands.ShowPermissions;
import me.scolastico.tools.web.admin.commands.ShowUsers;
import me.scolastico.tools.web.admin.dataholder.AdminConfig;
import me.scolastico.tools.web.admin.webserver.Assets;
import me.scolastico.tools.web.admin.webserver.Pages;
import me.scolastico.tools.web.admin.webserver.api.Get;
import me.scolastico.tools.web.admin.webserver.api.Login;
import me.scolastico.tools.web.admin.webserver.api.Logout;
import me.scolastico.tools.web.admin.webserver.api.Put;
import me.scolastico.tools.web.admin.webserver.api.Status;
import me.scolastico.tools.web.exceptions.WebServerRegistrationException;
import org.apache.commons.lang.RandomStringUtils;

public class AdminPanel {

  private static final HashMap<String, Pair<String, Instant>> tokens = new HashMap<>();
  private static final Hasher hasher = jargon2Hasher().type(Type.ARGON2d).memoryCost(65536).timeCost(3).parallelism(4).saltLength(16).hashLength(16);
  private static final Verifier verifier = jargon2Verifier();
  private static AdminConfig config = new AdminConfig();
  private static ConfigHandler<AdminConfig> configHandler;

  static {
    try {
      configHandler = new ConfigHandler<>(new AdminConfig(), "adminPanelSettings.json");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void enable() throws IOException, WebServerRegistrationException {
    enableConfig();
    enableCommands();
    enableWebServer();
  }

  public static void enableWebServer() throws WebServerRegistrationException {
    WebServer.registerWebInterface(new Get());
    WebServer.registerWebInterface(new Login());
    WebServer.registerWebInterface(new Logout());
    WebServer.registerWebInterface(new Put());
    WebServer.registerWebInterface(new Status());
    WebServer.registerWebInterface(new Assets());
    WebServer.registerWebInterface(new Pages());
  }

  public static void enableCommands() {
    ConsoleManager.registerCommand(new AddUser());
    ConsoleManager.registerCommand(new ChangePassword());
    ConsoleManager.registerCommand(new ChangePermissions());
    ConsoleManager.registerCommand(new RemoveUser());
    ConsoleManager.registerCommand(new ShowPermissions());
    ConsoleManager.registerCommand(new ShowUsers());
  }

  public static void enableConfig() throws IOException {
    if (configHandler.checkIfExists()) {
      config = configHandler.loadConfig();
      configHandler.storeConfig(config);
    } else {
      configHandler.saveDefaultConfig();
    }
  }

  public static String getUserFromToken(String token) {
    if (tokens.containsKey(token)) {
      Pair<String, Instant> data = tokens.get(token);
      if (data.getY().isBefore(Instant.now())) {
        removeToken(token);
      } else {
        return data.getX();
      }
    }
    return null;
  }

  public static String getNewToken(String user) {
    String token;
    do {
      token = RandomStringUtils.randomAlphanumeric(64);
    } while (tokens.containsKey(token));
    tokens.put(token, new Pair<>(user, Instant.now().plusSeconds(60*60)));
    return token;
  }

  public static void removeToken(String token) {
    tokens.remove(token);
  }

  public static Verifier getVerifier() {
    return verifier;
  }

  public static Hasher getHasher() {
    return hasher;
  }

  public static AdminConfig getConfig() {
    return config;
  }

  public static void setConfig(AdminConfig config) {
    AdminPanel.config = config;
  }

  public static ConfigHandler<AdminConfig> getConfigHandler() {
    return configHandler;
  }

  public static void setConfigHandler(ConfigHandler<AdminConfig> configHandler) {
    AdminPanel.configHandler = configHandler;
  }

}
