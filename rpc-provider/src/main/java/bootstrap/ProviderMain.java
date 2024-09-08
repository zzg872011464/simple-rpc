package bootstrap;

import config.RegistryConfig;
import config.RpcConfig;
import pojo.MetaInfo;
import registry.LocalRegistry;
import registry.ServerBeanManager;
import registry.center.RegistryManage;
import registry.center.RegistryManageFactory;
import server.RpcHttpServer;
import service.UserService;
import service.impl.UserServiceImpl;

public class ProviderMain {
    public static void main(String[] args) {
        registryService();

        RpcHttpServer rpcHttpServer = new RpcHttpServer();
        rpcHttpServer.doStart(RpcConfig.getInstance().getServerPort());
    }

    private static void registryService() {
        // 注册本地服务
        LocalRegistry.put(UserService.class.getName(), UserServiceImpl.class);
        ServerBeanManager.put(UserServiceImpl.class, new UserServiceImpl());

        // 注册配置中心
        RegistryManage registryManage = RegistryManageFactory.getInstance(RegistryConfig.getInstance().getRegistryType());
        RpcConfig rpcConfig = RpcConfig.getInstance();
        MetaInfo metaInfo = new MetaInfo();
        metaInfo.setIp("127.0.0.1");
        metaInfo.setPort(rpcConfig.getServerPort());
        metaInfo.setServiceName(UserService.class.getName());
        registryManage.registerInstance(metaInfo);
    }
}
