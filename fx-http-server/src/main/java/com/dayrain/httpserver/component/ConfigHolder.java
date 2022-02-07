package com.dayrain.httpserver.component;

import com.dayrain.httpserver.utils.FileUtils;

/**
 * 配置类句柄
 * @author peng
 * @date 2021/11/8
 */
public class ConfigHolder {

    private static Configuration configuration;

    private ConfigHolder() {
    }

    public synchronized static Configuration init() {
        configuration = FileUtils.load();
        return configuration;
    }

    public synchronized static void save() {
        FileUtils.saveConfig(configuration);
    }

    public synchronized static Configuration get() {
        if(configuration == null) {
            init();
        }
        return configuration;
    }

    public synchronized static void replace(Configuration config) {
        configuration = config;
    }
}
