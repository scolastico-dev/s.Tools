package me.scolastico.tools.ebean;

/**
 * All available database types
 */
public enum DataBaseType {
  SQL17("sql17"),
  MYSQL("mysql"),
  MARIADB("mariadb"),
  SQLITE("sqlite"),
  POSTGRES("postgres"),
  ORACLE("oracle"),
  H2("h2"),
  IN_MEMORY("in_memory");

  public final String name;

  private DataBaseType(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }
}
