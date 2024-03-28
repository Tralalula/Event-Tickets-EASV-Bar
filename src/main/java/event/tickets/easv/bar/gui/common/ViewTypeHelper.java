package event.tickets.easv.bar.gui.common;

import java.util.LinkedList;
import java.util.List;

import static event.tickets.easv.bar.gui.common.ViewType.NO_VIEW;

/**
 * Helper class for ViewType to provide basic utility methods.
 */
public class ViewTypeHelper {
    /**
     * Gets the ViewType whose name is equivalent to the provided string name, case-insensitively.
     *
     * @param name the name of the ViewType to find. Case-insensitive.
     * @return the corresponding ViewType, or ViewType.NO_VIEW if not found.
     * <br>
     * Example usage:<br>
     *  - byName(null) returns ViewType.NO_VIEW
     *  - byName("") returns ViewType.NO_VIEW
     *  - byName("dasHbOaRd") returns ViewType.DASHBOARD (if ViewType.DASHBOARD exists)<br>
     *  - byName("chocolate") returns ViewType.NO_VIEW (if ViewType.CHOCOLATE does not exist)
     */
    public static ViewType byName(String name) {
        for (var vt : ViewType.values()) {
            if (vt.name().equalsIgnoreCase(name)) return vt;
        }
        return NO_VIEW;
    }

    /**
     * Produces a path from the root parent view type to the provided view type.
     *
     * @param viewType the view type to construct a path for.
     * @return A list of ViewType from the root parent to the provided viewType.
     *         If viewType is null, returns an empty list.
     * <br><br>
     * Example usage:<br>
     * - Given a view hierarchy where ViewType.SHOW_EVENT is a child of ViewType.EVENT, and ViewType.EVENT is a root then:
     *   path(ViewType.SHOW_EVENT) returns [ViewType.EVENT, ViewType.SHOW_EVENT]
     */
    public static List<ViewType> pathOf(ViewType viewType) {
        return constructPath(viewType, new LinkedList<>());
    }

    private static List<ViewType> constructPath(ViewType currentType, List<ViewType> path) {
        // base case
        if (currentType == null) return path;

        // recursive case
        path.addFirst(currentType);
        return constructPath(currentType.parent(), path);
    }
}