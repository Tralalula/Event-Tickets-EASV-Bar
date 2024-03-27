package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.util.AppConfig;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventsView implements View {
    private final ObservableList<EventModel> model;
    private final BooleanProperty fetchingData;

    public EventsView(ObservableList<EventModel> model, BooleanProperty fetchingData) {
        this.model = model;
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        var btn = new Button("events");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.SHOW_EVENT));

        var gridview = new GridView<EventModel>();

        gridview.setItems(model);

        gridview.setCellWidth(326);
        gridview.setCellHeight(370);

        gridview.setCellFactory(cell -> eventCell());

        System.out.println(model.size());




        return new StackPane(gridview, progressIndicator);
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
            private final VBox content = new VBox(title, startDateTime, endDateTime);


            {
                title.getStyleClass().add(Styles.TITLE_3);
                startDateTime.getStyleClass().add(Styles.TEXT_NORMAL);
                endDateTime.getStyleClass().add(Styles.TEXT_NORMAL);
                card.getStyleClass().add(StyleConfig.EVENT_CARD);
                card.getStyleClass().add(Styles.ELEVATED_4);

                card.setMinWidth(326);
                card.setMinHeight(370);
                card.setMaxWidth(326);
                card.setMaxHeight(370);
                card.setPrefHeight(370);
                card.setPrefWidth(326);

                imageView = Images.topRoundImage(324, 160, 5);
                imageView.setFitWidth(324);
                imageView.setFitHeight(160);


                card.setSubHeader(imageView);
                card.setBody(content);
                card.setFooter(new Label("283 tickets sold"));
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image img = getImage(item.imageName().get());

                    imageView.setImage(img);

                    title.textProperty().bind(item.title());

                    startDateTime.textProperty().bind(dateTimeBinding(item.startDate(), item.startTime(), "Starts", formatter));
                    endDateTime.textProperty().bind(dateTimeBinding(item.endDate(), item.endTime(), "Ends", formatter));

                    setGraphic(card);
                }
            }
        };
    }

    private static final Map<String, Image> cache = new HashMap<>();

    public static Image getImage(String imagePath) {
        Image img = cache.get(imagePath);
        if (img == null) {
            String absolutePath = "file:///" + Paths.get("").toAbsolutePath() + AppConfig.IMAGE_DIR + imagePath.replace("\\", "/");
            img = new Image(absolutePath, true);
            cache.put(imagePath, img);
        }

        return img;
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
