package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.gui.widgets.Labels;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);
    private final EventModel model;
    private final ImageView image;
    private final HBox coordinators;
    private final TableView<TestModel> eventTicketsTableView;

    public ShowEventView(EventModel model) {
        this.model = model;
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
                Image img = EventsView.getImage(model.imageName().get());
                PixelReader reader = img.getPixelReader();
                WritableImage newImage = new WritableImage(reader, 0, 0, (int) img.getWidth(), 300);
                image.setImage(newImage);

                updateCoordinatorsView(model.users());
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
            var circle = new Circle(24);
            circle.getStyleClass().add(Styles.ELEVATED_3);

            String imageName = userModel.id().get() + "/" + userModel.imageName().get();
            System.out.println(imageName);
            EventsView.loadImagePattern(circle, EventsView.getProfileImage(imageName));

            var name = Labels.styledLabel(userModel.username(), Styles.TEXT_NORMAL);
            var box = new VBox(StyleConfig.STANDARD_SPACING, circle, name);
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
        var location = Labels.styledLabel(model.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.startDate(), model.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(EventsView.dateTimeBinding(model.endDate(), model.endTime(), "Ends", formatter));

        var headerBox = new VBox(image, title, location, startDateTime, endDateTime);

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(model.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(model.extraInfo());
        var noteBox = new VBox(noteText, note);

        var coordinatorsText = Labels.styledLabel("Event coordinators", Styles.TITLE_3);
        var coordinatorsBox = new VBox(coordinatorsText, coordinators);

        var ticketsText = Labels.styledLabel("Tickets", Styles.TITLE_3);
        var ticketsBox = new VBox(ticketsText, eventTicketsTableView);

        NodeUtils.bindVisibility(locationGuidanceBox, model.locationGuidance().isNotEmpty());
        NodeUtils.bindVisibility(noteBox, model.extraInfo().isNotEmpty());
        NodeUtils.bindVisibility(coordinatorsBox, Bindings.isEmpty(coordinators.getChildren()).not());

        results.getChildren().addAll(headerBox, locationGuidanceBox, noteBox, coordinatorsBox, ticketsBox);

        return results;
    }
}
