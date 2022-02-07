package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ConfigHolder;
import com.dayrain.httpserver.component.RequestType;
import com.dayrain.httpserver.component.Server;
import com.dayrain.httpserver.component.ServerConfig;
import com.dayrain.httpserver.component.ServerThreadHolder;
import com.dayrain.httpserver.component.ServerUrl;
import com.dayrain.httpserver.server.ServerThread;
import com.dayrain.httpserver.style.BackGroundFactory;
import com.dayrain.httpserver.style.ButtonFactory;
import com.dayrain.httpserver.style.CircleFactory;
import com.dayrain.httpserver.style.FormFactory;
import com.dayrain.httpserver.style.IconFactory;
import com.dayrain.httpserver.style.LabelFactory;
import com.dayrain.httpserver.style.StageFactory;
import com.dayrain.httpserver.utils.ListViewHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

/**
 * server容器
 * @author peng
 * @date 2021/11/11
 */
public class ServerPane extends TitledPane {

    private final ServerConfig serverConfig;

    private ServerUrlListView serverUrlListView;

    private final Circle statusCircle;
    private final Button editButton;
    private final Button openButton;
    private final Button deleteButton;
    private final Button addButton;

    public ServerPane(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
        this.statusCircle = CircleFactory.getLightCircle(ServerThreadHolder.contains(serverConfig.getServerName()) ? Color.GREEN : Color.RED);
        this.editButton = ButtonFactory.getButton("修改配置");
        this.openButton = ButtonFactory.getButton("开启服务");
        this.deleteButton = ButtonFactory.getButton("删除服务");
        this.addButton = ButtonFactory.getButton("添加接口");
        createView();
    }

    public void createView() {
        VBox vBox = new VBox();
        HBox headBox = new HBox();
        //设置服务启动与关闭
        openButton.setOnAction(this::startServer);
        editButton.setOnAction(this::updateServer);
        deleteButton.setOnAction(this::deleteSever);
        headBox.getChildren().addAll(openButton, editButton, deleteButton, addButton, statusCircle);
        HBox.setMargin(statusCircle, new Insets(0, 0, 0, 30));
        headBox.setSpacing(20d);
        headBox.setAlignment(Pos.CENTER);

        //添加url
        this.serverUrlListView = new ServerUrlListView(serverConfig);
        addButton.setOnAction(this::addUrl);
        vBox.getChildren().addAll(headBox, serverUrlListView);

        vBox.setSpacing(10d);
        VBox.setMargin(headBox, new Insets(10, 0, 0, 0));
        vBox.setPadding(Insets.EMPTY);
        setText(serverConfig.getServerName());
        this.setContent(vBox);
        this.setFont(Font.font("Microsoft YaHei", 18));
        this.setPrefWidth(600d);
        this.setExpanded(false);
        this.setBackground(BackGroundFactory.getBackGround());
        this.setOnMouseClicked(event -> {
            if (!serverConfig.getServerName().equals(ViewHolder.getLogArea().getCurrentServerName())) {
                ViewHolder.setLogOwner(serverConfig.getServerName());
                ViewHolder.refreshLog();
            }
        });

        HBox hBox = new HBox();
        hBox.setPrefHeight(60d);
        this.setGraphic(hBox);
    }

    /**
     * 启动服务
     */
    private void startServer(ActionEvent evt) {
        String serverName = serverConfig.getServerName();
        if (ServerThreadHolder.contains(serverName)) {
            ServerThreadHolder.remove(serverName);
            openButton.setText("开启服务");
            statusCircle.setFill(Color.RED);
        } else {
            ServerThreadHolder.add(serverName, new ServerThread(new Server(serverConfig)));
            openButton.setText("关闭服务");
            statusCircle.setFill(Color.GREEN);
        }
    }

