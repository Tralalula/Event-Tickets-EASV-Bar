package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.gui.common.EventModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

class MainControllerTest {
    private ObservableList<EventModel> masterList;
    private FilteredList<EventModel> filteredList;

    @BeforeEach
    void setup() {
        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;
        var locationGuidance = "";
        var extraInfo = "";

        var e1 = new Event(1, "International Food Festival", imageName, location, LocalDate.now().minusDays(1), endDate, startTime, endTime, locationGuidance, extraInfo);
        var e2 = new Event(2, "Vegan Cooking Workshop", imageName, location, LocalDate.now().plusDays(1), endDate, startTime, endTime, locationGuidance, extraInfo);
        var e3 = new Event(3, "Farm to Table Dinner", imageName, location, LocalDate.now().plusDays(2), endDate, startTime, endTime, locationGuidance, extraInfo);

/*
                new Event(4, "Wine and Cheese Night", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(5, "Italian Pasta Making Class", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(6, "French Cuisine Tasting", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(7, "Sushi Rolling Workshop", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(8, "Chocolate Making Class", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(9, "BBQ and Grill Cook-off", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(10, "Farmers Market Tour", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(11, "Pastry and Baking Workshop", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(12, "Coffee Tasting Experience", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(13, "Beer Brewing Demonstration", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(14, "Gourmet Burger Festival", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(15, "Mexican Fiesta Night", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(16, "Street Food Extravaganza", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(17, "Ice Cream Social", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(18, "Pizza Making Party", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(19, "Seafood Feast", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo),
                new Event(20, "Culinary Arts Festival", imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);*/
        masterList = FXCollections.observableArrayList(
                EventModel.fromEntity(e1),
                EventModel.fromEntity(e2),
                EventModel.fromEntity(e3)
        );

        filteredList = new FilteredList<>(masterList, event -> event.startDate().get().isAfter(LocalDate.now()));

    }

    @Test
    void testObservableList() {
       assertThat(filteredList.size()).isEqualTo(2);

        var imageName = "sample.png";
        var location = "6700, Esbjerg";
        var startDate = LocalDate.now();
        LocalDate endDate = null;
        var startTime = LocalTime.now();
        LocalTime endTime = null;
        var locationGuidance = "";
        var extraInfo = "";

       masterList.add(EventModel.fromEntity(new Event(4, "Wine and Cheese Night", imageName, location, LocalDate.now().plusDays(3), endDate, startTime, endTime, locationGuidance, extraInfo)));
       assertThat(filteredList.size()).isEqualTo(3);


    }
}