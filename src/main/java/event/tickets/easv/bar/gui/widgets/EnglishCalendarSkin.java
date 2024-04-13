package event.tickets.easv.bar.gui.widgets;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.CalendarSkin;
import javafx.scene.control.DateCell;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class EnglishCalendarSkin extends CalendarSkin {
    public EnglishCalendarSkin(Calendar control) {
        super(control);
    }

    @Override
    public void updateDayNameCells() {
        DateTimeFormatter localWeekDayNameFormatter = DateTimeFormatter.ofPattern("ccc").withLocale(Locale.ENGLISH);
        int firstDayOfWeek = WeekFields.of(Locale.ENGLISH).getFirstDayOfWeek().getValue();
        LocalDate date = LocalDate.of(2009, 7, 12 + firstDayOfWeek);

        for (int i = 0; i < this.daysPerWeek; ++i) {
            String name = localWeekDayNameFormatter.format(date.plusDays(i));
            this.dayNameCells.get(i).setText(capitalize(name));
        }
    }

    private static String capitalize(String word) {
        if (!word.isEmpty()) {
            int firstChar = word.codePointAt(0);
            if (!Character.isTitleCase(firstChar)) {
                String var10000 = new String(new int[]{Character.toTitleCase(firstChar)}, 0, 1);
                word = var10000 + word.substring(Character.offsetByCodePoints(word, 0, 1));
            }
        }

        return word;
    }
}
