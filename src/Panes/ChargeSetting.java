package Panes;

import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ChargeSetting extends HBox {
    public CheckBox checkBox;
    private Label label;

    public ChargeSetting(String string, boolean val) {
        this.checkBox = new CheckBox();
        checkBox.setSelected(val);
        this.label = new Label(string);
        this.getChildren().addAll(label, checkBox);
        this.setStyle("-fx-background-color: #d3d3d3;");
        this.setPrefWidth(200);
        checkBox.setPadding(new Insets(10, 10, 10, 10));
        label.setPadding(new Insets(10, 10, 10, 10));
    }
}
