package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class TicketsView implements View {
    private MainModel main;
    private final BooleanProperty fetchingData;

    private TicketsModel ticketsModel;
    private TableView<TicketModel> table;

    private final static int PREF_OVERLAY_WIDTH = 200;

    public TicketsView(MainModel main, BooleanProperty fetchingData) {
        this.main = main;

        this.fetchingData = fetchingData;

        main.ticketEventModels().addListener((ListChangeListener.Change<? extends TicketEventModel> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    // Handle added items
                    table.refresh();
                    System.out.println("Items added: " + change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    // Handle removed items
                    System.out.println("Items removed: " + change.getRemoved());
                }
                // Handle other change types if needed
            }
        });
    }

    @Override
    public Region getView() {
        HBox top = topBar();
        table = createTicketTableView();

        StackPane stackPane = new StackPane(table);
        stackPane.getStyleClass().addAll(Styles.BG_SUBTLE);

        VBox.setVgrow(stackPane, Priority.ALWAYS);

        VBox finalLayout = new VBox(top, stackPane);
        finalLayout.setPadding(new Insets(0, 10, 10, 10));
        return finalLayout;
    }

    public HBox topBar() {
        HBox top = new HBox();
        var search = new CustomTextField();
        search.setPromptText("Search");
        search.setLeft(new FontIcon(Feather.SEARCH));
        search.setPrefWidth(250);

        var addTicket = new Button(null, new FontIcon(Feather.PLUS));
        addTicket.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
        );

        addTicket.setOnAction(e -> ViewHandler.showOverlay("Add new Ticket", addTicket(), 300, 350));

        top.getChildren().addAll(search, new Spacer(), addTicket);
        return top;
    }

    private VBox addTicket() {
        VBox main = new VBox(10);

        var title = new Label("Title");
        var tf = new TextField();
        tf.setPromptText("Title");
        title.setPrefWidth(PREF_OVERLAY_WIDTH);

        var typeTitle = new Label("Type");
        var type = new ComboBox<String>();
        type.setPrefWidth(PREF_OVERLAY_WIDTH + 100);
        type.setItems(FXCollections.observableArrayList(
                "Paid",
                "Promotional"
        ));
        type.getSelectionModel().selectFirst();

        var button = new Button("Add ticket");
        button.setOnAction(e -> {
            Ticket createdTicket = ticketsModel.addTicket(new Ticket(tf.getText(), type.getValue()));
            //model.ticketModels().add(TicketModel.fromEntity(createdTicket));
        });

        main.getChildren().addAll(title, tf,  typeTitle, type, button);
        return main;
    }

    public TableView<TicketModel> createTicketTableView() {
        TableColumn<TicketModel, String> title = new TableColumn<>("Title");
        title.setCellValueFactory(c -> c.getValue().title());

        TableColumn<TicketModel, String> type = new TableColumn<>("Type");
        type.setCellValueFactory(c -> c.getValue().type());

        TableColumn<TicketModel, String> eventCount = new TableColumn<>("Events");
        eventCount.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().eventCount().get() + " events"));

        TableColumn<TicketModel, Void> buttons = new TableColumn<>("");
        buttons.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            private final Button deleteButton = new Button(null, new FontIcon(Feather.TRASH));
            {
                editButton.getStyleClass().addAll(
                        Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                );

                editButton.setOnAction(event -> {
                    TicketModel rowData = getTableView().getItems().get(getIndex());
                    ViewHandler.changeView(ViewType.SHOW_TICKET, rowData);
                });

                deleteButton.getStyleClass().addAll(
                        Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                );

                deleteButton.setOnAction(event -> {
                    // intet endnu
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(editButton, deleteButton);
                    buttons.setSpacing(20);
                    setGraphic(buttons);
                }
            }
        });

        TableView<TicketModel> table = new TableView<>();

        table.setTableMenuButtonVisible(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getStyleClass().addAll(Tweaks.NO_HEADER, Styles.STRIPED);

        //SortedList<TicketModel> sortedList = ticketsModel.sortToNewest(model);
        //sortedList.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(main.ticketModels());

        table.getColumns().addAll(title, type, eventCount, buttons);

        table.getStyleClass().add(StyleConfig.ACTIONABLE);
        handleRowClick(table);

        return table;
    }

    private void handleRowClick(TableView<TicketModel> table) {
        table.setRowFactory(tv -> {
            TableRow<TicketModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    TicketModel rowData = row.getItem();
                    ViewHandler.changeView(ViewType.SHOW_TICKET, rowData);
                }
            });
            return row;
        });
    }
}
