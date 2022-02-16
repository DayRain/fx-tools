package com.dayrain.ui.config;

import java.util.HashMap;
import java.util.Map;

public class DbMarkConfig {
    private static final Map<String, String> defaultCommentMap = new HashMap<>();

    static {
        defaultCommentMap.put("createTime", "创建时间");
        defaultCommentMap.put("updateTime", "更新时间");
        defaultCommentMap.put("id", "主键id");
    }

    public static String getDefaultComment(String columnName) {
        return defaultCommentMap.getOrDefault(columnName, null);
    }
}
