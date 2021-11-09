package me.scolastico.tools.cache;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.Callable;
import me.scolastico.tools.handler.ConfigHandler;
import me.scolastico.tools.handler.ErrorHandler;
import me.scolastico.tools.simplified.PBGson;
import org.apache.commons.io.FileUtils;

public class CacheManager {

  private static CacheData cacheData = null;
  private static ConfigHandler<CacheData> configHandler = null;

  public static void destroy() {
    try {
      File folder = new File(".cache");
      for (File file:folder.listFiles()) {
        if (!file.isDirectory()) file.delete();
      }
      cacheData = new CacheData();
      configHandler.saveDefaultConfig();
    } catch (Exception e) {
      ErrorHandler.handleFatal(e);
    }
  }

  public static synchronized void destroy(String id) {
    try {
      CacheData cacheData = getCacheData();
      String uuid = cacheData.getIds().get(id);
      cacheData.delId(id);
      cacheData.delObject(uuid);
      File file = new File(".cache/" + uuid);
      if (file.exists()) file.delete();
    } catch (Exception e) {
      ErrorHandler.handleFatal(e);
    }
  }

  public static void destroyOld() {
    CacheData cacheData = getCacheData();
    ArrayList<String> uuids = new ArrayList<>();
    for (String uuid:cacheData.getObjects().keySet()) {
      CacheObject object = cacheData.getObjects().get(uuid);
      if (object.getValidUntil().isBefore(Instant.now())) {
        uuids.add(uuid);
      }
    }
    ArrayList<String> ids = new ArrayList<>();
    for (String id:cacheData.getIds().keySet()) {
      if (uuids.contains(cacheData.getIds().get(id))) {
        ids.add(id);
      }
    }
    for (String id:ids) {
      destroy(id);
    }
  }

  public static <X> X get(String id, Class<X> classOf, Callable<X> callable) throws Exception {
    return get(id, classOf, callable, 3600);
  }

  public static <X> X get(String id, Class<X> classOf, Callable<X> callable, int validUntil) throws Exception {
    destroyOld();
    CacheData cacheData = getCacheData();
    if (cacheData.getIds().containsKey(id)) {
      try {
        String uuid = cacheData.getIds().get(id);
        CacheObject object = cacheData.getObjects().get(uuid);
        String content = FileUtils.readFileToString(new File(".cache/" + uuid), StandardCharsets.UTF_8);
        X value;
        if (object.getType() == CacheTypes.STRING) {
          value = (X) content;
        } else if (object.getType() == CacheTypes.INTEGER) {
          value = (X) (Integer) Integer.parseInt(content);
        } else if (object.getType() == CacheTypes.FLOAT) {
          value = (X) (Float) Float.parseFloat(content);
        } else if (object.getType() == CacheTypes.DOUBLE) {
          value = (X) (Double) Double.parseDouble(content);
        } else if (object.getType() == CacheTypes.LONG) {
          value = (X) (Long) Long.parseLong(content);
        } else if (object.getType() == CacheTypes.BOOLEAN) {
          value = (X) (Boolean) Boolean.parseBoolean(content);
        } else {
          value = PBGson.s().fromJson(content, classOf);
        }
        return value;
      } catch (Exception e) {
        ErrorHandler.handle(e);
        return callable.call();
      }
    } else {
      X value = callable.call();
      store(id, value, validUntil);
      return value;
    }
  }


  public static synchronized <X> void store(String id, X value) {
    store(id, value, 3600);
  }

  public static synchronized <X> void store(String id, X value, int validUntil) {
    destroyOld();
    CacheTypes type;
    String v;
    if (value instanceof String) {
      type = CacheTypes.STRING;
      v = (String) value;
    } else if (value instanceof Integer) {
      type = CacheTypes.INTEGER;
      v = Integer.toString((Integer) value);
    } else if (value instanceof Float) {
      type = CacheTypes.FLOAT;
      v = Float.toString((Float) value);
    } else if (value instanceof Double) {
      type = CacheTypes.DOUBLE;
      v = Double.toString((Double) value);
    } else if (value instanceof Long) {
      type = CacheTypes.LONG;
      v = Long.toString((Long) value);
    } else if (value instanceof Boolean) {
      type = CacheTypes.BOOLEAN;
      v = Boolean.toString((Boolean) value);
    } else {
      type = CacheTypes.OBJECT;
      v = PBGson.s().toJson(value);
    }
    CacheObject o = new CacheObject(type, Instant.now().plusSeconds(validUntil));
    String uuid;
    CacheData cacheData = getCacheData();
    if (cacheData.getIds().containsKey(id)) {
      uuid = cacheData.getIds().get(id);
      cacheData.delObject(uuid);
    } else {
      do {
        uuid = UUID.randomUUID().toString();
      } while (cacheData.getIds().containsValue(uuid));
      cacheData.addId(id, uuid);
    }
    cacheData.addObject(uuid, o);
    try {
      File file = new File(".cache/" + uuid);
      if (file.exists()) file.delete();
      file.createNewFile();
      FileUtils.writeStringToFile(file, v, StandardCharsets.UTF_8);
      configHandler.storeConfig(cacheData);
    } catch (Exception e) {
      ErrorHandler.handle(e);
    }
  }

  private static CacheData getCacheData() {
    try {
      if (cacheData != null) return cacheData;
      File folder = new File(".cache");
      if (!folder.exists() || !folder.isDirectory()) {
        folder.mkdirs();
      }
      configHandler = new ConfigHandler<>(new CacheData(), ".cache/.cfg");
      if (!configHandler.checkIfExists()) {
        configHandler.saveDefaultConfig();
      }
      cacheData = configHandler.loadConfig();
      return cacheData;
    } catch (Exception e) {
      ErrorHandler.handleFatal(e);
      return null;
    }
  }

}
