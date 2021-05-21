package Panes;

import Calculations.Bounds;
import Utlities.Function;
import Utlities.GraphPoint;
import Utlities.PointConversion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import sample.Controller;

import java.util.ArrayList;

public class GraphPane extends Pane {
    private final ArrayList<Line> axis = new ArrayList<>();
    private final ArrayList<Label> scaleLabels = new ArrayList<>();
    private Controller controller;
    private Bounds bounds;
    private double blockMaxSide;
    private double blockMinSide;
    //Address this if possible
    private boolean movable = true;
    private double initialX;
    private double initialY;
    private double xOffset;
    private double yOffset;
    private Bounds oldBounds;

    public GraphPane(Controller controller, Bounds bounds) {
        this.controller = controller;
        this.bounds = bounds;
        this.blockMaxSide = 40;
        this.blockMinSide = 20;
        resizingEvents();
        dragGraphEvents();
    }

    private void resizingEvents() {
        this.widthProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                for(var i : Controller.functionList) {
                    for(var j : i.anchorList) {
                        j.setCenterX(newSceneWidth.doubleValue() * j.getCenterX() / oldSceneWidth.doubleValue());
                    }

                }
                for(int i = 0; i < Controller.functionList.size(); i++) {
                    drawFunction(i);
                }
                drawAxis();
            }
        });
        this.heightProperty().addListener(new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                for(var i : Controller.functionList) {
                    for(var j : i.anchorList) {
                        j.setCenterY(newSceneHeight.doubleValue() * j.getCenterY() / oldSceneHeight.doubleValue());
                    }
                }
                for(int i = 0; i < Controller.functionList.size(); i++) {
                    drawFunction(i);
                }
                drawAxis();
            }
        });
    }

    private void dragGraphEvents() {
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton() != MouseButton.SECONDARY) {
                initialX = e.getX();
                initialY = e.getY();
                try {
                    oldBounds = (Bounds) bounds.clone();
                } catch (CloneNotSupportedException cloneNotSupportedException) {
                    cloneNotSupportedException.printStackTrace();
                }
            }
        });
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (movable && e.getButton() != MouseButton.SECONDARY) {
                double xZeroPixel = PointConversion.xCordToPixel(0, this, oldBounds);
                double yZeroPixel = PointConversion.yCordToPixel(0, this, oldBounds);
                xOffset = PointConversion.xPixelToCord(xZeroPixel + e.getX() - initialX, this, oldBounds);
                yOffset = PointConversion.yPixelToCord( yZeroPixel + e.getY() - initialY, this, oldBounds);

                double[][] cords = genCordArray();
                bounds.updateBounds(oldBounds.leftBound - xOffset,
                        oldBounds.rightBound - xOffset,
                        oldBounds.lowerBound - yOffset,
                        oldBounds.upperBound - yOffset);
                relocatePoints(cords);
                controller.updateSettingsPane();
            }
        });
        this.addEventHandler(ScrollEvent.SCROLL, e -> {
            double zoomFactor = 0.05;
            if (e.getDeltaY() < 0) {
                double[][] cords = genCordArray();
                bounds.updateBounds(bounds.leftBound - bounds.xRange * zoomFactor,
                        bounds.rightBound + bounds.xRange * zoomFactor,
                        bounds.lowerBound - bounds.yRange * zoomFactor,
                        bounds.upperBound + bounds.yRange * zoomFactor);
                relocatePoints(cords);
                controller.updateSettingsPane();
            } else if (e.getDeltaY() >  0) {
                double[][] cords = genCordArray();
                bounds.updateBounds(bounds.leftBound + bounds.xRange * zoomFactor,
                        bounds.rightBound - bounds.xRange * zoomFactor,
                        bounds.lowerBound + bounds.yRange * zoomFactor,
                        bounds.upperBound - bounds.yRange * zoomFactor);
                relocatePoints(cords);
                controller.updateSettingsPane();
            }
        });
    }

    public GraphPoint createPoint(double x, double y) {
        GraphPoint graphPoint = new GraphPoint(PointConversion.xPixelToCord(x, this),
                PointConversion.yPixelToCord(y, this), this);
        for(int i = 0; i < Controller.functionList.size(); i++) {
            drawFunction(i);
        }
        drawAxis();
        return graphPoint;
    }

    public void drawAxis() {
        while (!axis.isEmpty()) {
            this.getChildren().remove(axis.remove(0));
        }
        while (!scaleLabels.isEmpty()) {
            this.getChildren().remove(scaleLabels.remove(0));
        }

        Line xAxis = new Line(0, PointConversion.yCordToPixel(0, this), this.getWidth(),
                PointConversion.yCordToPixel(0, this));
        axis.add(xAxis);

        Line yAxis = new Line(PointConversion.xCordToPixel(0, this), 0,
                PointConversion.xCordToPixel(0, this), this.getHeight());
        if (PointConversion.xCordToPixel(0, this) < bounds.leftBound) {
            yAxis.visibleProperty().setValue(false);
        }
        axis.add(yAxis);
        xAxis.setStrokeWidth(3);
        yAxis.setStrokeWidth(3);
        this.getChildren().add(xAxis);
        this.getChildren().add(yAxis);
        xAxis.toBack();
        yAxis.toBack();

        double xInterval = (PointConversion.xCordToPixel(1, this)
                - PointConversion.xCordToPixel(0, this)) / 4;

        if (xInterval == 0) xInterval = 20;

        while (xInterval < blockMinSide || xInterval > blockMaxSide) {
            if (xInterval > blockMaxSide) xInterval /= 2;
            else if (xInterval < blockMinSide) xInterval *= 2;
        }

        double xZeroPixel = PointConversion.xCordToPixel(0, this);

        double xIntervalCord = PointConversion.xPixelToCord(xZeroPixel + xInterval, this);

        double leftLineC = bounds.leftBound + ((4 * xIntervalCord) - bounds.leftBound % (4 * xIntervalCord)) - 2 * (4 * xIntervalCord);

        double leftLineP = PointConversion.xCordToPixel(leftLineC, this);

        for(int i = 0; i < bounds.xRange / xIntervalCord + 4; i++) {
            double x = i * xInterval + leftLineP;
            if (x > PointConversion.xCordToPixel(bounds.leftBound, this)) {
                Line line = new Line(x, 0, x, this.getHeight());
                line.setStroke(Color.LIGHTGRAY);
                if (i % 4 == 0) {
                    line.setStrokeWidth(2);
                    Label label = new Label();
                    label.setText(scaleFormat(PointConversion.xPixelToCord(x, this)));
                    label.setLayoutX(x - label.getWidth() / 2);
                    if (bounds.lowerBound <= 0 && bounds.upperBound >= 0) {
                        label.setLayoutY(PointConversion.yCordToPixel(0, this) + 10);
                    } else if (bounds.lowerBound <= 0) {
                        label.setLayoutY(10);
                    } else {
                        label.setLayoutY(this.getHeight() - 20);
                    }
                    if (x + xInterval > PointConversion.xCordToPixel(0, this) &&
                            x - xInterval < PointConversion.xCordToPixel(0, this)) {
                        label.setText("0");
                        label.setLayoutX(x - label.getWidth() / 2 + 5);
                    }
                    scaleLabels.add(label);
                    this.getChildren().add(label);
                    label.toBack();
                }
                this.getChildren().add(line);
                line.toBack();
                axis.add(line);
            }
        }

        double yInterval = (PointConversion.yCordToPixel(0, this)
                - PointConversion.yCordToPixel(1, this)) / 4;

        if (yInterval == 0) yInterval = 20;

        while (yInterval < blockMinSide || yInterval > blockMaxSide) {
            if (yInterval > blockMaxSide) yInterval /= 2;
            else if (yInterval < blockMinSide) yInterval *= 2;
        }

        double yZeroPixel = PointConversion.yCordToPixel(0, this);

        double yIntervalCord = PointConversion.yPixelToCord(yZeroPixel - yInterval, this);

        double upperLineC = bounds.upperBound + ((4 * yIntervalCord) - bounds.upperBound % (4 * yIntervalCord));

        double upperLineP = PointConversion.yCordToPixel(upperLineC, this);

        for(int i = 0; i < bounds.yRange / yIntervalCord + 4; i++) {
            double y = upperLineP + i * yInterval;
            Line line = new Line(0, y, this.getWidth(), y);
            line.setStroke(Color.LIGHTGRAY);
            if (i % 4 == 0) {
                line.setStrokeWidth(2);
                Label label = new Label();
                label.setText(scaleFormat(PointConversion.yPixelToCord(y, this)));
                label.setLayoutY(y - label.getHeight() / 2);
                if (bounds.leftBound <= 0 && bounds.rightBound >= 0) {
                    label.setLayoutX(PointConversion.xCordToPixel(0, this) + 10);
                } else if (bounds.leftBound <= 0) {
                    label.setLayoutX(this.getWidth() - 30);
                } else {
                    label.setLayoutX(10);
                }
                if (!(y + yInterval > PointConversion.yCordToPixel(0, this) &&
                        y - yInterval < PointConversion.yCordToPixel(0, this))) {
                    scaleLabels.add(label);
                    this.getChildren().add(label);
                    label.toBack();
                }
            }
            this.getChildren().add(line);
            line.toBack();
            axis.add(line);
        }
    }

    public Function drawFunction(int functionIndex) {
        FunctionInfo fInfo = Controller.functionList.get(functionIndex);
        while (!fInfo.functionLines.isEmpty()) {
            this.getChildren().remove(fInfo.functionLines.remove(0));
        }
        ArrayList<GraphPoint> aList = Controller.functionList.get(functionIndex).anchorList;
        double[][] cords = new double[aList.size()][2];
        for(int i = 0 ; i < cords.length; i++) {
            cords[i][0] = PointConversion.xPixelToCord(aList.get(i).getCenterX(), this);
            cords[i][1] = PointConversion.yPixelToCord(aList.get(i).getCenterY(), this);
        }
        Function function = new Function(cords);
        double[] coeffs = function.getCoeffs();
        if(aList.size() >= 2) {
            if (coeffs.length != 0) {
                double[] yCords = new double[(int) this.getWidth()];
                for (int x = 0; x < this.getWidth(); x++) {
                    double y = 0;
                    for (int i = 0; i < coeffs.length; i++) {
                        y += coeffs[i] * Math.pow(PointConversion.xPixelToCord(x, this), i);
                    }
                    yCords[x] = y;
                }
                for (int x = 0; x < this.getWidth() - 1; x++) {
                    Line line = new Line(x, PointConversion.yCordToPixel(yCords[x], this), x + 1,
                            PointConversion.yCordToPixel(yCords[x + 1], this));
                    line.setStrokeWidth(2);
                    line.setStroke(Color.DARKCYAN);
                    fInfo.functionLines.add(line);
                    this.getChildren().add(line);
                    line.toBack();
                }
            }
            return function;
        }
        return function;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void relocatePoints(double[][] cords) {
        int numCord = 0;
        for(var i : Controller.functionList) {
            for(var j : i.anchorList) {
                j.setCenterX(PointConversion.xCordToPixel(cords[numCord][0], this));
                j.setCenterY(PointConversion.yCordToPixel(cords[numCord][1], this));
                j.setVisible(bounds.leftBound < cords[numCord][0] &&
                        cords[numCord][0] < bounds.rightBound && bounds.lowerBound < cords[numCord][1] &&
                        cords[numCord][1] < bounds.upperBound);
                numCord++;
            }
        }
        for(int i = 0; i < Controller.functionList.size(); i++) {
            drawFunction(i);
        }
        drawAxis();
    }

    public double[][] genCordArray() {
        int numCords = 0;
        for(var i : Controller.functionList) {
            for(var j : i.anchorList) {
                numCords++;
            }
        }
        double[][] cords = new double[numCords][2];
        numCords = 0;
        for(var i : Controller.functionList) {
            for(var j : i.anchorList) {
                cords[numCords][0] = PointConversion.xPixelToCord(j.getCenterX(), this);
                cords[numCords][1] = PointConversion.yPixelToCord(j.getCenterY(), this);
                numCords++;
            }
        }
        return cords;
    }

    public void setMovable(boolean movable) {
        this.movable = movable;
    }

    private String scaleFormat(double number) {
        if (Math.abs(number) > Math.pow(10, 5)) {
            return String.format("%.3e", number);
        } else if (Math.abs(number) < Math.pow(10, -2)) {
            return String.format("%.2e", number);
        } else if (number - (int) number < 0.0001 &&  number - (int) number > -0.0001) {
            return String.format("%d", (int) number);
        } else if (Math.abs(number - (int) number) < 1 &&  Math.abs(number - (int) number) > 0.9999) {
            return String.format("%d", (int) (number + (number / Math.abs(number))));
        } else {
            return String.format("%.2f", number);
        }
    }
}
