package serializer;

import com.alibaba.fastjson.JSON;
import constant.ResultCodeEnum;
import pojo.RpcRequest;
import pojo.RpcResponse;

import java.nio.charset.StandardCharsets;

public class FastJsonSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T object) {
        return JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) {
        T object = JSON.parseObject(bytes, type);
        if (object instanceof RpcRequest) {
            return deserializeRequest((RpcRequest) object);
        } else if (object instanceof RpcResponse) {
            return deserializeResponse((RpcResponse) object);
        }

        return object;
    }

    private <T> T deserializeRequest(RpcRequest request) {
        Object[] args = request.getArgs();
        Class<?>[] parameterTypes = request.getParameterTypes();
        for (int i = 0; i < args.length; i++) {
            if (!args[i].getClass().isAssignableFrom(parameterTypes[i])) {
                args[i] = JSON.parseObject(JSON.toJSONString(args[i]), parameterTypes[i]);
            }
        }

        return (T) request;
    }

    private <T> T deserializeResponse(RpcResponse response) {
        if (ResultCodeEnum.OK.getResultCode().equals(response.getResultCode())) {
            response.setResult(JSON.parseObject(JSON.toJSONString(
                    response.getResult()), response.getResultType()));
        }

        return (T) response;
    }
}
