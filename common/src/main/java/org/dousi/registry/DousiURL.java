package org.dousi.registry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.dousi.exception.DousiIllegalUrlException;

@Getter
@Setter
public class DousiURL {

  /**
   *  DousiURL format:
   *    distkv://127.0.0.1:8080/path?key1=value1&key2=value2
   *  Protocol|   Hosts&Posts  |Path|  Parameters(queryMap)
   */

  private String protocol;

  private String hostsPorts;

  private String path;

  private Map<String, Object> queryMap = new HashMap<>();

  public DousiURL(String url) {
    // protocol
    int protocolIndex = url.indexOf("://");
    if (protocolIndex < 0) {
      throw new DousiIllegalUrlException("Illegal url format: " + url);
    }
    protocol = url.substring(0, protocolIndex);

    // hostsPorts
    int hostsPortsIndex = url.indexOf("/", protocolIndex + 3);
    int pathIndex = url.indexOf("?", protocolIndex + 3);
    if (hostsPortsIndex > 0) {
      this.hostsPorts = url.substring(protocolIndex + 3, hostsPortsIndex);
    } else if (pathIndex > 0) {
      this.hostsPorts = url.substring(protocolIndex + 3, pathIndex);
    } else {
      this.hostsPorts = url.substring(protocolIndex + 3);
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
      for (String queryKV : querySplits) {
        String[] kv = queryKV.split("=");
        queryMap.put(kv[0], kv[1]);
      }
    }
  }

  public void addParameter(String key, String value) {
    this.queryMap.put(key, value);
  }

  public void addAllParameter(Map parameters) {
    this.queryMap.putAll(parameters);
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
