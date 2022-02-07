package com.dayrain.httpserver.style;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 圆
 * @author peng
 * @date 2021/11/11
 */
public class CircleFactory {

    public static Circle getLightCircle(Color color) {
        Circle circle = new Circle();
        circle.setRadius(10);
        circle.setFill(color);
        return circle;
    }
}
