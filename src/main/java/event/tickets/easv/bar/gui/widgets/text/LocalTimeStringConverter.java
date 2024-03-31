package event.tickets.easv.bar.gui.widgets.text;

import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalTimeStringConverter extends StringConverter<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String toString(LocalTime object) {
        if (object == null) return "";
        return formatter.format(object);
    }

    @Override
    public LocalTime fromString(String string) {
        if (string == null || string.isEmpty()) return null;

        try {
            if (string.contains(":")) return LocalTime.parse(string, formatter);
            if (string.length() == 2 && !string.contains(":")) {
                int value = Integer.parseInt(string);
                if (value > 24) return LocalTime.parse("00:" + string, formatter);
            }
            return switch (string.length()) {
                case 1 -> LocalTime.parse("0" + string + ":00", formatter);
                case 2 -> LocalTime.parse(string + ":00", formatter);
                case 3 -> LocalTime.parse(string.substring(0, 2) + ":" + string.substring(2) + "0", formatter);
                case 4 -> LocalTime.parse(string.substring(0, 2) + ":" + string.substring(2), formatter);
                default -> null;
            };
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
