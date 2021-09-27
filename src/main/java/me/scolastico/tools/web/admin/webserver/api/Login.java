package me.scolastico.tools.web.admin.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.TokenManager;
import me.scolastico.tools.web.admin.AdminPanel;
import me.scolastico.tools.web.admin.dataholder.AdminData;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

public class Login implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(
      context = "/.admin/api/login",
      acceptPost = true,
      acceptPost_JSON = true,
      jsonObject = AdminData.class
  )
  public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
    try {
      HTTP_EXCHANGE.getResponseHeaders().add("Content-Type", "application/json");
      AdminData data = (AdminData) JSON_OBJECT;
      if (data.getUsername() == null || data.getPassword() == null) {
        return new Pair<>(400, "{\"status\":\"error\",\"error\":\"bad request\"}");
      }
      String password = AdminPanel.getConfig().getUserAndPasswords().get(data.getUsername());
      if (password == null || !AdminPanel.getVerifier().hash(password).password(data.getPassword().getBytes(StandardCharsets.UTF_8)).verifyEncoded()) {
        return new Pair<>(403, "{\"status\":\"error\",\"error\":\"not authorized\"}");
      }
      String token = AdminPanel.getNewToken(data.getUsername());
      new TokenManager(HTTP_EXCHANGE, "j_ap_session_auth").setLoginToken(token, 3600L);
      return new Pair<>(200, "{\"status\":\"ok\",\"token\":\"" + token + "\"}");
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
    return new Pair<>(500, "{\"status\":\"error\",\"error\":\"internal error\"}");
  }

}
