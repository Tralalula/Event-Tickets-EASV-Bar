package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ShowTicketView implements View {
    private final TicketModel model = TicketModel.Empty();
    private final Label titleLabel = new Label();
    private final Label type = new Label();
    private final Label defaultPrice = new Label();
    private final Label defaultQuantity = new Label();
    private final Label events = new Label();

    public ShowTicketView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                model.update((TicketModel) newData);
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

        box.getChildren().addAll(titleLabel, type, defaultPrice);
        return box;
    }
}
