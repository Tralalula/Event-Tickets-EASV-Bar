package event.tickets.easv.bar.gui.component.dashboard;

import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DashboardView implements View {
    private int index = 0;
    private final String[] messages = {"Message 1,", "Message 2, ", "Message 3, ", "Message 4, ", "Message 5, "};
    private final NotificationType[] notificationTypes = {NotificationType.REGULAR, NotificationType.INFO, NotificationType.SUCCESS, NotificationType.WARNING, NotificationType.FAILURE};

    @Override
    public Region getView() {
        var btn = new Button("dashboard");

        btn.setOnAction(e -> {
            ViewHandler.notify(notificationTypes[index], messages[index]);
            index = (index + 1) % messages.length;
        });

        return btn;

    }
}
