package me.scolastico.tools.cache;

import java.time.Instant;

public class CacheObject {

  private final CacheTypes type;
  private final Instant validUntil;

  public CacheObject(CacheTypes type, Instant validUntil) {
    this.type = type;
    this.validUntil = validUntil;
  }

  public CacheTypes getType() {
    return type;
  }

  public Instant getValidUntil() {
    return validUntil;
  }

}
