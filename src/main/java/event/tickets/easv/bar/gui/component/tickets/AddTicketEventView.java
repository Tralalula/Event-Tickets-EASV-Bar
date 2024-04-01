package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import event.tickets.easv.bar.be.Ticket.Ticket;
import event.tickets.easv.bar.be.Ticket.TicketEvent;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.controlsfx.control.CheckComboBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddTicketEventView implements View {
    private final TicketModel model = TicketModel.Empty();
    private final static int PREF_WIDTH = 200;

    private MainModel main;
    private TicketsModel ticketsModel;

    private CheckComboBox<String> checkComboBox = new CheckComboBox<>();

    public AddTicketEventView(MainModel main) {
        this.main = main;
        this.ticketsModel = new TicketsModel(main);

        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
            }
        });
    }
    @Override
    public Region getView() {
        return view();
    }

    private VBox view() {
        VBox main = new VBox(10);

        var totalLabel = new Label("Total");
        var total = new CustomTextField();
        total.setPromptText("Total tickets");
        total.setRight(new FontIcon(Feather.CODE));
        total.setPrefWidth(150);

        var priceLabel = new Label("Price");
        var price = new CustomTextField();
        price.setPromptText("Total tickets");
        price.setRight(new FontIcon(Material2AL.ATTACH_MONEY));
        price.setPrefWidth(150);

        var add = new Button("Add");
        add.setOnAction(e -> addToEvents(Integer.parseInt(total.getText()), Integer.parseInt(price.getText())));

        main.getChildren().addAll(totalLabel, total,  priceLabel, price, new Label("Select for events"), mutliCombo(), add);
        return main;
    }

    public CheckComboBox<String> mutliCombo() {
        checkComboBox.setPrefWidth(200);

        main.eventModels().addListener(new ListChangeListener<EventModel>() {
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
    }

    public List<Integer> getSelectedEventIds() {
        List<Integer> selectedIds = new ArrayList<>();
        ObservableList<String> selectedTitles = checkComboBox.getCheckModel().getCheckedItems();

        for (String title : selectedTitles) {
            EventModel selectedEventModel = main.eventModels().stream()
                    .filter(eventModel -> eventModel.title().get().equals(title))
                    .findFirst()
                    .orElse(null);

            if (selectedEventModel != null)
                selectedIds.add(selectedEventModel.id().get());
        }

        return selectedIds;
    }


    private void addToEvents(int total, int price) {
        int ticketId = model.id().get();

        List<TicketEvent> newEntries = ticketsModel.addToEvent(ticketId, total, price, getSelectedEventIds());
    }
}
