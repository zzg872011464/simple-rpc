package invoke;

public interface Invoker {
    Object invoke(Invocation invocation) throws Throwable;
}
