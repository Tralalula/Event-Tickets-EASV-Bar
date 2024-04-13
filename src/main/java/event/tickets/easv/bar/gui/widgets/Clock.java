package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Clock extends VBox {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEEE, LLLL dd, yyyy");
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Clock() {
        var clockLbl = Labels.styledLabel(TIME_FORMATTER.format(LocalTime.now(ZoneId.systemDefault())), Styles.TITLE_2);
        var dateLbl = Labels.styledLabel(DATE_FORMATTER.format(LocalDate.now(ZoneId.systemDefault())));

        setStyle("-fx-border-width: 0 0 0.5 0; -fx-border-color: -color-border-default");
        setSpacing(StyleConfig.STANDARD_SPACING);
        getChildren().addAll(clockLbl, dateLbl);

        var timeLine = new Timeline(new KeyFrame(
                Duration.seconds(1),
                e -> {
                    var time = LocalTime.now(ZoneId.systemDefault());
                    clockLbl.setText(TIME_FORMATTER.format(time));
                }
        ));
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.playFromStart();
    }
}
