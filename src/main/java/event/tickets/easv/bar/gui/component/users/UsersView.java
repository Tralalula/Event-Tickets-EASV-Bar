package event.tickets.easv.bar.gui.component.users;

import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

public class UsersView implements View {
    private final ObservableList<UserModel> model;
    private final BooleanProperty fetchingData;

    public UsersView(ObservableList<UserModel> model, BooleanProperty fetchingData) {
        this.model = model;
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {
        var placeholder = new Label("No users found");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        var userList = new ListView<UserModel>();
        userList.setItems(model);
        userList.setPlaceholder(placeholder);
        userList.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        userList.setCellFactory(c -> {
            var cell = userCell();
            cell.getStyleClass().add("bg-subtle-list"); // eh, added own in style.css because AtlantaFX didn't apply?
            cell.setOnMouseClicked(e -> {
                var user = cell.getItem();
                System.out.println("User clicked: " + user.firstName().get() + " " + user.lastName().get());
            });
//
////            cell.setPadding(new Insets(StyleConfig.STANDARD_SPACING));
            return cell;
        });

        VBox.setVgrow(userList, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, userList);
    }

    private ListCell<UserModel> userCell() {
        return new ListCell<>() {
            private final Tile userContainer = new Tile();
            private final CircularImageView profileImage;
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

            final Region spacer = new Region();

            {
                spacer.setPrefHeight(10);
                spacer.setMinHeight(10);
                spacer.setMaxHeight(10);

                profileImage = new CircularImageView(32);

                numEventsContainer.getChildren().addAll(numEventsLabel, numEventsDescription);
                numFinishedEventsContainer.getChildren().addAll(numFinishedEventsLabel, numFinishedEventsDescription);
                numTicketsSoldContainer.getChildren().addAll(numTicketsSoldLabel, numTicketsSoldDescription);

                numEventsContainer.setAlignment(Pos.CENTER_LEFT);
                numFinishedEventsContainer.setAlignment(Pos.CENTER_LEFT);
                numTicketsSoldContainer.setAlignment(Pos.CENTER_LEFT);

                numEventsLabel.getStyleClass().add(Styles.TEXT_BOLD);
                numFinishedEventsLabel.getStyleClass().add(Styles.TEXT_BOLD);
                numTicketsSoldLabel.getStyleClass().addAll(Styles.TEXT_BOLD, Styles.ACCENT);

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
                    profileImage.setImage(EventsView.getProfileImage(item.id().get() + "/" + item.imageName().get()));

                    String firstName = item.firstName().get();
                    String lastName = item.lastName().get();
                    String initials;
                    if (lastName == null || lastName.isEmpty()) {
                        initials = firstName.length() > 1 ? firstName.substring(0, 2) : firstName;
                    } else {
                        initials = firstName.substring(0, 1) + lastName.substring(0, 1);
                    }

                    profileImage.setText(initials.toUpperCase());

                    userContainer.setGraphic(profileImage.get());
                    userContainer.titleProperty().bind(Bindings.concat(item.firstName(), " ", item.lastName()));
                    userContainer.getStyleClass().add(Styles.TEXT_BOLD);

                    userContainer.descriptionProperty().bind(Bindings.createObjectBinding(
                            () -> switch (item.rank().get()) {
                                    case ADMIN -> "Admin";
                                    case EVENT_COORDINATOR -> "Event coordinator";
                                }, item.rank()));

                    numEventsLabel.textProperty().bind(Bindings.size(item.events()).asString());
                    numFinishedEventsLabel.setText("375");
                    numTicketsSoldLabel.setText("283");



                    var wrapper = new VBox(gridPane, spacer);
                    gridPane.setMouseTransparent(true);
                    gridPane.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, Styles.BG_DEFAULT);
                    wrapper.getStyleClass().add(StyleConfig.ACTIONABLE);

                    setGraphic(wrapper);
                }
            }
        };
    }
}
