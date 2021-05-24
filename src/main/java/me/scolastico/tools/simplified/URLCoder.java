package me.scolastico.tools.simplified;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Url Decoder and Encoder for simplified de-/en- coding of urls without passing everytime the UTF-8 charset.
 */
public class URLCoder {

  /**
   * Decode your URL.
   * @param url URL to decode.
   * @return The decoded URL string.
   */
  public static String decodeURL(String url) {
    return URLDecoder.decode(url, StandardCharsets.UTF_8);
  }

  /**
   * Encode your URL.
   * @param url URL to encode.
   * @return The encoded URL string.
   */
  public static String encodeURL(String url) {
    return URLEncoder.encode(url, StandardCharsets.UTF_8);
  }

}
