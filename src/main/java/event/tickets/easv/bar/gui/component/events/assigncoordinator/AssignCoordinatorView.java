package event.tickets.easv.bar.gui.component.events.assigncoordinator;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Images;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

public class AssignCoordinatorView implements View {
    private final EventModel model;
    private final ObservableList<UserModel> models;

    public AssignCoordinatorView(EventModel model, ObservableList<UserModel> models) {
        this.model = model;
        this.models = models;
    }

    @Override
    public Region getView() {
        var searchField = new CustomTextField();
        searchField.setPromptText("By name or mail");
        searchField.setLeft(new FontIcon(Material2MZ.SEARCH));
        VBox.setVgrow(searchField, Priority.NEVER);

        var placeholder = new Label("Your search results will appear here");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        var resultList = new ListView<UserModel>();
        resultList.setItems(models);
        resultList.setPlaceholder(placeholder);
        resultList.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        resultList.setCellFactory(cell -> resultCell());
        VBox.setVgrow(resultList, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, searchField, resultList);
    }

    private ListCell<UserModel> resultCell() {
        return new ListCell<>() {
            private final Tile tile = new Tile();
            private StackPane imgageContainer;
            private final ImageView imgView;
            private StackPane placeholder;

            {
                imgView = Images.circle(32);
                setPadding(new Insets(4, 0, 4, 0));
            }

            @Override
            protected void updateItem(UserModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {

                    imgView.setImage(EventsView.getProfileImage(item.id().get() + "/" + item.imageName().get()));

                    String firstName = item.firstName().get();
                    String lastName = item.lastName().get();
                    String initials;
                    if (lastName == null || lastName.isEmpty()) {
                        initials = firstName.length() > 1 ? firstName.substring(0, 2) : firstName;
                    } else {
                        initials = firstName.substring(0, 1) + lastName.substring(0, 1);
                    }

                    placeholder = circlePlaceholder(initials.toUpperCase(), 32);
                    imgageContainer = new StackPane(placeholder, imgView);
                    var btn = new Button(null, new FontIcon(Material2OutlinedAL.ADD));
                    btn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

                    tile.setGraphic(imgageContainer);
                    tile.setDescription(item.mail().get());
                    tile.setTitle(firstName + " " + lastName);
                    tile.setAction(btn);
                    setGraphic(tile);
                }
            }
        };
    }

    public static StackPane circlePlaceholder(String text, int radius) {
        var circle = new Circle(radius);
        circle.setFill(Color.LIGHTGRAY);

        var label = new Label(text);
        label.getStyleClass().add(Styles.TEXT_BOLD);

        return new StackPane(circle, label);
    }
}
