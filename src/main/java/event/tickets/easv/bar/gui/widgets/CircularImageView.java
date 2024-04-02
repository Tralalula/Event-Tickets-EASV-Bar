package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CircularImageView extends StackPane {
    private final ImageView imageView;
    private final StackPane placeholder;
    private final Circle circle;
    private final Label label;

    public CircularImageView(double radius) {
        super();
        this.label = new Label("");
        this.circle = new Circle(radius);
        this.imageView = Images.circle(radius);
        this.placeholder = placeholder(radius);
    }

    public CircularImageView(double radius, String text) {
        super();
        this.label = new Label(text);
        this.circle = new Circle(radius);
        this.imageView = Images.circle(radius);
        this.placeholder = placeholder(radius);
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public StackPane get() {
        return new StackPane(placeholder, imageView);
    }

    public void setFill(Paint color) {
        circle.setFill(color);
    }

    private StackPane placeholder(double radius) {
        circle.setFill(Color.LIGHTGRAY);

        label.getStyleClass().add(Styles.TEXT_BOLD);

        return new StackPane(circle, label);
    }
}
