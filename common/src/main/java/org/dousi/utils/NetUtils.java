package org.dousi.utils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetUtils {

  private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

  public static final String LOCALHOST = "127.0.0.1";

  public static final String ANYHOST = "0.0.0.0";

  private static volatile InetAddress LOCAL_ADDRESS = null;

  private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

  private static final Pattern ADDRESS_PATTERN = Pattern
      .compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");

  private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

  public static boolean isInvalidLocalHost(String host) {
    return host == null || host.length() == 0 || host.equalsIgnoreCase("localhost") || host
        .equals("0.0.0.0")
        || (LOCAL_IP_PATTERN.matcher(host).matches());
  }

  public static boolean isValidLocalHost(String host) {
    return !isInvalidLocalHost(host);
  }

  /**
   * {@link #getLocalAddress(Map)}
   */
  public static InetAddress getLocalAddress() {
    return getLocalAddress(null);
  }

  public static InetAddress getLocalAddress(Map<String, Integer> destHostPorts) {
    if (LOCAL_ADDRESS != null) {
      return LOCAL_ADDRESS;
    }

    InetAddress localAddress = getLocalAddressByHostname();
    if (!isValidAddress(localAddress)) {
      localAddress = getLocalAddressBySocket(destHostPorts);
    }

    if (!isValidAddress(localAddress)) {
      localAddress = getLocalAddressByNetworkInterface();
    }

    if (isValidAddress(localAddress)) {
      LOCAL_ADDRESS = localAddress;
    }

    return localAddress;
  }

  private static InetAddress getLocalAddressByHostname() {
    try {
      InetAddress localAddress = InetAddress.getLocalHost();
      if (isValidAddress(localAddress)) {
        return localAddress;
      }
    } catch (Throwable e) {
      logger.warn("Failed to retriving local address by hostname:" + e);
    }
    return null;
  }

  private static InetAddress getLocalAddressBySocket(Map<String, Integer> destHostPorts) {
    if (destHostPorts == null || destHostPorts.size() == 0) {
      return null;
    }

    for (Map.Entry<String, Integer> entry : destHostPorts.entrySet()) {
      String host = entry.getKey();
      int port = entry.getValue();
      try {
        Socket socket = new Socket();
        try {
          SocketAddress addr = new InetSocketAddress(host, port);
          socket.connect(addr, 1000);
          return socket.getLocalAddress();
        } finally {
          try {
            socket.close();
          } catch (Throwable e) {
            logger.warn(String.format(
                "Failed to close the socket connecting to dest host:port(%s:%s) false, e=%s",
                host,
                port, e));
          }
        }
      } catch (Exception e) {
        logger.warn(String.format(
            "Failed to retriving local address by connecting to dest host:port(%s:%s) false, e=%s",
            host,
            port, e));
      }
    }
    return null;
  }

  private static InetAddress getLocalAddressByNetworkInterface() {
    try {
      Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
      if (interfaces != null) {
        while (interfaces.hasMoreElements()) {
          try {
            NetworkInterface network = interfaces.nextElement();
            Enumeration<InetAddress> addresses = network.getInetAddresses();
            while (addresses.hasMoreElements()) {
              try {
                InetAddress address = addresses.nextElement();
                if (isValidAddress(address)) {
                  return address;
                }
              } catch (Throwable e) {
                logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
              }
            }
          } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
          }
        }
      }
    } catch (Throwable e) {
      logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
    }
    return null;
  }

  public static boolean isValidAddress(String address) {
    return ADDRESS_PATTERN.matcher(address).matches();
  }

  public static boolean isValidAddress(InetAddress address) {
    if (address == null || address.isLoopbackAddress()) {
      return false;
    }
    String name = address.getHostAddress();
    return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN
        .matcher(name).matches());
  }

  //return ip to avoid lookup dns
  public static String getHostName(SocketAddress socketAddress) {
    if (socketAddress == null) {
      return null;
    }

    if (socketAddress instanceof InetSocketAddress) {
      InetAddress addr = ((InetSocketAddress) socketAddress).getAddress();
      if (addr != null) {
        return addr.getHostAddress();
      }
    }

    return null;
  }
}
