package Panes;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@SuppressWarnings("ALL")
public class ChargePane extends VBox {
    private HBox buttonBox;
    public Button toggleMode, settingsBtn, collapseBtn;
    public  ChargeSetting showGrid, solidBounds, showVectors, showEquipotential;

    public ChargePane() {
        this.buttonBox = new HBox();
        this.toggleMode = new Button();
        this.settingsBtn = new Button();
        this.collapseBtn = new Button();
        this.showGrid = new ChargeSetting("Show Grid", true);
        this.solidBounds = new ChargeSetting("Solid Bounds", false);
        this.showVectors = new ChargeSetting("Show Vectors", false);
        this.showEquipotential = new ChargeSetting("Show Equipotential Lines", false);
        toggleMode.setText("Add Functions");
        settingsBtn.setText("Settings");
        collapseBtn.setText("<<");
        buttonBox.getChildren().addAll(toggleMode, settingsBtn, collapseBtn);
        this.getChildren().addAll(buttonBox, showGrid, solidBounds, showVectors, showEquipotential);

        this.setStyle("-fx-background-color: #d3d3d3;");
        this.setPrefWidth(200);
    }

}
