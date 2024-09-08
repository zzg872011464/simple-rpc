package handler;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import config.RpcConfig;
import constant.ResultCodeEnum;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import pojo.RpcRequest;
import pojo.RpcResponse;
import registry.LocalRegistry;
import registry.ServerBeanManager;
import serializer.SerializeFactory;
import serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RpcHttpRequestHandler implements Handler<HttpServerRequest> {

    @Override
    public void handle(HttpServerRequest request) {
        Serializer serializer = SerializeFactory.getSerializer(RpcConfig.getInstance().getSerializer());

        request.bodyHandler(body -> {
                    byte[] bytes = body.getBytes();
                    RpcRequest rpcRequest = null;
                    RpcResponse rpcResponse = new RpcResponse();
                    try {
                        rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
                        Class<?> targetClass = LocalRegistry.get(rpcRequest.getServerName());
                        Object targetInstance = ServerBeanManager.get(targetClass);
                        if (null == targetInstance) {
                            targetInstance = targetClass.getDeclaredConstructor().newInstance();
                        }
                        Method method = targetClass.getMethod(rpcRequest.getMethod(), rpcRequest.getParameterTypes());
                        Object result = method.invoke(targetInstance, rpcRequest.getArgs());
                        rpcResponse.setResult(result);
                        rpcResponse.setResultType(method.getReturnType());
                        rpcResponse.setResultCode(ResultCodeEnum.OK.getResultCode());
                    } catch (InvocationTargetException e) {
                        rpcResponse.setException(e.getTargetException());
                        rpcResponse.setResultCode(ResultCodeEnum.FAIL.getResultCode());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    doResponse(request, rpcResponse, serializer);
                }

        );

    }

    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue());

        try {
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.setStatusCode(500).end("Internal Server Error");
        }
    }
}
