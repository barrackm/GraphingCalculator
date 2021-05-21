package Utlities;

import Panes.GraphPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class GraphPoint extends Circle {
    public GraphPoint(double x, double y, GraphPane graphPane) {
        super(5);
        this.setCenterX(PointConversion.xCordToPixel(x, graphPane));
        this.setCenterY(PointConversion.yCordToPixel(y, graphPane));
        this.setVisible(graphPane.getBounds().leftBound < x && x < graphPane.getBounds().rightBound
                && graphPane.getBounds().lowerBound < y && y < graphPane.getBounds().upperBound);
        graphPane.getChildren().add(this);
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            graphPane.setMovable(false);
        });
        this.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            graphPane.setMovable(true);
        });
    }
}
