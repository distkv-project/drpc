package registry;

import exception.DrpcIllegalUrlException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DrpcURL {

  /**
   *  DrpcURL format:
   *    https ://127.0.0.1:8080/path?key1=value1&key2=value2
   *  Protocol |  Hosts&Posts  |Path| Parameters(queryMap)
   */

  private String protocol;

  private String hostsPorts;

  private String path;

  private Map<String, Object> queryMap = new HashMap<>();

  public DrpcURL (String url) {
    // protocol
    int schemaIndex = url.indexOf("://");
    if (schemaIndex < 0) {
      throw new DrpcIllegalUrlException("Illegal url format: " + url);
    }
    protocol = url.substring(0, schemaIndex);

    // hostsPorts
    int hostsPortsIndex = url.indexOf("/", schemaIndex + 3);
    int pathIndex = url.indexOf("?", schemaIndex + 3);
    if (hostsPortsIndex > 0) {
      this.hostsPorts = url.substring(schemaIndex + 3, hostsPortsIndex);
    } else if (pathIndex > 0) {
      this.hostsPorts = url.substring(schemaIndex + 3, pathIndex);
    } else {
      this.hostsPorts = url.substring(schemaIndex + 3);
    }

    // path
    if (hostsPortsIndex > 0) {
      if (pathIndex > 0) {
        this.path = url.substring(hostsPortsIndex, pathIndex);
      } else {
        this.path = url.substring(hostsPortsIndex);
      }
    } else {
      this.path = "/";
    }

    // query
    if (pathIndex > 0) {
      String queries = url.substring(pathIndex + 1);
      String[] querySplits = queries.split("&");
      for(String queryKV : querySplits) {
        String[] kv = queryKV.split("=");
        queryMap.put(kv[0], kv[1]);
      }
    }
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public String getHostsPorts() {
    return hostsPorts;
  }

  public void setHostsPorts(String hostsPorts) {
    this.hostsPorts = hostsPorts;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Map<String, Object> getQueryMap() {
    return queryMap;
  }

  public void setQueryMap(Map<String, Object> queryMap) {
    this.queryMap = queryMap;
  }

  public void addParameter(String key, String value) {
    this.queryMap.put(key, value);
  }

  public void addAllParameter(Map Parameters) {
    this.queryMap.putAll(Parameters);
  }

  public Object removeParameter(String key) {
    return this.queryMap.remove(key);
  }

  public Object getParameter(String key, Object defaultValue) {
    Object value = this.queryMap.get(key);
    if (value != null) {
      return value;
    } else {
      return defaultValue;
    }
  }

  public String getStringParameter(String key, String defaultValue) {
    Object value = this.queryMap.get(key);
    if (value != null) {
      return (String) value;
    } else {
      return defaultValue;
    }
  }

  public int getIntParameter(String key, int defaultValue) {
    Object value = this.queryMap.get(key);
    if (value != null) {
      return Integer.valueOf((String)value);
    } else {
      return defaultValue;
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(protocol).append("://");
    if (!hostsPorts.isEmpty()) {
      builder.append(hostsPorts);
    }
    builder.append(path);
    int i = queryMap.size();
    if (i > 0) {
      builder.append("?");
      Map.Entry<String, Object> entry;
      Iterator<Map.Entry<String, Object>> iterator = queryMap.entrySet().iterator();
      while (iterator.hasNext()) {
        entry = iterator.next();
        builder.append(entry.getKey()).append("=").append(entry.getValue());
        if (iterator.hasNext()) {
          builder.append("&");
        }
      }
    }
    return builder.toString();
  }
}
