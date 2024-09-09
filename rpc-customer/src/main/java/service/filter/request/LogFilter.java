package service.filter.request;

import filter.request.RequestFilter;
import invoke.Invocation;
import invoke.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFilter implements RequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public Object doInvoke(Invoker invoker, Invocation invocation) throws Throwable {
        log.info("Before invoking: {}", invocation.method().getName());

        if (shouldBlockInvocation()) {
            log.warn("Blocked by LoggingFilter");
            return null;
        }

        Object result = invoker.invoke(invocation);

        log.info("After invoking: {}", invocation.method().getName());
        return result;
    }

    @Override
    public int getOrder() {
        return 1;
    }

    private boolean shouldBlockInvocation() {
        return false;
    }
}
