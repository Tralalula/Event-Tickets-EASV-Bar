package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.main.MainModel;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TicketsView implements View {
    private final MainModel mainModel;
    private final ObservableList<TicketModel> model;
    private final BooleanProperty fetchingData;

    public TicketsView(MainModel mainModel, ObservableList<TicketModel> model, BooleanProperty fetchingData) {
        this.mainModel = mainModel;
        this.model = model;
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {
        HBox top = topBar();
        TableView<TicketModel> table = createTicketTableView();

        VBox box = new VBox(top, table);

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

        // Create a placeholder region to push the button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        var addTicket = new Button(null, new FontIcon(Feather.PLUS));
        addTicket.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
        );

        top.getChildren().addAll(search, spacer, addTicket);
        return top;
    }

    public TableView<TicketModel> createTicketTableView() {
        TableColumn<TicketModel, String> col1 = new TableColumn<>("Title");
        col1.setCellValueFactory(c -> c.getValue().title());

        TableColumn<TicketModel, String> col2 = new TableColumn<>("Type");
        col2.setCellValueFactory(c -> c.getValue().type());

        TableColumn<TicketModel, String> col3 = new TableColumn<>("Category");
        col3.setCellValueFactory(c -> c.getValue().categoryName());

        TableColumn<TicketModel, Void> col4 = new TableColumn<>("");
        col4.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(null, new FontIcon(Feather.EDIT));
            {
                editButton.getStyleClass().addAll(
                        Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4
                );

                editButton.setOnAction(event -> {
                    TicketModel rowData = getTableView().getItems().get(getIndex());
                    ViewHandler.changeView(ViewType.SHOW_TICKET, rowData);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : editButton);
            }
        });


        TableView<TicketModel> table = new TableView<>();

        table.setTableMenuButtonVisible(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getStyleClass().addAll(Tweaks.NO_HEADER, Styles.STRIPED);

        table.setItems(model);
        table.getColumns().addAll(col1, col2, col3, col4);

        table.getStyleClass().add(StyleConfig.ACTIONABLE);
        handleRowClick(table);

        return table;
    }

    private void handleRowClick(TableView<TicketModel> table) {
        table.setRowFactory(tv -> {
            TableRow<TicketModel> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    TicketModel rowData = row.getItem();
                    ViewHandler.changeView(ViewType.SHOW_TICKET, rowData);
                }
            });
            return row;
        });
    }
}
