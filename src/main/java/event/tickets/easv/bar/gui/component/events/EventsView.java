package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Buttons;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.util.AppConfig;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.kordamp.ikonli.material2.Material2AL;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventsView implements View {
    private static final int CARD_WIDTH = 326;
    private static final int CARD_HEIGHT = 370;
    private static final int PROFILE_IMG_RADIUS = 20;
    private static final int CARRD_EVENT_IMAGE_WIDTH = CARD_WIDTH - 2; // needs to account for a bit on each side
    private static final int CARD_EVENT_IMAGE_HEIGHT = 160;

    private final ObservableList<EventModel> model;
    private final BooleanProperty fetchingData;

    public EventsView(ObservableList<EventModel> model, BooleanProperty fetchingData) {
        this.model = model;
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {

        var spacer = new Region();
        Button add = Buttons.actionIconButton(Material2AL.ADD, e -> ViewHandler.changeView(ViewType.CREATE_EVENT), StyleConfig.ACTIONABLE);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        var header = new HBox(add, spacer);
        header.setAlignment(Pos.TOP_RIGHT);
        header.setPadding(new Insets(0, 10, 10, 10));
        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        var gridview = new GridView<EventModel>();
        gridview.setItems(model);
        gridview.setCellWidth(CARD_WIDTH);
        gridview.setCellHeight(CARD_HEIGHT);
        gridview.setCellFactory(cell -> eventCell());
        gridview.setHorizontalCellSpacing(10);
        gridview.setVerticalCellSpacing(10);
        var content = new StackPane(gridview, progressIndicator);

        gridview.setPadding(new Insets(0, 10, 10, 0));
        var results = new VBox(header, content);
        return results;
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

            {
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

                circle1.getStyleClass().add(Styles.ELEVATED_3);
                circle2.getStyleClass().add(Styles.ELEVATED_3);
                circle3.getStyleClass().add(Styles.ELEVATED_3);

                footer.setLeft(profileImages);
                footer.setRight(new StackPane(ticketsSold));
                StackPane.setAlignment(ticketsSold, Pos.BOTTOM_RIGHT);


                card.setSubHeader(imageView);
                card.setBody(content);
                card.setFooter(footer);
                System.out.println(card.getPadding());
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image img = getImage(item.imageName().get());

                    imageView.setImage(img);

                    loadImagePattern(circle1, getProfileImage("profile1.jpg"));
                    loadImagePattern(circle2, getProfileImage("profile4.jpg"));
                    loadImagePattern(circle3, getProfileImage("profile8.jpeg"));

                    title.textProperty().bind(item.title());
                    location.textProperty().bind(item.location());
                    startDateTime.textProperty().bind(dateTimeBinding(item.startDate(), item.startTime(), "Starts", formatter));
                    endDateTime.textProperty().bind(dateTimeBinding(item.endDate(), item.endTime(), "Ends", formatter));

                    card.setOnMouseClicked(e -> {
                        ViewHandler.changeView(ViewType.SHOW_EVENT, item);
                    });

                    setGraphic(card);
                }
            }
        };
    }

    private static final Map<String, Image> cache = new HashMap<>();
    private static final Map<String, Image> profileCache = new HashMap<>();

    public static Image getImage(String imagePath) {
        Image img = cache.get(imagePath);
        if (img == null) {
            String absolutePath = "file:///" + Paths.get("").toAbsolutePath() + AppConfig.EVENT_IMAGES_DIR + imagePath.replace("\\", "/");
            img = new Image(absolutePath, true);
            cache.put(imagePath, img);
        }

        return img;
    }

    public static Image getProfileImage(String imagePath) {
        Image img = profileCache.get(imagePath);
        if (img == null) {
            String absolutePath = "file:///" + Paths.get("").toAbsolutePath() + AppConfig.PROFILE_IMAGES_DIR + imagePath.replace("\\", "/");
            img = new Image(absolutePath, true);
            profileCache.put(imagePath, img);
        }

        return img;
    }

    public static void loadImagePattern(Circle circle, Image image) {
        if (image.getProgress() == 1.0) {
            circle.setFill(new ImagePattern(image));
            return;
        }

        image.progressProperty().addListener((obs, ov, nv) -> {
            if (nv.doubleValue() == 1.0) circle.setFill(new ImagePattern(image));
        });
    }

    public static StringBinding dateTimeBinding(ObjectProperty<LocalDate> dateProperty,
                                                ObjectProperty<LocalTime> timeProperty,
                                                String prefix,
                                                DateTimeFormatter formatter) {
        return Bindings.createStringBinding(() -> {
            LocalDate date = dateProperty.get();
            LocalTime time = timeProperty.get();
            if (date != null && time != null) {
                return prefix + " " + LocalDateTime.of(date, time).format(formatter);
            } else {
                return "";
            }
        }, dateProperty, timeProperty);
    }
}
