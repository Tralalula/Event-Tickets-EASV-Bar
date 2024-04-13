package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.enums.Rank;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.events.assigncoordinator.AssignCoordinatorView;
import event.tickets.easv.bar.gui.component.tickets.TicketEventModel;
import event.tickets.easv.bar.gui.util.*;
import event.tickets.easv.bar.gui.widgets.*;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ShowEventView implements View {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd. MMMM HH:mm", Locale.ENGLISH);
    private final DeleteEventController controller;

    private final EventModel eventModelToShow = EventModel.Empty();
    private final ObservableList<EventModel> masterEventList;
    private final ObservableList<UserModel> masterUserList;
    private final ObservableList<TicketModel> masterTicketList;
    private final ImageView image;
    private final HBox coordinators;
    private final ListView<TicketEventModel> ticketsListView;

    public ShowEventView(ObservableList<EventModel> masterEventList, ObservableList<UserModel> masterUserList, ObservableList<TicketModel> masterTicketList, BooleanProperty eventsTicketsSynchronized) {
        this.controller = new DeleteEventController();
        this.masterEventList = masterEventList;
        this.masterUserList = masterUserList;
        this.masterTicketList = masterTicketList;
        this.coordinators = new HBox(StyleConfig.STANDARD_SPACING * 8);
        this.ticketsListView = new ListView<>();

        image = Images.round(400, 250, 8, 8, 8, 8);
        image.setPreserveRatio(true);
        image.setPickOnBounds(true);
        image.setFitWidth(400);
        image.setFitHeight(300);

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof EventModel) {
                eventModelToShow.update((EventModel) newData);
                image.imageProperty().bind(eventModelToShow.image());

                updateCoordinatorsView(eventModelToShow.users());

                eventModelToShow.users().addListener((ListChangeListener<? super UserModel>) c -> {
                    updateCoordinatorsView(eventModelToShow.users());
                });

                ticketsListView.setItems(eventModelToShow.tickets());
            }
        });
    }

    private TicketModel findTicketById(int id) {
        for (TicketModel ticketModel : masterTicketList) {
            if (ticketModel.id().get() == id) {
                return ticketModel;
            }
        }

        return TicketModel.Empty();
    }

    private EventModel findModelById(int id) {
        for (EventModel eventModel : masterEventList) {
            if (eventModel.id().get() == id) {
                return eventModel;
            }
        }

        return EventModel.Empty();
    }


    private ListView<TicketEventModel> ticketView() {
        var placeholder = new Label("No tickets found");
        ticketsListView.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        ticketsListView.setPlaceholder(placeholder);
        ticketsListView.setCellFactory(c -> {
            var cell = ticketCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });

        return ticketsListView;
    }

    private void updateCoordinatorsView(ObservableList<UserModel> users) {
        coordinators.getChildren().clear();

        for (UserModel userModel : users) {
            var photo = new CircularImageView(24);
            photo.textProperty().bind(BindingsUtils.initialize(userModel.firstName(), userModel.lastName()));
            photo.imageProperty().bind(userModel.image());

            var contextMenu = new ContextMenu();
            var editItem = MenuItems.createItem("_Remove", Feather.TRASH_2);

            editItem.setOnAction(e -> controller.onRemoveCoordinator(() -> {}, eventModelToShow, userModel));

            contextMenu.getItems().addAll(editItem);

            photo.get().setOnContextMenuRequested(e -> contextMenu.show(photo.get(), e.getScreenX(), e.getScreenY()));

            var name = Labels.styledLabel(Bindings.concat(userModel.firstName(), " ", userModel.lastName()), Styles.TEXT_NORMAL);
            var box = new VBox(StyleConfig.STANDARD_SPACING, photo.get(), name);
            box.setAlignment(Pos.CENTER);

            coordinators.getChildren().add(box);
        }
    }


    @Override
    public Region getView() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 4);
        results.setPadding(new Insets(10));
        results.setFillWidth(true);




        var coordinatorsText = Labels.styledLabel("Event coordinators", Styles.TITLE_3);
        var spacer = new Region();
        var add = Buttons.actionIconButton(Material2AL.ADD, e -> ViewHandler.showOverlay("Add coordinator", new AssignCoordinatorView(findModelById(eventModelToShow.id().get()), masterUserList).getView(), 600, 540), StyleConfig.ACTIONABLE);
        var box = new HBox(coordinatorsText, spacer, add);
        var coordinatorsBox = new VBox(box, coordinators);


        var ticketsText = Labels.styledLabel("Tickets", Styles.TITLE_3);
        var ticketsBox = new VBox(ticketsText, ticketView());

