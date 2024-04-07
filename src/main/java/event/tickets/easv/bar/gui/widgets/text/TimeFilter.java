package event.tickets.easv.bar.gui.widgets.text;

import javafx.scene.control.TextFormatter.Change;

import java.util.function.UnaryOperator;

public class TimeFilter implements UnaryOperator<Change> {
    @Override
    public Change apply(Change change) {
        String newText = change.getControlNewText();
        int colonPos = change.getControlText().indexOf(":");
        int caretPos = change.getControlCaretPosition();

        if (change.getText().equals(":")) {
            if (colonPos == -1) { // if there is no ':' we can add one
                return change;
            } else { // if there is ':' we cannot add one
                change.setText("");
                change.setCaretPosition(colonPos + 1); // we move the caret after ":"
                return change;
            }
        }

        if (change.isDeleted()) {
            int start = Math.max(0, caretPos - 1);
            int end = Math.min(change.getControlText().length(), caretPos);
            if (caretPos == colonPos + 1 && colonPos != 1) {
                change.setRange(start, end);
                return change;
            }
        }

        if (change.getText().equals("-")) {
            change.setText("");
            return null;
        }

        if (!newText.matches("([01]?[0-9]|2[0-3])?:?[0-5]?[0-9]?")) {
            return null;
        }

        return change;
    }
}
