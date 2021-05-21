package Panes;

import Calculations.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import sample.Controller;

public class InfoPane extends VBox {
    public Button addFunctionBtn, settingsBtn, collapseBtn;
    private Controller controller;
    private Bounds bounds;
    private GraphPane graphPane;

    public InfoPane(Controller controller, GraphPane graphPane, Bounds bounds) {
        this.bounds = bounds;
        this.addFunctionBtn = new Button();
        this.settingsBtn = new Button();
        this.collapseBtn = new Button();
        this.controller = controller;
        this.graphPane = graphPane;
        this.setStyle("-fx-background-color: #d3d3d3;");
        this.setPrefWidth(200);
        HBox buttonBox = new HBox();
        this.getChildren().add(buttonBox);
        addFunctionBtn.setText("Add Function");
        settingsBtn.setText("Settings");
        collapseBtn.setText("<<");
        buttonBox.getChildren().addAll(addFunctionBtn, settingsBtn, collapseBtn);
    }

    public FunctionInfo addFunction() {
        FunctionInfo functionInfo = new FunctionInfo(graphPane, this, bounds, controller);
        this.getChildren().add(functionInfo);
        return functionInfo;
    }
}
