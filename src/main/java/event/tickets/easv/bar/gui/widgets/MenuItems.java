package event.tickets.easv.bar.gui.widgets;

import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class MenuItems {
    public static MenuItem createItem(String text, Ikon graphic) {
        var item = new MenuItem(text);

        if (graphic != null) {
            item.setGraphic(new FontIcon(graphic));
        }

        return item;
    }
}
