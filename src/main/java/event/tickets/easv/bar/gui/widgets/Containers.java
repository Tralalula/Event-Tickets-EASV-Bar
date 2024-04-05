package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Containers {
    public static Node detailBox(String title, String description) {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        var titleLbl = new Label(title);
        titleLbl.getStyleClass().add(Styles.TITLE_4);
        var descriptionLbl = new Label(description);
        descriptionLbl.getStyleClass().add(Styles.TEXT_MUTED);
        results.getChildren().addAll(titleLbl, descriptionLbl);
        return results;
    }
}
