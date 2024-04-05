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

import java.nio.file.Files;
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

        var gridview = new EventGridView(model, fetchingData);
        var content = new StackPane(gridview.getView(), progressIndicator);

        return new VBox(header, content);
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
        System.out.println("path: " + imagePath);
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
