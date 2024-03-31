package event.tickets.easv.bar.gui.component.users.createuser;

import event.tickets.easv.bar.gui.common.UserModel;
import javafx.collections.ObservableList;

public class CreateUserController {
    private final CreateUserModel model;
    private final ObservableList<UserModel> models;

    public CreateUserController(CreateUserModel model, ObservableList<UserModel> models) {
        this.model = model;
        this.models = models;
    }
}
