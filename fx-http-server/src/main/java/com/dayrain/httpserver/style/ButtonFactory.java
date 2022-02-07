package com.dayrain.httpserver.style;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

public class ButtonFactory {

    public static Button getButton(String text) {

        BackgroundFill bgf = new BackgroundFill(Paint.valueOf("#145b7d"), new CornerRadii(10), new Insets(5));
        Button button = new Button();
        button.setText(text);
        button.setPrefWidth(80);
        button.setPrefHeight(50);
        button.setFont(Font.font("Microsoft YaHei", 15));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(bgf));
        return button;
    }
}
