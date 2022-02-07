package com.dayrain.httpserver.style;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class FormFactory {

    public static HBox getLine(Label label, Control control, double labelWidth, double nodeWidth, double maxWidth) {
        HBox hBox = new HBox();
        label.setPrefWidth(labelWidth);
        control.setPrefWidth(nodeWidth);
        hBox.getChildren().addAll(label, control);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setMaxWidth(maxWidth);
        return hBox;
    }

    public static HBox getButtonLine(Button button, double labelWidth, double width) {
        HBox hBox = new HBox();
        Label label = LabelFactory.getLabel("");
        label.setPrefWidth(labelWidth);
        hBox.getChildren().addAll(label, button);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setMaxWidth(width);
        return hBox;
    }
}
