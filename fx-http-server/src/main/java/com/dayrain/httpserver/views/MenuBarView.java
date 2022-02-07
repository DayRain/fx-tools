package com.dayrain.httpserver.views;

import com.dayrain.httpserver.Version;
import com.dayrain.httpserver.component.ConfigHolder;
import com.dayrain.httpserver.component.Configuration;
import com.dayrain.httpserver.component.ServerConfig;
import com.dayrain.httpserver.style.BackGroundFactory;
import com.dayrain.httpserver.style.ButtonFactory;
import com.dayrain.httpserver.style.LabelFactory;
import com.dayrain.httpserver.style.StageFactory;
import com.dayrain.httpserver.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单栏
 * @author peng
 * @date 2021/11/11
 */
public class MenuBarView extends MenuBar {

    private static final Logger logger = LoggerFactory.getLogger(MenuBarView.class);

    private final Menu menu1 = new Menu("文件");
    private final Menu menu2 = new Menu("设置");
    private final Menu menu3 = new Menu("帮助");

    public MenuBarView() {
        creatView();
    }

    private void creatView() {
        MenuItem menuItem11 = new MenuItem("新建");
        MenuItem menuItem12 = new MenuItem("导入");
        MenuItem menuItem13 = new MenuItem("导出");
        menu1.getItems().addAll(menuItem11, menuItem12, menuItem13);
        menuItem11.setOnAction(this::addServer);
        menuItem12.setOnAction(this::importConfig);
        menuItem13.setOnAction(this::exportConfig);

        MenuItem menuItem21 = new MenuItem("随机值长度");
        menu2.getItems().addAll(menuItem21);
        menuItem21.setOnAction(this::updateRandomLength);

        MenuItem menuItem31 = new MenuItem("说明书");
        MenuItem menuItem32 = new MenuItem("源码地址");
        menu3.getItems().addAll(menuItem31, menuItem32);
        menuItem31.setOnAction(this::linkIntroduce);
        menuItem32.setOnAction(this::linkGithub);

        this.setBackground(BackGroundFactory.getBackGround());

        this.getMenus().addAll(menu1, menu2, menu3);
    }

    public void updateRandomLength(ActionEvent evt) {
        Configuration configuration = ConfigHolder.get();
        Label strName = LabelFactory.getLabel("随机字符长度:");
        strName.setPrefWidth(150);
        TextField strField = new TextField(String.valueOf(configuration.getStringLen()));
        Label intName = LabelFactory.getLabel("随机整数长度:");
        TextField intField = new TextField(String.valueOf(configuration.getIntLen()));
        intField.setPrefWidth(150);

        HBox btnHBox = new HBox();
        Label saveTips1 = LabelFactory.getLabel("注: $string$代指随机字符串");
        Label saveTips2 = LabelFactory.getLabel("$int$代指随机整数");
        Button saveButton = ButtonFactory.getButton("保存");
        btnHBox.getChildren().addAll(saveButton);
        btnHBox.setAlignment(Pos.CENTER_RIGHT);
        btnHBox.setSpacing(20d);

        GridPane gridPane = new GridPane();
        gridPane.add(strName, 0, 0);
        gridPane.add(strField, 1, 0);
        gridPane.add(intName, 0, 1);
        gridPane.add(intField, 1, 1);
        gridPane.add(saveTips1, 0, 3);
        gridPane.add(saveTips2, 1, 3);

        gridPane.add(btnHBox, 1, 4);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20d);
        gridPane.setVgap(10d);

        Stage stage = StageFactory.getPopStage("更改随机值配置", new Scene(gridPane));
        stage.show();

        saveButton.setOnAction(event -> {
            String strLen = strField.getText();
            String intLen = intField.getText();
            configuration.setStringLen(Integer.parseInt(strLen));
            configuration.setIntLen(Integer.parseInt(intLen));
            ConfigHolder.save();
            stage.close();
        });
    }

    public void exportConfig(ActionEvent evt) {
        Stage stage = new Stage();
        stage.initOwner(ViewHolder.getPrimaryStage());
        FileChooser fileChooser = new FileChooser();
        String projectName = Version.VERSION_NAME;
        Configuration configuration = ConfigHolder.get();
        List<ServerConfig> serverConfigs = configuration.getServerConfigs();
        if(serverConfigs != null && serverConfigs.size()>1) {
            String serverName = serverConfigs.get(0).getServerName();
            if(serverName != null && !"".equals(serverName.trim())) {
                projectName = serverName;
            }
        }
        fileChooser.setTitle("导出配置");
        fileChooser.setInitialFileName(projectName + ".json");
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            FileUtils.saveConfig(ConfigHolder.get(), file);
        }
    }

    private void importConfig(ActionEvent evt) {
        Stage stage = new Stage();
        stage.initOwner(ViewHolder.getPrimaryStage());
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择一个文件");
        //过滤器
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("只能导入json文件", "*.json"));
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Configuration loadConfig = FileUtils.load(file);
            ConfigHolder.replace(loadConfig);
            FileUtils.saveConfig(loadConfig);
            ConfigHolder.replace(loadConfig);
            ViewHolder.getHomeView().restart();
        }
    }

    /**
     * 添加服务
     */
    private void addServer(ActionEvent evt) {
        Label nameLabel = LabelFactory.getLabel("服务名称:");
        nameLabel.setPrefWidth(80);
        TextField nameField = new TextField();
        Label portLabel = LabelFactory.getLabel("端口号:");
        TextField portField = new TextField();
        portField.setPrefWidth(80);

        HBox btnHBox = new HBox();
        Button saveButton = ButtonFactory.getButton("保存");
        btnHBox.getChildren().add(saveButton);
        btnHBox.setAlignment(Pos.CENTER_RIGHT);

        GridPane gridPane = new GridPane();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(portLabel, 0, 1);
        gridPane.add(portField, 1, 1);
        gridPane.add(btnHBox, 1, 3);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20d);
        gridPane.setVgap(10d);

        Stage stage = StageFactory.getPopStage("添加服务", new Scene(gridPane));
        stage.show();

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String port = portField.getText();
            ServerConfig serverConfig = new ServerConfig(name, Integer.parseInt(port), new ArrayList<>());
            ConfigHolder.get().getServerConfigs().add(serverConfig);
            ViewHolder.getServerContainer().addServer(serverConfig);
            ConfigHolder.save();
            stage.close();
        });
    }

    private void linkGithub(ActionEvent evt) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/DayRain/http-server-simulator"));
        } catch (IOException | URISyntaxException e) {
            logger.error("跳转到github时发生了错误: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void linkIntroduce(ActionEvent evt){
        try {
            Desktop.getDesktop().browse(new URI("https://blog.csdn.net/qq_37855749/article/details/121030800"));
        } catch (IOException | URISyntaxException e) {
            logger.error("跳转到csdn时发生了错误: {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
