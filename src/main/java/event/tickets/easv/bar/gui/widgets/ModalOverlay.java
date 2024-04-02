package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.ModalPane;
import javafx.animation.Animation;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Wrapper for ModalPane to fix animations only happening once during its lifetime.
 */
public class ModalOverlay extends ModalPane {
    public ModalOverlay() {
        super();
        // Animate when clicking bg to close
        this.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            event.consume();
            hide();
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
