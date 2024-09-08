package config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

public class ConfigUtil {
    public static <T> T loadConfig(T bean, String prefix, String environment) {
        StringBuilder stringBuilder = new StringBuilder("application");
        if (StrUtil.isNotBlank(environment)) {
            stringBuilder.append("-").append(environment);
        }
        stringBuilder.append(".properties");
        Props props = new Props(stringBuilder.toString());

        return props.fillBean(bean, prefix);
    }
}
