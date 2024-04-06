package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.util.StyleConfig;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class CircularImageView extends StackPane {
    private final ImageView imageView;
    private final StackPane placeholder;
    private final Circle circle;
    private final Label label;
    private final Region circleRegion;

    public CircularImageView(double radius) {
        super();
        this.label = new Label("");
        this.circle = new Circle(radius);
        this.imageView = Images.circle(radius);
        this.circleRegion = new Region();
        circleRegion.setMaxSize(radius * 2, radius * 2);
        this.placeholder = placeholder();
    }

    public CircularImageView(double radius, String text) {
        super();
        this.label = new Label(text);
        this.circle = new Circle(radius);
        this.imageView = Images.circle(radius);
        this.circleRegion = new Region();
        circleRegion.setMaxSize(radius * 2, radius * 2);
        this.placeholder = placeholder();
    }

//    public void setImage(Image image) {
//        imageView.setImage(image);
//    }

    public ObjectProperty<Image> imageProperty() {
        return imageView.imageProperty();
    }

    public StringProperty textProperty() {
        return label.textProperty();
    }

//    public void setText(String text) {
//        label.setText(text);
//    }

    public StackPane get() {
        return new StackPane(placeholder, imageView);
    }

    private StackPane placeholder() {
        label.getStyleClass().addAll(Styles.TEXT_BOLD, StyleConfig.CIRCLE_PLACEHOLDER_TEXT);
        circleRegion.setShape(circle);
        circleRegion.getStyleClass().add(StyleConfig.CIRCLE_PLACEHOLDER);



        return new StackPane(circleRegion, label);
    }
}
