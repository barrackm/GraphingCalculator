package sample;

import Calculations.Bounds;
import Panes.*;
import Utlities.GraphPoint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Controller extends BorderPane {
    private Stage window;
    public static ArrayList<FunctionInfo> functionList = new ArrayList<>();
    private Bounds bounds = new Bounds(-10,10,-10,10);
    private GraphPane graphPane;
    private InfoPane infoPane;
    private ChargePane chargePane;
    private final SettingsPane settingsPane;
    private Pane returnPane;
    int selectedFunction = -1;
    public boolean showGridB, solidBoundsB, showVectorsB, showEquipotentialB;

    public Controller(Stage window) {
        this.window = window;
        window.setMinWidth(300);
        window.setMinHeight(300);
        this.graphPane = new GraphPane(this, bounds);

        this.infoPane = new InfoPane(this, graphPane, bounds);
        this.settingsPane = new SettingsPane(bounds, graphPane);
        this.chargePane = new ChargePane();
        this.returnPane = infoPane;

        this.showGridB = true;
        this.solidBoundsB = false;
        this.showVectorsB = false;
        this.showEquipotentialB = false;

        this.setLeft(infoPane);
        this.setCenter(graphPane);

        addDrawEvents();
        addInfoPaneEvents();
        addSettingsPaneEvents();
        addChargePaneEvents();

        window.setScene(new Scene(this, 1000, 800));
        window.setTitle("");
        window.show();
    }

    private void addChargePaneEvents() {
        chargePane.toggleMode.setOnAction(e -> {
            this.setLeft(infoPane);
            this.returnPane = infoPane;
        });
        chargePane.settingsBtn.setOnAction(e -> {
            this.setLeft(settingsPane);
        });
        chargePane.collapseBtn.setOnAction(e -> {
            collapsedPane(chargePane);
        });
        chargePane.showGrid.checkBox.setOnAction(e -> {
            showGridB = chargePane.showGrid.checkBox.isSelected();
            graphPane.drawAxis();
        });
        chargePane.solidBounds.checkBox.setOnAction(e -> {
            solidBoundsB = chargePane.solidBounds.checkBox.isSelected();
        });
        chargePane.showVectors.checkBox.setOnAction(e -> {
            showVectorsB = chargePane.showVectors.checkBox.isSelected();
        });
        chargePane.showEquipotential.checkBox.setOnAction(e -> {
            showEquipotentialB = chargePane.showEquipotential.checkBox.isSelected();
        });
    }

    private void addDrawEvents() {
        graphPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.getButton() == MouseButton.SECONDARY){
                boolean skip = false;
                for(var i : Controller.functionList) {
                    for(var j : i.anchorList) {
                        if (e.getX() == j.getCenterX()) {
                            skip = true;
                        }
                    }
                }
                if (!skip && selectedFunction > -1) {
                    GraphPoint point = graphPane.createPoint(e.getX(), e.getY());
                    functionList.get(selectedFunction).addPointMouseEvent(point);
                }
            }
        });

        graphPane.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            graphPane.drawAxis();
        });
    }

    private void addInfoPaneEvents() {
        infoPane.settingsBtn.setOnAction(e -> {
            this.setLeft(settingsPane);
        });
        infoPane.addFunctionBtn.setOnAction(e -> {
            selectedFunction = functionList.size();
            functionList.add(infoPane.addFunction());
        });
        infoPane.collapseBtn.setOnAction(e -> {
            collapsedPane(infoPane);
        });
        infoPane.toggleMode.setOnAction(e -> {
            this.setLeft(chargePane);
            this.returnPane = chargePane;
        });
    }

    private void addSettingsPaneEvents() {
        settingsPane.returnBtn.setOnAction(e -> {
            this.setLeft(returnPane);
        });
    }

    private void collapsedPane(VBox parent) {
        VBox pane = new VBox();
        Button expand = new Button();
        expand.setText(">>");
        pane.getChildren().add(expand);
        expand.setOnAction(e -> {
            this.setLeft(parent);
        });
        this.setLeft(pane);
    }

    public void updateSettingsPane() {
        settingsPane.updateAllFields();
    }

    public void setSelectedFunction(int selectedFunction) {
        this.selectedFunction = selectedFunction;
    }
}
