package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.Message;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedAL;

public class Messages {
    public static Message regularMessage(String title, String message) {
        return new Message(title, message, new FontIcon(Material2OutlinedAL.CHAT_BUBBLE_OUTLINE));
    }

}
