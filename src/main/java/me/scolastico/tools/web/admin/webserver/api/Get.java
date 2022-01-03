package me.scolastico.tools.web.admin.webserver.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.console.ConsoleManager;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.TokenManager;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

/**
 * Internal class of the admin panel module.
 */
public class Get implements SimpleWebsiteInterface {

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  @Override
  @WebServerRegistration(
      context = "/.admin/api/get"
  )
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    try {
      HTTP_EXCHANGE.getResponseHeaders().add("Content-Type", "application/json");
      String token = new TokenManager(HTTP_EXCHANGE, "j_ap_session_auth").getLoginToken();
      if (token == null) {
        return new Pair<>(403, "{\"status\":\"error\",\"error\":\"not authorized\"}");
      }
      String user = AdminPanel.getUserFromToken(token);
      if (user == null) {
        return new Pair<>(403, "{\"status\":\"error\",\"error\":\"not authorized\"}");
      }
      HashMap<String, Object> ret = new HashMap<>();
      ret.put("status", "ok");
      ret.put("output", ConsoleManager.getLastLogLines());
      return new Pair<>(200, gson.toJson(ret));
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return new Pair<>(500, "{\"status\":\"error\",\"error\":\"internal error\"}");
  }

}
