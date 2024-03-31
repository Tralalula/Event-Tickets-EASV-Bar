package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;

public class CreateEventView implements View {
    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var title = Labels.styledLabel("Create Event", Styles.TITLE_1);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        var eventTitle = titledField("Event title");
        var location = titledField("Location");
        var startEndTime = doubleField("Start time", "End time");
        var startEndDate = doubleDatePicker("Start date", "End date");
        var image = titledImageUpload("Event image");
        var extraInfo = titledField("Extra info");
        var locationGuidance = titledField("Location guidance");
        var submit = createButton("Create event");
        results.getChildren().addAll(title, eventTitle, location, startEndTime, startEndDate, image, extraInfo, locationGuidance, submit);
        results.setPadding(new Insets(10));
        return results;
    }

    private Node createButton(String text) {
        var button = new Button(text);
        button.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.ACCENT);
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private Node titledField(String text) {
        var title = Labels.styledLabel(text, Styles.TEXT_NORMAL);
        var field = new TextField();
        return new VBox(StyleConfig.STANDARD_SPACING, title, field);
    }

    private Node titledDatePicker(String text, boolean defaultPrompt) {
        var title = Labels.styledLabel(text, Styles.TEXT_NORMAL);
        var datepicker = new DatePicker();
        if (defaultPrompt) {
            datepicker.setValue(LocalDate.now(ZoneId.systemDefault()));
            datepicker.setPromptText("dd-MM-yyyy");
        } else {
            datepicker.setPromptText("");
        }
        datepicker.setEditable(false);
        datepicker.setShowWeekNumbers(true);
        datepicker.setMaxWidth(Double.MAX_VALUE);
        return new VBox(StyleConfig.STANDARD_SPACING, title, datepicker);
    }

    private Node doubleField(String text1, String text2) {
        var field1 = titledField(text1);
        var field2 = titledField(text2);
        var results = new HBox(StyleConfig.STANDARD_SPACING, field1, field2);
        results.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(field1, Priority.ALWAYS);
        HBox.setHgrow(field2, Priority.ALWAYS);
        return results;
    }


    private Node doubleDatePicker(String text1, String text2) {
        var field1 = titledDatePicker(text1, true);
        var field2 = titledDatePicker(text2, false);
        var results = new HBox(StyleConfig.STANDARD_SPACING, field1, field2);
        results.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(field1, Priority.ALWAYS);
        HBox.setHgrow(field2, Priority.ALWAYS);
        return results;
    }

    private Node titledImageUpload(String text) {
        var title = Labels.styledLabel(text, Styles.TEXT_NORMAL);

        var uploadArea = new StackPane();
        uploadArea.setMinSize(200, 150);
        uploadArea.setPrefSize(200, 150);

        var promptText = new Label("Click here to browse");

        uploadArea.getChildren().add(promptText);
/*        uploadArea.setStyle(
                "-fx-border-width: 2; " +
                "-fx-border-style: dashed; " +
                "-fx-border-radius: 10; "
        );*/

        uploadArea.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, StyleConfig.ACTIONABLE, Styles.BG_NEUTRAL_MUTED);
        StackPane.setAlignment(promptText, Pos.CENTER);

        return new VBox(StyleConfig.STANDARD_SPACING, title, uploadArea);
    }

}
