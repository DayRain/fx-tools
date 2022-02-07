package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ServerUrl;
import javafx.stage.Stage;

/**
 * 主要视图的句柄
 * @author peng
 * @date 2021/11/11
 */
public class ViewHolder {

    private static Stage primaryStage;

    private static LogArea logArea;

    private static ServerContainer serverContainer;

    private static HomeView homeView;

    public static void setPrimaryStage(Stage primaryStage) {
        ViewHolder.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    static void setLogArea(LogArea logArea) {
        ViewHolder.logArea = logArea;
    }

    public synchronized static LogArea getLogArea() {
        return logArea;
    }

    public static ServerContainer getServerContainer() {
        return serverContainer;
    }

    static void setServerContainer(ServerContainer serverContainer) {
        ViewHolder.serverContainer = serverContainer;
    }

    public static void setHomePage(HomeView homeView) {
        ViewHolder.homeView = homeView;
    }

    public static HomeView getHomeView() {
        return ViewHolder.homeView;
    }

    public static void log(ServerUrl serverUrl, String params, String resp) {
        if(logArea != null) {
            logArea.log(serverUrl, params, resp);
        }
    }

    public static void refreshLog() {
        logArea.refresh();
    }

    public static void setLogOwner(String serverName) {
        logArea.setCurrentServerName(serverName);
    }
}
