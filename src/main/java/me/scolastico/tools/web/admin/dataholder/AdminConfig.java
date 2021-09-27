package me.scolastico.tools.web.admin.dataholder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Internal class of the admin panel module.
 */
public class AdminConfig {

  private HashMap<String, String> userAndPasswords = new HashMap<>();
  private boolean enablePermissions = false;
  private HashMap<String, ArrayList<String>> permissions = new HashMap<>();

  public boolean hasUserPermission(String username, String permission) {
    if (!enablePermissions) return true;
    return permissions.getOrDefault(username, new ArrayList<>()).contains(permission);
  }

  public boolean isEnablePermissions() {
    return enablePermissions;
  }

  public HashMap<String, ArrayList<String>> getPermissions() {
    return permissions;
  }

  public void setEnablePermissions(boolean enablePermissions) {
    this.enablePermissions = enablePermissions;
  }

  public void setPermissions(HashMap<String, ArrayList<String>> permissions) {
    this.permissions = permissions;
  }

  public HashMap<String, String> getUserAndPasswords() {
    return userAndPasswords;
  }

  public void setUserAndPasswords(HashMap<String, String> userAndPasswords) {
    this.userAndPasswords = userAndPasswords;
  }

}
