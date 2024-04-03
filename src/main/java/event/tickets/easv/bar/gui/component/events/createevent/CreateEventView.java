package event.tickets.easv.bar.gui.component.events.createevent;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.DatePickers;
import event.tickets.easv.bar.gui.widgets.Labels;
import event.tickets.easv.bar.gui.widgets.TextFields;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class CreateEventView implements View {
    private final CreateEventModel model;
    private final CreateEventController controller;

    public CreateEventView(ObservableList<EventModel> models) {
        this.model = new CreateEventModel();
        this.controller = new CreateEventController(model, models);
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);

        var title = Labels.styledLabel("Create event", Styles.TITLE_1);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        var eventTitle = TextFields.promptedTextField("Event title", model.eventTitleProperty());
        var location = TextFields.promptedTextField("Location", model.locationProperty());
        var startEndTime = doubleTimeField("Start time", "End time");
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
            controller.onCreateEvent(() -> saveButton.disableProperty().bind(model.okToCreateProperty().not()));
        });

        saveButton.getStyleClass().addAll(StyleConfig.ACTIONABLE, Styles.ACCENT);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        return saveButton;
    }

    private Node doubleTimeField(String text1, String text2) {
        var field1 = TextFields.promptedTimeField(text1, model.startTimeProperty());
        var field2 = TextFields.promptedTimeField(text2, model.endTimeProperty());
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
        int uploadAreaWidth = 200;
        int uploadAreHeight = 150;
        int imageWidth = uploadAreaWidth * 2;
        int imageHeight = uploadAreHeight * 2;

        var title = Labels.styledLabel(text, Styles.TEXT_NORMAL);

        var uploadArea = new StackPane();
        uploadArea.setMinSize(uploadAreaWidth, uploadAreHeight);

        var promptText = new Label("Click here to browse");

        var imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(imageHeight);
        imageView.setFitWidth(imageWidth);
        imageView.imageProperty().bind(model.imageProperty());

        imageView.imageProperty().subscribe(() -> uploadArea.setMinSize(imageWidth, imageHeight));

        NodeUtils.bindVisibility(imageView, model.imageProperty().isNotNull());

        var container = new VBox(StyleConfig.STANDARD_SPACING, imageView, promptText);
        container.setAlignment(Pos.CENTER);

        uploadArea.getChildren().add(container);

        uploadArea.setOnMouseClicked(evt -> controller.findImage());

        uploadArea.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT, StyleConfig.ACTIONABLE, Styles.BG_NEUTRAL_MUTED);
        StackPane.setAlignment(container, Pos.CENTER);

        return new VBox(title, uploadArea);
    }

}
