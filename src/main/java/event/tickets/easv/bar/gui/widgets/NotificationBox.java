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
    private final static Duration DEFAULT_ANIMATION_DURATION = Duration.millis(250);
    private final static int MAX_NOTIFICATIONS = 5;

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
        if (notificationBox.getChildren().size() >= MAX_NOTIFICATIONS) {
            notificationBox.getChildren().removeFirst();
        }

        configureNotification(notification);
        notificationBox.getChildren().add(notification);
    }

    private void configureNotification(Notification notification) {
        notification.widthProperty().addListener((obs, ov, nv) -> {
            double translateX = nv.doubleValue();
            notification.setTranslateX(translateX);

            var slideIn = new TranslateTransition(DEFAULT_ANIMATION_DURATION, notification);
            slideIn.setFromX(translateX);
            slideIn.setToX(0);
            slideIn.play();
        });

        notification.setOnClose(e -> {
            var out = Animations.slideOutRight(notification, DEFAULT_ANIMATION_DURATION);
            out.setOnFinished(f -> notificationBox.getChildren().remove(notification));
            out.playFromStart();
        });
    }

    public StackPane getNotificationArea() {
        return notificationArea;
    }
}
