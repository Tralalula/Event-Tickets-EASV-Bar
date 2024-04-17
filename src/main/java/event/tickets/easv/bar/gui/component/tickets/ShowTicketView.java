package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.MaskTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.DoubleStringConverter;
import atlantafx.base.util.IntegerStringConverter;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.Listeners;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ShowTicketView implements View {
    private TicketModel model = TicketModel.Empty();
    private final TicketsModel ticketsModel;
    private final MainModel main;

    private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");
    private static final int CELL_PADDING = 10;
    private static final int CELL_HEIGHT = 50;

    private final Label titleLabel = new Label();
    private final Label type = new Label();
    private final Label defaultPrice = new Label();
    private final Label eventsCount = new Label();

    private final BooleanProperty isPromotional = new SimpleBooleanProperty();

    private ListView<TicketEventModel> ticketEventList = new ListView<TicketEventModel>();
    private TableView<TicketEventModel> table;

    private final static int PREF_TEXTFIELD_WIDTH = 200;
    private CheckComboBox<EventModelWrapper> checkComboBox = new CheckComboBox<>();

    private BooleanProperty eventsTicketsSynchronizedProperty;
    private FilteredList<TicketEventModel> filteredTicketEventModels;
    private static final StringProperty search = new SimpleStringProperty("");


    public ShowTicketView(MainModel main, TicketsModel ticketsModel, BooleanProperty eventsTicketsSynchronizedProperty) {
        this.main = main;
        this.ticketsModel = ticketsModel;

        this.eventsTicketsSynchronizedProperty = eventsTicketsSynchronizedProperty;

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
                this.filteredTicketEventModels = new FilteredList<>(model.ticketEvents());
                ticketEventList.setItems(filteredTicketEventModels);
                isPromotional.set(model.type().get().equals("Promotional"));
                updateTexts();
                updateListCell();
            }
        });

        setupSearchFilter();

        // For at sørge for den lytter til events ændringer
        checkComboBox = multiCombo();
    }

    @Override
    public Region getView() {
        return topSection();
    }

    private void setupSearchFilter() {
        this.searchProperty().addListener((obs, ov, nv) -> {
            Predicate<TicketEventModel> searchPredicate = (TicketEventModel ticketEventModel) -> {
                if (nv == null || nv.isEmpty()) {
                    return true;
                }

                var lowercase = nv.toLowerCase();
                if (ticketEventModel.event().get() != null)
                    return ticketEventModel.event().get().title().get().toLowerCase().contains(lowercase);

                return false;
            };

            filteredTicketEventModels.setPredicate(searchPredicate);
        });
    }

    private void updateListCell() {
        ticketEventList.setCellFactory(c -> {
            var cell = ticketCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });
    }

    public VBox topSection() {
        var placeholder = new Label("No tickets found");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        ticketEventList.setItems(model.ticketEvents());
        ticketEventList.setPlaceholder(placeholder);
        ticketEventList.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        updateListCell();

        Listeners.addOnceChangeListener(eventsTicketsSynchronizedProperty, ticketEventList::refresh);


        StackPane stackPane = new StackPane(ticketEventList);
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, ticketDetails(), stackPane);
    }

    // Nogle ting vil bare ikke opdatere......................
    private void updateTexts() {
        eventsCount.textProperty().set(Integer.toString(model.ticketEvents().size()) + " events");
    }

    private VBox ticketDetails() {
        titleLabel.textProperty().bind(model.title());
        titleLabel.getStyleClass().add(Styles.TITLE_3);

        type.getStyleClass().add(Styles.TEXT_SUBTLE);
        type.textProperty().bind(model.type());

        VBox top = new VBox(titleLabel, type);

        eventsCount.getStyleClass().add(Styles.TEXT_SUBTLE);
       // eventsCount.textProperty().bind(Bindings.size(model.ticketEvents()).asString().concat(" events"));

        defaultPrice.setText("Default price: 250 DKK,-");

        var searchField = new CustomTextField();
        searchField.setPromptText("By title");
        searchField.setLeft(new FontIcon(Feather.SEARCH));
        searchField.setPrefWidth(250);
        searchField.textProperty().bindBidirectional(search);

        var addTicketEvent = new Button(null, new FontIcon(Feather.PLUS));
        addTicketEvent.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
        );

        addTicketEvent.setOnAction(e -> ViewHandler.showOverlay(isPromotional.get() ? "Add promotional ticket" : "Add ticket to event", addTickets(), 300, 350));

        var controls = new HBox();
        controls.getChildren().addAll(searchField, new Spacer(), addTicketEvent);

        var details = new VBox(StyleConfig.STANDARD_SPACING);
        details.getChildren().addAll(top, defaultPrice, eventsCount, controls);
        details.setPadding(new Insets(0, StyleConfig.STANDARD_SPACING * 2, 0, StyleConfig.STANDARD_SPACING));

        return details;
    }

    private ListCell<TicketEventModel> ticketCell() {
        return new ListCell<>() {
            private final Label titleLabel = new Label();
            private final Label totalLabel = new Label();
            private final Label leftLabel = new Label();
            private final Label boughtLabel = new Label();
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
                totalLabel.getStyleClass().add(Styles.TEXT_MUTED);
                leftLabel.getStyleClass().add(Styles.TEXT_MUTED);
                boughtLabel.getStyleClass().add(Styles.TEXT_MUTED);
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

                if (!isPromotional.get()) {
                    ColumnConstraints column6 = new ColumnConstraints();
                    column6.setPercentWidth(width);

                    gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5, column6);
                } else {
                    gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
                }

                gridPane.add(titleLabel, 0, 0);
                gridPane.add(totalLabel, 1, 0);
                gridPane.add(leftLabel, 2, 0);
                gridPane.add(boughtLabel, 3, 0);

                if (!isPromotional.get())
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
                    //table.getColumns().addAll(title, total, left, bought, price, actionButtons);

                    titleLabel.textProperty().bind(item.event().get() != null ? item.event().get().title() : new SimpleStringProperty("Not associated"));
                    totalLabel.textProperty().bind(item.total().asString().concat(" total"));
                    leftLabel.textProperty().bind(item.left().asString().concat(" left"));
                    boughtLabel.textProperty().bind(item.bought().asString().concat(" bought"));
                    priceLabel.textProperty().bind(Bindings.concat("DKK ", item.price().asString(), ",-"));

                    gridPane.setPadding(new Insets(0, CELL_PADDING * 2, 0, CELL_PADDING * 2));

                    var assignButton = new Button(null, new FontIcon(Material2MZ.PERSON_ADD));
                    var editButton = new Button(null, new FontIcon(Feather.EDIT));
                    var deleteButton = new Button(null, new FontIcon(Feather.TRASH));

                    assignButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    assignButton.setOnAction(event -> {
                        if (item.left().get() > 0)
                            ViewHandler.showOverlay("Assign ticket", assignCustomer(item), 300, 350);
                        else
                            ViewHandler.notify(NotificationType.WARNING, "Not enough tickets left!");
                    });

                    editButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    editButton.setOnAction(event -> {
                        ViewHandler.showOverlay("Edit ticket", editTickets(item), 300, 350);
                    });

                    deleteButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    deleteButton.setOnAction(event -> {
                        try {
                            ticketsModel.deleteTicketEvent(item, model);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
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

    public VBox assignCustomer(TicketEventModel ticketEventModel) {
        VBox vBox = new VBox(10);

        VBox email = new VBox(0);
        Label emailLabel = new Label("Customer email:");
        TextField emailValue = new TextField();
        emailValue.setPromptText("example@email.com");

        email.getChildren().addAll(emailLabel, emailValue);

        VBox amount = new VBox(0);
        var amountLabel = new Label("Ticket quantity");

        var amountValue = new Spinner<Integer>(1, ticketEventModel.left().get(), 1);
        IntegerStringConverter.createFor(amountValue);
        amountValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        amountValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        amountValue.setEditable(true);

        amount.getChildren().addAll(amountLabel, amountValue);

        HBox addBox = new HBox(5);

        var add = new Button("Add");
        var err = new Label();
        err.getStyleClass().add(Styles.DANGER);

        add.setOnAction(e -> {
            try {
                if (!ticketsModel.isValidEmail(emailValue.getText()))
                    emailValue.pseudoClassStateChanged(Styles.STATE_DANGER, true);

                boolean generateTickets = ticketsModel.generateTickets(model, ticketEventModel, amountValue.getValue(), emailValue.getText());
                if (generateTickets) {
                    ViewHandler.notify(NotificationType.SUCCESS, "Ticket(s) sent to " + emailValue.getText());
                    emailValue.pseudoClassStateChanged(Styles.STATE_DANGER, false);
                }

            } catch (Exception ex) {
                ViewHandler.notify(NotificationType.FAILURE, ex.getMessage());
            }
        });

        addBox.getChildren().addAll(add, err);

        vBox.getChildren().addAll(email, amount, addBox);
        return vBox;
    }

    private VBox addTickets() {
        VBox main = new VBox(10);

        VBox tickets = new VBox(0);
        var totalLabel = new Label("Ticket quantity");

        var totalValue = new Spinner<Integer>(1, 500, 1);
        IntegerStringConverter.createFor(totalValue);
        totalValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        totalValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        totalValue.setEditable(true);

        tickets.getChildren().addAll(totalLabel, totalValue);
        main.getChildren().add(tickets);

        VBox price = new VBox(0);
        var priceLabel = new Label("Price (DKK)");

        var priceValue = new Spinner<Double>(1.00, 500.00, 1.00);
        DoubleStringConverter.createFor(priceValue);
        priceValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        priceValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        priceValue.setEditable(true);
        price.getChildren().addAll(priceLabel, priceValue);

        if (!isPromotional.get())
            main.getChildren().add(price);


        VBox events = new VBox(0);
        var selectEventsLabel = new Label("Select events");
        selectEventsLabel.setAlignment(Pos.CENTER);

        events.getChildren().addAll(selectEventsLabel, checkComboBox);
        main.getChildren().add(events);

        checkComboBox.setPrefWidth(PREF_TEXTFIELD_WIDTH + 100);

        VBox addEvent = new VBox(0);
        HBox addBox = new HBox(5);

        var add = new Button("Add");
        var err = new Label();
        err.getStyleClass().add(Styles.DANGER);

        add.setOnAction(e -> {
            List<EventModel> selectedEvents = getSelectedEventModels();
            if (!isPromotional.get() && selectedEvents.size() <= 0) {
                err.setText("You must select atleast 1 event");
                return;
            }

            addToEvents(totalValue.getValue(), isPromotional.get() ? 0 : priceValue.getValue(), selectedEvents);
        });

        addBox.getChildren().addAll(add, err);
        addEvent.getChildren().add(addBox);

        main.getChildren().addAll(addEvent);
        return main;
    }

    private VBox editTickets(TicketEventModel ticketEvent) {
        VBox mainBox = new VBox(10);

        VBox tickets = new VBox(0);
        var totalLabel = new Label("Ticket quantity");

        var totalValue = new Spinner<Integer>(ticketEvent.bought().get(), 500, 1);
        IntegerStringConverter.createFor(totalValue);
        totalValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        totalValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        totalValue.setEditable(true);

        tickets.getChildren().addAll(totalLabel, totalValue);
        mainBox.getChildren().add(tickets);

        VBox price = new VBox(0);
        var priceLabel = new Label("Price (DKK)");

        var priceValue = new Spinner<Double>(1.00, 500.00, ticketEvent.price().getValue());
        DoubleStringConverter.createFor(priceValue);
        priceValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        priceValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        priceValue.setEditable(true);
        price.getChildren().addAll(priceLabel, priceValue);

        if (!isPromotional.get())
            mainBox.getChildren().add(price);

        VBox addEvent = new VBox(0);
        HBox addBox = new HBox(5);

        var add = new Button("Add");
        var err = new Label();
        err.getStyleClass().add(Styles.DANGER);

        add.setOnAction(e -> {
            TicketEvent edited = ticketEvent.toEntity();
            edited.setQuantity(totalValue.getValue());
            edited.setPrice(priceValue.getValue());

            try {
                ticketsModel.editEventTicket(ticketEvent, TicketEventModel.fromEntity(edited));
            } catch (Exception ex) {
                ViewHandler.notify(NotificationType.FAILURE, "Error occured!\n" + ex.getMessage());

            }
        });

        addBox.getChildren().addAll(add, err);
        addEvent.getChildren().add(addBox);

        mainBox.getChildren().addAll(addEvent);
        return mainBox;
    }


    public CheckComboBox<EventModelWrapper> multiCombo() {
        CheckComboBox<EventModelWrapper> checkComboBox = new CheckComboBox<>();
        checkComboBox.setPrefWidth(200);

        main.eventModels().addListener((ListChangeListener.Change<? extends EventModel> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (EventModel eventModel : change.getAddedSubList()) {
                        EventModelWrapper wrapper = new EventModelWrapper(eventModel);
                        checkComboBox.getItems().add(wrapper);
                    }
                }
            }
        });

        return checkComboBox;
    }

    public List<EventModel> getSelectedEventModels() {
        List<EventModel> selectedModels = new ArrayList<>();
        ObservableList<EventModelWrapper> selectedTitles = checkComboBox.getCheckModel().getCheckedItems();

        for (EventModelWrapper wrapper : selectedTitles)
            selectedModels.add(wrapper.getEventModel());

        return selectedModels;
    }

    private void addToEvents(int total, double price, List<EventModel> selectedEvents) {
        int ticketId = model.id().get();

        if (!isPromotional.get())
            ticketsModel.addToEvent(model, main, ticketId, total, price, selectedEvents);
        else {
            try {
                ticketsModel.addToEvent(model, main, ticketId, total, 0, selectedEvents);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public StringProperty searchProperty() {
        return search;
    }
}
