package event.tickets.easv.bar.gui.common;

import javafx.scene.layout.Region;

/**
 * Represents the View in a Model-View-Controller architecture.
 */
public interface View {
    /**
     * Retrieves the visual representation of the view.
     *
     * @return a Region object that represents the graphical content of the view. This should never be null.
     */
    Region getView();
}
