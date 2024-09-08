package config;

import bootstrap.RpcApplication;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import registry.center.RegistryManage;
import registry.center.RegistryManageFactory;

import static constant.RpcConstant.REGISTRY_CONFIG_PREFIX;

@Data
@Slf4j
public class RegistryConfig {

    private static volatile RegistryConfig instance;

    /**
     * 注册中心类别
     */
    private String registryType = "nacos";

    /**
     * 注册中心地址
     */
    private String address = "127.0.0.1:8848";

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 超时时间（单位毫秒）
     */
    private Long timeout = 10000L;

    public static RegistryConfig getInstance() {
        if (null == instance) {
            synchronized (RpcApplication.class) {
                if (null == instance) {
                    init();
                }
            }
        }

        return instance;
    }

    private static void init() {
        RegistryConfig registryConfig = ConfigUtil.loadConfig(new RegistryConfig(), REGISTRY_CONFIG_PREFIX, "");
        RegistryManage registry = RegistryManageFactory.getInstance(registryConfig.getRegistryType());
        registry.init(registryConfig.getAddress());
        log.info("init registry config : {}", registryConfig);
        instance = registryConfig;
    }

    private RegistryConfig(){};
}
