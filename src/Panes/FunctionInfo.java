package Panes;

import Calculations.Bounds;
import Utlities.GraphPoint;
import Utlities.PointConversion;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import sample.Controller;

import java.util.ArrayList;


public class FunctionInfo extends VBox {
    public ArrayList<GraphPoint> anchorList = new ArrayList<>();
    public ArrayList<Line> functionLines = new ArrayList<>();
    private Button addPointBtn, deleteFunction;
    private TextField functionField;
    private GraphPane graphPane;
    private InfoPane infoPane;
    private Bounds bounds;
    private Controller controller;

    public FunctionInfo(GraphPane graphPane, InfoPane infoPane, Bounds bounds, Controller controller) {
        this.controller = controller;
        this.graphPane = graphPane;
        this.infoPane = infoPane;
        this.bounds = bounds;
        this.setPrefWidth(200);
        this.addPointBtn = new Button();
        this.deleteFunction = new Button();
        addPointBtn.setText("Add Point");
        deleteFunction.setText("Delete Function");
        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(addPointBtn, deleteFunction);

        this.functionField = new TextField();
        this.getChildren().addAll(functionField, buttonBox);

        addPointBtnEvent();

        deleteFunction.setOnAction(e -> {
            infoPane.getChildren().remove(this);
            controller.functionList.remove(this);
            for (var i : anchorList) {
                graphPane.getChildren().remove(i);
            }
            for (var line : functionLines) {
                graphPane.getChildren().remove(line);
            }
            controller.setSelectedFunction(0);
        });
    }

    public TextField[] addPoint(GraphPoint point) {
        anchorList.add(point);
        HBox container = new HBox();
        TextField xField = new TextField();
        TextField yField = new TextField();
        xField.setMaxWidth(60);
        yField.setMaxWidth(60);
        double x = PointConversion.xPixelToCord(point.getCenterX(), graphPane);
        double y = PointConversion.yPixelToCord(point.getCenterY(), graphPane);
        xField.setText(String.format(setFormat(x), x));
        yField.setText(String.format(setFormat(y), y));
        Button removePoint = new Button();
        removePoint.setText("-");
        removePoint.setOnAction(e -> {
            graphPane.getChildren().remove(point);
            anchorList.remove(point);
            this.getChildren().remove(container);
            updateFunctionField();
        });
        container.getChildren().addAll(new Label(" ( "), xField, new Label(" ,  "), yField, new Label(" ) "), removePoint);
        this.getChildren().add(container);
        updateFunctionField();
        return new TextField[]{xField, yField};
    }

    public void addPointMouseEvent(GraphPoint point) {
        TextField[] textFields = addPoint(point);
        addTextFieldEvents(textFields, point);
        point.addEventHandler(MouseEvent.MOUSE_DRAGGED, e2 -> {
            controller.setSelectedFunction(controller.functionList.indexOf(this));
            if (e2.getX() < 0) {
                point.setCenterX(0);
            } else point.setCenterX(Math.min(e2.getX(), graphPane.getWidth()));
            if (e2.getY() < 0) {
                point.setCenterY(0);
            } else point.setCenterY(Math.min(e2.getY(), graphPane.getHeight()));
            updatePoint(textFields, PointConversion.xPixelToCord(e2.getX(), graphPane),
                    PointConversion.yPixelToCord(e2.getY(), graphPane));
            updateFunctionField();
        });
    }

    private void addPointBtnEvent() {
        this.addPointBtn.setOnAction(e -> {
            GraphPoint point = graphPane.createPoint(PointConversion.xCordToPixel(bounds.xCentre, graphPane),
                    PointConversion.yCordToPixel(bounds.yCentre, graphPane));
            TextField[] textFields = addPoint(point);
            addTextFieldEvents(textFields, point);
            controller.setSelectedFunction(controller.functionList.indexOf(this));
            point.addEventHandler(MouseEvent.MOUSE_DRAGGED, e2 -> {
                controller.setSelectedFunction(controller.functionList.indexOf(this));
                if (e2.getX() < 0) {
                    point.setCenterX(0);
                } else point.setCenterX(Math.min(e2.getX(), graphPane.getWidth()));
                if (e2.getY() < 0) {
                    point.setCenterY(0);
                } else point.setCenterY(Math.min(e2.getY(), graphPane.getHeight()));
                updatePoint(textFields, PointConversion.xPixelToCord(e2.getX(), graphPane),
                        PointConversion.yPixelToCord(e2.getY(), graphPane));
                updateFunctionField();
            });
            updateFunctionField();
        });
    }

    private String setFormat(double number) {
        if (Math.abs(number) < 0.01 && number != 0) return "%.2e";
        else return "%.3f";
    }

    private void addTextFieldEvents(TextField[] textFields, GraphPoint graphPoint) {
        textFields[0].focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    try {
                        double xCord = Double.parseDouble(textFields[0].getText());
                        graphPoint.setCenterX(PointConversion.xCordToPixel(xCord, graphPane));
                        graphPoint.setVisible(bounds.leftBound < xCord && xCord < bounds.rightBound);
                        updateFunctionField();
                    } catch (Exception e) {
                        double x = PointConversion.xPixelToCord(graphPoint.getCenterX(), graphPane);
                        textFields[0].setText(String.format(setFormat(x),x));
                    }
                    updateFunctionField();
                }
            }
        });
        textFields[0].setOnAction(e -> {
            try {
                double xCord = Double.parseDouble(textFields[0].getText());
                graphPoint.setCenterX(PointConversion.xCordToPixel(xCord, graphPane));
                graphPoint.setVisible(bounds.leftBound < xCord && xCord < bounds.rightBound);
                updateFunctionField();
            } catch (Exception err) {
                double x = PointConversion.xPixelToCord(graphPoint.getCenterX(), graphPane);
                textFields[0].setText(String.format(setFormat(x),x));
            }
            updateFunctionField();
        });

        textFields[1].focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (!t1) {
                    try {
                        double yCord = Double.parseDouble(textFields[1].getText());
                        graphPoint.setCenterY(PointConversion.yCordToPixel(yCord, graphPane));
                        graphPoint.setVisible(bounds.lowerBound < yCord && yCord < bounds.upperBound);
                        updateFunctionField();
                    } catch (Exception e) {
                        double y = PointConversion.xPixelToCord(graphPoint.getCenterY(), graphPane);
                        textFields[1].setText(String.format(setFormat(y),y));
                    }
                }
                updateFunctionField();
            }
        });
        textFields[1].setOnAction(e -> {
            try {
                double yCord = Double.parseDouble(textFields[1].getText());
                graphPoint.setCenterY(PointConversion.yCordToPixel(yCord, graphPane));
                graphPoint.setVisible(bounds.leftBound < yCord && yCord < bounds.rightBound);
                updateFunctionField();
            } catch (Exception err) {
                double y = PointConversion.xPixelToCord(graphPoint.getCenterY(), graphPane);
                textFields[1].setText(String.format(setFormat(y),y));
            }
            updateFunctionField();
        });
    }

    private void updateFunctionField() {
        functionField.setText(graphPane.drawFunction(getFunctionIndex()).getFunction());
        graphPane.drawAxis();
    }

    public void updatePoint(TextField[] textFields, double x, double y) {
        textFields[0].setText(String.format(setFormat(x), x));
        textFields[1].setText(String.format(setFormat(y), y));
    }

    private int getFunctionIndex() {
        return Controller.functionList.indexOf(this);
    }
}
