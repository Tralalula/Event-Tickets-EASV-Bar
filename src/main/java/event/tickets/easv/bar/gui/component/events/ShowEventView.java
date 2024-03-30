package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);
    private final EventModel model = EventModel.Empty();
    private final ImageView image;


    public ShowEventView() {
        image = new ImageView();
        image.setPreserveRatio(true);
        image.setPickOnBounds(true);
        image.setFitWidth(1200);
        image.setFitHeight(300);

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                model.update((EventModel) newData);
                Image img = EventsView.getImage(model.imageName().get());
                PixelReader reader = img.getPixelReader();
                WritableImage newImage = new WritableImage(reader, 0, 0, (int) img.getWidth(), 300);
                image.setImage(newImage);
            }
        });
    }


    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 4);

        results.setFillWidth(true);

        var title = Labels.styledLabel(model.title(), Styles.TITLE_1);
        var location = Labels.styledLabel(model.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.startDate(), model.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.endDate(), model.endTime(), "Ends", formatter));

        var headerBox = new VBox(image, title, location, startDateTime, endDateTime);

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(model.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(model.extraInfo());
        var noteBox = new VBox(noteText, note);

        var coordinatorsText = Labels.styledLabel("Event coordinators", Styles.TITLE_3);
        var coordinators = new HBox(StyleConfig.STANDARD_SPACING * 8);

        for (int i = 6; i < 9; i++) {
            var circle = new Circle(24);
            circle.getStyleClass().add(Styles.ELEVATED_3);
            var n = "";
            if (i == 6) n = "Jel Chibuzo";
            if (i == 7) n = "Opi Watihana";
            if (i == 8) n = "Izabella Tabakova";
            var name = Labels.styledLabel(n, Styles.TEXT_NORMAL);
            EventsView.loadImagePattern(circle, EventsView.getProfileImage("profile" + i + ".jpeg"));
            var box = new VBox(StyleConfig.STANDARD_SPACING, circle, name);
            box.setAlignment(Pos.CENTER);
            coordinators.getChildren().add(box);
        }

        var coordinatorsBox = new VBox(coordinatorsText, coordinators);

        NodeUtils.bindVisibility(locationGuidanceBox, model.locationGuidance().isNotEmpty());
        NodeUtils.bindVisibility(noteBox, model.extraInfo().isNotEmpty());


        results.getChildren().addAll(headerBox, locationGuidanceBox, noteBox, coordinatorsBox);

        return results;
    }
}
