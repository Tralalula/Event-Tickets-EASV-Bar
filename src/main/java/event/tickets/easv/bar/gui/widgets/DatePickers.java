package event.tickets.easv.bar.gui.widgets;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;

public class DatePickers {
    public static Node promptedDatePicker(String prompt, ObjectProperty<LocalDate> boundProperty) {
        return new VBox(Labels.styledLabel(prompt), boundDatePicker(boundProperty));
    }

    public static Node boundDatePicker(ObjectProperty<LocalDate> boundProperty) {
        var result = new DatePicker();
        result.valueProperty().bindBidirectional(boundProperty);
        result.setMaxWidth(Double.MAX_VALUE);
        result.setShowWeekNumbers(true);
        result.setEditable(false);
        result.setPromptText("dd-MM-yyyy");
        result.setDayCellFactory(c -> new FutureDateCell());
        return result;
    }

    static class FutureDateCell extends DateCell {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(LocalDate.now(ZoneId.systemDefault())));
        }
    }
}
