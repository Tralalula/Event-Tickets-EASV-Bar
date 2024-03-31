package event.tickets.easv.bar.gui.widgets;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

public class Icons {
    public static FontIcon styledIcon(Ikon iconCode, String... styles) {
        FontIcon results = new FontIcon(iconCode);
        results.getStyleClass().addAll(styles);
        return results;
    }
}
