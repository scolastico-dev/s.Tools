package me.scolastico.tools.web.admin.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
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
public class Status implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(
      context = "/.admin/api/status"
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
      return new Pair<>(200, "{\"status\":\"ok\", \"ts\":\"" + (System.currentTimeMillis()/1000L) + "\"}");
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return new Pair<>(500, "{\"status\":\"error\",\"error\":\"internal error\"}");
  }

}
