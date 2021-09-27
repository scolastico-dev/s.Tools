package me.scolastico.tools.web.admin.commands;

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
    name = "ap-remove-user",
    mixinStandardHelpOptions = true,
    description = "Remove an user from the admin panel.",
    version = "1.0.0"
)
public class RemoveUser implements Callable<Integer> {

  @Parameters(
      paramLabel = "<username>",
      index = "0",
      arity = "1",
      description = "The username of the user you want to remove."
  )
  private String username;

  @Override
  public Integer call() throws Exception {
    AdminConfig config = AdminPanel.getConfig();
    HashMap<String, String> users = config.getUserAndPasswords();
    if (!users.containsKey(username)) {
      System.out.println("Sorry, no user with the name '" + username + "' exists!");
      return 1;
    }
    users.remove(username);
    config.setUserAndPasswords(users);
    AdminPanel.getConfigHandler().storeConfig(config);
    AdminPanel.setConfig(config);
    System.out.println("Done! Removed user with username '" + username + "'.");
    return 0;
  }

}
