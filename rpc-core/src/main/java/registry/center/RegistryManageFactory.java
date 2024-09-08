package registry.center;

import serializer.Serializer;
import spi.SpiLoader;

public class RegistryManageFactory {
    static {
        SpiLoader.loadDefaultPath(RegistryManage.class);
    }

    public static RegistryManage getInstance(String key) {
        return SpiLoader.getInstance(RegistryManage.class,key);
    }
}
