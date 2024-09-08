package pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse implements Serializable {
    private Object result;
    private Class<?> resultType;
    private Throwable exception;
    private Integer resultCode;
}
