package me.scolastico.tools.web;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import me.scolastico.tools.pairs.Pair;
import me.scolastico.tools.routine.Routine;
import me.scolastico.tools.routine.RoutineAnswer;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.exceptions.WebServerRegistrationException;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterfaceWithPreHandler;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class WebServerTest {

  @Test
  @Order(1)
  void start() throws IOException, WebServerRegistrationException {
    WebServer.start(8050);
    WebServer.registerAllWebInterfacesInPackage("me.scolastico.tools.web");
  }

  @Test
  @Order(2)
  void checkStatic() throws IOException {
    URL url = new URL("http://localhost:8050/.internal/echoTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(staticText, IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(3)
  void checkStaticPathValues() throws IOException {
    URL url = new URL("http://localhost:8050/.internal/echoTest/static/1");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals("one", IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
    url = new URL("http://localhost:8050/.internal/echoTest/static/2");
    connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals("two", IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
    url = new URL("http://localhost:8050/.internal/echoTest/static/3");
    connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(404, connection.getResponseCode());
  }

  @Test
  @Order(4)
  void checkIndividualPathValues() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(64);
    URL url = new URL("http://localhost:8050/.internal/echoTest/" + random);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(random, IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(5)
  void checkIndividualGetValues() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(20);
    URL url = new URL("http://localhost:8050/.internal/echoTest?" + random.substring(0,10) + "=" + random.substring(10, 20));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(random.substring(0,10) + " -> " + random.substring(10, 20), IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(6)
  void checkIndividualPostValues() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(20);
    URL url = new URL("http://localhost:8050/.internal/echoTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setDoOutput(true);
    IOUtils.write(random.substring(0,10) + "=" + random.substring(10, 20), connection.getOutputStream(), StandardCharsets.UTF_8);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(random.substring(0,10) + " -> " + random.substring(10, 20), IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(7)
  void checkIndividualDeepPathValues() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(20);
    URL url = new URL("http://localhost:8050/.internal/echoTest/deep/" + random.substring(0,10) + "/" + random.substring(10, 20));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(random.substring(0,10) + ";" + random.substring(10, 20) + ";", IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(8)
  void checkIndividualJsonValue() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(20);
    URL url = new URL("http://localhost:8050/.internal/jsonTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    IOUtils.write("{\"key\":\"" + random + "\"}", connection.getOutputStream(), StandardCharsets.UTF_8);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(random, IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(9)
  void checkPreHandler() throws IOException {
    WebServer.addPreHandler(new ExamplePreHandler());
    URL url = new URL("http://localhost:8050/.internal/preTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals("{\"status\":\"ok\"}", IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(10)
  void checkIndividualWrongJsonValue() throws IOException {
    final String random = RandomStringUtils.randomAlphanumeric(20);
    URL url = new URL("http://localhost:8050/.internal/jsonTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);
    IOUtils.write(random, connection.getOutputStream(), StandardCharsets.UTF_8);
    Assertions.assertEquals(400, connection.getResponseCode());
  }

  @Test
  @Order(11)
  void checkNotFound() throws IOException {
    URL url = new URL("http://localhost:8050/.internal/404");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(404, connection.getResponseCode());
  }

  @Test
  @Order(12)
  void checkWrongMethod() throws IOException {
    URL url = new URL("http://localhost:8050/.internal/echoTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    connection.setRequestMethod("DELETE");
    Assertions.assertEquals(405, connection.getResponseCode());
  }

  @Test
  @Order(13)
  void checkTooManyRequests() throws IOException {
    URL url = new URL("http://localhost:8050/.internal/weightTest");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals("ok", IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
    connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(429, connection.getResponseCode());
    url = new URL("http://localhost:8050/.internal/echoTest");
    connection = (HttpURLConnection) url.openConnection();
    connection.setConnectTimeout(5000);
    Assertions.assertEquals(200, connection.getResponseCode());
    Assertions.assertEquals(staticText, IOUtils.toString(connection.getInputStream(), StandardCharsets.UTF_8));
  }

  @Test
  @Order(14)
  void stop() {
    WebServer.stop();
  }

  public static class WebserverGetEchoTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/echoTest/?~", acceptPathValues = true, acceptGet_GET_VALUES = true)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      if (PATH_VALUES.length == 0) {
        if (GET_VALUES != null) {
          StringBuilder tmp = new StringBuilder();
          for (String key:GET_VALUES.keySet()) {
            tmp.append(key).append(" -> ").append(GET_VALUES.get(key)).append("\n");
          }
          if (tmp.length() > 0) {
            tmp = new StringBuilder(tmp.substring(0, tmp.length() - 1));
          }
          return new Pair<>(200, tmp.toString());
        } else {
          return new Pair<>(200, staticText);
        }
      } else {
        return new Pair<>(200, PATH_VALUES[0]);
      }
    }
  }

  public static class WebserverPostEchoTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/echoTest", acceptPost = true)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      if (X_WWW_FORM_URLENCODED != null) {
        StringBuilder tmp = new StringBuilder();
        for (String key:X_WWW_FORM_URLENCODED.keySet()) {
          tmp.append(key).append(" -> ").append(X_WWW_FORM_URLENCODED.get(key)).append("\n");
        }
        if (tmp.length() > 0) {
          tmp = new StringBuilder(tmp.substring(0, tmp.length() - 1));
        }
        return new Pair<>(200, tmp.toString());
      } else {
        return new Pair<>(200, staticText);
      }
    }
  }

  public static class WebserverPathValuesEchoTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/echoTest/deep/~~",  acceptPathValues = true)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      StringBuilder tmp = new StringBuilder();
      for (String value:PATH_VALUES) {
        tmp.append(value).append(";");
      }
      return new Pair<>(200, tmp.toString());
    }
  }

  public static class WebserverStaticEchoTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/echoTest/static/1|2",  acceptPathValues = true)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      return new Pair<>(200, (PATH_VALUES[0].equals("1") ? "one" : "two"));
    }
  }

  public static class WebserverJsonValueTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/jsonTest", acceptPost = true, acceptPost_JSON = true, jsonObject = JsonTestObject.class)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      JsonTestObject object = (JsonTestObject) JSON_OBJECT;
      return new Pair<>(200, object.getKey());
    }
  }

  public static class WebserverWeightTestPage implements SimpleWebsiteInterface {
    @Override
    @WebServerRegistration(context = "/.internal/weightTest", usageWeight = 63000)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT) {
      return new Pair<>(200, "ok");
    }
  }

  public static class WebserverPreHandlerTestPage implements SimpleWebsiteInterfaceWithPreHandler {
    @Override
    @WebServerRegistration(context = "/.internal/preTest", usageWeight = 63000)
    public Pair<Integer, String> handleRequest(HttpExchange HTTP_EXCHANGE, String[] PATH_VALUES, HashMap<String, String> GET_VALUES, HashMap<String, String> X_WWW_FORM_URLENCODED, String RAW, List<FileItem> FILES, Object JSON_OBJECT, HashMap<String, Object> PRE_HANDLER) {
      if (PRE_HANDLER.get("test").equals(123)) {
        return new Pair<>(200, "{\"status\":\"ok\"}");
      } else {
        return new Pair<>(500, "{\"status\":\"error\",\"error\":\"pre handler value 'test' is not '123'\"}");
      }
    }
  }

  public static class ExamplePreHandler implements Routine {
    @Override
    public RoutineAnswer execute(HashMap<String, Object> objectMap) throws Exception {
      objectMap.put("test", 123);
      return new RoutineAnswer(objectMap);
    }
  }

  public static class JsonTestObject {

    private String key = "value";

    public JsonTestObject(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

    public void setKey(String key) {
      this.key = key;
    }

  }

  private static final String staticText = "me.scolastico.tools - Web Server Echo Test - powered by <a href=\"https://scolasti.co/\">scolastico</a>";

}