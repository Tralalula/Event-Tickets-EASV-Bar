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
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AssignCoordinatorView implements View {
    private final AssignCoordinatorModel model;
    private final AssignCoordinatorController controller;
    private final FilteredList<UserModel> filteredUserModels;
    private final EventModel currentEventModel;
    private Predicate<UserModel> excludeAssociatedUsers;

    public AssignCoordinatorView(EventModel currentEventModel, ObservableList<UserModel> masterUserList) {
        this.model = new AssignCoordinatorModel();
        this.currentEventModel = currentEventModel;
        this.controller = new AssignCoordinatorController(model, this.currentEventModel);

        initializeExcludePredicate();
        this.filteredUserModels = new FilteredList<>(masterUserList, excludeAssociatedUsers);

        setupSearchFilter();
    }

    private void initializeExcludePredicate() {
        excludeAssociatedUsers = userModel -> {
            Set<Integer> associatedUserIds = currentEventModel.users().stream()
                                                                      .map(user -> user.id().get())
                                                                      .collect(Collectors.toSet());
            return !associatedUserIds.contains(userModel.id().get());
        };
    }

    private void setupSearchFilter() {
        model.searchProperty().addListener((obs, ov, nv) -> {
            Predicate<UserModel> searchPredicate = userModel -> {
                if (nv == null || nv.isEmpty()) {
                    return true;
                }

                var lowercase = nv.toLowerCase();
                return userModel.firstName().get().toLowerCase().contains(lowercase) ||
                       userModel.lastName().get().toLowerCase().contains(lowercase) ||
                       userModel.mail().get().toLowerCase().contains(lowercase);
            };

            filteredUserModels.setPredicate(excludeAssociatedUsers.and(searchPredicate));
        });
    }

    @Override
    public Region getView() {
        var searchField = new CustomTextField();
        searchField.setPromptText("By name or mail");
        searchField.setLeft(new FontIcon(Material2MZ.SEARCH));
        searchField.textProperty().bindBidirectional(model.searchProperty());
        VBox.setVgrow(searchField, Priority.NEVER);

        var placeholder = new Label("Your search results will appear here");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        var resultList = new ListView<UserModel>();
        resultList.setItems(filteredUserModels);
        resultList.setPlaceholder(placeholder);
        resultList.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        resultList.setCellFactory(cell -> resultCell());
        VBox.setVgrow(resultList, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, searchField, resultList);
    }

    private ListCell<UserModel> resultCell() {
        return new ListCell<>() {
            private final Tile tile = new Tile();
            private final Button assignBtn = new Button(null);
            private final CircularImageView photo;

            {
                photo = new CircularImageView(32);
                setPadding(new Insets(4, 0, 4, 0));
                assignBtn.setGraphic(new FontIcon(Material2OutlinedAL.ADD));
                assignBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);
            }

            @Override
            protected void updateItem(UserModel item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {

                    photo.setImage(EventsView.getProfileImage(item.id().get() + "/" + item.imageName().get()));

                    String firstName = item.firstName().get();
                    String lastName = item.lastName().get();
                    String initials;
                    if (lastName == null || lastName.isEmpty()) {
                        initials = firstName.length() > 1 ? firstName.substring(0, 2) : firstName;
                    } else {
                        initials = firstName.substring(0, 1) + lastName.substring(0, 1);
                    }

                    photo.setText(initials.toUpperCase());

                    BooleanProperty selectedProperty = model.selectionStateProperty(item);
                    assignBtn.graphicProperty().bind(Bindings.when(selectedProperty)
                                                       .then(new FontIcon(Material2OutlinedAL.CHECK))
                                                       .otherwise(new FontIcon(Material2OutlinedAL.ADD)));
                    assignBtn.disableProperty().bind(model.okToAssignProperty().not());
                    assignBtn.setOnAction(e -> {
                        assignBtn.disableProperty().unbind();
                        assignBtn.setDisable(true);
                        selectedProperty.set(true);
                        controller.onAssignCoordinator(() -> assignBtn.disableProperty().bind(model.okToAssignProperty().not()), item);
                    });

                    tile.setGraphic(photo.get());
                    tile.setDescription(item.mail().get());
                    tile.setTitle(firstName + " " + lastName);
                    tile.setAction(assignBtn);
                    setGraphic(tile);
                }
            }
        };
    }
}
