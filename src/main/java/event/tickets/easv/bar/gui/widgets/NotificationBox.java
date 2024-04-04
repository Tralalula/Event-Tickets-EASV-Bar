package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.Notification;
import atlantafx.base.util.Animations;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class NotificationBox {
    private final VBox notificationBox = new VBox(StyleConfig.STANDARD_SPACING);
    private final StackPane notificationArea = new StackPane();

    public NotificationBox() {
        StackPane.setAlignment(notificationBox, Pos.TOP_RIGHT);
        notificationArea.setPickOnBounds(false);
        notificationBox.setPickOnBounds(false);
        notificationArea.getChildren().add(notificationBox);
        notificationBox.setAlignment(Pos.TOP_RIGHT);
        StackPane.setMargin(notificationBox, new Insets(0, 10, 0, 0));
    }

    public void addNotification(Notification notification) {
        if (notificationBox.getChildren().size() > 4) {
            notificationBox.getChildren().removeFirst();
        }

        notificationBox.getChildren().add(notification);
        var slideIn = new TranslateTransition(Duration.millis(200), notification);
        notification.setTranslateX(420);
        slideIn.setFromX(300);
        slideIn.setToX(0);
        slideIn.setFromY(68);
        slideIn.setDelay(Duration.millis(50));
        slideIn.play();

        notification.setOnClose(e -> {
            var out = Animations.slideOutRight(notification, Duration.millis(250));
            out.setOnFinished(f -> notificationBox.getChildren().remove(notification));
            out.playFromStart();
        });
    }

    public StackPane getNotificationArea() {
        return notificationArea;
    }
}
