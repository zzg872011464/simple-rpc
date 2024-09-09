package filter.request;

import invoke.Invocation;
import invoke.Invoker;

public interface RequestFilter {
    Object doInvoke(Invoker invoker, Invocation invocation) throws Throwable;

    int getOrder();
}
