package config;

import bootstrap.RpcApplication;
import constant.SerializerKeys;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static constant.RpcConstant.RPC_CONFIG_PREFIX;

@Data
@ToString
@Slf4j
public class RpcConfig {
    private static volatile RpcConfig instance;

    private String name = "rpc";
    private String version = "1.0";
    private String serverHost = "localhost";
    private Integer serverPort = 8888;
    private String serializer = SerializerKeys.JDK.name();

    public static RpcConfig getInstance() {
        if (null == instance) {
            synchronized (RpcApplication.class) {
                if (null == instance) {
                    init();
                }
            }
        }

        return instance;
    }

    public String getUrl() {
        return serverHost + ":" + serverPort;
    }


    private static void init() {
        RpcConfig newConfig = ConfigUtil.loadConfig(new RpcConfig(), RPC_CONFIG_PREFIX, "");
        log.info("init rpc config : {}", newConfig);
        instance = newConfig;
    }


    private RpcConfig(){}
}
