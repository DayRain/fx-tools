package com.dayrain.httpserver.component;

import com.dayrain.httpserver.utils.FileUtils;
import com.dayrain.httpserver.views.ViewHolder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * 请求处理
 * @author peng
 * @date 2021/11/8
 */
public class RequestHandler implements HttpHandler {

    private static final String STRING_PATTERN = "$string$";

    private static final String INT_PATTERN = "$int$";

    private final ServerUrl serverUrl;

    public RequestHandler(ServerUrl serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Override
    public void handle(HttpExchange exchange) {
        RequestType requestType = serverUrl.getRequestType();
        String param = null;
        if (RequestType.GET.equals(requestType)) {
            param = handleGetRequest(exchange);
        }

        if (RequestType.POST.equals(requestType)) {
            param = handlePostRequest(exchange);
        }

        String resp = replaceResp(serverUrl.getResponseBody());

        ViewHolder.log(serverUrl, param, resp);
        response(exchange, resp);
    }

    private String handleGetRequest(HttpExchange exchange) {
        return exchange.getRequestURI().getQuery();
    }

    private String handlePostRequest(HttpExchange exchange) {

        return FileUtils.getFromInputStream(exchange.getRequestBody());
    }

    private void response(HttpExchange exchange, String jsonBody) {
        try {
            byte[] bytes = jsonBody.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(200, bytes.length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(jsonBody.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String replaceResp(String resp) {

        if (resp == null || "".equals(resp)) {
            return resp;
        }

        Configuration configuration = ConfigHolder.get();
        int stringLen = configuration.getStringLen();
        int intLen = configuration.getIntLen();

        while (resp.contains(STRING_PATTERN)) {
            resp = resp.replace(STRING_PATTERN, randomString(stringLen));
        }

        while (resp.contains(INT_PATTERN)) {
            resp = resp.replace(INT_PATTERN, String.valueOf(randomInt(intLen)));
        }

        return resp;
    }

    private String randomString(int len) {
        String res = UUID.randomUUID().toString();
        if (len > res.length()) {
            len = res.length();
        }
        return UUID.randomUUID().toString().substring(0, len);
    }

    private int randomInt(int len) {
        int res = (int) Math.pow(10, len - 1);

        return res + (int) (Math.pow(10, len - 1) * Math.random());
    }
}
