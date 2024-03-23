package event.tickets.easv.bar.gui.util;

import atlantafx.base.controls.Breadcrumbs;
import atlantafx.base.controls.Breadcrumbs.BreadCrumbItem;
import event.tickets.easv.bar.util.StringUtils;

import java.util.LinkedList;

public class BreadcrumbBuilder {
    public static BreadCrumbItem<String> buildBreadCrumbs(ViewType viewType) {
        var crumbNames = new LinkedList<String>();
        collectCrumbNames(viewType, crumbNames);
        formatBreadcrumbs(crumbNames);
        return Breadcrumbs.buildTreeModel(crumbNames.toArray(new String[0]));
    }

    private static void formatBreadcrumbs(LinkedList<String> crumbNames) {
        if (crumbNames.isEmpty()) return;

        // capitalize first
        crumbNames.set(0, StringUtils.capitalize(crumbNames.getFirst()));

        // lowercase rest
        for (var i = 1; i < crumbNames.size(); i++) {
            crumbNames.set(i, crumbNames.get(i).toLowerCase());
        }
    }

    private static void collectCrumbNames(ViewType viewType, LinkedList<String> crumbNames) {
        if (viewType == null) return; // base case

        collectCrumbNames(viewType.parent(), crumbNames); // recursive case
        crumbNames.addLast(viewType.name());
    }
}