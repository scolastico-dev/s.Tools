package me.scolastico.tools.web.admin.commands;

import java.util.HashMap;
import java.util.concurrent.Callable;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.admin.dataholder.AdminConfig;
import picocli.CommandLine.Command;

/**
 * Internal class of the admin panel module.
 */
@Command(
    name = "ap-show-users",
    mixinStandardHelpOptions = true,
    description = "Show an list of all users from the admin panel.",
    version = "1.0.0"
)
public class ShowUsers implements Callable<Integer> {

  @Override
  public Integer call() throws Exception {
    AdminConfig config = AdminPanel.getConfig();
    HashMap<String, String> users = config.getUserAndPasswords();
    StringBuilder builder = new StringBuilder();
    for (String u:users.keySet()) {
      builder.append(u).append("\n");
    }
    String ret = "No users found!";
    if (users.size() > 0) {
      ret = builder.toString();
    }
    System.out.println("The following users where found:");
    System.out.println(ret);
    return 0;
  }

}
