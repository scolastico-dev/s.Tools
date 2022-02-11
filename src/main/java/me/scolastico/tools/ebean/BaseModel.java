package me.scolastico.tools.ebean;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Base model for database classes. Extend this class with your database class.
 */
@MappedSuperclass
public abstract class BaseModel extends Model {

  @Id
  protected UUID id;

  @Version
  protected Long version;

  @WhenCreated
  protected Instant created;

  @WhenModified
  protected Instant modified;

  protected BaseModel() {
    super();
    this.id = null;
    this.version = null;
    this.created = null;
    this.modified = null;
  }

  protected BaseModel(String dbName) {
    super(dbName);
    this.id = null;
    this.version = null;
    this.created = null;
    this.modified = null;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Instant getCreated() {
    return created;
  }

  public void setCreated(Instant created) {
    this.created = created;
  }

  public Instant getModified() {
    return modified;
  }

  public void setModified(Instant modified) {
    this.modified = modified;
  }

}
