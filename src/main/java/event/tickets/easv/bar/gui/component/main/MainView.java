package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.controls.Breadcrumbs;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.common.WindowType;
import event.tickets.easv.bar.gui.component.auth.LoginView;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.component.dashboard.DashboardView;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.component.events.ShowEventView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.util.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.Objects;

public class MainView implements View {
    private final Region loginView;
    private final Region dashboardView;
    private final Region eventsView;
    private final Region showEventView;

    private Breadcrumbs<String> crumbs;

    public MainView() {
        this.loginView = new LoginView().getView();
        this.dashboardView = new DashboardView().getView();
        this.eventsView = new EventsView().getView();
        this.showEventView = new ShowEventView().getView();
    }

    @Override
    public Region getView() {
        var results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/style.css")).toExternalForm());
        results.getStyleClass().add(Styles.BG_SUBTLE);
        results.getStyleClass().add("main");

        var top = topbar();
        var left = sidebar();
        var center = center();

        NodeUtils.bindVisibility(top, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));
        NodeUtils.bindVisibility(left, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));

        BorderPane.setMargin(top, new Insets(StyleConfig.STANDARD_SPACING));
        BorderPane.setMargin(left, new Insets(0, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING));

        results.setTop(top);
        results.setLeft(left);
        results.setCenter(center);

        return results;
    }

    private Region center() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        var content = content();
        VBox.setVgrow(content, Priority.ALWAYS);

        results.getChildren().addAll(createCrumbs(), content);
        return results;
    }

    private Region content() {
        NodeUtils.bindVisibility(loginView, ViewHandler.activeViewProperty().isEqualTo(ViewType.LOGIN));
        NodeUtils.bindVisibility(dashboardView, ViewHandler.activeViewProperty().isEqualTo(ViewType.DASHBOARD));
        NodeUtils.bindVisibility(eventsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.EVENTS));
        NodeUtils.bindVisibility(showEventView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_EVENT));

        return new StackPane(loginView, dashboardView, eventsView, showEventView);
    }


    private Region createCrumbs() {
        crumbs = new Breadcrumbs<>();
        crumbs.setSelectedCrumb(BreadcrumbBuilder.buildBreadCrumbs(ViewHandler.activeViewProperty().get()));

        ViewHandler.activeViewProperty().addListener((obs, ov, nv) -> crumbs.setSelectedCrumb(BreadcrumbBuilder.buildBreadCrumbs(nv)));
        NodeUtils.bindVisibility(crumbs, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));

        crumbs.onCrumbActionProperty().set(event -> {
            var crumbText = event.getSelectedCrumb().getValue();
            var viewType = ViewType.byName(crumbText);
            if (viewType != null) ViewHandler.changeView(viewType);
        });

        return crumbs;
    }

    private Region topbar() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);
        results.setMinHeight(50);

        return results;
    }

    private Region sidebar() {
        var results = new VBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);
        results.setMinWidth(250);

        var dashboard = new Button("Dashboard");
        var events = new Button("Events");
        var tickets = new Button("Tickets");
        var users = new Button("Users");
        var verifyTicket = new Button("Verify Ticket");
        var login = new Button("Login");

        results.getChildren().addAll(
                dashboard,
                events,
                tickets,
                users,
                verifyTicket,
                login
        );
        results.setAlignment(Pos.TOP_CENTER);

        dashboard.setOnAction(e -> ViewHandler.changeView(ViewType.DASHBOARD));
        events.setOnAction(e -> ViewHandler.changeView(ViewType.EVENTS));
        login.setOnAction(e -> ViewHandler.changeView(ViewType.LOGIN));

        return results;
    }

}
