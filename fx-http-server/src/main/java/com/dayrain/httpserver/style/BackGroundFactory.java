package com.dayrain.httpserver.style;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BackGroundFactory {
    public static Background getBackGround() {
        BackgroundFill backgroundFill = new BackgroundFill(Color.GRAY, new CornerRadii(1),
                new Insets(0.0, 0.0, 0.0, 0.0));
        return new Background(backgroundFill);
    }
}
