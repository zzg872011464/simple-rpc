package registry;

import java.util.concurrent.ConcurrentHashMap;

public class ServerBeanManager {
    private static final ConcurrentHashMap<Class<?>,Object> beanMap = new ConcurrentHashMap<>();

    public static void put(Class<?> implClass,Object object) {
        beanMap.put(implClass, object);
    }

    public static Object get(Class<?> serverName) {
        return beanMap.get(serverName);
    }
}
