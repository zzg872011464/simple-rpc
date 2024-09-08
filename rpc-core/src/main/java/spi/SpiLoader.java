package spi;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpiLoader {
    private final static Map<String, Map<String, Class<?>>> loaderMap = new HashMap<>();
    private final static Map<String, Object> instanceCache = new HashMap<>();
    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    public static void loadDefaultPath(Class<?> tClass) {
        for (String dir : SCAN_DIRS) {
            load(tClass, dir);
        }
    }

    /**
     * spi加载文件,文件每行值格式示例 : key=tClass
     */
    public static void load(Class<?> tClass, String path) {
        String targetFilePath = path + tClass.getName();
        URL resource = ResourceUtil.getResource(targetFilePath);
        if (null == resource) {
            return;
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            Map<String, Class<?>> singletonClassMap = Optional
                    .ofNullable(loaderMap.get(tClass.getName()))
                    .orElse(new HashMap<>());
            for (String line = bufferedReader.readLine(); StrUtil.isNotBlank(line); line = bufferedReader.readLine()) {
                String[] split = line.split("=");
                if (split.length == 2) {
                    singletonClassMap.put(split[0], Class.forName(split[1]));
                }
            }

            loaderMap.put(tClass.getName(), singletonClassMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getInstance(Class<T> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }
        if (!keyClassMap.containsKey(key)) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }

        // 从实例缓存中加载指定类型的实例
        return getInstanceClass(keyClassMap.get(key));
    }



    public static <T> List<T> getAllInstance(Class<T> tClass) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("SpiLoader 未加载 %s 类型", tClassName));
        }

        return (List<T>) keyClassMap.values().stream().map(SpiLoader::getInstanceClass).toList();
    }

    private static <T> T getInstanceClass(Class<?> implClass) {
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }
}
