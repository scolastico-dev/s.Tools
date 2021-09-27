package me.scolastico.tools.ebean;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.TxIsolation;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import java.sql.SQLException;
import org.slf4j.LoggerFactory;

/**
 * Database connector for easy connection with ebean databases.
 */
public class DatabaseConnector {

  private Database database = null;
  private final DataSourceConfig dataSourceConfig = new DataSourceConfig();
  private final MigrationConfig migrationConfig = new MigrationConfig();
  private final io.ebean.config.DatabaseConfig dbConfig = new io.ebean.config.DatabaseConfig();
  private DatabaseConfig config = null;
  private boolean started = false;
  private boolean debug = false;
  private boolean migrated = false;
  private final boolean defaultServer;

  /**
   * Get a database connector. The database can be the default server.
   * @param defaultServer Should the connected database be the default database?
   */
  public DatabaseConnector(boolean defaultServer) {
    this.defaultServer = defaultServer;
  }

  /**
   * Get a database connector. The database will not be the default server.
   */
  public DatabaseConnector() {
    defaultServer = false;
  }

  /**
   * Load and initialize the database drivers. Execute this once before connecting to a database.
   * @throws ClassNotFoundException If a driver was not found a ClassNotFoundException will then be thrown.
   */
  public static void loadDatabaseDrivers() throws ClassNotFoundException {
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    Class.forName("com.mysql.cj.jdbc.Driver");
    Class.forName("org.mariadb.jdbc.Driver");
    Class.forName("org.sqlite.JDBC");
    Class.forName("org.postgresql.Driver");
    Class.forName("oracle.jdbc.driver.OracleDriver");
    Class.forName("org.h2.Driver");
  }

  /**
   * Connect to a database.
   * @param config The DatabaseConfig with all important values for the connection.
   */
  public synchronized void connectToDatabase(DatabaseConfig config) {
    if (!started) {
      LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
      Logger ebeanLogger = loggerContext.getLogger("io.ebean");
      Logger reflectionsLogger = loggerContext.getLogger("org.reflections");
      debug = config.isDebug();
      if (debug) {
        ebeanLogger.setLevel(Level.DEBUG);
        reflectionsLogger.setLevel(Level.DEBUG);
      } else {
        ebeanLogger.setLevel(Level.WARN);
        reflectionsLogger.setLevel(Level.WARN);
      }
      this.config = config;
      dataSourceConfig.setUsername(config.getUsername());
      dataSourceConfig.setPassword(config.getPassword());
      migrationConfig.setMigrationPath("dbmigration/" + config.getDatabaseType().name);
      switch (config.getDatabaseType().name) {
        case "sql17":
          dataSourceConfig.setDriver("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          dataSourceConfig.setUrl("jdbc:sqlserver://"
              + config.getHost() + ":"
              + config.getPort() + ";"
              + "databaseName=" + config.getDatabase()
          );
          break;

        case "mysql":
          dataSourceConfig.setDriver("com.mysql.cj.jdbc.Driver");
          dataSourceConfig.setUrl("jdbc:mysql://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase()
          );
          break;

        case "mariadb":
          dataSourceConfig.setDriver("org.mariadb.jdbc.Driver");
          dataSourceConfig.setUrl("jdbc:mariadb://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase() + "?useLegacyDatetimeCode=false"
          );
          break;

        case "sqlite":
          dataSourceConfig.setDriver("org.sqlite.JDBC");
          dataSourceConfig.setUrl("jdbc:sqlite:" + config.getHost());
          dataSourceConfig.setIsolationLevel(TxIsolation.READ_UNCOMMITTED.getLevel());
          break;

        case "postgres":
          dataSourceConfig.setDriver("org.postgresql.Driver");
          dataSourceConfig.setUrl("jdbc:postgresql://"
              + config.getHost() + ":"
              + config.getPort() + "/"
              + config.getDatabase()
          );
          break;

        case "oracle":
          dataSourceConfig.setDriver("oracle.jdbc.driver.OracleDriver");
          dataSourceConfig.setUsername(config.getDatabase());
          dataSourceConfig.setUrl("jdbc:oracle:thin:@"
              + config.getHost() + ":"
              + config.getPort() + ":XE");
          break;

        case "h2":
          dataSourceConfig.setDriver("org.h2.Driver");
          dataSourceConfig.setUrl("jdbc:h2:mem" + config.getDatabase());
          break;
      }
      dbConfig.setDataSourceConfig(dataSourceConfig);
      dbConfig.setName(config.getDatabase());
      dbConfig.setDefaultServer(defaultServer);
      database = DatabaseFactory.create(dbConfig);
      started = true;
    }
  }

  /**
   * Run database migrations.
   */
  public synchronized void runMigrations() throws SQLException {
    if (!migrated) {
      MigrationRunner migrationRunner = new MigrationRunner(migrationConfig);
      migrationRunner.run(database.dataSource().getConnection());
      migrated = true;
    }
  }

  public Database getDatabase() {
    return database;
  }

  public DataSourceConfig getDataSourceConfig() {
    return dataSourceConfig;
  }

  public MigrationConfig getMigrationConfig() {
    return migrationConfig;
  }

  public io.ebean.config.DatabaseConfig getDbConfig() {
    return dbConfig;
  }

  public DatabaseConfig getConfig() {
    return config;
  }

  public boolean isStarted() {
    return started;
  }

  public boolean isDebug() {
    return debug;
  }

  public boolean isMigrated() {
    return migrated;
  }

}
