package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ConfigHolder;
import com.dayrain.httpserver.component.ServerUrl;
import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 打印日志的组件
 * 每个服务都有各自的组件
 * @author peng
 * @date 2021/11/8
 */
public class LogArea extends TextArea {

    private static final ConcurrentHashMap<String, String> LOG_MAP = new ConcurrentHashMap<>();

    private static final int LOG_LENGTH = 5000;

    private ContextMenu contextMenu;

    private MenuItem clearMenu;

    /**
     * 当前服务名
     */
    private String currentServerName;

    public synchronized void log(ServerUrl serverUrl, String params, String resp) {
        if(serverUrl.isHiddenLog()) {
            return;
        }

        String str = "[" + now() + "]" +
                serverUrl.getUrlName() + "   " +
                serverUrl.getUrl() + "   " + serverUrl.getRequestType().name() + "\n" +
                "参数: " + "\n" +
                params + "\n" +
                "返回值: " + "\n" +
                resp +
                "\n\n";

        logAppend(serverUrl.getServerName(), str);

        refresh();

    }

    public void logAppend(String serverName, String logContent) {
        String log = LOG_MAP.getOrDefault(serverName, null);
        if(log == null) {
            LOG_MAP.put(serverName, logContent);
            return;
        }

        log += logContent;
        if(log.length() > LOG_LENGTH) {
            log = log.substring(log.length() - LOG_LENGTH);
        }
        LOG_MAP.put(serverName, log);
    }

    public LogArea() {
        createView();
    }

    public void createView() {
        this.setEditable(false);
        this.setFont(Font.font("Microsoft YaHei", 16));
        this.setPrefWidth(582);
        this.setPrefHeight(ConfigHolder.get().getHeight());

        clearMenu = new MenuItem("清空");
        contextMenu = new ContextMenu();
        contextMenu.getItems().addAll(clearMenu);
        this.setContextMenu(contextMenu);

        clearMenu.setOnAction(event -> {
            clear();
        });
    }

    public String getCurrentServerName() {
        return currentServerName;
    }

    public synchronized void setCurrentServerName(String currentServerName) {
        this.currentServerName = currentServerName;
    }

    private static String now() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-DD-mm HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public void refresh() {
        if(currentServerName == null) {
            //拒绝刷新
            return;
        }
        Platform.runLater(()->{
            setText("");
            appendText(LOG_MAP.getOrDefault(currentServerName, ""));
            setScrollTop(Double.MAX_VALUE);
        });
    }
}
