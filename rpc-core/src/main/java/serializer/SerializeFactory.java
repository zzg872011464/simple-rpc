package serializer;

import spi.SpiLoader;

public class SerializeFactory {
    static {
        SpiLoader.loadDefaultPath(Serializer.class);
    }

    public static Serializer getSerializer(String key) {
        return SpiLoader.getInstance(Serializer.class, key);
    }
}
