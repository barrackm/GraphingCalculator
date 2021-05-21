package Panes;

import Calculations.Bounds;
import Utlities.PointConversion;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sample.Controller;

public class SettingsPane extends VBox {

    public final Button returnBtn;
    private Label leftLabel, rightLabel, lowerLabel, upperLabel;
    private final Bounds bounds;
    public TextField leftField, rightField, lowerField, upperField;
    private final GraphPane graphPane;

    public SettingsPane(Bounds bounds, GraphPane graphPane) {
        this.graphPane = graphPane;

        this.returnBtn = new Button();
        returnBtn.setText("Return");

        this.bounds = bounds;

        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(returnBtn);

        HBox leftBox = new HBox();
        HBox rightBox = new HBox();
        HBox lowerBox = new HBox();
        HBox upperBox = new HBox();

        this.leftLabel = new Label("Left Bound: ");
        this.rightLabel = new Label("Right Bound: ");
        this.lowerLabel = new Label("Lower Bound: ");
        this.upperLabel = new Label("Upper Bound: ");

        this.leftField = new TextField();
        this.rightField = new TextField();
        this.lowerField = new TextField();
        this.upperField = new TextField();

        leftField.setText("" + bounds.leftBound);
        rightField.setText("" + bounds.rightBound);
        lowerField.setText("" + bounds.lowerBound);
        upperField.setText("" + bounds.upperBound);

        leftField.setMaxWidth(60);
        rightField.setMaxWidth(60);
        lowerField.setMaxWidth(60);
        upperField.setMaxWidth(60);

        leftBox.getChildren().addAll(leftLabel, leftField);
        rightBox.getChildren().addAll(rightLabel, rightField);
        lowerBox.getChildren().addAll(lowerLabel, lowerField);
        upperBox.getChildren().addAll(upperLabel, upperField);

        this.getChildren().addAll(buttonBox, leftBox, rightBox, lowerBox, upperBox);

        bindFields();
    }

    private void bindFields() {
        leftField.setOnAction(e -> {
            leftFieldUpdate();
        });
        leftField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) leftFieldUpdate();
            }
        });
        rightField.setOnAction(e -> {
            rightFieldUpdate();
        });
        rightField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) rightFieldUpdate();
            }
        });
        lowerField.setOnAction(e -> {
            lowerFieldUpdate();
        });
        lowerField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) lowerFieldUpdate();
            }
        });
        upperField.setOnAction(e -> {
            upperFieldUpdate();
        });
        upperField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(!t1) upperFieldUpdate();
            }
        });
    }

    private void leftFieldUpdate() {
        try {
            double[][] cords = graphPane.genCordArray();
            double value = Double.parseDouble(leftField.getText());
            if (bounds.rightBound > value) {
                bounds.updateBounds(value, bounds.rightBound, bounds.lowerBound, bounds.upperBound);
                graphPane.relocatePoints(cords);
            }
            else {
                leftField.setText("" + bounds.leftBound);
            }
        } catch (Exception err) {
            leftField.setText("" + bounds.leftBound);
        }
    }

    private void rightFieldUpdate() {
        try {
            double[][] cords = graphPane.genCordArray();
            double value = Double.parseDouble(rightField.getText());
            if (bounds.leftBound < value) {
                bounds.updateBounds(bounds.leftBound, value, bounds.lowerBound, bounds.upperBound);
                graphPane.relocatePoints(cords);
            }
            else {
                rightField.setText("" + bounds.rightBound);
            }
        } catch (Exception err) {
            rightField.setText("" + bounds.rightBound);
        }
    }

    private void lowerFieldUpdate() {
        try {
            double[][] cords = graphPane.genCordArray();
            double value = Double.parseDouble(lowerField.getText());
            if (bounds.upperBound > value) {
                bounds.updateBounds(bounds.leftBound, bounds.rightBound, value, bounds.upperBound);
                graphPane.relocatePoints(cords);
            }
            else {
                lowerField.setText("" + bounds.lowerBound);
            }
        } catch (Exception err) {
            lowerField.setText("" + bounds.lowerBound);
        }
    }

    private void upperFieldUpdate() {
        try {
            double[][] cords = graphPane.genCordArray();
            double value = Double.parseDouble(upperField.getText());
            if (bounds.lowerBound < value) {
                bounds.updateBounds(bounds.leftBound, bounds.rightBound, bounds.lowerBound, value);
                graphPane.relocatePoints(cords);
            }
            else {
                upperField.setText("" + bounds.upperBound);
            }
        } catch (Exception err) {
            upperField.setText("" + bounds.upperBound);
        }
    }

    public void updateAllFields() {
        leftField.setText("" + bounds.leftBound);
        rightField.setText("" + bounds.rightBound);
        lowerField.setText("" + bounds.lowerBound);
        upperField.setText("" + bounds.upperBound);
    }
}