    /**
     * 更新服务
     */
    private void updateServer(ActionEvent evt) {
        Label serverName = LabelFactory.getLabel("服务名称:");
        serverName.setPrefWidth(80);
        TextField nameField = new TextField(serverConfig.getServerName());
        Label portLabel = LabelFactory.getLabel("端口:");
        TextField portField = new TextField(String.valueOf(serverConfig.getPort()));
        portField.setPrefWidth(80);

        HBox btnHBox = new HBox();
        Label saveTips = LabelFactory.getLabel("重启后生效");
        Button saveButton = ButtonFactory.getButton("保存");
        btnHBox.getChildren().addAll(saveTips, saveButton);
        btnHBox.setAlignment(Pos.CENTER_RIGHT);
        btnHBox.setSpacing(20d);

        GridPane gridPane = new GridPane();
        gridPane.add(serverName, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(portLabel, 0, 1);
        gridPane.add(portField, 1, 1);

        gridPane.add(btnHBox, 1, 3);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20d);
        gridPane.setVgap(10d);

        Stage stage = StageFactory.getPopStage("更新服务配置", new Scene(gridPane));
        stage.show();

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            int port = Integer.parseInt(portField.getText());
            serverConfig.setPort(port);
            serverConfig.setServerName(name);
            ConfigHolder.save();
            stage.close();
        });
    }

    /**
     * 删除服务
     */
    private void deleteSever(ActionEvent evt) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setGraphic(new ImageView(IconFactory.getIcon()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconFactory.getIcon());
        alert.setHeaderText("是否确定删除该服务？");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            //从配置文件中移除
            ConfigHolder.get().getServerConfigs().remove(serverConfig);
            //从视图中移除
            ViewHolder.getServerContainer().removeServer(serverConfig);
            //如果当前线程未关闭，则关闭
            ServerThreadHolder.remove(serverConfig.getServerName());
            ConfigHolder.save();
        });
        alert.show();
    }

    private void addUrl(ActionEvent evt) {
        VBox vBox = new VBox();

        Label nameLabel = LabelFactory.getLabel("接口名称:");
        TextField nameField = new TextField();
        HBox hBox1 = FormFactory.getLine(nameLabel, nameField, 120, 300, 500);

        Label urlLabel = LabelFactory.getLabel("接口地址:");
        TextField urlField = new TextField();
        HBox hBox2 = FormFactory.getLine(urlLabel, urlField, 120, 300, 500);

        Label typeLabel = LabelFactory.getLabel("请求方式:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setItems(FXCollections.observableArrayList("POST", "GET"));
        choiceBox.setValue("POST");
        HBox hBox3 = FormFactory.getLine(typeLabel, choiceBox, 120, 70, 500);

        Label logLabel = LabelFactory.getLabel("日志记录:");
        ChoiceBox<String> choiceBox2 = new ChoiceBox<>();
        choiceBox2.setItems(FXCollections.observableArrayList("显示", "隐藏"));
        choiceBox2.setValue("显示");
        HBox hBox4 = FormFactory.getLine(logLabel, choiceBox2, 120, 70, 500);

        Label respLabel = LabelFactory.getLabel("返回结果:");
        TextArea textArea = new TextArea();
        HBox hBox5 = FormFactory.getLine(respLabel, textArea, 120, 300, 500);

        Button saveButton = ButtonFactory.getButton("保存");
        HBox hBox6 = FormFactory.getButtonLine(saveButton, 120, 500);

        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5, hBox6);
        vBox.setSpacing(20d);
        vBox.setAlignment(Pos.CENTER);

        Stage stage = StageFactory.getPopStage("新增接口信息", new Scene(vBox));
        stage.show();

        saveButton.setOnAction(event1 -> {
            String name = nameField.getText();
            List<ServerUrl> serverUrls = serverConfig.getServerUrls();
            for (ServerUrl serverUrl : serverUrls) {
                if (serverUrl.getUrlName().equals(name)) {
                    return;
                }
            }
            String url = urlField.getText();
            String resp = textArea.getText();
            String type = choiceBox.getValue();
            String hidLog = choiceBox.getValue();
            ServerUrl serverUrl = new ServerUrl(serverConfig.getServerName(), name, url, type.equals(RequestType.POST.name()) ? RequestType.POST : RequestType.GET, resp);
            serverUrl.setHiddenLog("隐藏".equals(hidLog));
            serverUrls.add(serverUrl);
            ServerThreadHolder.addUrl(serverUrl);
            ListViewHelper.addAndRefresh(serverUrl, serverUrlListView);
            ConfigHolder.save();
            stage.close();
        });
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}
