package event.tickets.easv.bar.gui.common;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ViewTypeHelperTest {
    @Test
    void byName() {
        assertThat(ViewTypeHelper.byName(null)).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewTypeHelper.byName("")).isEqualTo(ViewType.NO_VIEW);
        assertThat(ViewTypeHelper.byName("SHOW_EVENT")).isEqualTo(ViewType.SHOW_EVENT);
        assertThat(ViewTypeHelper.byName("show_event")).isEqualTo(ViewType.SHOW_EVENT);
        assertThat(ViewTypeHelper.byName("sHoW_eVeNt")).isEqualTo(ViewType.SHOW_EVENT);
        assertThat(ViewTypeHelper.byName(" show_event ")).isEqualTo(ViewType.NO_VIEW);
    }

    @Test
    void pathOf() {
        assertThat(ViewTypeHelper.pathOf(null)).isEmpty();
        assertThat(ViewTypeHelper.pathOf(ViewType.DASHBOARD)).isEqualTo(List.of(ViewType.DASHBOARD));
        assertThat(ViewTypeHelper.pathOf(ViewType.ASSIGN_TICKET)).isEqualTo(List.of(ViewType.EVENTS, ViewType.SHOW_EVENT, ViewType.ASSIGN_TICKET));
    }
}