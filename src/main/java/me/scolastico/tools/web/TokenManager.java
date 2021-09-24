package me.scolastico.tools.web;

import com.sun.net.httpserver.HttpExchange;

/**
 * An manager to set and get login tokens.
 */
public class TokenManager {

  private final HttpExchange exchange;
  private final String cookieName;

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
    return new TokenManager(exchange, "j_auth_token").getLoginToken();
  }

  /**
   * Function to set a login cookie which is valid for one day.
   * @param exchange The http exchange where to set the login cookie.
   * @param token The token which should be set in the login cookie.
   */
  public static void setLoginToken(HttpExchange exchange, String token) {
    new TokenManager(exchange, "j_auth_token").setLoginToken(token);
  }

  /**
   * Function to set a login cookie.
   * @param exchange The http exchange where to set the login cookie.
   * @param token The token which should be set in the login cookie.
   * @param validUntil Valid until defines how much seconds should the login cookie be valid.
   */
  public static void setLoginToken(HttpExchange exchange, String token, Long validUntil) {
    new TokenManager(exchange, "j_auth_token").setLoginToken(token, validUntil);
  }

  public TokenManager(HttpExchange exchange, String cookieName) {
    this.exchange = exchange;
    this.cookieName = cookieName;
  }

  /**
   * <pre>
   * Get the login token from a http exchange.
   * The login token can be in a...
   *  - Cookie (set the name with the constructor.
   *  - "Authorization" header with or without the "Bearer" tag.
   *  If multiple tokens are set the first one which is found will be returned.
   *  The order for the checks is the same as above.
   * </pre>
   * @return The login token if found else NULL.
   */
  public String getLoginToken() {
    String token = null;
    try {
      if (exchange.getRequestHeaders().containsKey("Authorization")) {
        token = exchange.getRequestHeaders().getFirst("Authorization");
        if (token.startsWith("Bearer ")) token = token.substring(7);
      }
      if (token == null) {
        token = CookieManager.getCookie(exchange, cookieName);
      }
    } catch (Exception ignored) {}
    return token;
  }

  /**
   * Function to set a login cookie which is valid for one day.
   * @param token The token which should be set in the login cookie.
   */
  public void setLoginToken(String token) {
    setLoginToken(exchange, token, 86400L);
  }

  /**
   * Function to set a login cookie.
   * @param token The token which should be set in the login cookie.
   * @param validUntil Valid until defines how much seconds should the login cookie be valid.
   */
  public void setLoginToken(String token, Long validUntil) {
    exchange.getResponseHeaders().set("Set-Cookie", cookieName + "=" + token + "; path=/; Max-Age=" + validUntil);
  }

}
