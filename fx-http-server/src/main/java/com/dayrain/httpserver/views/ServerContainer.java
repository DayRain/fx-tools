package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ServerConfig;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * server容器
 * @author peng
 * @date 2021/11/11
 */
public class ServerContainer extends VBox {

    private final List<ServerPane> serverPanes;

    public ServerContainer(List<ServerPane> serverPanes) {
        this.serverPanes = serverPanes;
        createView();
    }

    public void createView() {
        getChildren().addAll(serverPanes);
    }

    public void refresh() {
        getChildren().clear();
        getChildren().addAll(serverPanes);
    }

    public synchronized void addServer(ServerConfig serverConfig) {
        serverPanes.add(new ServerPane(serverConfig));
        refresh();
    }

    public synchronized void removeServer(ServerConfig serverConfig) {
        if(serverConfig == null) {
            return;
        }
        serverPanes.removeIf(serverPane -> serverConfig.getServerName().equals(serverPane.getServerConfig().getServerName()));
        refresh();
    }
}
