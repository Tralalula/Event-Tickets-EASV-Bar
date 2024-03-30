package event.tickets.easv.bar.gui.component.tickets;

import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;

public class AddTicketView implements View {
    private final static int PREF_WIDTH = 200;

    private TicketsModel ticketsModel;
    private MainModel model;

    public AddTicketView(TicketsModel ticketsModel, MainModel model) {
        this.ticketsModel = ticketsModel;
        this.model = model;
    }

    @Override
    public Region getView() {

        return view();
    }

    private VBox view() {
        VBox main = new VBox(10);

        var title = new Label("Title");
        var tf = new TextField();
        tf.setPromptText("Title");

        var typeTitle = new Label("Type");
        var type = new ComboBox<String>();
        type.setPrefWidth(PREF_WIDTH);
        type.setItems(FXCollections.observableArrayList(
                "Paid",
                "Promotional"
        ));
        type.getSelectionModel().selectFirst();

        var button = new Button("Add ticket");
        button.setOnAction(e -> {
            Ticket createdTicket = ticketsModel.add(new Ticket(tf.getText(), type.getValue()));
            model.ticketModels().add(TicketModel.fromEntity(createdTicket));
        });
        //main.getChildren().addAll(title, tf,  typeTitle, type, new Label("Select for events"), mutliCombo());
        main.getChildren().addAll(title, tf,  typeTitle, type, button);
        return main;
    }

    /*
    public CheckComboBox<String> mutliCombo() {
        CheckComboBox<String> checkComboBox = new CheckComboBox<>();
        checkComboBox.setPrefWidth(200);

        model.addListener(new ListChangeListener<EventModel>() {
            @Override
            public void onChanged(Change<? extends EventModel> change) {
                while (change.next()) {
                    if (change.wasAdded()) {
                        for (EventModel eventModel : change.getAddedSubList()) {
                            checkComboBox.getItems().add(eventModel.title().get());
                        }
                    }
                }
            }
        });

        checkComboBox.getCheckModel().check(0);
        checkComboBox.getCheckModel().check(1);

        return checkComboBox;
    }*/
}
