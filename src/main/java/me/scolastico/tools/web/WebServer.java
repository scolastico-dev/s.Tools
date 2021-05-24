package me.scolastico.tools.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.scolastico.tools.dataholder.SchedulerConfiguration;
import me.scolastico.tools.handler.SchedulerHandler;
import me.scolastico.tools.simplified.URLCoder;
import me.scolastico.tools.web.dataholder.WebServerRegistrationData;
import me.scolastico.tools.web.annoations.WebServerRegistration;
import me.scolastico.tools.web.enums.SpecialWebsite;
import me.scolastico.tools.web.enums.WebServerRequestType;
import me.scolastico.tools.web.exceptions.WebServerRegistrationException;
import me.scolastico.tools.web.interfaces.AdvancedWebsiteInterface;
import me.scolastico.tools.web.interfaces.SimpleWebsiteInterface;
import me.scolastico.tools.pairs.Pair;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.reflections.Reflections;

/**
 * An simple webserver for easy management of your api's.
 */
public class WebServer implements HttpHandler {

  private static HttpServer server;
  private static ArrayList<WebServerRegistrationData> registrations = null;
  private static HashMap<SpecialWebsite, WebServerRegistrationData> specialRegistrations = null;
  private static HashMap<String, Integer> usageWeights = new HashMap<>();
  private static int maxUsageWeight = 60000;
  private static int weightLessPerSecond = 100;
  private static long schedulerId = 0L;

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
  private static final SchedulerConfiguration schedulerConfiguration = new SchedulerConfiguration(20, new Runnable() {
    @Override
    public void run() {
      HashMap<String, Integer> clone = new HashMap<>();
      for (String key:usageWeights.keySet()) clone.put(key, usageWeights.get(key));
      for (String key:clone.keySet()) {
        int weight = clone.getOrDefault(key, 0) - weightLessPerSecond;
        usageWeights.remove(key);
        if (weight > 0) usageWeights.put(key, weight);
      }
    }
  });

  public static int getUsageWeight(String ip) {
    return usageWeights.getOrDefault(ip, 0);
  }

  /**
   * To remove a certain value of weight from a user.
   * @param ip The ip of the user.
   * @param usageWeight How much usage weight should be removed?
   */
  public static void removeUsageWeight(String ip, int usageWeight) {
    addUsageWeight(ip, -usageWeight);
  }

  /**
   * To add a certain value of weight from a user.
   * @param ip The ip of the user.
   * @param usageWeight How much usage weight should be added?
   */
  public static void addUsageWeight(String ip, int usageWeight) {
    int weight = getUsageWeight(ip) + usageWeight;
    usageWeights.remove(ip);
    if (weight > 0) {
      usageWeights.put(ip, weight);
    }
  }

  /**
   * Set the max usage weight.
   * If an user try's to open a site with more weight than 0 and his weight
   * is over this value the SpecialSite LIMIT_REACHED_PAGE will be shown!
   * @param maxUsageWeight The max usage weight.
   */
  public static void setMaxUsageWeight(int maxUsageWeight) {
    WebServer.maxUsageWeight = maxUsageWeight;
  }

  /**
   * Set how much usage weight an user loses each second.
   * @param weightLessPerSecond How much weight should a user lose every second.
   */
  public static void setWeightLessPerSecond(int weightLessPerSecond) {
    WebServer.weightLessPerSecond = weightLessPerSecond;
  }

  /**
   * Start the webserver.
   * @param port Port to which the webserver will listen on.
   * @throws IOException If the server cannot start an IOException is thrown.
   */
  public static void start(Integer port) throws IOException {
    if (registrations == null) {
      registrations = new ArrayList<>();
      specialRegistrations = new HashMap<>();
      usageWeights = new HashMap<>();
      server = HttpServer.create(new InetSocketAddress(port), 0);
      server.createContext("/",new WebServer());
      server.start();
      schedulerId = SchedulerHandler.registerTask(schedulerConfiguration);
    }
  }

  /**
   * Stop the webserver and reset all registrations.
   */
  public static void stop() {
    if (registrations != null) {
      server.stop(0);
      registrations = null;
      SchedulerHandler.removeConfiguration(schedulerId);
    }
  }

