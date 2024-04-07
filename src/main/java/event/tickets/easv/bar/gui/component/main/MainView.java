package event.tickets.easv.bar.gui.component.main;

import atlantafx.base.controls.*;
import atlantafx.base.theme.*;
import event.tickets.easv.bar.gui.common.*;
import event.tickets.easv.bar.gui.component.dashboard.DashboardView;
import event.tickets.easv.bar.gui.component.events.createevent.CreateEventView;
import event.tickets.easv.bar.gui.component.events.EventsView;
import event.tickets.easv.bar.gui.component.events.ShowEventView;
import event.tickets.easv.bar.gui.component.tickets.*;
import event.tickets.easv.bar.gui.component.users.ShowUserView;
import event.tickets.easv.bar.gui.component.users.UsersView;
import event.tickets.easv.bar.gui.component.users.createuser.CreateUserView;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.util.*;
import event.tickets.easv.bar.util.SessionManager;
import javafx.application.Application;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainView implements View {
    private static final PseudoClass ACTIVE_PSEUDO_CLASS = PseudoClass.getPseudoClass("active");
    private static final PseudoClass HOVER_PSEUDO_CLASS = PseudoClass.getPseudoClass("hover");

    private final MainModel model;
    private final MainController controller;

    private final Region authView;
    private final Region dashboardView;
    private final Region eventsView;
    private final Region createEventView;
    private final Region showEventView;
    private final Region ticketsView;
    private final Region showTicketView;

    private final Region usersView;
    private final Region showUserView;
    private final Region createUserView;

    private Breadcrumbs<String> crumbs;

    public MainView() {
        this.model = new MainModel();
        this.controller = new MainController(model);

        try {
            this.authView = new AuthView().getView();
        } catch (Exception e) {
            throw new RuntimeException("Fejl opstået");
        }

        this.dashboardView = new DashboardView(model.eventModels(), model.fetchingEventsProperty(), model.eventsUsersSynchronizedProperty()).getView();
        this.eventsView = new EventsView(model.eventModels(), model.fetchingEventsProperty(), model.eventsUsersSynchronizedProperty()).getView();
        this.createEventView = new CreateEventView().getView();
        this.showEventView = new ShowEventView(model.eventModels(), model.userModels(), model.eventsTicketsSynchronizedProperty()).getView();

        this.ticketsView = new TicketsView(model, model.fetchingTicketsProperty()).getView();

        TicketsModel ticketsModel = new TicketsModel(model);
        this.showTicketView = new ShowTicketView(model, ticketsModel).getView();
        this.createUserView = new CreateUserView().getView();
        this.showUserView = new ShowUserView(model.eventsUsersSynchronizedProperty()).getView();
        this.usersView = new UsersView(model.userModels(), model.fetchingUsersProperty(), model.eventsUsersSynchronizedProperty()).getView();
    }

    @Override
    public Region getView() {
        var results = new StackPane();
        //        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/debug.css")).toExternalForm());
        results.getStylesheets().add(Objects.requireNonNull(this.getClass().getResource("/css/style.css")).toExternalForm());
        results.getStyleClass().add(Styles.BG_SUBTLE);

        var main = new BorderPane();

        main.getStyleClass().add("main");

        var top = topbar();
        var left = sidebar();
        var center = center();

        NodeUtils.bindVisibility(top, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));
        NodeUtils.bindVisibility(left, ViewHandler.activeWindowProperty().isEqualTo(WindowType.MAIN_APP));

        BorderPane.setMargin(top, new Insets(StyleConfig.STANDARD_SPACING));
        BorderPane.setMargin(left, new Insets(0, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING, StyleConfig.STANDARD_SPACING));

        main.setTop(top);
        main.setLeft(left);
        main.setCenter(center);

        results.getChildren().addAll(main, ViewHandler.overlay());
        StackPane.setAlignment(main, Pos.TOP_LEFT);
        return results;
    }

    private Region center() {
        var vbox = new VBox(StyleConfig.STANDARD_SPACING);
        var content = content();
        VBox.setVgrow(content, Priority.ALWAYS);

        vbox.getChildren().addAll(createCrumbs(), content);

        var stackPane = new StackPane();
        stackPane.getChildren().addAll(vbox, ViewHandler.notificationArea());

        return stackPane;
    }

    private Region content() {
        NodeUtils.bindVisibility(authView, ViewHandler.activeWindowProperty().isEqualTo(WindowType.AUTH));
        NodeUtils.bindVisibility(dashboardView, ViewHandler.activeViewProperty().isEqualTo(ViewType.DASHBOARD));
        NodeUtils.bindVisibility(eventsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.EVENTS));
        NodeUtils.bindVisibility(createEventView, ViewHandler.activeViewProperty().isEqualTo(ViewType.CREATE_EVENT).or(ViewHandler.activeViewProperty().isEqualTo(ViewType.EDIT_EVENT)));
        NodeUtils.bindVisibility(showEventView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_EVENT));
        NodeUtils.bindVisibility(ticketsView, ViewHandler.activeViewProperty().isEqualTo(ViewType.TICKETS));
        NodeUtils.bindVisibility(showTicketView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_TICKET));
        NodeUtils.bindVisibility(usersView, ViewHandler.activeViewProperty().isEqualTo(ViewType.USERS));
        NodeUtils.bindVisibility(showUserView, ViewHandler.activeViewProperty().isEqualTo(ViewType.SHOW_USER));
        NodeUtils.bindVisibility(createUserView, ViewHandler.activeViewProperty().isEqualTo(ViewType.CREATE_USER).or(ViewHandler.activeViewProperty().isEqualTo(ViewType.EDIT_USER)));

        var content = new StackPane(authView, dashboardView, eventsView, createEventView, showEventView, ticketsView, showTicketView, usersView, showUserView, createUserView);

        var scrollPane = new ScrollPane(content);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
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

        final boolean[] isDarkTheme = {false};

        modeSwitch.setOnAction(event -> {
            isDarkTheme[0] = !isDarkTheme[0];
            setTheme(isDarkTheme[0]);
            modeSwitch.setGraphic(isDarkTheme[0] ? darkTheme : lightTheme);
        });

        MenuItem logoutMenuItem = new MenuItem("Log out");
        logoutMenuItem.setOnAction(event -> {
            if (SessionManager.getInstance().logout()) ViewHandler.changeView(ViewType.LOGIN);
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

        settings.textProperty().bind(SessionManager.getInstance().getUserModel().firstName());


        results.getChildren().addAll(minimizeMaximize, title, search, spacer, languageSelect, modeSwitch, settings);

        return results;
    }

    private void setTheme(boolean isDark) {
        if (isDark) {
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else {
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
    }

    private Region sidebar() {
        var results = new VBox(StyleConfig.STANDARD_SPACING * 2);
        results.getStyleClass().addAll(Styles.BG_DEFAULT, StyleConfig.ROUNDING_DEFAULT);
        results.setPadding(new Insets(25));
        results.setMinWidth(250);

        var dashboard = createButton("Dashboard", Material2AL.DASHBOARD, ViewType.DASHBOARD);
        var events = createButton("Events", Material2AL.EVENT_AVAILABLE, ViewType.EVENTS);
        var tickets = createButton("Tickets", FontAwesomeSolid.TICKET_ALT, ViewType.TICKETS);
        var users = createButton("Users", FontAwesomeSolid.USERS, ViewType.USERS);

        var verifyTicket = new Button("Verify ticket", new FontIcon(Feather.CHECK));
        verifyTicket.getStyleClass().addAll(Styles.BUTTON_OUTLINED);
        verifyTicket.setMinWidth(200);
        verifyTicket.setMaxWidth(Double.MAX_VALUE);
        verifyTicket.setMinHeight(38);
        verifyTicket.setMaxHeight(38);

        var login = createButton("Login", null, ViewType.NO_VIEW);

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
        users.setOnAction(e -> ViewHandler.changeView(ViewType.USERS));
        verifyTicket.setOnAction(e -> ViewHandler.showOverlay("Verify ticket", new Label("Ticket found"), 450, 450));

        return results;
    }

    private Button createButton(String text, Ikon icon, ViewType viewType) {
        var btn = new Button();
        btn.getStyleClass().addAll(Styles.TEXT_BOLD, StyleConfig.ACTIONABLE, Styles.FLAT, "nav-button");
        var hbox = new HBox(16);
        hbox.setAlignment(Pos.CENTER_LEFT);

        var lbl = new Label(text);
        lbl.getStyleClass().add("nav-text");

        FontIcon fontIcon = null;
        if (icon != null) {
            fontIcon = new FontIcon(icon);
            fontIcon.getStyleClass().add("nav-icon");
            hbox.getChildren().add(fontIcon);
        }
        hbox.getChildren().add(lbl);

        btn.setGraphic(hbox);

        btn.setMinWidth(200);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMinHeight(60);
        btn.setMaxHeight(60);
        hbox.setPadding(new Insets(0, 10, 0, 10));

        final FontIcon fIcon = fontIcon;
        ViewHandler.activeRootViewProperty().subscribe(vt -> {
            boolean isActive = vt == viewType;
            btn.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, isActive);
            lbl.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, isActive);
            if (fIcon != null) fIcon.pseudoClassStateChanged(ACTIVE_PSEUDO_CLASS, isActive);
        });

        return btn;
    }
}
