package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.ModalPane;
import javafx.animation.Animation;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Wrapper for ModalPane to fix animations only happening once during its lifetime.
 */
public class ModalOverlay extends ModalPane {
    private final BooleanProperty hideRequested = new SimpleBooleanProperty(false);
    public ModalOverlay() {
        super();
        // Animate when clicking bg to close
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node target = event.getTarget() instanceof Node ? (Node) event.getTarget() : null;
            // Hvis det er en scrollpane og har styleclassen scrollable-content
            if (target instanceof StackPane && ((StackPane) target).getStyleClass().contains("scrollable-content")) {
                event.consume();
                hide();
            }
        });
    }

    @Override
    public void show(Node node) {
        Animation inAnimation = inTransitionFactory.get().apply(node);
        super.show(node);
        inAnimation.play();
    }

    @Override
    public void hide() {
        Animation outAnimation = outTransitionFactory.get().apply(getContent());
        outAnimation.setOnFinished(evt -> super.hide(true));
        outAnimation.play();
    }

    @Override
    public void hide(boolean clear) {
        hide();
    }
}
