package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AssignTicketView  implements View {
    private final TicketEventModel model = TicketEventModel.Empty();
    private ObjectProperty<TicketEventModel> ticketEvent;

    private TicketsModel ticketsModel;

    public AssignTicketView(TicketsModel ticketsModel) {
        this.ticketsModel = ticketsModel;
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketEventModel) {
                model.update((TicketEventModel) newData);
            }
        });
    }
    @Override
    public Region getView() {
        VBox vBox = new VBox(5);
        Label label = new Label("Customer email:");
        TextField email = new TextField();
        email.setPromptText("example@email.com");

        Label amountLabel = new Label("Amount");
        TextField amount = new TextField();
        amount.setText("1");

        Button button = new Button("Send to customer");
        button.setOnAction(e -> {
            ticketsModel.generateTickets(model.ticketId().get(), Integer.parseInt(amount.getText()), email.getText());
        });

        vBox.getChildren().addAll(label, email, amountLabel, amount, button);

        return vBox;
    }
}
