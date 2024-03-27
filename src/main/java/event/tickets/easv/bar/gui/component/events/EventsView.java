package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.util.AppConfig;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EventsView implements View {
    private final ObservableList<EventModel> model;

    public EventsView(ObservableList<EventModel> model) {
        this.model = model;
    }

    @Override
    public Region getView() {
        var btn = new Button("events");
        btn.setOnAction(e -> ViewHandler.changeView(ViewType.SHOW_EVENT));

        var gridview = new GridView<EventModel>();

        gridview.setItems(model);

        gridview.setCellWidth(326);
        gridview.setCellHeight(370);

        gridview.setCellFactory(cell -> eventCell());

        return gridview;
    }


    private GridCell<EventModel> eventCell() {
        return new GridCell<>() {
            private final Card card = new Card();
            private final ImageView imageView = new ImageView();
            private final Label title = new Label();

            {
                title.getStyleClass().add(Styles.TITLE_3);

                card.getStyleClass().add(StyleConfig.EVENT_CARD);

                card.setMinWidth(326);
                card.setMinHeight(370);
                card.setMaxWidth(326);
                card.setMaxHeight(370);
                card.setPrefHeight(370);
                card.setPrefWidth(326);

                imageView.setFitWidth(325);
                imageView.setFitHeight(160);
                StackPane.setAlignment(imageView, Pos.TOP_CENTER);
//                imageView.setPreserveRatio(true);

                card.setHeader(imageView);

                card.setSubHeader(title);
//                card.setSubHeader(title);
                card.setBody(new Label("asdasd"));
                card.setFooter(new Label("283 tickets sold"));
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image img = getImage(item.imageName().get()); // skal test hvis observable ikke bliver udfyldt med det samme.

                    img.exceptionProperty().addListener((obs, ov, nv) -> {
                        System.out.println("fejl: " + nv);
                    });
                    imageView.setImage(img);

                    title.textProperty().bind(item.title());
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

}
