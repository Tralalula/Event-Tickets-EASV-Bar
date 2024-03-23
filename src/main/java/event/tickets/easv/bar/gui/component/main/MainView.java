package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.component.common.View;
import event.tickets.easv.bar.gui.theme.StyleConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class MainView implements View {
    @Override
    public Region getView() {
        var results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/style.css")).toExternalForm());
        results.getStyleClass().add(Styles.BG_SUBTLE);
        results.getStyleClass().add("main");


        var top = topbar();
        var left = sidebar();

        BorderPane.setMargin(top, new Insets(StyleConfig.STANDARD_SPACING));
        BorderPane.setMargin(left, new Insets(0, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING));

        results.setTop(top);
        results.setLeft(left);

        return results;
    }

    private Region topbar() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);
        results.setMinHeight(50);

        return results;
    }

    private Region sidebar() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);
        results.setMinWidth(250);

        results.getChildren().addAll(
                new Button("Dashboard"),
                new Button("Events"),
                new Button("Tickets"),
                new Button("Users"),
                new Button("Verify Ticket")
        );
        results.setAlignment(Pos.TOP_CENTER);

        return results;
    }

}
