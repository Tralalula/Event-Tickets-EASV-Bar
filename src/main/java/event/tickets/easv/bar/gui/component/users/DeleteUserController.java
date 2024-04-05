package event.tickets.easv.bar.gui.component.users;

import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.common.Action.DeleteUser;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;

public class DeleteUserController {
    public DeleteUserController() {
    }

    void onDeleteUser(Runnable postTaskGuiActions, UserModel userModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> deleteUser(userModel),
                postTaskGuiActions,
                success -> ActionHandler.handle(new DeleteUser(userModel)),
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to delete user")
        );
    }

    private Result<Boolean> deleteUser(UserModel userModel) {
        return new EntityManager().delete(userModel.toEntity());
    }
}