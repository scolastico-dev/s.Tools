package me.scolastico.tools.simplified;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

class URLCoderTest {

  @Test
  void decodeURL() {
    Assertions.assertEquals("https://scolasti.co/", URLCoder.decodeURL("https%3A%2F%2Fscolasti.co%2F"));
  }

  @Test
  void encodeURL() {
    Assertions.assertEquals("https%3A%2F%2Fscolasti.co%2F", URLCoder.encodeURL("https://scolasti.co/"));
  }

  @Test
  void randomURLs() {
    for (int i = 0; i < 100; i++) {
      String randomString = RandomStringUtils.randomAscii(1024);
      Assertions.assertEquals(randomString, URLCoder.decodeURL(URLCoder.encodeURL(randomString)));
    }
  }

}