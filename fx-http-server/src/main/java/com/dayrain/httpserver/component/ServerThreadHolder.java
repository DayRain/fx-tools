package com.dayrain.httpserver.component;

import com.dayrain.httpserver.server.ServerThread;

import java.util.HashMap;

/**
 * 正在运行的 server 线程组
 * @author peng
 * @date 2021/11/8
 */
public class ServerThreadHolder {

    private static final HashMap<String, ServerThread> threadMap = new HashMap<>();

    /**
     * 停止所有线程
     */
    public synchronized static void stopAll() {
        if(threadMap.size() > 0) {
            for (String serverName : threadMap.keySet()) {
                ServerThread serverThread = threadMap.get(serverName);
                serverThread.stopServer();
            }
        }
    }

    public synchronized static void add(String serverName, ServerThread serverThread) {
        if(!threadMap.containsKey(serverName)) {
            serverThread.start();
            threadMap.put(serverName, serverThread);
        }
    }

    public synchronized static void remove(String serverName) {
        if(contains(serverName)) {
            threadMap.get(serverName).stopServer();
            threadMap.remove(serverName);
        }
    }

    public synchronized static boolean contains(String key) {
        return threadMap.containsKey(key);
    }

    public synchronized static void addUrl(ServerUrl serverUrl) {
        if(serverUrl == null || !contains(serverUrl.getServerName())) {
            return;
        }

        ServerThread serverThread = threadMap.get(serverUrl.getServerName());
        serverThread.addContext(serverUrl);
    }

    public static void replaceUrl(String beforeName, String beforeUrl, ServerUrl serverUrl) {
        if(contains(beforeName)) {
            ServerThread serverThread = threadMap.get(beforeName);
            serverThread.replaceUrl(beforeUrl, serverUrl);
        }
    }
}
