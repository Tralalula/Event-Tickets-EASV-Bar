package event.tickets.easv.bar.gui.component.dashboard;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.events.EventGridView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;

public class DashboardView implements View {
    private final ObservableList<EventModel> eventsMasterList;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;


    public DashboardView(ObservableList<EventModel> eventsMasterList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.eventsMasterList = eventsMasterList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
    }

    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);

        var eventsTitle = Labels.styledLabel("Upcoming events", Styles.TITLE_2);
        eventsTitle.setPadding(new Insets(0, 0, 0, 10));

        var today = LocalDate.now(ZoneId.systemDefault());
        var filteredEvents = new FilteredList<>(eventsMasterList,
                eventModel -> !eventModel.startDate().get().isBefore(today));

        var sortedEvents = new SortedList<>(filteredEvents,
                Comparator.comparing(eventModel -> eventModel.startDate().get()));

        var gridView = new EventGridView(sortedEvents, eventsUsersSynchronized);

        results.getChildren().addAll(eventsTitle, gridView.getView());
        return results;
    }
}
