package registry;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LocalRegistry {
    private static final ConcurrentHashMap<String,Class<?>> serverMap  = new ConcurrentHashMap<>();

    public static void put(String serverName,Class<?> impl) {
        serverMap.put(serverName,impl);
    }

    public static Class<?> get(String serverName) {
        return Optional.of(serverMap.get(serverName)).get();
    }
}
