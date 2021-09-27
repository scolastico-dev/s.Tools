package me.scolastico.tools.web.admin.commands;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Callable;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.admin.dataholder.AdminConfig;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

/**
 * Internal class of the admin panel module.
 */
@Command(
    name = "ap-add-user",
    mixinStandardHelpOptions = true,
    description = "Add an user to the admin panel.",
    version = "1.0.0"
)
public class AddUser  implements Callable<Integer> {

  @Parameters(
      paramLabel = "<username>",
      index = "0",
      arity = "1",
      description = "The username for the user."
  )
  private String username;

  @Parameters(
      paramLabel = "<password>",
      index = "1",
      arity = "1",
      description = "The password for the user."
  )
  private String password;

  @Override
  public Integer call() throws Exception {
    AdminConfig config = AdminPanel.getConfig();
    HashMap<String, String> users = config.getUserAndPasswords();
    if (users.containsKey(username)) {
      System.out.println("Sorry, an user with the name '" + username + "' exists already!");
      return 1;
    }
    String hash = AdminPanel.getHasher().password(password.getBytes(StandardCharsets.UTF_8)).encodedHash();
    users.put(username, hash);
    config.setUserAndPasswords(users);
    AdminPanel.getConfigHandler().storeConfig(config);
    AdminPanel.setConfig(config);
    System.out.println("Done! Created user with username '" + username + "'.");
    return 0;
  }

}
