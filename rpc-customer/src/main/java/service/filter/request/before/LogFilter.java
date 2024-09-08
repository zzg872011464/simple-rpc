package service.filter.request.before;

import filter.request.before.BeforeRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pojo.RpcRequest;

public class LogFilter implements BeforeRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void doFilter(RpcRequest rpcRequest) {
        log.info("LogFilter ----{}", rpcRequest.toString());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
