package event.tickets.easv.bar.gui.util;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

public class ValidationService {
    public BooleanProperty isValidFirstName(StringProperty firstName) {
        BooleanProperty valid = new SimpleBooleanProperty(false);
        valid.bind(Bindings.createBooleanBinding(
                () -> firstName.get() != null && !firstName.get().isBlank() && firstName.get().length() > 1,
                firstName));

        return valid;
    }

    public BooleanProperty isValidMail(StringProperty mail) {
        BooleanProperty valid = new SimpleBooleanProperty(false);
        valid.bind(Bindings.createBooleanBinding(() -> mail.get() != null &&
                                                      !mail.get().isBlank() &&
                                                       mail.get().contains("@"), mail)
        );

        return valid;
    }
}
