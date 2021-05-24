package me.scolastico.tools.handler;

import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class ConfigHandlerTest {

  private static ConfigHandler<TestConfig> configHandler;

  @Test
  @Order(0)
  void setup() throws IOException {
    configHandler = new ConfigHandler<>(new TestConfig(), "./tmp.json", false);
  }

  @Test
  @Order(1)
  void saveDefaultConfig() throws IOException {
    configHandler.saveDefaultConfig();
  }

  @Test
  @Order(2)
  void loadConfig() throws IOException {
    Assertions.assertEquals("value", configHandler.loadConfig().getKey());
  }

  @Test
  @Order(3)
  void storeConfig() throws IOException {
    TestConfig config = new TestConfig();
    config.setKey("test");
    configHandler.storeConfig(config);
  }

  @Test
  @Order(4)
  void checkStoredConfig() throws IOException {
    Assertions.assertEquals("test", configHandler.loadConfig().getKey());
  }

  @Test
  @Order(5)
  void delete() {
    Assertions.assertTrue(configHandler.delete());
  }

  @Test
  @Order(6)
  void checkIfExists() {
    Assertions.assertFalse(configHandler.checkIfExists());
  }

  private static class TestConfig {

    private String key = "value";

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

  }

}