  /**
   * Register all webinterfaces in a package through reflections.
   * @param packageName The package name/path as a string. Example: "me.scolastico.tools"
   * @throws WebServerRegistrationException Throws WebserverRegistrationException if the registration fails.
   */
  public static void registerAllWebInterfacesInPackage(String packageName) throws WebServerRegistrationException {
    try {
      Reflections reflections = new Reflections(packageName);
      Set<Class<? extends SimpleWebsiteInterface>> simpleClasses = reflections.getSubTypesOf(SimpleWebsiteInterface.class);
      for (Class<? extends SimpleWebsiteInterface> c:simpleClasses) {
        SimpleWebsiteInterface obj = c.getDeclaredConstructor().newInstance();
        registerWebInterface(obj);
      }
      Set<Class<? extends AdvancedWebsiteInterface>> advancedClasses = reflections.getSubTypesOf(AdvancedWebsiteInterface.class);
      for (Class<? extends AdvancedWebsiteInterface> c:advancedClasses) {
        AdvancedWebsiteInterface obj = c.getDeclaredConstructor().newInstance();
        registerWebInterface(obj);
      }
    } catch (Exception e) {
      throw new WebServerRegistrationException(e.getMessage());
    }
  }

  /**
   * If you want to register a single webinterface you can register it with this function.
   * @param websiteInterface The website interface you want to register.
   * @throws WebServerRegistrationException Throws WebserverRegistrationException if the registration fails.
   */
  public static void registerWebInterface(AdvancedWebsiteInterface websiteInterface) throws WebServerRegistrationException {
    if (registrations != null) {
      for (Method method:websiteInterface.getClass().getDeclaredMethods()) {
        if (method.isAnnotationPresent(WebServerRegistration.class)) {
          WebServerRegistration registration = method.getAnnotation(WebServerRegistration.class);
          if (registration.website() == SpecialWebsite.NORMAL_PAGE) {
            if (method.getName().equals("getRequest")) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration, WebServerRequestType.GET));
            } else if (method.getName().equals("postRequest")) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration, WebServerRequestType.POST));
            } else if (method.getName().equals("putRequest")) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration, WebServerRequestType.PUT));
            } else if (method.getName().equals("patchRequest")) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration, WebServerRequestType.PATCH));
            } else if (method.getName().equals("deleteRequest")) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration, WebServerRequestType.DELETE));
            } else {
              throw new WebServerRegistrationException("Method '" + method.getName() + "' not allowed for registration!");
            }
          } else {
            throw new WebServerRegistrationException("The advanced website interfaces does not support SpecialWebsites.");
          }
        }
      }
    }
  }

  /**
   * If you want to register a single webinterface you can register it with this function.
   * @param websiteInterface The website interface you want to register.
   * @throws WebServerRegistrationException Throws WebserverRegistrationException if the registration fails.
   */
  public static void registerWebInterface(SimpleWebsiteInterface websiteInterface) throws WebServerRegistrationException {
    if (registrations != null) {
      for (Method method:websiteInterface.getClass().getDeclaredMethods()) {
        if (method.isAnnotationPresent(WebServerRegistration.class)) {
          WebServerRegistration registration = method.getAnnotation(WebServerRegistration.class);
          if (method.getName().equals("handleRequest")) {
            if (registration.website() == SpecialWebsite.NORMAL_PAGE) {
              registrations.add(new WebServerRegistrationData(websiteInterface, registration));
            } else {
              if (specialRegistrations.containsKey(registration.website())) {
                throw new WebServerRegistrationException("Failed registering special website '" + registration.website().toString() + "' because its already existing.");
              } else {
                specialRegistrations.put(registration.website(), new WebServerRegistrationData(websiteInterface, registration));
              }
            }
          } else {
            throw new WebServerRegistrationException("Method '" + method.getName() + "' not allowed for registration!");
          }
        }
      }
    }
  }

  /**
   * Check if the webserver is running.
   * @return Returns true if the webserver is running.
   */
  public static boolean isRunning() {
    return registrations != null;
  }

  private WebServer() {}

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    Pair<Integer, String> ret = null;
    boolean methodError = false;
    String[] PATH_VALUES = null;
    HashMap<String, String> X_WWW_FORM_URLENCODED = null;
    String RAW = null;
    List<FileItem> FILES = null;
    HashMap<String, String> GET_VALUES = null;
    Object JSON_OBJECT = null;
    String path = exchange.getRequestURI().getPath();
    boolean limitReached = false;
    int userWeight = getUsageWeight(exchange.getRemoteAddress().toString());
    outerLoop:
    for (WebServerRegistrationData data:registrations) {
      SimpleWebsiteInterface simpleWebsiteInterface = data.getSimpleWebsiteInterface();
      AdvancedWebsiteInterface advancedWebsiteInterface = data.getAdvancedWebsiteInterface();
      WebServerRegistration registration = data.getRegistration();
      if (userWeight > maxUsageWeight && registration.usageWeight() != 0) {
        limitReached = true;
        continue;
      }
      for (String key:registration.context()) {
        boolean correctMethod = false, isCorrectPath = false, acceptGet = false, acceptPost = false, acceptPatch = false, acceptPut = false, acceptDelete = false;
        if (registration.acceptPathValues()) {
          Pair<String[], Boolean> pair = getPathValues(path, key);
          if (pair.getY()) {
            PATH_VALUES = pair.getX();
            isCorrectPath = true;
          }
        } else {
          if (path.equals(key)) {
            isCorrectPath = true;
          }
        }
        if (isCorrectPath) {
          if (simpleWebsiteInterface != null) {
            if (registration.acceptPost()) {
              acceptPost = true;
            } else if (registration.acceptPatch()) {
              acceptPatch = true;
            } else if (registration.acceptPut()) {
              acceptPut = true;
            } else if (registration.acceptDelete()) {
              acceptDelete= true;
            } else {
              acceptGet = true;
            }
          } else if (advancedWebsiteInterface != null) {
            if (data.getWebServerRequestType() == WebServerRequestType.GET) {
              acceptGet = true;
            } else if (data.getWebServerRequestType() == WebServerRequestType.POST) {
              acceptPost = true;
            } else if (data.getWebServerRequestType() == WebServerRequestType.PATCH) {
              acceptPatch = true;
            } else if (data.getWebServerRequestType() == WebServerRequestType.PUT) {
              acceptPut = true;
            } else if (data.getWebServerRequestType() == WebServerRequestType.DELETE) {
              acceptDelete= true;
            }
          }
          if (acceptGet) {
            correctMethod = exchange.getRequestMethod().equalsIgnoreCase("GET");
            if (registration.acceptGet_GET_VALUES()) {
              GET_VALUES = getGetValues(exchange);
            }
          }
          if (acceptPost) {
            correctMethod = exchange.getRequestMethod().equalsIgnoreCase("POST");
            if (registration.acceptPost_RAW_STRING()) {
              RAW = getRawValue(exchange);
            } else if (registration.acceptPost_FILE()) {
              FILES = getFile(exchange);
            } else if (registration.acceptPost_JSON()) {
              JSON_OBJECT = getJsonObject(exchange, registration.jsonObject());
            } else {
              X_WWW_FORM_URLENCODED = getPostValues(exchange);
            }
          }
          if (acceptPatch) {
            correctMethod = exchange.getRequestMethod().equalsIgnoreCase("PATCH");
            if (registration.acceptPatch_RAW_STRING()) {
              RAW = getRawValue(exchange);
            } else if (registration.acceptPatch_FILE()) {
              FILES = getFile(exchange);
            } else if (registration.acceptPatch_JSON()) {
              JSON_OBJECT = getJsonObject(exchange, registration.jsonObject());
            } else {
              X_WWW_FORM_URLENCODED = getPostValues(exchange);
            }
          }
          if (acceptPut) {
            correctMethod = exchange.getRequestMethod().equalsIgnoreCase("PUT");
            if (registration.acceptPut_RAW_STRING()) {
              RAW = getRawValue(exchange);
            } else if (registration.acceptPut_FILE()) {
              FILES = getFile(exchange);
            } else if (registration.acceptPut_JSON()) {
              JSON_OBJECT = getJsonObject(exchange, registration.jsonObject());
            } else {
              X_WWW_FORM_URLENCODED = getPostValues(exchange);
            }
          }
          if (acceptDelete) {
            correctMethod = exchange.getRequestMethod().equalsIgnoreCase("DELETE");
            if (registration.acceptDelete_GET_VALUES()) {
              GET_VALUES = getGetValues(exchange);
            }
          }
          if (correctMethod) {
            if (registration.jsonObject() != void.class) {
              if (JSON_OBJECT == null) {
                try {
                  sendSpecialPage(exchange, SpecialWebsite.NOT_JSON_PAGE, 400, "json not valid");
                  exchange.close();
                } catch (Exception ignored) {}
                return;
              }
            }
            try {
              if (simpleWebsiteInterface != null) {
                ret = simpleWebsiteInterface.handleRequest(exchange, PATH_VALUES, GET_VALUES, X_WWW_FORM_URLENCODED, RAW, FILES, JSON_OBJECT);
              } else if (advancedWebsiteInterface != null) {
                if (acceptGet) {
                  ret = advancedWebsiteInterface.getRequest(exchange, PATH_VALUES, GET_VALUES);
                } else if (acceptPost) {
                  ret = advancedWebsiteInterface.postRequest(exchange, PATH_VALUES, X_WWW_FORM_URLENCODED, RAW, FILES, JSON_OBJECT);
                } else if (acceptPatch) {
                  ret = advancedWebsiteInterface.patchRequest(exchange, PATH_VALUES, X_WWW_FORM_URLENCODED, RAW, FILES, JSON_OBJECT);
                } else if (acceptPut) {
                  ret = advancedWebsiteInterface.putRequest(exchange, PATH_VALUES, X_WWW_FORM_URLENCODED, RAW, FILES, JSON_OBJECT);
                } else if (acceptDelete) {
                  ret = advancedWebsiteInterface.deleteRequest(exchange, PATH_VALUES, GET_VALUES);
                }
              }
              methodError = false;
              limitReached = false;
              addUsageWeight(exchange.getRemoteAddress().toString(), registration.usageWeight());
              break outerLoop;
            } catch (Exception e) {
              e.printStackTrace();
              try {
                sendSpecialPage(exchange, SpecialWebsite.INTERNAL_ERROR_PAGE, 500, "not found");
                exchange.close();
              } catch (Exception ignored) {}
              return;
            }
          } else {
            methodError = true;
          }
        }
      }
    }
    try {
      if (limitReached) {
        sendSpecialPage(exchange, SpecialWebsite.LIMIT_REACHED_PAGE, 429, "limit reached");
      } else if (methodError) {
        sendSpecialPage(exchange, SpecialWebsite.METHOD_NOT_SUPPORTED_PAGE, 405, "method not allowed");
      } else {
        if (ret != null) {
          if (ret.getX() != 0) {
            exchange.sendResponseHeaders(ret.getX(), ret.getY().length());
            IOUtils.write(ret.getY(), exchange.getResponseBody(), StandardCharsets.UTF_8);
          } else {
            exchange.close();
            return;
          }
        } else {
          sendSpecialPage(exchange, SpecialWebsite.NOT_FOUND_PAGE, 404, "not found");
        }
      }
      exchange.close();
    } catch (Exception ignored) {}
  }

  private void sendSpecialPage(HttpExchange exchange, SpecialWebsite specialWebsite, int statusCode, String defaultMessage) throws IOException {
    if (specialRegistrations.containsKey(specialWebsite)) {
      Pair<Integer, String> ret = specialRegistrations.get(specialWebsite).getSimpleWebsiteInterface().handleRequest(
          exchange, null, null, null, null, null, null
      );
      exchange.sendResponseHeaders(ret.getX(), ret.getY().length());
      IOUtils.write(ret.getY(), exchange.getResponseBody(), StandardCharsets.UTF_8);
    } else {
      exchange.sendResponseHeaders(statusCode, defaultMessage.length());
      IOUtils.write(defaultMessage, exchange.getResponseBody(), StandardCharsets.UTF_8);
    }
  }

  private Pair<String[], Boolean> getPathValues(String path, String key) {
    ArrayList<String> tmpPathValues = new ArrayList<>();
    String[] pathSegments = key.split("/");
    String[] requestPathSegments = path.split("/");
    boolean correctPath = true;
    if (!key.endsWith("/~~") && !key.endsWith("/?~~")) {
      if (pathSegments.length < requestPathSegments.length) {
        return new Pair<>(null, false);
      }
    }
    for (int i = 0; i != pathSegments.length; i++) {
      String segment = pathSegments[i];
      String requestPathSegment = requestPathSegments.length > i ? requestPathSegments[i] : null;
      if (segment.startsWith("?")) {
        if (requestPathSegment == null) {
          break;
        } else {
          segment = segment.substring(1);
        }
      }
      if (segment.equals("~~")) {
        if (i == pathSegments.length-1) {
          for (int i2 = i; i2 != requestPathSegments.length; i2++) {
            requestPathSegment = requestPathSegments[i2];
            tmpPathValues.add(requestPathSegment);
          }
        } else {
          System.err.println("[WARNING] [WEBSERVER] The registration for the path '" + key + "' is invalid! '~~' is only allowed on the end!");
          correctPath = false;
          break;
        }
      } else if (segment.equals("~")) {
        tmpPathValues.add(requestPathSegment);
      } else {
        String[] splitStaticValues = segment.split("\\|");
        if (splitStaticValues.length != 1) {
          boolean found = false;
          for (String splitStaticValue:splitStaticValues) {
            if (splitStaticValue.equals(requestPathSegment)) {
              found = true;
              tmpPathValues.add(requestPathSegment);
              break;
            }
          }
          if (!found) {
            correctPath = false;
            break;
          }
        } else if (!segment.equals(requestPathSegment)) {
          correctPath = false;
          break;
        }
      }
    }
    if (correctPath) {
      return new Pair<>(tmpPathValues.toArray(new String[0]), true);
    }
    return new Pair<>(null, false);
  }

  private static String getRawValue(HttpExchange exchange) {
    try {
      return IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
    } catch (Exception ignored) {}
    return null;
  }

  private static List<FileItem> getFile(HttpExchange exchange) {
    try {
      DiskFileItemFactory factory = new DiskFileItemFactory();
      ServletFileUpload upload = new ServletFileUpload(factory);
      return upload.parseRequest(new RequestContext() {
        @Override
        public String getCharacterEncoding() {
          return "UTF-8";
        }

        @Override
        public String getContentType() {
          return exchange.getRequestHeaders().getFirst("Content-type");
        }

        @Override
        public int getContentLength() {
          return 0; //tested to work with 0 as return; deprecated anyways
        }

        @Override
        public InputStream getInputStream() throws IOException {
          return exchange.getRequestBody();
        }
      });
    } catch (Exception ignored) {}
    return null;
  }

  private static Object getJsonObject(HttpExchange exchange, Class<?> jsonObjectClass) {
    try {
      if (!exchange.getRequestHeaders().containsKey("Content-Type")) return null;
      if (!exchange.getRequestHeaders().getFirst("Content-Type").equals("application/json")) return null;
      String raw = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
      return gson.fromJson(raw, jsonObjectClass);
    } catch (Exception ignored) {}
    return null;
  }

  private static HashMap<String, String> getGetValues(HttpExchange exchange) {
    String query = exchange.getRequestURI().getQuery();
    if (query == null || query.equals("")) return null;
    HashMap<String, String> result = new HashMap<>();
    for (String param : query.split("&")) {
      String[] entry = param.split("=");
      if (entry.length > 1) {
        result.put(entry[0], entry[1]);
      }else{
        result.put(entry[0], "");
      }
    }
    return result;
  }

  private static HashMap<String, String> getPostValues(HttpExchange exchange) {
    try {
      if (!exchange.getRequestHeaders().containsKey("Content-Type")) return null;
      if (!exchange.getRequestHeaders().getFirst("Content-Type").equals("application/x-www-form-urlencoded")) return null;
      HashMap<String, String> hashMap = new HashMap<>();
      String query = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
      for (String pair:query.split("&")) {
        String[] keyValue = pair.split("=");
        if (keyValue.length == 2) {
          hashMap.put(keyValue[0], URLCoder.decodeURL(keyValue[1]));
        }
      }
      return hashMap;
    } catch (Exception ignored) {}
    return null;
  }

}
