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
    private static final StringProperty search = new SimpleStringProperty(""); // didn't work without static, not sure why.

    private final ObservableList<EventModel> masterUserList;
    private final FilteredList<EventModel> filteredEventModels;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;
    private final BooleanProperty eventsTicketsSynchronized;

    public EventsView(ObservableList<EventModel> masterEventList, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized, BooleanProperty eventsTicketsSynchronized) {
        this.masterUserList = masterEventList;
        this.filteredEventModels = new FilteredList<>(masterEventList);
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
        this.eventsTicketsSynchronized = eventsTicketsSynchronized;

        setupSearchFilter();
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


    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 3 ,0 ,StyleConfig.STANDARD_SPACING));

        var searchField = new CustomTextField();
        searchField.setPromptText("By title");
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

    private StringProperty searchProperty() {
        return search;
    }

}
