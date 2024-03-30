package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.controls.Breadcrumbs;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.dashboard.DashboardView;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.component.events.ShowEventView;
import event.tickets.easv.bar.gui.component.tickets.AddTicketView;
import event.tickets.easv.bar.gui.component.tickets.ShowTicketView;
import event.tickets.easv.bar.gui.component.tickets.TicketsModel;
import event.tickets.easv.bar.gui.component.tickets.TicketsView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.util.*;
import event.tickets.easv.bar.util.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2MZ;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainView implements View {
    private final MainModel model;
    private final MainController controller;

    private final Region authView;
    private final Region dashboardView;
    private final Region eventsView;
    private final Region showEventView;
    private final Region ticketsView;
    private final Region addTicketView;
    private final Region showTicketView;

    private Breadcrumbs<String> crumbs;

    public MainView() {
        this.model = new MainModel();
        this.controller = new MainController(model);

        try {
            this.authView = new AuthView().getView();
        } catch (Exception e) {
            throw new RuntimeException("Fejl opstået");
        }

        this.dashboardView = new DashboardView().getView();
        this.eventsView = new EventsView(model.eventModels(), model.fetchingEventsProperty()).getView();
        this.showEventView = new ShowEventView().getView();

        this.ticketsView = new TicketsView(model, model.ticketModels(), model.fetchingTicketsProperty()).getView();
        this.addTicketView = new AddTicketView(new TicketsModel(model.eventModels()), model).getView();
        this.showTicketView = new ShowTicketView(new TicketsModel(model.eventModels())).getView();
    }

    @Override
    public Region getView() {
        var results = new BorderPane();
//        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/debug.css")).toExternalForm());
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
        NodeUtils.bindVisibility(authView, ViewHandler.activeWindowProperty().isEqualTo(WindowType.AUTH));

        NodeUtils.bindVisibility(dashboardView, ViewHandler.activeViewProperty().isEqualTo(ViewType.DASHBOARD));
        NodeUtils.bindVisibility(eventsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.EVENTS));
        NodeUtils.bindVisibility(showEventView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_EVENT));

        NodeUtils.bindVisibility(ticketsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.TICKETS));
        NodeUtils.bindVisibility(addTicketView, ViewHandler.activeViewProperty().isEqualTo(ViewType.ADD_TICKET));
        NodeUtils.bindVisibility(showTicketView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_TICKET));

        return new StackPane(authView, dashboardView, eventsView, showEventView, ticketsView, addTicketView, showTicketView);
    }

    private Region createCrumbs() {
        crumbs = new Breadcrumbs<>();
        crumbs.setSelectedCrumb(BreadcrumbUtils.buildBreadCrumbs(ViewHandler.activeViewProperty().get()));

        ViewHandler.activeViewProperty().addListener((obs, ov, nv) -> crumbs.setSelectedCrumb(BreadcrumbUtils.buildBreadCrumbs(nv)));
        NodeUtils.bindVisibility(crumbs, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));

        crumbs.onCrumbActionProperty().set(event -> {
            var crumbText = event.getSelectedCrumb().getValue();
            var viewType = ViewTypeHelper.byName(crumbText);
            if (viewType != null) ViewHandler.changeView(viewType);
        });

        return crumbs;
    }

    private Region topbar() {
        var results = new HBox(StyleConfig.STANDARD_SPACING);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT, StyleConfig.PADDING_DEFAULT);
        results.setMinHeight(50);
        results.setAlignment(Pos.CENTER_LEFT);

        var minimizeMaximize = new FontIcon(Material2MZ.MENU);
        minimizeMaximize.getStyleClass().add("outer-icon");

        var title = new Label("Event Manager");
        title.getStyleClass().addAll(Styles.ACCENT, Styles.TEXT_BOLD, Styles.TITLE_4);

        HBox.setMargin(minimizeMaximize, new Insets(0, 0, 0, 20));
        HBox.setMargin(title, new Insets(0, 85, 0, 0));

        var search = new CustomTextField();
        search.getStyleClass().add(Styles.SMALL); // Synes den er lidt stor? Fjern denne linje hvis den er for lille
        search.setPromptText("Search");
        search.setRight(new FontIcon(Feather.SEARCH));
        search.setFocusTraversable(false);
        search.setPrefWidth(450);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        List<MenuItem> languages = new ArrayList<>();
        languages.add(new MenuItem("Danish"));
        languages.add(new MenuItem("English"));
        languages.add(new MenuItem("Deutsch"));

        var languageSelect = new MenuButton();
        languageSelect.setGraphic(new FontIcon(Feather.FLAG));
        languageSelect.getItems().setAll(languages);
        languageSelect.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT
        );

        var lightTheme = new FontIcon(Feather.SUN);
        var darkTheme = new FontIcon(Feather.MOON);

        var modeSwitch = new Button(null, lightTheme);
        modeSwitch.getStyleClass().addAll(Styles.BUTTON_ICON, Styles.FLAT);
        modeSwitch.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                modeSwitch.setGraphic(modeSwitch.getGraphic() == lightTheme ? darkTheme : lightTheme);
            }
        });

        MenuItem logoutMenuItem = new MenuItem("Log out");
        logoutMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if (SessionManager.getInstance().logout())
                    ViewHandler.changeView(ViewType.LOGIN);
            }
        });

        List<MenuItem> settingsItems = new ArrayList<>();
        settingsItems.add(new MenuItem("Profile"));
        settingsItems.add(new MenuItem("Settings"));
        settingsItems.add(logoutMenuItem);

        var settings = new MenuButton("Username");
        settings.getItems().setAll(settingsItems);
        settings.getStyleClass().addAll(
                Styles.FLAT
        );

        settings.textProperty().bind(SessionManager.getInstance().loggedInUsernameProperty());


        results.getChildren().addAll(minimizeMaximize, title, search, spacer, languageSelect, modeSwitch, settings);

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
        tickets.setOnAction(e -> ViewHandler.changeView(ViewType.TICKETS));
        login.setOnAction(e -> ViewHandler.changeView(ViewType.LOGIN));

        return results;
    }

}
