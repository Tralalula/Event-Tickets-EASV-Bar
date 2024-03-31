package event.tickets.easv.bar.gui.widgets;

import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class TextFields {
    public static Node promptedTextField(String prompt, StringProperty boundProperty) {
        return new VBox(Labels.styledLabel(prompt), boundTextField(boundProperty));
    }

    public static Node boundTextField(StringProperty boundProperty) {
        var result = new TextField();
        result.textProperty().bindBidirectional(boundProperty);
        return result;
    }
}
