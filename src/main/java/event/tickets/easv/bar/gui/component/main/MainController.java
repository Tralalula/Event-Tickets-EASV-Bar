package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final EntityManager manager;
    private final MainModel model;

    public MainController(MainModel model) {
        this.model = model;
        try {
            this.manager = new EntityManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fetchEvents();
    }

    public void fetchEvents() {
        BackgroundExecutor.performBackgroundTask(
                () -> manager.all(Event.class),
                events -> model.eventModels().setAll(convertToModels(events)),
                error -> System.out.println("Fejl: " + error)
        );
    }

    private List<EventModel> convertToModels(List<Event> events) {
        List<EventModel> eventModels = new ArrayList<>();

        for (var event : events) {
            eventModels.add(EventModel.fromEntity(event));
        }

        return eventModels;
    }
}
