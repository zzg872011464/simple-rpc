package filter.request;

import invoke.Invocation;
import invoke.Invoker;
import spi.SpiLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RequestFilterChain {
    private static final List<RequestFilter> allInstance;

    static {
        SpiLoader.loadDefaultPath(RequestFilter.class);
        allInstance = new ArrayList<>(SpiLoader.getAllInstance(RequestFilter.class));
        allInstance.sort(Comparator.comparingInt(RequestFilter::getOrder));
    }

    public Object invoke(Invoker invoker, Invocation invocation) throws Throwable {
        Invoker chain = createFilterChainInvoker(invoker);
        return chain.invoke(invocation);
    }

    private Invoker createFilterChainInvoker(Invoker finalInvoker) {
        Invoker last = finalInvoker;
        for (int i = allInstance.size() - 1; i >= 0; i--) {
            final RequestFilter requestFilter = allInstance.get(i);
            final Invoker next = last;
            last = invocation -> requestFilter.doInvoke(next, invocation);
        }
        return last;
    }
}
