package me.scolastico.tools.web.admin.commands;

import java.util.ArrayList;
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
    name = "ap-show-permissions",
    mixinStandardHelpOptions = true,
    description = "Show the permissions of an user from the admin panel.",
    version = "1.0.0"
)
public class ShowPermissions implements Callable<Integer> {

  @Parameters(
      paramLabel = "<username>",
      index = "0",
      arity = "1",
      description = "The username of the user where you want to change the permissions."
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
    HashMap<String, ArrayList<String>> allPermissions = config.getPermissions();
    ArrayList<String> permissions = allPermissions.getOrDefault(username, new ArrayList<>());
    StringBuilder builder = new StringBuilder();
    for (String p:permissions) {
      builder.append(p).append("\n");
    }
    String ret = "No permissions found!";
    if (permissions.size() > 0) {
      ret = builder.toString();
    }
    System.out.println("The user with the name '" + username + "' has the following permissions:");
    System.out.println(ret);
    return 0;
  }

}
