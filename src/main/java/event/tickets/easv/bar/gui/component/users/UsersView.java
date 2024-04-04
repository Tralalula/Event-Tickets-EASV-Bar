package event.tickets.easv.bar.gui.component.users;

import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class UsersView implements View {
    private final ObservableList<UserModel> model;
    private final BooleanProperty fetchingData;

    public UsersView(ObservableList<UserModel> model, BooleanProperty fetchingData) {
        this.model = model;
        this.fetchingData = fetchingData;
    }

    @Override
    public Region getView() {
        return null;
    }
}
