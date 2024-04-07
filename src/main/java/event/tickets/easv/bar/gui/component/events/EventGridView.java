package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.util.*;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
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
    private final BooleanProperty eventsUsersSynchronized;

    public EventGridView(ObservableList<EventModel> model, BooleanProperty eventsUsersSynchronized) {
        this.controller = new DeleteEventController();
        this.model = model;
        this.gridview = new GridView<>();
        this.eventsUsersSynchronized = eventsUsersSynchronized;

        Listeners.addOnceChangeListener(eventsUsersSynchronized, () -> setItems(model));
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

            private final CircularImageView photo1 = new CircularImageView(PROFILE_IMG_RADIUS);
            private final CircularImageView photo2 = new CircularImageView(PROFILE_IMG_RADIUS);
            private final CircularImageView photo3 = new CircularImageView(PROFILE_IMG_RADIUS);
            private final HBox profileImages = new HBox(StyleConfig.STANDARD_SPACING, photo1.get(), photo2.get(), photo3.get());

            private final Label ticketsSold = new Label("283 tickets sold");

            private final BorderPane footer = new BorderPane();

            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem editItem = MenuItems.createItem("_Edit", Feather.EDIT);
            private final MenuItem deleteItem = MenuItems.createItem("_Delete", Feather.TRASH_2);

            {
                photo1.get().getStyleClass().addAll(Styles.INTERACTIVE, StyleConfig.ACTIONABLE);
                photo2.get().getStyleClass().addAll(Styles.INTERACTIVE, StyleConfig.ACTIONABLE);
                photo3.get().getStyleClass().addAll(Styles.INTERACTIVE, StyleConfig.ACTIONABLE);


                editItem.setMnemonicParsing(true);
                deleteItem.setMnemonicParsing(true);

                contextMenu.getItems().addAll(editItem, deleteItem);
                card.setContextMenu(contextMenu);

                title.getStyleClass().add(Styles.TITLE_3);
                location.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_BOLD);
                startDateTime.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_NORMAL);
                endDateTime.getStyleClass().addAll(Styles.TEXT_MUTED, Styles.TEXT_NORMAL);
                card.getStyleClass().addAll(Styles.ELEVATED_4, StyleConfig.EVENT_CARD, StyleConfig.ACTIONABLE, Styles.INTERACTIVE);

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

                    updatePhotos(item, photo1, photo2, photo3);

                    item.users().addListener((ListChangeListener.Change<? extends UserModel> c) -> {
                        updatePhotos(item, photo1, photo2, photo3);
                    });

                    NodeUtils.bindVisibility(photo1.get(), Bindings.size(item.users()).greaterThan(0));
                    NodeUtils.bindVisibility(photo2.get(), Bindings.size(item.users()).greaterThan(1));
                    NodeUtils.bindVisibility(photo3.get(), Bindings.size(item.users()).greaterThan(2));


                    title.textProperty().bind(item.title());
                    location.textProperty().bind(item.location());
                    startDateTime.textProperty().bind(BindingsUtils.dateTimeBinding(item.startDate(), item.startTime(), "Starts", formatter));
                    endDateTime.textProperty().bind(BindingsUtils.dateTimeBinding(item.endDate(), item.endTime(), "Ends", formatter));

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

    private void updatePhotos(EventModel item, CircularImageView photo1, CircularImageView photo2, CircularImageView photo3) {
        for (int i = 0; i < item.users().size(); i++) {
            CircularImageView photo = switch (i) {
                case 0 -> photo1;
                case 1 -> photo2;
                case 2 -> photo3;
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };

            int finalI = i;
            photo.get().setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    ViewHandler.changeView(ViewType.SHOW_USER, item.users().get(finalI));
                    e.consume();
                }
            });

            photo.textProperty().bind(BindingsUtils.initialize(item.users().get(i).firstName(), item.users().get(i).lastName()));
            photo.imageProperty().bind(item.users().get(i).image());
        }
    }
}
