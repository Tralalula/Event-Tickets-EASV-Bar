package event.tickets.easv.bar.gui.component.events;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Styles;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.View;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.common.ViewType;
import event.tickets.easv.bar.gui.util.NodeUtils;
import event.tickets.easv.bar.gui.util.StyleConfig;
import event.tickets.easv.bar.gui.widgets.Buttons;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.*;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;

public class EventsView implements View {
    private final ObservableList<EventModel> model;
    private final BooleanProperty fetchingData;
    private final BooleanProperty eventsUsersSynchronized;

    public EventsView(ObservableList<EventModel> model, BooleanProperty fetchingData, BooleanProperty eventsUsersSynchronized) {
        this.model = model;
        this.fetchingData = fetchingData;
        this.eventsUsersSynchronized = eventsUsersSynchronized;
    }

    public HBox topBar() {
        HBox top = new HBox();
        top.setPadding(new Insets(0 ,StyleConfig.STANDARD_SPACING * 3 ,0 ,StyleConfig.STANDARD_SPACING));

        var search = new CustomTextField();
        search.setPromptText("Search");
        search.setLeft(new FontIcon(Feather.SEARCH));
        search.setPrefWidth(250);

        var addTicket = new Button(null, new FontIcon(Feather.PLUS));
        addTicket.getStyleClass().addAll(
                Styles.BUTTON_ICON, Styles.FLAT, Styles.ACCENT, Styles.TITLE_4, StyleConfig.ACTIONABLE
        );
        addTicket.setOnAction(e -> ViewHandler.changeView(ViewType.CREATE_EVENT));

        top.getChildren().addAll(search, new Spacer(), addTicket);
        return top;
    }

    @Override
    public Region getView() {
        var top = topBar();
        top.setPadding(new Insets(0, 0, 0, StyleConfig.STANDARD_SPACING));

        ProgressIndicator progressIndicator = new ProgressIndicator();
        NodeUtils.bindVisibility(progressIndicator, fetchingData);

        var gridview = new EventGridView(model, eventsUsersSynchronized);
        var content = new StackPane(gridview.getView(), progressIndicator);

        return new VBox(top, content);
    }
}
