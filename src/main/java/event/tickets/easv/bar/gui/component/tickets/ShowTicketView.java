package event.tickets.easv.bar.gui.component.tickets;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.TicketModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.component.main.MainModel;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

public class ShowTicketView implements View {
    private TicketModel ticketModel;
    private final Label titleLabel = new Label();
    private final Label type = new Label();
    private final Label defaultPrice = new Label();
    private final Label defaultQuantity = new Label();
    private final Label events = new Label();

    public ShowTicketView() {
        ViewHandler.currentViewDataProperty().subscribe((oldData, newData) -> {
            if (newData instanceof TicketModel) {
                ticketModel = (TicketModel) newData;
                initializeBindings();
            }
        });
    }

    private void initializeBindings() {
        if (ticketModel != null) {
            titleLabel.textProperty().bind(ticketModel.title());
            type.textProperty().bind(ticketModel.type());
        }
    }

    @Override
    public Region getView() {
        return topSection();
    }

    public VBox topSection() {
        VBox box = new VBox(5);
        titleLabel.getStyleClass().add(Styles.TITLE_3);
        type.getStyleClass().add(Styles.TEXT_SUBTLE);

        defaultPrice.setText("Default price: 250 DKK,-");

        box.getChildren().addAll(titleLabel, type, defaultPrice);
        return box;
    }
}
