package event.tickets.easv.bar.gui.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ViewHandlerTest {

    @BeforeEach
    void setup() {
        ViewHandler.reset();
    }

    @Test
    void changeViewCannotBeNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ViewHandler.changeView(null),
                "Expected changeView(null) to throw IllegalArgumentException, but it didn't."
        );
    }

    @Test
    void changeView() {
        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);

        ViewHandler.changeView(ViewType.LOGIN);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.LOGIN);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.AUTH);
    }

    @Test
    void previousViewInitial() {
        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);
    }

    @Test
    void previousViewAfterChangeView() {
        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);

        ViewHandler.changeView(ViewType.DASHBOARD);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);
    }

    @Test
    void previousViewAfterChangeViewThrice() {
        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);

        ViewHandler.changeView(ViewType.DASHBOARD);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.changeView(ViewType.EVENTS);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.EVENTS);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.changeView(ViewType.SHOW_EVENT);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.SHOW_EVENT);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.EVENTS);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);
    }

    @Test
    void previousViewResettingAfterWindowChange() {
        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.NONE);

        ViewHandler.changeView(ViewType.LOGIN);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.LOGIN);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.AUTH);

        ViewHandler.changeView(ViewType.DASHBOARD);

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);

        ViewHandler.previousView();

        assertThat(ViewHandler.activeViewProperty().get()).isEqualTo(ViewType.DASHBOARD);
        assertThat(ViewHandler.activeWindowProperty().get()).isEqualTo(WindowType.MAIN_APP);
    }

    @Test
    void activeViewProperty() {
        var activeViewProp = ViewHandler.activeViewProperty();
        assertThat(activeViewProp).isNotNull();
        assertThat(ViewType.NO_VIEW).isEqualTo(activeViewProp.get());

        ViewHandler.changeView(ViewType.DASHBOARD);
        assertThat(ViewType.DASHBOARD).isEqualTo(activeViewProp.get());
    }

    @Test
    void activeWindowProperty() {
        var activeWindowProp = ViewHandler.activeWindowProperty();
        assertThat(activeWindowProp).isNotNull();
        assertThat(WindowType.NONE).isEqualTo(activeWindowProp.get());

        ViewHandler.changeView(ViewType.DASHBOARD);
        assertThat(WindowType.MAIN_APP).isEqualTo(activeWindowProp.get());
    }
}