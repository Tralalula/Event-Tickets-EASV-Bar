package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import event.tickets.easv.bar.be.Ticket;
import event.tickets.easv.bar.gui.common.View;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class TicketsView implements View {
    @Override
    public Region getView() {
        HBox top = topBar();
        TableView<Ticket> table = createTicketTableView(
                List.of(
                        new Ticket(Ticket.TicketType.EVENT, "VIP", 50),
                        new Ticket(Ticket.TicketType.EVENT, "Normal", 20),
                        new Ticket(Ticket.TicketType.PROMOTIONAL, "1 Free beer", 0),
                        new Ticket(Ticket.TicketType.PROMOTIONAL, "Earpods", 25),
                        new Ticket(Ticket.TicketType.EVENT, "hello", 65)
                ));

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
        search.setPromptText("Prompt text");
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

    public TableView<Ticket> createTicketTableView(List<Ticket> tickets) {
        TableColumn<Ticket, String> col1 = new TableColumn<>("Type");
        col1.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType().name()));

        TableColumn<Ticket, String> col2 = new TableColumn<>("Title");
        col2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));

        TableColumn<Ticket, String> col3 = new TableColumn<>("Price");
        col3.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getPrice())));

        TableView<Ticket> table = new TableView<>();

        table.setTableMenuButtonVisible(false);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getStyleClass().addAll(Tweaks.NO_HEADER, Styles.STRIPED);

        table.setItems(FXCollections.observableList(tickets));
        table.getColumns().setAll(col1, col2, col3);

        return table;
    }

}
