package me.scolastico.tools.web.admin.commands;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.Callable;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.admin.dataholder.AdminConfig;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(
    name = "ap-passwd",
    mixinStandardHelpOptions = true,
    description = "Change the password of an user.",
    version = "1.0.0"
)
public class ChangePassword  implements Callable<Integer> {

  @Parameters(
      paramLabel = "<username>",
      index = "0",
      arity = "1",
      description = "The username of the user where you want to change the password."
  )
  private String username;

  @Parameters(
      paramLabel = "<password>",
      index = "1",
      arity = "1",
      description = "The password you want to set."
  )
  private String password;

  @Override
  public Integer call() throws Exception {
    AdminConfig config = AdminPanel.getConfig();
    HashMap<String, String> users = config.getUserAndPasswords();
    if (!users.containsKey(username)) {
      System.out.println("Sorry, no user with the name '" + username + "' exists!");
      return 1;
    }
    String hash = AdminPanel.getHasher().password(password.getBytes(StandardCharsets.UTF_8)).encodedHash();
    users.remove(username);
    users.put(username, hash);
    config.setUserAndPasswords(users);
    AdminPanel.getConfigHandler().storeConfig(config);
    AdminPanel.setConfig(config);
    System.out.println("Done! Changed password of the user with username '" + username + "'.");
    return 0;
  }

}
