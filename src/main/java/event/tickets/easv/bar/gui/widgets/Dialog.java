package event.tickets.easv.bar.gui.widgets;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class Dialog extends VBox {
    public Dialog(int width, int height) {
        super();

        setSpacing(10);
        setAlignment(Pos.CENTER);
        setMinSize(width, height);
        setMaxSize(width, height);
        setStyle("-fx-background-color: -color-bg-default");
    }
}