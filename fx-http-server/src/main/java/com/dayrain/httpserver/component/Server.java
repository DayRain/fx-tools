package com.dayrain.httpserver.component;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 服务
 * @author peng
 * @date 2021/10/25
 */
public class Server implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final ServerConfig serverConfig;

    private HttpServer httpServer;

    public Server(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }


    @Override
    public void run() {
        start();
        System.out.println("【" + serverConfig.getServerName() + "】服务已开启...");
    }

    public synchronized void start() {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(serverConfig.getPort()), 0);
            httpServer.setExecutor(Executors.newCachedThreadPool());
            for (ServerUrl serverUrl : serverConfig.getServerUrls()) {
                addContext(serverUrl);
            }
            httpServer.start();

        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

    }

    public synchronized void stop() {
        if (httpServer != null) {
            System.out.println("【" + serverConfig.getServerName() + "】服务已关闭...");
            httpServer.stop(0);
        }
    }

    public synchronized void restart() {
        stop();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        start();
    }

    public synchronized void addContext(ServerUrl serverUrl) {
        httpServer.createContext(serverUrl.getUrl(), new RequestHandler(serverUrl));
    }

    public void removeContext(String url) {
        httpServer.removeContext(url);
    }
}
