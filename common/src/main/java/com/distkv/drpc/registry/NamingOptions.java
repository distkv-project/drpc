package registry;

import java.util.Map;

public class NamingOptions {

  private String group = "default";

  private String vision = "0.0.1";

  private String serviceId = "";

  private Map<String, String> parameters;

  private boolean ignoreFailOfNamingService = false;

  public NamingOptions() {

  }

  public NamingOptions(String group,
                       String vision,
                       String serviceId,
                       Map parameters,
                       boolean ignoreFailOfNamingService) {
    this.group = group;
    this.vision = vision;
    this.serviceId =serviceId;
    this.parameters = parameters;
    this.ignoreFailOfNamingService = ignoreFailOfNamingService;
  }

  public NamingOptions(NamingOptions op) {
    this.group = op.getGroup();
    this.vision = op.getVision();
    this.serviceId = op.getServiceId();
    this.parameters = op.getParameters();
    this.ignoreFailOfNamingService = op.isIgnoreFailOfNamingService();
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getVision() {
    return vision;
  }

  public void setVision(String vision) {
    this.vision = vision;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public boolean isIgnoreFailOfNamingService() {
    return ignoreFailOfNamingService;
  }

  public void setIgnoreFailOfNamingService(boolean ignoreFailOfNamingService) {
    this.ignoreFailOfNamingService = ignoreFailOfNamingService;
  }
}
