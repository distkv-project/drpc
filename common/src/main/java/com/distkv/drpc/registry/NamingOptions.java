package registry;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
