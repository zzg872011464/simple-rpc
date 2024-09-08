package filter.request.before;

import pojo.RpcRequest;

public interface BeforeRequestFilter {
    void doFilter(RpcRequest rpcRequest);

    int getOrder();
}
