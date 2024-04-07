package event.tickets.easv.bar.gui.component.users;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.BindingsUtils;
import event.tickets.easv.bar.gui.util.Listeners;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class UsersView implements View {
    private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");
    private final DeleteUserController controller;
    private final ObservableList<UserModel> model;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;

    public UsersView(ObservableList<UserModel> model, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.controller = new DeleteUserController();
        this.model = model;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
    }

    @Override
    public Region getView() {
        var placeholder = new Label("No users found");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        var top = topBar();
        top.setPadding(new Insets(0, 0, 0, StyleConfig.STANDARD_SPACING));


        var userList = new ListView<UserModel>();
        userList.setItems(model);
        userList.setPlaceholder(placeholder);
        userList.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        userList.setCellFactory(c -> {
            var cell = userCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });

        Listeners.addOnceChangeListener(eventsUsersSynchronized, userList::refresh);

        VBox.setVgrow(userList, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, top, userList);
    }

    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 3 ,0 ,StyleConfig.STANDARD_SPACING));

        var search = new CustomTextField();
        search.setPromptText("Search");
        search.setLeft(new FontIcon(Feather.SEARCH));
        search.setPrefWidth(250);

        var addUser = new Button("", new FontIcon(Feather.USER_PLUS));
        addUser.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4, StyleConfig.ACTIONABLE);
        addUser.setOnAction(e -> ViewHandler.changeView(ViewType.CREATE_USER));


        top.getChildren().addAll(search, new Spacer(), addUser);
        return top;
    }


    private ListCell<UserModel> userCell() {
        return new ListCell<>() {

            private final CircularImageView profileImage = new CircularImageView(32);;

            private final HBox userContainer = new HBox(StyleConfig.STANDARD_SPACING);
            private final VBox userContainerText = new VBox(StyleConfig.STANDARD_SPACING);
            private final Label userFullNameLabel = new Label();
            private final Label userRankLabel = new Label();

            private final VBox numEventsContainer = new VBox(StyleConfig.STANDARD_SPACING);
            private final Label numEventsLabel = new Label();
            private final Label numEventsDescription = new Label("Events");

            private final VBox numFinishedEventsContainer = new VBox(StyleConfig.STANDARD_SPACING);
            private final Label numFinishedEventsLabel = new Label();
            private final Label numFinishedEventsDescription = new Label("Finished events");

            private final VBox numTicketsSoldContainer = new VBox(StyleConfig.STANDARD_SPACING);
            private final Label numTicketsSoldLabel = new Label();
            private final Label numTicketsSoldDescription = new Label("Tickets sold");

            private final GridPane gridPane = new GridPane();

            private final Region spacer = new Region();

            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem editItem = MenuItems.createItem("_Edit", Feather.EDIT);
            private final MenuItem deleteItem = MenuItems.createItem("_Delete", Feather.TRASH_2);

            {


                editItem.setMnemonicParsing(true);
                deleteItem.setMnemonicParsing(true);

                contextMenu.getItems().addAll(editItem, deleteItem);
                setContextMenu(contextMenu);

                spacer.setPrefHeight(StyleConfig.STANDARD_SPACING);
                spacer.setMinHeight(StyleConfig.STANDARD_SPACING);
                spacer.setMaxHeight(StyleConfig.STANDARD_SPACING);


                userContainer.getChildren().addAll(profileImage.get(), userContainerText);
                userContainer.setPadding(new Insets(StyleConfig.STANDARD_SPACING));
                userContainerText.getChildren().addAll(userFullNameLabel, userRankLabel);
                numEventsContainer.getChildren().addAll(numEventsLabel, numEventsDescription);
                numFinishedEventsContainer.getChildren().addAll(numFinishedEventsLabel, numFinishedEventsDescription);
                numTicketsSoldContainer.getChildren().addAll(numTicketsSoldLabel, numTicketsSoldDescription);

                userContainer.setAlignment(Pos.CENTER_LEFT);
                userContainerText.setAlignment(Pos.CENTER_LEFT);
                numEventsContainer.setAlignment(Pos.CENTER_LEFT);
                numFinishedEventsContainer.setAlignment(Pos.CENTER_LEFT);
                numTicketsSoldContainer.setAlignment(Pos.CENTER_LEFT);

                userFullNameLabel.getStyleClass().add(Styles.TEXT_BOLD);
                numEventsLabel.getStyleClass().add(Styles.TEXT_BOLD);
                numFinishedEventsLabel.getStyleClass().add(Styles.TEXT_BOLD);
                numTicketsSoldLabel.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.ACCENT);

                userRankLabel.getStyleClass().add(Styles.TEXT_MUTED);
                numEventsDescription.getStyleClass().add(Styles.TEXT_MUTED);
                numFinishedEventsDescription.getStyleClass().add(Styles.TEXT_MUTED);
                numTicketsSoldDescription.getStyleClass().add(Styles.TEXT_MUTED);

                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(35);
                double width = (100 - column1.getPercentWidth()) / 3;

                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(width);
                ColumnConstraints column3 = new ColumnConstraints();
                column3.setPercentWidth(width);
                ColumnConstraints column4 = new ColumnConstraints();
                column4.setPercentWidth(width);

                gridPane.getColumnConstraints().addAll(column1, column2, column3, column4);

                gridPane.add(userContainer, 0, 0);
                gridPane.add(numEventsContainer, 1, 0);
                gridPane.add(numFinishedEventsContainer, 2, 0);
                gridPane.add(numTicketsSoldContainer, 3, 0);

                gridPane.setMouseTransparent(true);
                gridPane.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, "list-cell-grid");

                hoverProperty().addListener((obs, ov, nv) -> gridPane.pseudoClassStateChanged(HOVER_PSEUDO_CLASS, nv));

                spacer.getStyleClass().add("list-cell-spacer");
                spacer.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
            }


            @Override
            protected void updateItem(UserModel item, boolean empty) {
                if (item == getItem() && empty == isEmpty()) return;

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
//                    profileImage.setImage(EventsView.getProfileImage(item.id().get() + "/" + item.imageName().get()));
                    profileImage.imageProperty().bind(item.image());
                    profileImage.textProperty().bind(BindingsUtils.initialize(item.firstName(), item.lastName()));


                    userFullNameLabel.textProperty().bind(Bindings.concat(item.firstName(), " ", item.lastName()));

                    userRankLabel.textProperty().bind(Bindings.createObjectBinding(
                            () -> switch (item.rank().get()) {
                                    case ADMIN -> "Admin";
                                    case EVENT_COORDINATOR -> "Event coordinator";
                                }, item.rank()));


                    numEventsLabel.textProperty().bind(Bindings.size(item.events()).asString());
                    numFinishedEventsLabel.setText("375");
                    numTicketsSoldLabel.setText("283");

                    var wrapper = new VBox(gridPane, spacer);
                    wrapper.getStyleClass().add(StyleConfig.ACTIONABLE);

                    wrapper.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            ViewHandler.changeView(ViewType.SHOW_USER, item);
                        }
                    });

                    editItem.setOnAction(e -> ViewHandler.changeView(ViewType.EDIT_USER, item));

                    deleteItem.setOnAction(e -> Alerts.confirmDeleteUser(
                            item,
                            userModel -> controller.onDeleteUser(() -> {}, item))
                    );

                    setGraphic(wrapper);
                }
            }
        };
    }
}
