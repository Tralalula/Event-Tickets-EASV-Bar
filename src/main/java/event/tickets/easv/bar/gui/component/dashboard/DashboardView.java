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
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class DashboardView implements View {
    private final ObservableList<EventModel> eventsMasterList;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;
    private static final LocalDate TODAY = LocalDate.now(ZoneId.systemDefault());


    public DashboardView(ObservableList<EventModel> eventsMasterList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.eventsMasterList = eventsMasterList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
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
        cal.setShowWeekNumbers(true);

        results.setAlignment(Pos.TOP_RIGHT);

        results.getChildren().addAll(cal);
        return results;
    }
}
