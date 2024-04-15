package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.MaskTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.DoubleStringConverter;
import atlantafx.base.util.IntegerStringConverter;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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

    public ShowTicketView(MainModel main, TicketsModel ticketsModel) {
        this.main = main;
        this.ticketsModel = ticketsModel;

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
                ticketEventList.setItems(model.ticketEvents());
                isPromotional.set(model.type().get().equals("Promotional"));
                updateTexts();
            }
        });

        // For at sørge for den lytter til events ændringer
        checkComboBox = multiCombo();
    }

    @Override
    public Region getView() {
        return topSection();
    }

    public VBox topSection() {
        var placeholder = new Label("No tickets found");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        ticketEventList.setItems(model.ticketEvents());
        ticketEventList.setPlaceholder(placeholder);
        ticketEventList.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        ticketEventList.setCellFactory(c -> {
            var cell = ticketCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });

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

        var search = new CustomTextField();
        search.setPromptText("Search");
        search.setLeft(new FontIcon(Feather.SEARCH));
        search.setPrefWidth(250);
        search.setFocusTraversable(false);

        var addTicketEvent = new Button(null, new FontIcon(Feather.PLUS));
        addTicketEvent.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
        );
        addTicketEvent.setOnAction(e -> ViewHandler.showOverlay(isPromotional.get() ? "Add promotional ticket" : "Add ticket to event", addTickets(), 300, 350));

        var controls = new HBox();
        controls.getChildren().addAll(search, new Spacer(), addTicketEvent);

        var details = new VBox(StyleConfig.STANDARD_SPACING);
        details.getChildren().addAll(top, defaultPrice, eventsCount, controls);
        details.setPadding(new Insets(0, StyleConfig.STANDARD_SPACING * 2, 0, StyleConfig.STANDARD_SPACING));

        return details;
    }

    public TableView<TicketEventModel> createTicketTableView() {
        TableColumn<TicketEventModel, String> title = new TableColumn<>("Title");

        title.setCellValueFactory(cellData -> {
            if (cellData.getValue().event() != null && cellData.getValue().event().get() != null)
                return cellData.getValue().event().get().title();

                return new SimpleStringProperty("suckadoi");
        });

        TableColumn<TicketEventModel, String> total = new TableColumn<>("Total");
        total.setCellValueFactory(cellData -> {
            Integer totalint = cellData.getValue().total().get();
            return new SimpleStringProperty(totalint + " Total");
        });

        TableColumn<TicketEventModel, String> left = new TableColumn<>("Left");
        left.setCellValueFactory(cellData -> {
            Integer leftint = cellData.getValue().left().get();
            return new SimpleStringProperty(leftint + " left");
        });

        TableColumn<TicketEventModel, String> bought = new TableColumn<>("Bought");
        bought.setCellValueFactory(cellData -> {
            Integer boughtint = cellData.getValue().bought().get();
            return new SimpleStringProperty(boughtint + " Bought");
        });

        TableColumn<TicketEventModel, String> price = new TableColumn<>("Price");
        price.setCellValueFactory(cellData -> {
            Double pricedouble = cellData.getValue().price().get();
            String formattedPrice = String.format("DKK %.2f,-", pricedouble);
            return new SimpleStringProperty(formattedPrice);
        });

        TableColumn<TicketEventModel, Void> actionButtons = new TableColumn<>("");
        actionButtons.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button assignButton = new Button(null, new FontIcon(Material2MZ.PERSON_ADD));

            {
                editButton.getStyleClass().addAll(
                        Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                );

                editButton.setOnAction(event -> {
                    TicketEventModel rowData = getTableView().getItems().get(getIndex());
                    ViewHandler.changeView(ViewType.SHOW_TICKET, rowData);
                });

                assignButton.getStyleClass().addAll(
                        Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                );

                assignButton.setOnAction(event -> {
                    TicketEventModel rowData = getTableView().getItems().get(getIndex());
                    if (rowData.left().get() > 0)
                        ViewHandler.showOverlay("Assign ticket", assignCustomer(rowData), 300, 350);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, assignButton);
                    buttons.setSpacing(20);
                    setGraphic(buttons);
                }
            }
        });

        table = new TableView<>();

        table.setTableMenuButtonVisible(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getStyleClass().addAll(Tweaks.NO_HEADER, Styles.STRIPED);

        table.setItems(model.ticketEvents());
        table.getColumns().addAll(title, total, left, bought, price, actionButtons);

        table.getStyleClass().add(StyleConfig.ACTIONABLE);

        return table;
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
                ColumnConstraints column6 = new ColumnConstraints();
                column6.setPercentWidth(width);

                gridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5, column6);

                gridPane.add(titleLabel, 0, 0);
                gridPane.add(totalLabel, 1, 0);
                gridPane.add(leftLabel, 2, 0);
                gridPane.add(boughtLabel, 3, 0);
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
                        ViewHandler.showOverlay("Assign ticket", assignCustomer(item), 300, 350);
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
                ticketsModel.generateTickets(ticketEventModel, amountValue.getValue(), emailValue.getText());
            } catch (Exception ex) {
                ViewHandler.notify(NotificationType.FAILURE, ex.getMessage());
            }
        });

        addBox.getChildren().addAll(add, err);

        vBox.getChildren().addAll(email, amount, addBox);
        return vBox;
    }

    private VBox editEventTicket() {
        VBox main = new VBox(10);

        VBox tickets = new VBox(0);
        var totalLabel = new Label("Ticket quantity");

        var totalValue = new Spinner<Integer>(1, 500, 1);
        IntegerStringConverter.createFor(totalValue);
        totalValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        totalValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        totalValue.setEditable(true);

        tickets.getChildren().addAll(totalLabel, totalValue);

        VBox price = new VBox(0);
        var priceLabel = new Label("Price (DKK)");

        var priceValue = new Spinner<Double>(1.00, 500.00, 1.00);
        DoubleStringConverter.createFor(priceValue);
        priceValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        priceValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        priceValue.setEditable(true);

        price.getChildren().addAll(priceLabel, priceValue);

        VBox events = new VBox(0);
        var selectEventsLabel = new Label("Select events");
        selectEventsLabel.setAlignment(Pos.CENTER);

        events.getChildren().addAll(selectEventsLabel, checkComboBox);

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

            addToEvents(totalValue.getValue(), priceValue.getValue(), selectedEvents);
        });

        addBox.getChildren().addAll(add, err);
        addEvent.getChildren().add(addBox);

        main.getChildren().addAll(tickets,  price, events, addEvent);
        return main;
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

        VBox price = new VBox(0);
        var priceLabel = new Label("Price (DKK)");

        var priceValue = new Spinner<Double>(1.00, 500.00, 1.00);
        DoubleStringConverter.createFor(priceValue);
        priceValue.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        priceValue.setPrefWidth(PREF_TEXTFIELD_WIDTH + 200);
        priceValue.setEditable(true);

        price.getChildren().addAll(priceLabel, priceValue);

        VBox events = new VBox(0);
        var selectEventsLabel = new Label("Select events");
        selectEventsLabel.setAlignment(Pos.CENTER);

        events.getChildren().addAll(selectEventsLabel, checkComboBox);

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

            addToEvents(totalValue.getValue(), priceValue.getValue(), selectedEvents);
        });

        addBox.getChildren().addAll(add, err);
        addEvent.getChildren().add(addBox);

        main.getChildren().addAll(tickets,  price, events, addEvent);
        return main;
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
                ticketsModel.addSpecialTicket(model, main, ticketId, total, price);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
