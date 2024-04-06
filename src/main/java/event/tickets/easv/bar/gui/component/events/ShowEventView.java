package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.events.assigncoordinator.AssignCoordinatorView;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.*;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);
    private final DeleteEventController controller;

    private final EventModel model;
    private final ObservableList<UserModel> masterUserList;
    private final ImageView image;
    private final HBox coordinators;
    private final TableView<TestModel> eventTicketsTableView;

    public ShowEventView(EventModel model, ObservableList<UserModel> masterUserList) {
        this.controller = new DeleteEventController();
        this.model = model;
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
                model.update((EventModel) newData);
                image.imageProperty().bind(model.image());

                updateCoordinatorsView(model.users());

                model.users().addListener((ListChangeListener<? super UserModel>) c -> {
                    updateCoordinatorsView(model.users());
                });
                eventTicketsTableView.setItems(model.tests());
            }
        });
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
            var photo = new CircularImageView(24, "OK");
            String imageName = userModel.id().get() + "/" + userModel.imageName().get();
            photo.setImage(EventsView.getProfileImage(imageName));

            var name = Labels.styledLabel(userModel.username(), Styles.TEXT_NORMAL);
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



        var title = Labels.styledLabel(model.title(), Styles.TITLE_1);
        var deleteBtn = new Button(null, new FontIcon(Material2AL.DELETE));
        deleteBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        var editBtn = new Button(null, new FontIcon(Material2AL.EDIT));
        editBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        deleteBtn.setOnAction(e -> Alerts.confirmDeleteEvent(
                model,
                eventModel -> controller.onDeleteEvent(ViewHandler::previousView, model))
        );

        editBtn.setOnAction(e -> ViewHandler.changeView(ViewType.EDIT_EVENT, model));

        var titleBox = new HBox(title, editBtn, deleteBtn);

        var location = Labels.styledLabel(model.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.startDate(), model.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.endDate(), model.endTime(), "Ends", formatter));

        var headerBox = new VBox(image, titleBox, location, startDateTime, endDateTime);

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(model.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(model.extraInfo());
        var noteBox = new VBox(noteText, note);

        var coordinatorsText = Labels.styledLabel("Event coordinators", Styles.TITLE_3);
        var spacer = new Region();
        var add = Buttons.actionIconButton(Material2AL.ADD, e -> ViewHandler.showOverlay("Add coordinator", new AssignCoordinatorView(model, masterUserList).getView(), 600, 540), StyleConfig.ACTIONABLE);
        var box = new HBox(coordinatorsText, spacer, add);
        var coordinatorsBox = new VBox(box, coordinators);


        var ticketsText = Labels.styledLabel("Tickets", Styles.TITLE_3);
        var ticketsBox = new VBox(ticketsText, eventTicketsTableView);

        NodeUtils.bindVisibility(locationGuidanceBox, model.locationGuidance().isNotEmpty());
        NodeUtils.bindVisibility(noteBox, model.extraInfo().isNotEmpty());
        NodeUtils.bindVisibility(coordinatorsBox, Bindings.isEmpty(coordinators.getChildren()).not());

        results.getChildren().addAll(headerBox, locationGuidanceBox, noteBox, coordinatorsBox, ticketsBox);

        return results;
    }

}
