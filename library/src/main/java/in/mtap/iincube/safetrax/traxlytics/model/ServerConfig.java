package in.mtap.iincube.safetrax.traxlytics.model;


public class ServerConfig {

  private String username;
  private String password;
  private String url;
  private String dbName;
  private String colName;

  public ServerConfig(String username, String password, String url, String dbName, String colName) {
    this.username = username;
    this.password = password;
    this.url = url;
    this.dbName = dbName;
    this.colName = colName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDbName() {
    return dbName;
  }

  public void setDbName(String dbName) {
    this.dbName = dbName;
  }

  public String getColName() {
    return colName;
  }

  public void setColName(String colName) {
    this.colName = colName;
  }
}
