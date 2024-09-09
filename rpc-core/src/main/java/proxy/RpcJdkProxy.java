package proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.nacos.api.naming.pojo.Instance;
import config.RegistryConfig;
import config.RpcConfig;
import filter.request.RequestFilterChain;
import invoke.Invocation;
import invoke.Invoker;
import pojo.RpcRequest;
import pojo.RpcResponse;
import registry.center.RegistryManage;
import registry.center.RegistryManageFactory;
import serializer.SerializeFactory;
import serializer.Serializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

public class RpcJdkProxy implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation(method, args);
        Invoker realInvoker = new RealInvoker();
        RequestFilterChain requestFilterChain = new RequestFilterChain();
        return requestFilterChain.invoke(realInvoker, invocation);
    }

    private class RealInvoker implements Invoker {
        @Override
        public Object invoke(Invocation invocation) throws Throwable {
            Serializer serializer = SerializeFactory.getSerializer(RpcConfig.getInstance().getSerializer());
            RpcRequest rpcRequest = RpcRequest.builder()
                    .serverName(invocation.method().getDeclaringClass().getName())
                    .method(invocation.method().getName())
                    .parameterTypes(invocation.method().getParameterTypes())
                    .args(invocation.args())
                    .build();

            byte[] serialized = serializer.serialize(rpcRequest);
            try (HttpResponse httpResponse = HttpRequest.post(getServerUrl(invocation.method().getDeclaringClass().getName()))
                    .body(serialized)
                    .execute()) {
                byte[] bytes = httpResponse.bodyBytes();
                RpcResponse rpcResponse = serializer.deserialize(bytes, RpcResponse.class);

                if (rpcResponse.getException() != null) {
                    throw rpcResponse.getException();
                }
                return rpcResponse.getResult();
            }
        }
    }

    private String getServerUrl(String serverName) {
        RegistryManage registryManage = RegistryManageFactory.getInstance(RegistryConfig.getInstance().getRegistryType());
        Instance instance = Optional.ofNullable(registryManage.getAllInstances(serverName))
                .filter(item -> !item.isEmpty())
                .map(item -> item.get(0))
                .orElseThrow(() -> new RuntimeException("No instance found for server: " + serverName));

        return instance.getIp() + ":" + instance.getPort();
    }
}