//        NodeUtils.bindVisibility(coordinatorsBox, Bindings.isEmpty(coordinators.getChildren()).not());

        results.getChildren().addAll(top(), coordinatorsBox, ticketsBox);

        return results;
    }

    private Node top() {
        var results = new HBox(StyleConfig.STANDARD_SPACING * 2);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);

        results.getChildren().addAll(image, eventInfo(), new Spacer(), actionIcons());
        return results;
    }

    private Node actionIcons() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);

        var deleteBtn = new Button(null, new FontIcon(Material2AL.DELETE));
        deleteBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        var editBtn = new Button(null, new FontIcon(Material2AL.EDIT));
        editBtn.getStyleClass().addAll(Styles.BUTTON_CIRCLE, StyleConfig.ACTIONABLE, Styles.FLAT);

        NodeUtils.bindVisibility(editBtn, SessionManager.getInstance().getUserModel().rank().isEqualTo(Rank.EVENT_COORDINATOR));

        deleteBtn.setOnAction(e -> Alerts.confirmDeleteEvent(
                eventModelToShow,
                eventModel -> controller.onDeleteEvent(ViewHandler::previousView, eventModelToShow))
        );

        editBtn.setOnAction(e -> ViewHandler.changeView(ViewType.EDIT_EVENT, eventModelToShow));

        results.getChildren().addAll(editBtn, deleteBtn);

        return results;
    }

    private Node eventInfo() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        var title = Labels.styledLabel(eventModelToShow.title(), Styles.TITLE_1);

        var location = Labels.styledLabel(eventModelToShow.location(), Styles.TITLE_4);
        var startDateTime = Labels.styledLabel(BindingsUtils.dateTimeBinding(eventModelToShow.startDate(), eventModelToShow.startTime(), "Starts", formatter));
        var endDateTime = Labels.styledLabel(BindingsUtils.dateTimeBinding(eventModelToShow.endDate(), eventModelToShow.endTime(), "Ends", formatter));

        var locationGuidanceText = Labels.styledLabel("Location guidance", Styles.TEXT_BOLD);
        var locationGuidance = Labels.styledLabel(eventModelToShow.locationGuidance());
        var locationGuidanceBox = new VBox(locationGuidanceText, locationGuidance);

        var noteText = Labels.styledLabel("Note", Styles.TEXT_BOLD);
        var note = Labels.styledLabel(eventModelToShow.extraInfo());
        var noteBox = new VBox(noteText, note);

        NodeUtils.bindVisibility(locationGuidanceBox, eventModelToShow.locationGuidance().isNotEmpty());
        NodeUtils.bindVisibility(noteBox, eventModelToShow.extraInfo().isNotEmpty());

        results.getChildren().addAll(title, location, startDateTime, endDateTime, locationGuidanceBox, noteBox);
        return results;
    }

    private ListCell<TicketEventModel> ticketCell() {
        return new ListCell<>() {
            private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");
            private static final int CELL_PADDING = 10;
            private static final int CELL_HEIGHT = 50;

            private final Label titleLabel = new Label();
            private final Label typeLabel = new Label();
            private final Label totalLabel = new Label();
            private final Label leftLabel = new Label();
            private final Label priceLabel = new Label();

            private final GridPane gridPane = new GridPane();
            private final Region spacer = new Spacer();

            private final ContextMenu contextMenu = new ContextMenu();
            private final MenuItem editItem = MenuItems.createItem("_Edit", Feather.EDIT);
            private final MenuItem deleteItem = MenuItems.createItem("_Delete", Feather.TRASH_2);
            {
                editItem.setMnemonicParsing(true);
                deleteItem.setMnemonicParsing(true);

                contextMenu.getItems().addAll(editItem, deleteItem);
                setContextMenu(contextMenu);

                spacer.setPrefHeight(0);
                spacer.setMinHeight(0);
                spacer.setMaxHeight(0);

                spacer.setPadding(new Insets(0,CELL_PADDING * 2,0,CELL_PADDING * 2));
                titleLabel.setPadding(new Insets(0, 0, 0, CELL_PADDING));

                titleLabel.getStyleClass().add(Styles.TEXT_BOLD);
                typeLabel.getStyleClass().add(Styles.TEXT_MUTED);
                totalLabel.getStyleClass().add(Styles.TEXT_MUTED);
                leftLabel.getStyleClass().add(Styles.TEXT_MUTED);
                priceLabel.getStyleClass().add(Styles.TEXT_MUTED);

                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(35);
                double width = (100 - column1.getPercentWidth()) / 3;

                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(width);
                ColumnConstraints column3 = new ColumnConstraints();
                column3.setPercentWidth(width);
                ColumnConstraints column4 = new ColumnConstraints();
                column4.setPercentWidth(width);
                ColumnConstraints column5 = new ColumnConstraints();
                column5.setPercentWidth(width);
                ColumnConstraints column6 = new ColumnConstraints();
                column6.setPercentWidth(width);

                gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5, column6);

                gridPane.add(titleLabel, 0, 0);
                gridPane.add(typeLabel, 1, 0);
                gridPane.add(totalLabel, 2, 0);
                gridPane.add(leftLabel, 3, 0);
                gridPane.add(priceLabel, 4, 0);

                gridPane.setMinHeight(CELL_HEIGHT);
                gridPane.setAlignment(Pos.CENTER_LEFT);

                gridPane.setMouseTransparent(true);
                gridPane.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, "list-cell-grid");

                hoverProperty().addListener((obs, ov, nv) -> gridPane.pseudoClassStateChanged(HOVER_PSEUDO_CLASS, nv));

                spacer.getStyleClass().add("list-cell-spacer");
                spacer.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
            }

            @Override
            protected void updateItem(TicketEventModel item, boolean empty) {
                if (item == getItem() && empty == isEmpty()) return;

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {

                    TicketModel ticket = findTicketById(item.ticketId().get());

                    titleLabel.textProperty().bind(ticket.title());
                    typeLabel.textProperty().bind(ticket.type());
                    totalLabel.textProperty().bind(item.total().asString().concat(" total"));
                    leftLabel.textProperty().bind(item.left().asString().concat(" left"));
                    priceLabel.textProperty().bind(Bindings.concat("DKK ", item.price().asString(), ",-"));

                    gridPane.setPadding(new Insets(0, CELL_PADDING * 2, 0, CELL_PADDING * 2));

                    var assignButton = new Button(null, new FontIcon(Material2MZ.PERSON_ADD));
                    var editButton = new Button(null, new FontIcon(Feather.EDIT));
                    var deleteButton = new Button(null, new FontIcon(Feather.TRASH));

                    assignButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    assignButton.setOnAction(event -> {
//                        ViewHandler.showOverlay("Assign ticket", assignCustomer(item), 300, 350);
                    });

                    editButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    editButton.setOnAction(event -> {
                        //ViewHandler.changeView(ViewType.SHOW_TICKET, item);
                    });

                    deleteButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    deleteButton.setOnAction(event -> {
                        // intet endnu
                    });

                    HBox buttons = new HBox(assignButton, editButton, deleteButton);
                    buttons.setPadding(new Insets(0, CELL_PADDING, 0, 0));
                    buttons.setAlignment(Pos.CENTER_RIGHT);
                    buttons.setSpacing(20);

                    StackPane stackPane = new StackPane(gridPane, buttons);
                    stackPane.setAlignment(Pos.CENTER_LEFT);

                    var wrapper = new VBox(stackPane, spacer);

                    // Tilføj runde hjørner til første element
                    /*
                    if (getIndex() == 0) {
                        gridPane.getStyleClass().add("list-cell-grid-top");
                    }

                    // Tilføj runde hjørner til sidste element
                    if (getIndex() == getListView().getItems().size() - 1) {
                        gridPane.getStyleClass().add("list-cell-grid-bottom");
                    }*/

                    wrapper.getStyleClass().add(StyleConfig.ACTIONABLE);

                    wrapper.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            // ViewHandler.changeView(ViewType.SHOW_TICKET, item);
                        }
                    });


                    editItem.setOnAction(e -> System.out.println("Edit ticket: " + item.id().get()));
                    // deleteItem.setOnAction(e -> Alerts.confirmDeleteUser(
                    //         item,
                    //         userModel -> controller.onDeleteUser(() -> {}, item))
                    //);

                    setGraphic(wrapper);
                }
            }
        };
    }
}
