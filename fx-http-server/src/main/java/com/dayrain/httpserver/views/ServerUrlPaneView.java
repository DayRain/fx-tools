package com.dayrain.httpserver.views;

import com.dayrain.httpserver.component.ConfigHolder;
import com.dayrain.httpserver.component.RequestType;
import com.dayrain.httpserver.component.ServerConfig;
import com.dayrain.httpserver.component.ServerThreadHolder;
import com.dayrain.httpserver.component.ServerUrl;
import com.dayrain.httpserver.style.ButtonFactory;
import com.dayrain.httpserver.style.IconFactory;
import com.dayrain.httpserver.style.StageFactory;
import com.dayrain.httpserver.style.FormFactory;
import com.dayrain.httpserver.style.LabelFactory;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

/**
 * url面板
 * @author peng
 * @date 2021/11/11
 */
public class ServerUrlPaneView extends BorderPane {
    private final ServerUrl serverUrl;
    private final ServerConfig serverConfig;
    private final ListView<ServerUrl> serverListViews;

    public ServerUrlPaneView(ServerUrl serverUrl, ServerConfig serverConfig, ListView<ServerUrl> serverListViews) {
        this.serverUrl = serverUrl;
        this.serverConfig = serverConfig;
        this.serverListViews = serverListViews;
        createView();
    }

    public void createView() {
        HBox labelBox = new HBox();
        Label nameLabel = LabelFactory.getLabel(serverUrl.getUrlName());
        nameLabel.setPrefWidth(180d);
        nameLabel.setMaxWidth(180d);


        Label typeLabel = LabelFactory.getLabel(serverUrl.getRequestType().name());
        typeLabel.setPrefWidth(65d);

        Label urlLabel = LabelFactory.getLabel(serverUrl.getUrl());
        urlLabel.setMaxWidth(220d);
        labelBox.getChildren().addAll(nameLabel, typeLabel, urlLabel);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        HBox btnBox = new HBox();
        Button configButton = ButtonFactory.getButton("配置");
        Button deleteButton = ButtonFactory.getButton("删除");
        btnBox.setSpacing(10d);
        btnBox.getChildren().addAll(configButton, deleteButton);
        HBox.setMargin(deleteButton, new Insets(0, 5, 0, 0));

        deleteButton.setOnAction(this::deleteUrl);
        configButton.setOnAction(this::updateUrl);

        this.setLeft(labelBox);
        this.setRight(btnBox);
    }


    private void deleteUrl(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setGraphic(new ImageView(IconFactory.getIcon()));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(IconFactory.getIcon());
        alert.setHeaderText("是否确定删除该接口？");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setOnAction(event1 -> {
            List<ServerUrl> serverUrls = serverConfig.getServerUrls();
            serverUrls.remove(serverUrl);
            ConfigHolder.save();
            ListViewHelper.deleteAndRefresh(serverUrl, serverListViews);
        });
        alert.show();
    }

    private void updateUrl(ActionEvent event) {
        VBox vBox = new VBox();

        Label nameLabel = LabelFactory.getLabel("接口名称:");
        TextField nameField = new TextField(serverUrl.getUrlName());
        HBox hBox1 = FormFactory.getLine(nameLabel, nameField, 120, 300, 500);

        Label urlLabel = LabelFactory.getLabel("接口地址:");
        TextField urlField = new TextField(serverUrl.getUrl());
        HBox hBox2 = FormFactory.getLine(urlLabel, urlField, 120, 300, 500);

        Label typeLabel = LabelFactory.getLabel("请求方式:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setItems(FXCollections.observableArrayList("POST", "GET"));
        choiceBox.setValue(serverUrl.getRequestType().name());
        HBox hBox3 = FormFactory.getLine(typeLabel, choiceBox, 120, 70, 500);

        Label logLabel = LabelFactory.getLabel("日志记录:");
        ChoiceBox<String> choiceBox2 = new ChoiceBox<>();
        choiceBox2.setItems(FXCollections.observableArrayList("显示", "隐藏"));
        choiceBox2.setValue(serverUrl.isHiddenLog() ? "隐藏" : "显示");
        HBox hBox4 = FormFactory.getLine(logLabel, choiceBox2, 120, 70, 500);

        Label respLabel = LabelFactory.getLabel("返回结果:");
        TextArea textArea = new TextArea(serverUrl.getResponseBody());
        HBox hBox5 = FormFactory.getLine(respLabel, textArea, 120, 300, 500);

        Button saveButton = ButtonFactory.getButton("保存");
        HBox hBox6 = FormFactory.getButtonLine(saveButton, 120, 500);

        vBox.getChildren().addAll(hBox1, hBox2, hBox3, hBox4, hBox5, hBox6);
        vBox.setSpacing(20d);
        vBox.setAlignment(Pos.CENTER);

        Stage stage = StageFactory.getPopStage("更新接口信息", new Scene(vBox));
        stage.show();

        saveButton.setOnAction(event1 -> {
            String name = nameField.getText();
            String url = urlField.getText();
            String resp = textArea.getText();
            String type = choiceBox.getValue();
            String hidLog = choiceBox2.getValue();
            if(url == null) {
                return;
            }

            if(!url.startsWith("/")) {
                url = "/" + url;
            }
            String beforeUrl = serverUrl.getUrl();
            serverUrl.setUrlName(name);
            serverUrl.setUrl(url);
            serverUrl.setResponseBody(resp);
            serverUrl.setHiddenLog("隐藏".equals(hidLog));
            serverUrl.setRequestType(type.equals(RequestType.POST.name()) ? RequestType.POST : RequestType.GET);
            ListViewHelper.refresh(serverListViews);
            ConfigHolder.save();

            ServerThreadHolder.replaceUrl(serverUrl.getServerName(), beforeUrl, serverUrl);
            stage.close();
        });
    }

}
