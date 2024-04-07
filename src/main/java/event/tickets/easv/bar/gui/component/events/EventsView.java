package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
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
    private final ObservableList<EventModel> model;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;
    private final BooleanProperty eventsTicketsSynchronized;
    private static final StringProperty search = new SimpleStringProperty("");
    private final FilteredList<EventModel> filteredEventModels;
    private EventGridView gridView;

    public StringProperty searchProperty() {
        return search;
    }

    public EventsView(ObservableList<EventModel> masterEventList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized, BooleanProperty eventsTicketsSynchronized) {
        this.model = masterEventList;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
        this.eventsTicketsSynchronized = eventsTicketsSynchronized;
        this.filteredEventModels = new FilteredList<>(masterEventList);

        setupSearchFilter();
    }

    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 3 ,0 ,StyleConfig.STANDARD_SPACING));

        var searchField = new CustomTextField();
        searchField.setLeft(new FontIcon(Feather.SEARCH));
        searchField.setPrefWidth(250);
        searchField.textProperty().bindBidirectional(searchProperty());

        var addEvent = new Button(null, new FontIcon(Feather.PLUS));
        addEvent.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4, StyleConfig.ACTIONABLE
        );
        addEvent.setOnAction(e -> ViewHandler.changeView(ViewType.CREATE_EVENT));

        top.getChildren().addAll(searchField, new Spacer(), addEvent);
        return top;
    }

    private void setupSearchFilter() {
        this.searchProperty().addListener((obs, ov, nv) -> {
            Predicate<EventModel> searchPredicate = eventModel -> {
                if (nv == null || nv.isEmpty()) {
                    return true;
                }

                return eventModel.title().get().toLowerCase().contains(nv.toLowerCase());
            };

            filteredEventModels.setPredicate(searchPredicate);
        });
    }

    @Override
    public Region getView() {
        var top = topBar();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        gridView = new EventGridView(filteredEventModels, eventsUsersSynchronized, eventsTicketsSynchronized);
        var content = new StackPane(gridView.getView(), progressIndicator);

        return new VBox(top, content);
    }
}
