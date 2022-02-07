package com.dayrain.httpserver.style;

import com.dayrain.httpserver.views.ViewHolder;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 窗口
 * @author peng
 * @date 2021/11/11
 */
public class StageFactory {

    /**
     * 获取标准弹窗
     * @author peng
     * @date 2021/11/11
     */
    public static Stage getPopStage(String name, Scene scene) {
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setWidth(700);
        stage.setHeight(500);
        stage.initOwner(ViewHolder.getPrimaryStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.getIcons().addAll(IconFactory.getIcon());
        stage.setScene(scene);
        return stage;
    }
}
