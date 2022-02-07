package com.dayrain.httpserver.component;

import java.util.Map;

/**
 * 请求路径
 * @author peng
 * @date 2021/11/8
 */
public class ServerUrl {

    /**
     * 路径名
     */
    private String urlName;
    /**
     * 路径url
     */
    private String url;
    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 请求类型
     */
    private RequestType requestType;
    /**
     * 响应体
     */
    private String responseBody;
    /**
     * 是否隐藏日志
     */
    private boolean hiddenLog;
    /**
     * 请求头
     */
    private Map<String, String> headerMap;

    public ServerUrl() {
    }

    public ServerUrl(String serverName, String urlName, String url, RequestType requestType, String responseBody) {
        this.serverName = serverName;
        this.urlName = urlName;
        this.url = url;
        this.requestType = requestType;
        this.responseBody = responseBody;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isHiddenLog() {
        return hiddenLog;
    }

    public void setHiddenLog(boolean hiddenLog) {
        this.hiddenLog = hiddenLog;
    }
}
