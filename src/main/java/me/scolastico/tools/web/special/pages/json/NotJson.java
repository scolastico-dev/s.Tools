package me.scolastico.tools.web.special.pages.json;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.enums.SpecialWebsite;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import org.apache.commons.fileupload.FileItem;

/**
 * Special page for json instead of plain text. Can be used with:
 * <code>
 *   WebServer.registerAllWebInterfacesInPackage("me.scolastico.tools.web.special.pages.json");
 * </code>
 */
public class NotJson implements SimpleWebsiteInterface {

  @Override
  @WebServerRegistration(
      context = "",
      website = SpecialWebsite.NOT_JSON_PAGE
  )
  public Pair<Integer, String> handleRequest(HttpExchange httpExchange, String[] strings, HashMap<String, String> hashMap, HashMap<String, String> hashMap1, String s, List<FileItem> list, Object o) {
    httpExchange.getResponseHeaders().add("Content-Type", "application/json");
    return new Pair<>(400, "{\"status\":\"error\",\"error\":\"not json\"}");
  }

}
