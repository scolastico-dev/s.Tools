package me.scolastico.tools.web;

import com.sun.net.httpserver.HttpExchange;
import java.util.ArrayList;
import java.util.List;
import me.scolastico.tools.simplified.URLCoder;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Set and get cookies from an HttpExchange.
 */
public class CookieManager {

  private static final char[] notAllowedChars = {'(', ')', '<', '>', '@', ',', ';', ':', '\\', '"', '/', '[', ']', '?', '=', '{', '}', ' ', '\t'};

  /**
   * Get the value of an cookie.
   * Please remember that this is client sided and can be manipulated!
   * The first one which is found will be returned.
   * @param exchange The http exchange where to get the cookie.
   * @param cookieName The cookie name.
   * @return The value of the cookie or null if not found.
   */
  public static String getCookie(HttpExchange exchange, String cookieName) {
    try {
      for (String cookies:exchange.getRequestHeaders().get("Cookie")) {
        for (String cookie:cookies.split("; ")) {
          String[] nameAndValue = cookie.split("=");
          if (nameAndValue.length == 2) {
            String name = nameAndValue[0];
            String value = nameAndValue[1];
            if (name.equals(removeNotAllowedChars(cookieName))) {
              return URLCoder.decodeURL(value);
            }
          }
        }
      }
    } catch (NullPointerException ignored) {}
    return null;
  }

  /**
   * Set an cookie value.
   * @param exchange The http exchange where to set the cookie.
   * @param cookieName The cookie name.
   * @param cookieValue The value to store in the cookie.
   * @param valid How long in seconds should the cookie stored? Please remember that this is client sided and can be manipulated!
   */
  public static void setCookie(HttpExchange exchange, String cookieName, String cookieValue, long valid) {
    List<String> cookies = new ArrayList<>();
    if (exchange.getRequestHeaders().containsKey("Set-Cookie")) {
      cookies = exchange.getResponseHeaders().get("Set-Cookie");
    }
    cookies.add(removeNotAllowedChars(cookieName) + "=\"" + URLCoder.encodeURL(cookieValue) + "\"; Max-Age=" + valid + ";");
    exchange.getResponseHeaders().put("Cookie", cookies);
  }

  /**
   * Remove an cookie.
   * Please remember that this is client sided and can be manipulated!
   * @param exchange The http exchange where to remove the cookie.
   * @param cookieName The cookie name.
   */
  public static void removeCookie(HttpExchange exchange, String cookieName) {
    setCookie(exchange, cookieName, "deleted", 1);
  }

  private static String removeNotAllowedChars(String string) {
    StringBuilder stringBuilder = new StringBuilder();
    for (char c:string.toCharArray()) {
      if (!ArrayUtils.contains(notAllowedChars, c)) {
        stringBuilder.append(c);
      }
    }
    return stringBuilder.toString();
  }

}
