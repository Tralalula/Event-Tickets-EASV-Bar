package event.tickets.easv.bar.gui.component.events.createevent;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.DatePickers;
import event.tickets.easv.bar.gui.widgets.Labels;
import event.tickets.easv.bar.gui.widgets.TextFields;
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
    private final CreateEventModel model;
    private final CreateEventController controller;

    public CreateEventView() {
        this.model = new CreateEventModel();
        this.controller = new CreateEventController(model);
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var title = Labels.styledLabel("Create Event", Styles.TITLE_1);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        var eventTitle = TextFields.promptedTextField("Event title", model.eventTitleProperty());
        var location = TextFields.promptedTextField("Location", model.locationProperty());
        var startEndTime = doubleField("Start time", "End time");
        var startEndDate = doubleDatePicker("Start date", "End date");
        var image = titledImageUpload("Event image");
        var extraInfo = TextFields.promptedTextField("Extra info", model.extraInfoProperty());
        var locationGuidance = TextFields.promptedTextField("Location guidance", model.locationGuidanceProperty());
        var submit = createSaveButton();
        results.getChildren().addAll(title, eventTitle, location, startEndTime, startEndDate, image, extraInfo, locationGuidance, submit);
        results.setPadding(new Insets(10));
        return results;
    }

    private Node createSaveButton() {
        var saveButton = new Button("Create event");
        saveButton.disableProperty().bind(model.okToCreateProperty().not());
        saveButton.setOnAction(evt -> {
            saveButton.disableProperty().unbind();
            saveButton.setDisable(true);
            controller.createEvent(() -> saveButton.disableProperty().bind(model.okToCreateProperty().not()));
        });

        saveButton.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.ACCENT);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        return saveButton;
    }

    private Node titledField(String text) {
        var title = Labels.styledLabel(text, Styles.TEXT_NORMAL);
        var field = new TextField();
        return new VBox(title, field);
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
        var field1 = DatePickers.promptedDatePicker(text1, model.startDateProperty());
        var field2 = DatePickers.promptedDatePicker(text2, model.endDateProperty());
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

        return new VBox(title, uploadArea);
    }

}
