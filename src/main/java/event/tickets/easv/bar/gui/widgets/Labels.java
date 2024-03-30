package event.tickets.easv.bar.gui.widgets;

import javafx.beans.value.ObservableStringValue;
import javafx.scene.control.Label;

public class Labels {
    public static Label styledLabel(String text, String... styles) {
        Label results = new Label(text);
        results.getStyleClass().addAll(styles);
        return results;
    }

    public static Label styledLabel(ObservableStringValue text, String... styles) {
        var results = styledLabel("", styles);
        results.textProperty().bind(text);
        return results;
    }

}
