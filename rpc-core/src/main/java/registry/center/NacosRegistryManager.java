package registry.center;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import pojo.MetaInfo;

import java.util.List;
import java.util.Properties;

@Slf4j
public class NacosRegistryManager implements RegistryManage {
    private NamingService namingService;

    public void init(String serverAddress) {
        System.out.println();
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddress);
        try {
            this.namingService = NacosFactory.createNamingService(properties);
        } catch (NacosException e) {
            log.error("init nacos service fail exception : {}", e.getErrMsg());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerInstance(MetaInfo metaInfo) {
        Instance instance = new Instance();
        BeanUtil.copyProperties(metaInfo, instance);
        try {
            namingService.registerInstance(metaInfo.getServiceName(), metaInfo.getIp(), metaInfo.getPort());
        } catch (NacosException e) {
            log.error("Service instance register fail: {} with IP: {}, serverPort: {}, error :{}", instance.getServiceName(), instance.getIp(), instance.getPort(), e.getErrMsg());
            throw new RuntimeException(e);
        }
        log.info("Service instance registered: {} with IP: {} and serverPort: {}", instance.getServiceName(), instance.getIp(), instance.getPort());
    }

    @Override
    public void deregisterInstance(MetaInfo metaInfo) {
        Instance instance = new Instance();
        BeanUtil.copyProperties(metaInfo, instance);
        try {
            namingService.deregisterInstance(instance.getServiceName(), instance);
        } catch (NacosException e) {
            log.error("Service instance deregistered fail: {} with IP: {}, serverPort: {}, error :{}", instance.getServiceName(), instance.getIp(), instance.getPort(), e.getErrMsg());
            throw new RuntimeException(e);
        }
        log.info("Service instance deregistered: {} with IP: {} and serverPort: {}", instance.getServiceName(), instance.getIp(), instance.getPort());

    }

    @Override
    public List<Instance> getAllInstances(String serviceName) {
        try {
            return namingService.getAllInstances(serviceName);
        } catch (NacosException e) {
            log.error("get instance fail service name : {}", serviceName);
            throw new RuntimeException(e);
        }
    }
}
