package registry;

import java.util.HashMap;
import java.util.Map;

import com.distkv.drpc.exception.DrpcRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NamingServiceFactoryManager {

  private Logger logger = LoggerFactory.getLogger(NamingServiceFactoryManager.class);

  //FactoryManager thread consistence;
  private volatile NamingServiceFactoryManager instance;

  private Map<String, NamingServiceFactory> namingServiceFactoryMap;

  private NamingServiceFactoryManager getInstance() {
    if (instance == null) {
      synchronized (NamingServiceFactoryManager.class) {
        if(instance == null) {
          instance = new NamingServiceFactoryManager();
        }
        return instance;
      }
    }
    return instance;
  }

  private NamingServiceFactoryManager() {
    this.namingServiceFactoryMap = new HashMap<>();
    this.namingServiceFactoryMap.put("dst", new DistKvNamingServiceFactory());
  }

  public void registerNamingServiceFactory(NamingServiceFactory factory) {
    if (namingServiceFactoryMap.get(factory.getName()) != null) {
      throw new DrpcRuntimeException("NamingServiceFactory exists: " + factory.getName());
    }
    this.namingServiceFactoryMap.put(factory.getName(), factory);
    logger.info("ServiceFactory has registered: " + factory.getName());
  }

  public NamingServiceFactory getNamingServiceFactory(String name) {
    return this.namingServiceFactoryMap.get(name);
  }
}
