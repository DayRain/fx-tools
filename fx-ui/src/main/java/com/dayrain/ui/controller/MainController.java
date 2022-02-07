package com.dayrain.ui.controller;

import com.dayrain.httpserver.HttpServerService;
import com.dayrain.httpserver.HttpServerServiceImpl;
import com.dayrain.ui.ImageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private HttpServerService httpServerService = new HttpServerServiceImpl();

    @FXML
    public void httpServer(ActionEvent event) {
        httpServerService.start();
    }

    @FXML
    public void netTools(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/net-tools/net-tools-main.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FX TOOLS");
        stage.setScene(new Scene(root, 600, 800));
        stage.getIcons().add(ImageUtils.getIcon());
        stage.show();
    }
}
