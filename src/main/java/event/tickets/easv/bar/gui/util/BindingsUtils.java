package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BindingsUtils {
    public static StringBinding initialize(StringProperty firstName, StringProperty lastName) {
        return Bindings.createStringBinding(() -> StringUtils.initialize(firstName.get(), lastName.get()).toUpperCase(), firstName, lastName);
    }

    public static StringBinding dateTimeBinding(ObjectProperty<LocalDate> dateProperty,
                                                ObjectProperty<LocalTime> timeProperty,
                                                String prefix,
                                                DateTimeFormatter formatter) {
        return Bindings.createStringBinding(() -> {
            LocalDate date = dateProperty.get();
            LocalTime time = timeProperty.get();
            if (date != null && time != null) {
                return prefix + " " + LocalDateTime.of(date, time).format(formatter);
            } else {
                return "";
            }
        }, dateProperty, timeProperty);
    }
}
