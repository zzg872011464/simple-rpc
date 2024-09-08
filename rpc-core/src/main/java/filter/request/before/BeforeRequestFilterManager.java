package filter.request.before;

import pojo.RpcRequest;
import spi.SpiLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BeforeRequestFilterManager {
    private static final List<BeforeRequestFilter> allInstance;

    static {
        SpiLoader.loadDefaultPath(BeforeRequestFilter.class);
        allInstance = new ArrayList<>(SpiLoader.getAllInstance(BeforeRequestFilter.class));
        allInstance.sort(Comparator.comparingInt(BeforeRequestFilter::getOrder));
    }

    public static void doFilter(RpcRequest request) {
        allInstance.forEach(filer -> filer.doFilter(request));
    }
}
