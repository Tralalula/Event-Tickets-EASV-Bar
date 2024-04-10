package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.Main;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.util.Listeners;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.function.Predicate;

public class EventsView implements View {
    private static final StringProperty search = new SimpleStringProperty(""); // didn't work without static, not sure why.

    private final ObservableList<EventModel> masterUserList;
    private FilteredList<EventModel> filteredEventModels;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;
    private final BooleanProperty eventsTicketsSynchronized;
    private Predicate<EventModel> excludeNonAssociatedEvents;

    public EventsView(ObservableList<EventModel> masterEventList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized, BooleanProperty eventsTicketsSynchronized) {
        this.masterUserList = masterEventList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
        this.eventsTicketsSynchronized = eventsTicketsSynchronized;


        initializeExcludePredicate();
        this.filteredEventModels = new FilteredList<>(masterEventList, excludeNonAssociatedEvents);

        setupSearchFilter();

        SessionManager.getInstance().getUserModel().rank().addListener((obs, ov, nv) -> {
            initializeExcludePredicate();
            filteredEventModels.setPredicate(excludeNonAssociatedEvents);
        });

        Listeners.addOnceChangeListener(eventsUsersSynchronized, () -> {
            initializeExcludePredicate();
            filteredEventModels.setPredicate(excludeNonAssociatedEvents);
        });
    }

    private void initializeExcludePredicate() {
        Rank rank = SessionManager.getInstance().getUserModel().rank().get();
        if (rank == Rank.EVENT_COORDINATOR) {
            int userId = SessionManager.getInstance().getUserModel().id().get();
            excludeNonAssociatedEvents = eventModel -> eventModel.users().stream()
                    .anyMatch(userModel -> userModel.id().get() == userId);
        } else {
            excludeNonAssociatedEvents = eventModel -> true;
        }
    }

    private void setupSearchFilter() {
        this.searchProperty().addListener((obs, ov, nv) -> {
            Predicate<EventModel> searchPredicate = eventModel -> {
                if (nv == null || nv.isEmpty()) {
                    return true;
                }
                return eventModel.title().get().toLowerCase().contains(nv.toLowerCase());
            };

            filteredEventModels.setPredicate(excludeNonAssociatedEvents.and(searchPredicate));
        });
    }

    @Override
    public Region getView() {
        var top = topBar();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        EventGridView gridView = new EventGridView(filteredEventModels, eventsUsersSynchronized, eventsTicketsSynchronized);
        var content = new StackPane(gridView.getView(), progressIndicator);

        return new VBox(top, content);
    }


    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 3 ,0 ,StyleConfig.STANDARD_SPACING));

        var searchField = new CustomTextField();
        searchField.setPromptText("By title");
        searchField.setLeft(new FontIcon(Feather.SEARCH));
        searchField.setPrefWidth(250);
        searchField.textProperty().bindBidirectional(search);

        var addEvent = new Button(null, new FontIcon(Feather.PLUS));
        addEvent.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4, StyleConfig.ACTIONABLE
        );
        addEvent.setOnAction(e -> ViewHandler.changeView(ViewType.CREATE_EVENT));

        NodeUtils.bindVisibility(addEvent, SessionManager.getInstance().getUserModel().rank().isEqualTo(Rank.EVENT_COORDINATOR));

        top.getChildren().addAll(searchField, new Spacer(), addEvent);
        return top;
    }


    public StringProperty searchProperty() {
        return search;
    }
}
