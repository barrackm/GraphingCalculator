package Panes;

import Calculations.Bounds;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import sample.Controller;

public class InfoPane extends VBox {
    public Button addFunctionBtn, settingsBtn, collapseBtn, toggleMode;
    private Controller controller;
    private Bounds bounds;
    private GraphPane graphPane;
    private FlowPane buttonBox;

    public InfoPane(Controller controller, GraphPane graphPane, Bounds bounds) {
        this.bounds = bounds;
        this.addFunctionBtn = new Button();
        this.settingsBtn = new Button();
        this.collapseBtn = new Button();
        this.toggleMode = new Button();
        this.controller = controller;
        this.graphPane = graphPane;
        this.buttonBox = new FlowPane();
        addFunctionBtn.setText("Add Function");
        settingsBtn.setText("Settings");
        collapseBtn.setText("<<");
        toggleMode.setText("Place Charges");
        this.getChildren().add(buttonBox);
        buttonBox.getChildren().addAll(addFunctionBtn, settingsBtn, collapseBtn, toggleMode);

        this.setStyle("-fx-background-color: #d3d3d3;");
        this.setPrefWidth(200);
    }

    public FlowPane getButtonBox() {
        return buttonBox;
    }

    public FunctionInfo addFunction() {
        FunctionInfo functionInfo = new FunctionInfo(graphPane, this, bounds, controller);
        this.getChildren().add(functionInfo);
        return functionInfo;
    }
}
