package me.scolastico.tools.web.admin.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.admin.dataholder.AdminConfig;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Internal class of the admin panel module.
 */
@Command(
    name = "ap-change-permissions",
    mixinStandardHelpOptions = true,
    description = "Change the permissions of an user from the admin panel.",
    version = "1.0.0"
)
public class ChangePermissions  implements Callable<Integer> {

  @Option(
      names = {"-r", "--remove"},
      description = "Remove an permission instead of giving it."
  )
  private boolean remove;

  @Parameters(
      paramLabel = "<username>",
      index = "0",
      arity = "1",
      description = "The username of the user where you want to change the permissions."
  )
  private String username;

  @Parameters(
      paramLabel = "<permissions>",
      index = "1..*",
      arity = "1..*",
      description = "The permissions which should be changed."
  )
  private String[] permission;

  @Override
  public Integer call() throws Exception {
    AdminConfig config = AdminPanel.getConfig();
    HashMap<String, String> users = config.getUserAndPasswords();
    if (!users.containsKey(username)) {
      System.out.println("Sorry, no user with the name '" + username + "' exists!");
      return 1;
    }
    HashMap<String, ArrayList<String>> allPermissions = config.getPermissions();
    ArrayList<String> permissions = allPermissions.getOrDefault(username, new ArrayList<>());
    for (String p:permission) {
      if (remove) {
        permissions.remove(p);
      } else {
        if (!permissions.contains(p)) {
          permissions.add(p);
        }
      }
    }
    allPermissions.remove(username);
    allPermissions.put(username, permissions);
    config.setPermissions(allPermissions);
    AdminPanel.getConfigHandler().storeConfig(config);
    AdminPanel.setConfig(config);
    System.out.println("Done! Changed permissions of the user with username '" + username + "'.");
    return 0;
  }

}
