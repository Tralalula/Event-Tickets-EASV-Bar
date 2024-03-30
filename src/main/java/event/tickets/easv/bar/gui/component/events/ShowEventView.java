package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);
    private final EventModel model = EventModel.Empty();

    public ShowEventView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                model.update((EventModel) newData);
            }
        });
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 4);
        var title = Labels.styledLabel(model.title(), Styles.TITLE_1);
        var location = Labels.styledLabel(model.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.startDate(), model.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.endDate(), model.endTime(), "Ends", formatter));

        var headerBox = new VBox(title, location, startDateTime, endDateTime);

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(model.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(model.extraInfo());
        var noteBox = new VBox(noteText, note);

        NodeUtils.bindVisibility(locationGuidanceBox, model.locationGuidance().isNotEmpty());

        results.getChildren().addAll(headerBox, locationGuidanceBox, noteBox);

        return results;
    }
}
