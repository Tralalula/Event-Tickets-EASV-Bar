package event.tickets.easv.bar.gui.util;

import event.tickets.easv.bar.util.StringUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;

public class BindingsUtils {
    public static StringBinding initialize(StringProperty firstName, StringProperty lastName) {
        return Bindings.createStringBinding(() -> StringUtils.initialize(firstName.get(), lastName.get()).toUpperCase(), firstName, lastName);
    }
}
