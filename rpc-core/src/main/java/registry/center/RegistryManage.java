package registry.center;

import com.alibaba.nacos.api.naming.pojo.Instance;
import pojo.MetaInfo;

import java.util.List;

public interface RegistryManage {
    void init(String serverAddress);
    void registerInstance(MetaInfo metaInfo);
    void deregisterInstance(MetaInfo metaInfo);
    List<Instance> getAllInstances(String serviceName);
}
