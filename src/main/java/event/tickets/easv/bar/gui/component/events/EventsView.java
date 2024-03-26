package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Card;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.widgets.Images;
import event.tickets.easv.bar.util.AppConfig;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.nio.file.Path;
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

        gridview.setCellWidth(336);
        gridview.setCellHeight(370);

        gridview.setCellFactory(cell -> eventCell());

/*
        Card c = new Card();
        c.setPrefWidth(326);
        c.setPrefHeight(370);
*/
/*        Image img = new Image("file:/../data/event_images/sample.png", true);
        ImageView imgView = new ImageView();
        img.progressProperty().addListener((obs, ov, nv) -> {
            if (nv.doubleValue() == 1.0) {
                Platform.runLater(() -> {
                    imgView.setImage(img);
                    imgView.setFitWidth(326);
                    imgView.setFitHeight(160);
                    imgView.setPreserveRatio(true);
                    c.setHeader(imgView);
                });
            }
        });*//*


        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String absolutePath = s + "/data/event_images/sample.png";

        System.out.println("currentRelativePath: " + currentRelativePath);
        System.out.println("s: " + s);
        System.out.println("absolutePath: " + absolutePath);

        var img = new Image("file:///" + absolutePath.replace("\\", "/"), true);
        var imgView = new ImageView(img);
        c.setHeader(Images);

        img.exceptionProperty().addListener((observable, oldValue, exception) -> {
            if (exception != null) {
                System.out.println("FEJL: " + exception.getMessage());
            }
        });
*/


        return gridview;
    }


    private GridCell<EventModel> eventCell() {
        return new GridCell<>() {
            private final Card card = new Card();
            private final ImageView imageView = new ImageView();
            private final Label title = new Label();

            {
                card.setMinWidth(336);
                card.setMinHeight(370);
                card.setMaxWidth(336);
                card.setMaxHeight(370);

                imageView.setFitWidth(314);
                imageView.setFitHeight(160);

                card.setHeader(imageView);
                card.setSubHeader(title);
            }

            @Override
            protected void updateItem(EventModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Image img = getImage(item.imageName().get()); // skal test hvis observable ikke bliver udfyldt med det samme.
                    imageView.setImage(img);


                    title.textProperty().bind(item.imageName());
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
