package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.widgets.text.LocalTimeStringConverter;
import event.tickets.easv.bar.gui.widgets.text.TimeFilter;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

import java.time.LocalTime;

public class TextFields {
    public static Node promptedTextField(String prompt, StringProperty boundProperty) {
        return new VBox(Labels.styledLabel(prompt), boundTextField(boundProperty));
    }

    public static Node promptedTimeField(String prompt, ObjectProperty<LocalTime> boundProperty) {
        return new VBox(Labels.styledLabel(prompt), boundTimeField(boundProperty));
    }

    public static Node boundTextField(StringProperty boundProperty) {
        var result = new TextField();
        result.textProperty().bindBidirectional(boundProperty);
        return result;
    }

    public static Node boundValidatedTextField(StringProperty boundProperty, BooleanProperty validProperty) {
        var result = new TextField();
        result.textProperty().bindBidirectional(boundProperty);

        validProperty.addListener((obs, ov, nv) -> {
            result.pseudoClassStateChanged(Styles.STATE_SUCCESS, nv);
            result.pseudoClassStateChanged(Styles.STATE_DANGER, !nv);
        });

        return result;
    }

    public static Node boundTimeField(ObjectProperty<LocalTime> boundProperty) {
        var result = new TextField();
        result.setPromptText("HH:mm");
        TextFormatter<LocalTime> formatter = new TextFormatter<>(new LocalTimeStringConverter(), null, new TimeFilter());
        result.setTextFormatter(formatter);
        formatter.valueProperty().bindBidirectional(boundProperty);
        return result;
    }
}
