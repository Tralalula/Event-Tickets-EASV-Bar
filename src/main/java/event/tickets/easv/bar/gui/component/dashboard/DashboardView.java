package event.tickets.easv.bar.gui.component.dashboard;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.events.EventGridView;
import event.tickets.easv.bar.gui.util.BindingsUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import event.tickets.easv.bar.gui.widgets.Clock;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.css.PseudoClass;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

        eventListView.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        eventListView.setCellFactory(c -> {
            var cell = eventCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });
        eventListView.setPadding(new Insets(0));
        eventListView.setMinWidth(300);

    }

    private ListCell<EventModel> eventCell() {
        return new ListCell<>() {
            private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");

            private final GridPane gridPane = new GridPane();
            private final HBox container = new HBox(gridPane);
            private final Region spacer = new Spacer();
            private final CircularImageView eventImage = new CircularImageView(24);

            private final Label eventTitle = new Label();
            private final HBox eventTitleContainer = new HBox(eventTitle);

            private final Label startTime = new Label();
            private final HBox startTimeContainer = new HBox(startTime);

            {
                eventTitleContainer.setAlignment(Pos.CENTER_LEFT);
                startTimeContainer.setAlignment(Pos.CENTER_RIGHT);

                eventTitle.setWrapText(true);
                eventTitle.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.TEXT_MUTED, Styles.TEXT_SMALL);

                startTime.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_SMALL);


                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(25);

                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(50);

                ColumnConstraints column3 = new ColumnConstraints();
                column3.setPercentWidth(25);

                gridPane.getColumnConstraints().addAll(column1, column2, column3);
                gridPane.add(eventImage.get(), 0, 0);
                gridPane.add(eventTitleContainer, 1, 0);
                gridPane.add(startTimeContainer, 2, 0);


                gridPane.setPadding(new Insets(4, StyleConfig.STANDARD_SPACING, 4, -4));

                gridPane.setMouseTransparent(true);
                gridPane.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, "list-cell-grid");
                hoverProperty().addListener((obs, ov, nv) -> gridPane.pseudoClassStateChanged(HOVER_PSEUDO_CLASS, nv));


                spacer.setPrefHeight(StyleConfig.STANDARD_SPACING);
                spacer.setMinHeight(StyleConfig.STANDARD_SPACING);
                spacer.setMaxHeight(StyleConfig.STANDARD_SPACING);
                spacer.getStyleClass().add("list-cell-spacer");
                spacer.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                if (item == getItem() && empty == isEmpty()) return;

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    eventImage.imageProperty().bind(item.image());
                    eventImage.textProperty().bind(BindingsUtils.initialize(item.title()));

                    eventTitle.textProperty().bind(item.title());

                    startTime.textProperty().bind(Bindings.createStringBinding(() -> item.startTime().get().toString(), item.startTime()));

                    var wrapper = new VBox(gridPane, spacer);
                    wrapper.getStyleClass().add(StyleConfig.ACTIONABLE);

                    wrapper.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            ViewHandler.changeView(ViewType.SHOW_EVENT, item);
                        }
                    });

                    gridPane.setMaxWidth(292);
                    gridPane.setMinWidth(292);
                    gridPane.setPrefWidth(292);
                    wrapper.setMaxWidth(Control.USE_PREF_SIZE);

                    setGraphic(wrapper);
                }
            }
        };
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

        var listViewWrapper = new StackPane(eventListView);
        listViewWrapper.getStyleClass().add("listview-wrapper");

        results.getChildren().addAll(cal, listViewWrapper);
        return results;
    }

    private void updateEventListView() {
        filteredEventsForDate.setPredicate(eventModel -> eventModel.startDate().get().equals(selectedDate.get()));
    }
}