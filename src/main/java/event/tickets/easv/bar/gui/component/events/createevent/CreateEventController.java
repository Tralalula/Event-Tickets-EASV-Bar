package event.tickets.easv.bar.gui.component.events.createevent;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;

import java.time.LocalDate;
import java.time.LocalTime;



public class CreateEventController {
    private final CreateEventModel model;
    public CreateEventController(CreateEventModel model) {
        this.model = model;
        model.okToCreateProperty().bind(Bindings.createBooleanBinding(
                this::isDataValid,
                model.eventTitleProperty(),
                model.locationProperty(),
                model.extraInfoProperty()
        ));
    }

    void createEvent(Runnable postTaskGuiActions) {
        Task<Boolean> createTask = new Task<>() {
            @Override
            protected Boolean call() {
                return createEvent();
            }
        };

        createTask.setOnSucceeded(evt -> {
            postTaskGuiActions.run();
            if (!createTask.getValue()) {
                System.out.println("hey");
            }
        });

        var saveThread = new Thread(createTask);
        saveThread.start();
    }

    private boolean createEvent() {
        String title = model.eventTitleProperty().get();
        String location = model.locationProperty().get();
        String locationGuidance = model.locationGuidanceProperty().get();
        String extraInfo = model.extraInfoProperty().get();
        var event = new Event(title, "", location, LocalDate.now(), null, LocalTime.now(), null, locationGuidance, extraInfo);
        Result<Event> result = new EntityManager().add(event);
        switch (result) {
            case Success<Event> s -> {
                System.out.println(s.result());
                return true;
            }
            case Failure<Event> f -> {
                System.out.println(f.message());
                return false;
            }
        }
    }

    private boolean isDataValid() {
        // Title (required)
        // Location (required)
        // Location guidance (optional)
        // Start time (required), End time (optional)
        // Start date (required), End date (optional)
        // Image (optional)
        // Extra info (required)

        return !model.eventTitleProperty().get().isEmpty() &&
                !model.locationProperty().get().isEmpty() &&
                !model.extraInfoProperty().get().isEmpty();
    }
}
