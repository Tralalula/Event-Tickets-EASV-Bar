package event.tickets.easv.bar.gui.util;

import atlantafx.base.controls.Breadcrumbs;
import atlantafx.base.controls.Breadcrumbs.BreadCrumbItem;

import java.util.LinkedList;

public class BreadcrumbBuilder {

    public static BreadCrumbItem<String> buildBreadCrumbs(ViewType viewType) {
        var crumbNames = new LinkedList<String>();
        collectCrumbNames(viewType, crumbNames);
        return Breadcrumbs.buildTreeModel(crumbNames.toArray(new String[0]));
    }

    private static void collectCrumbNames(ViewType viewType, LinkedList<String> crumbNames) {
        if (viewType == null) return; // base case

        collectCrumbNames(viewType.parent(), crumbNames); // recursive case
        crumbNames.addLast(viewType.name());
    }
}