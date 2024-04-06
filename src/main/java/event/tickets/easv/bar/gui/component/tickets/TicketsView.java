package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.Alerts;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.CircularImageView;
import event.tickets.easv.bar.gui.widgets.MenuItems;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class TicketsView implements View {
    private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");
    private static final int CELL_PADDING = 10;
    private static final int CELL_HEIGHT = 50;

    private MainModel main;
    private final BooleanProperty fetchingData;

    private TicketsModel ticketsModel;
    private TableView<TicketModel> table;

    private final static int PREF_OVERLAY_WIDTH = 200;

    public TicketsView(MainModel main, BooleanProperty fetchingData) {
        this.main = main;
        this.ticketsModel = new TicketsModel(main);
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {
        HBox top = topBar();

        var placeholder = new Label("No tickets found");
        placeholder.getStyleClass().add(Styles.TITLE_4);

        var ticketList = new ListView<TicketModel>();
        ticketList.setItems(main.ticketModels());
        ticketList.setPlaceholder(placeholder);
        ticketList.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        ticketList.setCellFactory(c -> {
            var cell = ticketCell();
            cell.getStyleClass().add("bg-subtle-list");
            return cell;
        });

        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        StackPane stackPane = new StackPane(ticketList, progressIndicator);
        VBox.setVgrow(stackPane, Priority.ALWAYS);

        return new VBox(StyleConfig.STANDARD_SPACING, top, stackPane);
    }

    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 2 ,0 ,StyleConfig.STANDARD_SPACING));

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
            try {
                ticketsModel.addTicket(new Ticket(tf.getText(), type.getValue()));
            } catch (Exception ex) {
                ViewHandler.notify(NotificationType.FAILURE, ex.getMessage());
            }

        });

        main.getChildren().addAll(title, tf,  typeTitle, type, button);
        return main;
    }

    private ListCell<TicketModel> ticketCell() {
        return new ListCell<>() {
            private final Label titleLabel = new Label();
            private final Label typeLabel = new Label();
            private final Label eventsLabel = new Label();

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
                eventsLabel.getStyleClass().add(Styles.TEXT_MUTED);

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

                gridPane.add(titleLabel, 0, 0);
                gridPane.add(typeLabel, 1, 0);
                gridPane.add(eventsLabel, 2, 0);

                gridPane.setMinHeight(CELL_HEIGHT);
                gridPane.setAlignment(Pos.CENTER_LEFT);

                gridPane.setMouseTransparent(true);
                gridPane.getStyleClass().addAll(StyleConfig.ROUNDING_DEFAULT, "list-cell-grid");

                hoverProperty().addListener((obs, ov, nv) -> gridPane.pseudoClassStateChanged(HOVER_PSEUDO_CLASS, nv));

                spacer.getStyleClass().add("list-cell-spacer");
                spacer.addEventFilter(MouseEvent.MOUSE_CLICKED, MouseEvent::consume);
            }

            @Override
            protected void updateItem(TicketModel item, boolean empty) {
                if (item == getItem() && empty == isEmpty()) return;

                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    titleLabel.textProperty().bind(item.title());
                    typeLabel.textProperty().bind(item.type());

                    eventsLabel.textProperty().bind(item.eventCount().asString());

                    gridPane.setPadding(new Insets(0, CELL_PADDING * 2, 0, CELL_PADDING * 2));

                    var editButton = new Button(null, new FontIcon(Feather.EDIT));
                    var deleteButton = new Button(null, new FontIcon(Feather.TRASH));

                    editButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    editButton.setOnAction(event -> {
                        ViewHandler.changeView(ViewType.SHOW_TICKET, item);
                    });

                    deleteButton.getStyleClass().addAll(
                            Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                    );

                    deleteButton.setOnAction(event -> {
                        // intet endnu
                    });

                    HBox buttons = new HBox(editButton, deleteButton);
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
                            ViewHandler.changeView(ViewType.SHOW_TICKET, item);
                        }
                    });


                    editItem.setOnAction(e -> System.out.println("Edit ticket: " + item.title().get()));
                    // deleteItem.setOnAction(e -> Alerts.confirmDeleteUser(
                    //         item,
                    //         userModel -> controller.onDeleteUser(() -> {}, item))
                    // );

                    setGraphic(wrapper);
                }
            }
        };
    }
}
