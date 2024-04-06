package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.kordamp.ikonli.feather.Feather;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static event.tickets.easv.bar.gui.component.events.EventsView.*;

public class EventGridView implements View {
    private static final int CARD_WIDTH = 326;
    private static final int CARD_HEIGHT = 370;
    private static final int PROFILE_IMG_RADIUS = 20;
    private static final int CARRD_EVENT_IMAGE_WIDTH = CARD_WIDTH - 2; // needs to account for a bit on each side
    private static final int CARD_EVENT_IMAGE_HEIGHT = 160;

    private final DeleteEventController controller;
    private final GridView<EventModel> gridview;
    private final ObservableList<EventModel> model;

    public EventGridView(ObservableList<EventModel> model) {
        this.controller = new DeleteEventController();
        this.model = model;
        gridview = new GridView<>();
    }

    public Region getView() {
        gridview.setItems(model);
        gridview.setCellWidth(CARD_WIDTH);
        gridview.setCellHeight(CARD_HEIGHT);
        gridview.setCellFactory(cell -> eventCell());
        gridview.setHorizontalCellSpacing(10);
        gridview.setVerticalCellSpacing(10);

        gridview.setPadding(new Insets(0, 10, 10, 0));
        return gridview;
    }

    public void setItems(ObservableList<EventModel> items) {
        gridview.setItems(items);
    }

    private GridCell<EventModel> eventCell() {
        return new GridCell<>() {
            private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HHmm", Locale.ENGLISH);

            private final Card card = new Card();
            private final ImageView imageView;
            private final Label title = new Label();
            private final Label location = new Label();
            private final Label startDateTime = new Label();
            private final Label endDateTime = new Label();
            private final VBox content = new VBox(title, location, startDateTime, endDateTime);

            private final Circle circle1 = new Circle(PROFILE_IMG_RADIUS);
            private final Circle circle2 = new Circle(PROFILE_IMG_RADIUS);
            private final Circle circle3 = new Circle(PROFILE_IMG_RADIUS);
            private final HBox profileImages = new HBox(StyleConfig.STANDARD_SPACING, circle1, circle2, circle3);

            private final Label ticketsSold = new Label("283 tickets sold");

            private final BorderPane footer = new BorderPane();

            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem editItem = MenuItems.createItem("_Edit", Feather.EDIT);
            private final MenuItem deleteItem = MenuItems.createItem("_Delete", Feather.TRASH_2);

            {
                editItem.setMnemonicParsing(true);
                deleteItem.setMnemonicParsing(true);

                contextMenu.getItems().addAll(editItem, deleteItem);
                card.setContextMenu(contextMenu);

                title.getStyleClass().add(Styles.TITLE_3);
                location.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_BOLD);
                startDateTime.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_NORMAL);
                endDateTime.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_NORMAL);
                card.getStyleClass().addAll(Styles.ELEVATED_4, StyleConfig.EVENT_CARD);

                card.setMinWidth(CARD_WIDTH);
                card.setMinHeight(CARD_HEIGHT);

                card.setMaxWidth(CARD_WIDTH);
                card.setMaxHeight(CARD_HEIGHT);

                card.setPrefWidth(CARD_WIDTH);
                card.setPrefHeight(CARD_HEIGHT);

                imageView = Images.topRoundImage(CARRD_EVENT_IMAGE_WIDTH, CARD_EVENT_IMAGE_HEIGHT, 5);
                imageView.setFitWidth(CARRD_EVENT_IMAGE_WIDTH);
                imageView.setFitHeight(CARD_EVENT_IMAGE_HEIGHT);

                ticketsSold.getStyleClass().addAll(Styles.TEXT_SUBTLE, Styles.TEXT_BOLD);

                footer.setLeft(profileImages);
                footer.setRight(new StackPane(ticketsSold));
                StackPane.setAlignment(ticketsSold, Pos.BOTTOM_RIGHT);


                card.setSubHeader(imageView);
                card.setBody(content);
                card.setFooter(footer);
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.imageProperty().bind(item.image());

                    loadImagePattern(circle1, getProfileImage("profile1.jpg"));
                    loadImagePattern(circle2, getProfileImage("profile4.jpg"));
                    loadImagePattern(circle3, getProfileImage("profile8.jpeg"));

                    title.textProperty().bind(item.title());
                    location.textProperty().bind(item.location());
                    startDateTime.textProperty().bind(dateTimeBinding(item.startDate(), item.startTime(), "Starts", formatter));
                    endDateTime.textProperty().bind(dateTimeBinding(item.endDate(), item.endTime(), "Ends", formatter));

                    card.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            ViewHandler.changeView(ViewType.SHOW_EVENT, item);
                        }
                    });

                    editItem.setOnAction(e -> {
                        ViewHandler.changeView(ViewType.EDIT_EVENT, item);
                    });


                    deleteItem.setOnAction(e -> Alerts.confirmDeleteEvent(
                            item,
                            eventModel -> controller.onDeleteEvent(() -> {}, item))
                    );
                    setGraphic(card);
                }
            }
        };
    }
}
