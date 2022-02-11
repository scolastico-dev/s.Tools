package me.scolastico.tools.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class CacheManagerTest {


  @Test
  @Order(1)
  void prepare() throws IOException {
    File folder = new File(".cache");
    if (folder.exists() && folder.isDirectory()) {
      FileUtils.deleteDirectory(folder);
    }
  }

  @Test
  @Order(2)
  void store() {
    CacheManager.store("1", "test1", 3);
    CacheManager.store("2", "test2", 60);
    CacheManager.store("3", "test3", 60);
    assertEquals(4, new File(".cache").listFiles().length);
  }

  @Test
  @Order(3)
  void get() throws Exception {
    assertEquals("test1", CacheManager.get("1", String.class, new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "something else1";
      }
    }, 60));
    Thread.sleep(3000);
    assertEquals("something else2", CacheManager.get("1", String.class, new Callable<String>() {
      @Override
      public String call() throws Exception {
        return "something else2";
      }
    }, 60));
  }

  @Test
  @Order(4)
  void destroy() {
    assertEquals(4, new File(".cache").listFiles().length);
    CacheManager.destroy("1");
    assertEquals(3, new File(".cache").listFiles().length);
  }

  @Test
  @Order(5)
  void destroyAll() {
    assertEquals(3, new File(".cache").listFiles().length);
    CacheManager.destroy();
    assertEquals(1, new File(".cache").listFiles().length);
  }

  @Test
  @Order(6)
  void cleanup() throws IOException {
    FileUtils.deleteDirectory(new File(".cache"));
  }

}