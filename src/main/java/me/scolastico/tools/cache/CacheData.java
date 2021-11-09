package me.scolastico.tools.cache;

import java.util.HashMap;

/**
 * Internal CacheData object for storing data of the cache.
 */
public class CacheData {

  HashMap<String, CacheObject> objects = new HashMap<>();
  HashMap<String, String> ids = new HashMap<>();

  public HashMap<String, CacheObject> getObjects() {
    return objects;
  }

  public void setObjects(HashMap<String, CacheObject> objects) {
    this.objects = objects;
  }

  public void addObject(String key, CacheObject object) {
    objects.put(key, object);
  }

  public void delObject(String key) {
    objects.remove(key);
  }

  public HashMap<String, String> getIds() {
    return ids;
  }

  public void setIds(HashMap<String, String> ids) {
    this.ids = ids;
  }

  public void addId(String key, String object) {
    ids.put(key, object);
  }

  public void delId(String key) {
    ids.remove(key);
  }

}
