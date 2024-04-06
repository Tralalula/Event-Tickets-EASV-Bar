package event.tickets.easv.bar.gui.component.dashboard;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.events.EventGridView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Clock;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;


public class DashboardView implements View {
    private static final LocalDate TODAY = LocalDate.now(ZoneId.systemDefault());

    private final ObservableList<EventModel> eventsMasterList;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;
    private final ListView<EventModel> eventListView;
    private final ObjectProperty<LocalDate> selectedDate = new SimpleObjectProperty<>(TODAY);
    private final FilteredList<EventModel> filteredEventsForDate;


    public DashboardView(ObservableList<EventModel> eventsMasterList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.eventsMasterList = eventsMasterList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
        this.eventListView = new ListView<>();
        this.filteredEventsForDate = new FilteredList<>(eventsMasterList);

        filteredEventsForDate.setPredicate(eventModel -> eventModel.startDate().get().equals(selectedDate.get()));
        eventListView.setItems(filteredEventsForDate);
    }


    @Override
    public Region getView() {
        var results = new GridPane();

        var events = events();
        var calender = calender();

        var column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);

        var column2 = new ColumnConstraints();
        column2.setHgrow(Priority.NEVER);
        column2.setPrefWidth(Region.USE_COMPUTED_SIZE);

        results.getColumnConstraints().addAll(column1, column2);

        results.add(events, 0, 0);
        results.add(calender, 1, 0);

        return results;
    }

    public Node events() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);

        var eventsTitle = Labels.styledLabel("Upcoming events", Styles.TITLE_2);
        eventsTitle.setPadding(new Insets(0, 0, 0, 10));

        var filteredEvents = new FilteredList<>(eventsMasterList,
                eventModel -> !eventModel.startDate().get().isBefore(TODAY));

        var sortedEvents = new SortedList<>(filteredEvents,
                Comparator.comparing(eventModel -> eventModel.startDate().get()));

        var gridView = new EventGridView(sortedEvents, eventsUsersSynchronized);

        results.getChildren().addAll(eventsTitle, gridView.getView());
        return results;
    }

    public Node calender() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        results.setPadding(new Insets(0, 10, 0, 0));

        var cal = new Calendar(TODAY);
        cal.setTopNode(new Clock());
        cal.setShowWeekNumbers(true);

        cal.valueProperty().addListener((obs, ov, nv) -> {
            selectedDate.set(nv);
            updateEventListView();
        });

        results.setAlignment(Pos.TOP_RIGHT);

        results.getChildren().addAll(cal, eventListView);
        return results;
    }

    private void updateEventListView() {
        filteredEventsForDate.setPredicate(eventModel -> eventModel.startDate().get().equals(selectedDate.get()));
    }
}