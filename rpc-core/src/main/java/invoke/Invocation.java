package invoke;

import java.lang.reflect.Method;

public record Invocation(Method method, Object[] args) {
}