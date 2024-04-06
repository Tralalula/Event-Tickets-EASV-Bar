package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.events.assigncoordinator.AssignCoordinatorView;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.BindingsUtils;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.*;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);
    private final DeleteEventController controller;

    private final EventModel eventModelToShow = EventModel.Empty();
    private final ObservableList<EventModel> masterEventList;
    private final ObservableList<UserModel> masterUserList;
    private final ImageView image;
    private final HBox coordinators;
    private final TableView<TestModel> eventTicketsTableView;

    public ShowEventView(ObservableList<EventModel> masterEventList, ObservableList<UserModel> masterUserList) {
        this.controller = new DeleteEventController();
        this.masterEventList = masterEventList;
        this.masterUserList = masterUserList;
        coordinators = new HBox(StyleConfig.STANDARD_SPACING * 8);
        eventTicketsTableView = new TableView<>();

        initializeTableView();

        image = new ImageView();
        image.setPreserveRatio(true);
        image.setPickOnBounds(true);
        image.setFitWidth(1200);
        image.setFitHeight(300);

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                eventModelToShow.update((EventModel) newData);
                image.imageProperty().bind(eventModelToShow.image());

                updateCoordinatorsView(eventModelToShow.users());

                eventModelToShow.users().addListener((ListChangeListener<? super UserModel>) c -> {
                    updateCoordinatorsView(eventModelToShow.users());
                });
                eventTicketsTableView.setItems(eventModelToShow.tests());
            }
        });
    }

    private EventModel findModelById(int id) {
        for (EventModel eventModel : masterEventList) {
            if (eventModel.id().get() == id) {
                return eventModel;
            }
        }

        return EventModel.Empty();
    }

    private void initializeTableView() {
        TableColumn<TestModel, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cdf -> cdf.getValue().title);
        eventTicketsTableView.getColumns().add(titleCol);

        TableColumn<TestModel, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cdf -> cdf.getValue().type);
        eventTicketsTableView.getColumns().add(typeCol);

        TableColumn<TestModel, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(cdf -> cdf.getValue().quantity);
        eventTicketsTableView.getColumns().add(quantityCol);

        TableColumn<TestModel, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(cdf -> cdf.getValue().price);
        eventTicketsTableView.getColumns().add(priceCol);

        var placeHolder = new Label("Trying to get data. Please wait.");
        placeHolder.setAlignment(Pos.CENTER);
        eventTicketsTableView.setPlaceholder(placeHolder);

        eventTicketsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void updateCoordinatorsView(ObservableList<UserModel> users) {
        coordinators.getChildren().clear();

        for (UserModel userModel : users) {
            var photo = new CircularImageView(24);
            photo.textProperty().bind(BindingsUtils.initialize(userModel.firstName(), userModel.lastName()));
            photo.imageProperty().bind(userModel.image());

            var name = Labels.styledLabel(Bindings.concat(userModel.firstName(), " ", userModel.lastName()), Styles.TEXT_NORMAL);
            var box = new VBox(StyleConfig.STANDARD_SPACING, photo.get(), name);
            box.setAlignment(Pos.CENTER);

            coordinators.getChildren().add(box);
        }
    }


    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 4);
        results.setPadding(new Insets(10));
        results.setFillWidth(true);



        var title = Labels.styledLabel(eventModelToShow.title(), Styles.TITLE_1);
        var deleteBtn = new Button(null, new FontIcon(Material2AL.DELETE));
        deleteBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        var editBtn = new Button(null, new FontIcon(Material2AL.EDIT));
        editBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        deleteBtn.setOnAction(e -> Alerts.confirmDeleteEvent(
                eventModelToShow,
                eventModel -> controller.onDeleteEvent(ViewHandler::previousView, eventModelToShow))
        );

        editBtn.setOnAction(e -> ViewHandler.changeView(ViewType.EDIT_EVENT, eventModelToShow));

        var titleBox = new HBox(title, editBtn, deleteBtn);

        var location = Labels.styledLabel(eventModelToShow.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(EventsView.dateTimeBinding(eventModelToShow.startDate(), eventModelToShow.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(EventsView.dateTimeBinding(eventModelToShow.endDate(), eventModelToShow.endTime(), "Ends", formatter));

        var headerBox = new VBox(image, titleBox, location, startDateTime, endDateTime);

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(eventModelToShow.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(eventModelToShow.extraInfo());
        var noteBox = new VBox(noteText, note);

        var coordinatorsText = Labels.styledLabel("Event coordinators", Styles.TITLE_3);
        var spacer = new Region();
        var add = Buttons.actionIconButton(Material2AL.ADD, e -> ViewHandler.showOverlay("Add coordinator", new AssignCoordinatorView(findModelById(eventModelToShow.id().get()), masterUserList).getView(), 600, 540), StyleConfig.ACTIONABLE);
        var box = new HBox(coordinatorsText, spacer, add);
        var coordinatorsBox = new VBox(box, coordinators);


        var ticketsText = Labels.styledLabel("Tickets", Styles.TITLE_3);
        var ticketsBox = new VBox(ticketsText, eventTicketsTableView);

        NodeUtils.bindVisibility(locationGuidanceBox, eventModelToShow.locationGuidance().isNotEmpty());
        NodeUtils.bindVisibility(noteBox, eventModelToShow.extraInfo().isNotEmpty());
//        NodeUtils.bindVisibility(coordinatorsBox, Bindings.isEmpty(coordinators.getChildren()).not());

        results.getChildren().addAll(headerBox, locationGuidanceBox, noteBox, coordinatorsBox, ticketsBox);

        return results;
    }

}
