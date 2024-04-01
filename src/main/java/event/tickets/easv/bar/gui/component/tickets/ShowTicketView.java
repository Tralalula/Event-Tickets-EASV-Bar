package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.bll.TicketManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.beans.EventHandler;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShowTicketView implements View {

    private final ObservableList<TicketEventModel> ticketEventModels = FXCollections.observableArrayList();

    private final TicketModel model = TicketModel.Empty();
    private final Label titleLabel = new Label();
    private final Label type = new Label();
    private final Label defaultPrice = new Label();
    private final Label defaultQuantity = new Label();
    private final Label events = new Label();

    private BooleanProperty fetchingData;
    public ShowTicketView(BooleanProperty fetchingData) {
        this.fetchingData = fetchingData;

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
                ticketEventModels.setAll(((TicketModel) newData).ticketEvents());
            }
        });
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
        associate.setOnAction(e -> {
            ViewHandler.changeView(ViewType.ADD_TICKET_EVENT, model);
        });

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
            Float price = cellData.getValue().price().get();
            return new SimpleStringProperty("DKK " + price + ",-");
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

        TableView<TicketEventModel> table = new TableView<>();

        table.setTableMenuButtonVisible(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getStyleClass().addAll(Tweaks.NO_HEADER, Styles.STRIPED);

        table.setItems(ticketEventModels);
        table.getColumns().addAll(col1, col2, col3, col4, col5, col6);

        table.getStyleClass().add(StyleConfig.ACTIONABLE);

        return table;
    }
}
