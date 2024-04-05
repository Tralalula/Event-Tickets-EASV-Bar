package event.tickets.easv.bar.gui.component.users;

import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class ShowUserView implements View {
    private final UserModel model;
    private final ObservableList<EventModel> masterEventList;

    public ShowUserView(UserModel model, ObservableList<EventModel> masterEventList) {
        this.model = model;
        this.masterEventList = masterEventList;
    }

    @Override
    public Region getView() {
        return new Button("Show user");
    }
}
