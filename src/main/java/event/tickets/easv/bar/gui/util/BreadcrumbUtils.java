package event.tickets.easv.bar.gui.util;

import atlantafx.base.controls.Breadcrumbs;
import atlantafx.base.controls.Breadcrumbs.BreadCrumbItem;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.common.ViewTypeHelper;
import event.tickets.easv.bar.util.StringUtils;

import java.util.LinkedList;

/**
 * Contains utility methods for manipulating breadcrumbs (AtlantaFX control)
 */
public class BreadcrumbUtils {
    /**
     * Consumes a view type and produces breadcrumb tree matching the view type tree.
     *
     * @param viewType the view type from which to construct the breadcrumb tree.
     * @return BreadCrumbItem representing the root of the breadcrumb tree.
     */
    public static BreadCrumbItem<String> buildBreadCrumbs(ViewType viewType) {
        var crumbNames = ViewTypeHelper.pathOf(viewType).stream()
                .map(Enum::name)
                .toArray(String[]::new);
        formatBreadcrumbs(crumbNames);
        return Breadcrumbs.buildTreeModel(crumbNames);
    }

    private static void formatBreadcrumbs(String[] crumbNames) {
        if (crumbNames.length == 0) return;

        // capitalize first
        crumbNames[0] = StringUtils.capitalize(crumbNames[0]);

        // lowercase rest
        for (var i = 1; i < crumbNames.length; i++) {
            crumbNames[i] = crumbNames[i].toLowerCase();
        }
    }
}