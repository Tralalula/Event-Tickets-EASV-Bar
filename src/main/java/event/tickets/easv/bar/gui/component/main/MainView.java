package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.component.auth.LoginView;
import event.tickets.easv.bar.gui.component.common.View;
import event.tickets.easv.bar.gui.component.dashboard.DashboardView;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.theme.StyleConfig;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.ViewHandler;
import event.tickets.easv.bar.gui.util.ViewType;
import event.tickets.easv.bar.gui.util.WindowType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.util.Objects;

public class MainView implements View {
    private final Region loginView;
    private final Region dashboardView;
    private final Region eventsView;

    public MainView() {
        this.loginView = new LoginView().getView();
        this.dashboardView = new DashboardView().getView();
        this.eventsView = new EventsView().getView();
    }

    @Override
    public Region getView() {
        var results = new BorderPane();
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/style.css")).toExternalForm());
        results.getStyleClass().add(Styles.BG_SUBTLE);
        results.getStyleClass().add("main");

        var top = topbar();
        var left = sidebar();
        var center = content();

        NodeUtils.bindVisibility(top, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));
        NodeUtils.bindVisibility(left, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));

        BorderPane.setMargin(top, new Insets(StyleConfig.STANDARD_SPACING));
        BorderPane.setMargin(left, new Insets(0, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING));

        results.setTop(top);
        results.setLeft(left);
        results.setCenter(center);

        return results;
    }

    private Region content() {
        NodeUtils.bindVisibility(loginView, ViewHandler.activeViewProperty().isEqualTo(ViewType.LOGIN));
        NodeUtils.bindVisibility(dashboardView, ViewHandler.activeViewProperty().isEqualTo(ViewType.DASHBOARD));
        NodeUtils.bindVisibility(eventsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.EVENTS));

        return new StackPane(loginView, dashboardView, eventsView);
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
