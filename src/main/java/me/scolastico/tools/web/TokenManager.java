package me.scolastico.tools.web;

import com.sun.net.httpserver.HttpExchange;

/**
 * An manager to set and get login tokens.
 */
public class TokenManager {

  /**
   * <pre>
   * Get the login token from a http exchange.
   * The login token can be in a...
   *  - "j_auth_token" cookie.
   *  - "Authorization" header with or without the "Bearer" tag.
   *  If multiple tokens are set the first one which is found will be returned.
   *  The order for the checks is the same as above.
   * </pre>
   * @param exchange The http exchange where to look for the login cookie.
   * @return The login token if found else NULL.
   */
  public static String getLoginToken(HttpExchange exchange) {
    String token = null;
    try {
      if (exchange.getRequestHeaders().containsKey("Authorization")) {
        token = exchange.getRequestHeaders().getFirst("Authorization");
        if (token.startsWith("Bearer ")) token = token.substring(7);
      }
      if (token == null && exchange.getRequestHeaders().containsKey("Cookie")) {
        for (String cookie:exchange.getResponseHeaders().getFirst("Cookie").split("; ")) {
          if (cookie.startsWith("j_session_auth=")) {
            token = cookie.substring(15);
          }
        }
      }
    } catch (Exception ignored) {}
    return token;
  }

  /**
   * Function to set a login cookie which is valid for one day.
   * @param exchange The http exchange where to set the login cookie.
   * @param token The token which should be set in the login cookie.
   */
  public static void setLoginToken(HttpExchange exchange, String token) {
    setLoginToken(exchange, token, 86400L);
  }

  /**
   * Function to set a login cookie which is valid for one day.
   * @param exchange The http exchange where to set the login cookie.
   * @param token The token which should be set in the login cookie.
   * @param validUntil Valid until defines how much seconds should the login cookie be valid.
   */
  public static void setLoginToken(HttpExchange exchange, String token, Long validUntil) {
    exchange.getResponseHeaders().set("Set-Cookie", "j_session_auth=" + token + "; path=/; Max-Age=" + validUntil);
  }

}
