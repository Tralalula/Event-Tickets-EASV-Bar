package event.tickets.easv.bar.gui.component.main;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private final EntityManager manager;
    private final MainModel model;

    public MainController(MainModel model) {
        this.model = model;
        this.manager = new EntityManager();

        fetchEvents();
    }

    public void fetchEvents() {
        BackgroundExecutor.performBackgroundTask(
                () -> manager.all(Event.class),
                this::processResult
        );
    }

    private void processResult(Result<List<Event>> result) {
        switch (result) {
            case Success<List<Event>> s -> model.eventModels().setAll(convertToModels(s.result()));
            case Failure<List<Event>> f -> System.out.println("Error: " + f.cause());
        }
    }

    private List<EventModel> convertToModels(List<Event> events) {
        List<EventModel> eventModels = new ArrayList<>();

        for (var event : events) {
            eventModels.add(EventModel.fromEntity(event));
        }

        return eventModels;
    }
}
