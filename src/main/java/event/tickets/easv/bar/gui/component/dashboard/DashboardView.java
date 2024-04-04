package event.tickets.easv.bar.gui.component.dashboard;

import event.tickets.easv.bar.gui.common.MessageType;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;

public class DashboardView implements View {
    private int messageIndex = 0;
    private String[] messages = {"Message 1,", "Message 2, ", "Message 3, ", "Message 4, ", "Message 5, "};

    @Override
    public Region getView() {
        var btn = new Button("dashboard");

        btn.setOnAction(e -> {
            ViewHandler.notify(MessageType.FAILURE, messages[messageIndex]);
            messageIndex = (messageIndex + 1) % messages.length;
        });

        return btn;

    }
}
