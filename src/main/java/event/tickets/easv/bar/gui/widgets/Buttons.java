package event.tickets.easv.bar.gui.widgets;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class Buttons {
    public static Button actionButton(String text, EventHandler<ActionEvent> actionEventHandler) {
        Button results = new Button(text);
        results.setOnAction(actionEventHandler);
        return results;
    }

    public static Button actionIconButton(Ikon iconCode, EventHandler<ActionEvent> actionEventHandler, String... styles) {
        Button results = actionButton("", actionEventHandler);
        FontIcon icon = Icons.styledIcon(iconCode, styles);

        results.setGraphic(icon);
        results.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-border: none;");
        results.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        results.setFocusTraversable(false);

        return results;
    }
}
