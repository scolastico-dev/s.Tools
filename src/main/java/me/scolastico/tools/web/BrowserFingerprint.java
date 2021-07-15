package me.scolastico.tools.web;

import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.codec.digest.DigestUtils;

public class BrowserFingerprint {

  private static boolean behindCloudFlare = false;

  public static void setBehindCloudFlare(boolean behindCloudFlare) {
    BrowserFingerprint.behindCloudFlare = behindCloudFlare;
  }

  private final String userAgent;
  private final String forwardFor;
  private final String ip;

  public BrowserFingerprint(HttpExchange exchange) {
    this.userAgent = exchange.getRequestHeaders().getFirst("User-Agent") == null ? "" : exchange.getRequestHeaders().getFirst("User-Agent");
    this.forwardFor = exchange.getRequestHeaders().getFirst("X-Forwarded-For") == null ? "" : exchange.getRequestHeaders().getFirst("X-Forwarded-For");
    if (behindCloudFlare) {
      this.ip = exchange.getRequestHeaders().getFirst("CF-Connecting-IP") == null ? "" : exchange.getRequestHeaders().getFirst("CF-Connecting-IP");
    } else {
      this.ip = exchange.getRemoteAddress().getAddress().getHostAddress();
    }
  }

  public String getUserAgent() {
    return userAgent;
  }

  public String getForwardFor() {
    return forwardFor;
  }

  public String getIp() {
    return ip;
  }

  public String getFingerprintHash() {
    return DigestUtils.md5Hex(userAgent+forwardFor+ip).toUpperCase();
  }

}
