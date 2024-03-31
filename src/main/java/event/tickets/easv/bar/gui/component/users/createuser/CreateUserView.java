package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.gui.common.UserModel;
import event.tickets.easv.bar.gui.common.View;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class CreateUserView implements View {
    private final CreateUserModel model;
    private final CreateUserController controller;

    public CreateUserView(ObservableList<UserModel> models) {
        this.model = new CreateUserModel();
        this.controller = new CreateUserController(model, models);
    }

    @Override
    public Region getView() {
        return null;
    }
}
