package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.MaskTextField;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import atlantafx.base.util.DoubleStringConverter;
import atlantafx.base.util.IntegerStringConverter;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.bll.TicketManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.converter.FloatStringConverter;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.beans.EventHandler;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowTicketView implements View {
    private final TicketsModel ticketsModel;
    private final MainModel main;

    private TicketModel model = TicketModel.Empty();
    private final Label titleLabel = new Label();
    private final Label type = new Label();
    private final Label defaultPrice = new Label();
    private final Label defaultQuantity = new Label();
    private final Label events = new Label();

    private TableView<TicketEventModel> table;

    private final static int PREF_TEXTFIELD_WIDTH = 200;
    private CheckComboBox<EventModelWrapper> checkComboBox = new CheckComboBox<>();

    public ShowTicketView(MainModel main, TicketsModel ticketsModel) {
        this.main = main;
        this.ticketsModel = ticketsModel;

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
                table.setItems(model.ticketEvents());
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
        VBox box = new VBox(5);
        titleLabel.textProperty().bind(model.title());
        titleLabel.getStyleClass().add(Styles.TITLE_3);

        type.getStyleClass().add(Styles.TEXT_SUBTLE);
        type.textProperty().bind(model.type());

        defaultPrice.setText("Default price: 250 DKK,-");

        var associate = new Button("Add");
        associate.setOnAction(e -> ViewHandler.showOverlay("Add ticket to event", addTickets(), 300, 350));

        box.getChildren().addAll(titleLabel, type, defaultPrice, associate, createTicketTableView());
        return box;
    }

    public TableView<TicketEventModel> createTicketTableView() {
        TableColumn<TicketEventModel, String> col1 = new TableColumn<>("Title");

        col1.setCellValueFactory(cellData -> {
            if (cellData.getValue().event() != null && cellData.getValue().event().get() != null)
                return cellData.getValue().event().get().title();

                return new SimpleStringProperty("suckadoi");
        });

        TableColumn<TicketEventModel, String> col2 = new TableColumn<>("Total");
        col2.setCellValueFactory(cellData -> {
            Integer total = cellData.getValue().total().get();
            return new SimpleStringProperty(total + " Total");
        });

        TableColumn<TicketEventModel, String> col3 = new TableColumn<>("Left");
        col3.setCellValueFactory(cellData -> {
            Integer left = cellData.getValue().left().get();
            return new SimpleStringProperty(left + " left");
        });

        TableColumn<TicketEventModel, String> col4 = new TableColumn<>("Bought");
        col4.setCellValueFactory(cellData -> {
            Integer bought = cellData.getValue().bought().get();
            return new SimpleStringProperty(bought + " bought");
        });

        TableColumn<TicketEventModel, String> col5 = new TableColumn<>("Price");
        col5.setCellValueFactory(cellData -> {
            Double price = cellData.getValue().price().get();
            String formattedPrice = String.format("DKK %.2f,-", price);
            return new SimpleStringProperty(formattedPrice);
        });

        TableColumn<TicketEventModel, Void> col6 = new TableColumn<>("");
        col6.setCellFactory(param -> new TableCell<>() {
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
                    ViewHandler.changeView(ViewType.ASSIGN_TICKET_VIEW, rowData);
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
        table.getColumns().addAll(col1, col2, col3, col4, col5, col6);

        table.getStyleClass().add(StyleConfig.ACTIONABLE);

        return table;
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
            if (selectedEvents.size() <= 0) {
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

        ticketsModel.addToEvent(model, main, ticketId, total, price, selectedEvents);
    }

}
