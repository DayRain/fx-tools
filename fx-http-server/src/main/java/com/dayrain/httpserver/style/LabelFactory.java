package com.dayrain.httpserver.style;

import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class LabelFactory {

    public static Label getLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Microsoft YaHei", 15));
        return label;
    }
}
