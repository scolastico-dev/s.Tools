package me.scolastico.tools.ebean;

import io.ebean.annotation.Platform;
import io.ebean.dbmigration.DbMigration;
import java.io.IOException;

/**
 * DatabaseMigrationGenerator to generate ebean migration dll's.
 */
public class DatabaseMigrationGenerator {

  /**
   * Generate database migration dll's. Run this function once in development after you changed something in the database classes.
   * @param version The semantic version string of the database.
   * @param name The name of this migration.
   * @throws IOException If the files can't be written.
   */
  public static void create(String version, String name) throws IOException {
    DbMigration dbMigration = DbMigration.create();
    dbMigration.setVersion(version);
    dbMigration.setName(name);
    dbMigration.addPlatform(Platform.SQLSERVER17, "sql17");
    dbMigration.addPlatform(Platform.MYSQL, "mysql");
    dbMigration.addPlatform(Platform.MARIADB, "mariadb");
    dbMigration.addPlatform(Platform.SQLITE, "sqlite");
    dbMigration.addPlatform(Platform.POSTGRES, "postgres");
    dbMigration.addPlatform(Platform.ORACLE, "oracle");
    dbMigration.addPlatform(Platform.H2, "h2");
    dbMigration.generateMigration();
  }

}
