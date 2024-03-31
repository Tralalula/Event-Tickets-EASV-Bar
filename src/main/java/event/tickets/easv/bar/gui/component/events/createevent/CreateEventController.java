package event.tickets.easv.bar.gui.component.events.createevent;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.util.AppConfig;
import event.tickets.easv.bar.util.FailureType;
import event.tickets.easv.bar.util.FileManagementService;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import event.tickets.easv.bar.util.Result.Failure;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
                model.startTimeProperty(),
                model.startDateProperty(),
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
        String imageName = model.imageNameProperty().get();
        String location = model.locationProperty().get();
        LocalTime startTime = model.startTimeProperty().get();
        LocalTime endTime = model.endTimeProperty().get();
        LocalDate startDate = model.startDateProperty().get();
        LocalDate endDate = model.endDateProperty().get();
        String extraInfo = model.extraInfoProperty().get();
        String locationGuidance = model.locationGuidanceProperty().get();

        // todo: rimelig grim kode her med billeder og try/catch, må fiks senere
        try {
            FileManagementService.copyImageToDir(model.imagePathProperty().get(), AppConfig.EVENT_TEMP_IMAGE_DIR, imageName);
        } catch (IOException e) {
            System.out.println("Error saving image to temp directory: " + e.getMessage());
            return false;
        }

        var event = new Event(title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);
        Result<Event> result = new EntityManager().add(event);
        switch (result) {
            case Success<Event> s -> {
                try {
                    // todo: gør så det bliver resized?
                    var finalDir = AppConfig.EVENT_IMAGES_DIR2 + "\\" + s.result().id();
                    Files.createDirectories(Paths.get(finalDir));
                    Files.move(Paths.get(AppConfig.EVENT_TEMP_IMAGE_DIR, imageName), Paths.get(finalDir, imageName), StandardCopyOption.REPLACE_EXISTING);
                    return true;
                } catch (IOException e) {
                    System.out.println("Error moving image to final directory: " + e.getMessage());
                    return false;
                }
            }
            case Failure<Event> f -> {
                System.out.println(f.message());
                return false;
            }
        }
    }

    void findImage() {
        var chooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files", "*.jpg", "*.jpeg", "*.png");
        chooser.getExtensionFilters().add(filter);

        var file = chooser.showOpenDialog(null);
        if (file != null) {
            var path = file.toURI().toString();
            model.imagePathProperty().set(path);
            model.imageNameProperty().set(file.getName());
            model.imageProperty().set(new Image(model.imagePathProperty().get(), true));
        }
    }

    private boolean isDataValid() {
        // Title (required)
        // Location (required)
        // Start time (required), End time (optional)
        // Start date (required), End date (optional)
        // Image (optional)
        // Extra info (required)
        // Location guidance (optional)

        return !model.eventTitleProperty().get().isEmpty() &&
                !model.locationProperty().get().isEmpty() &&
                model.startTimeProperty().get() != null &&
                model.startDateProperty().get() != null &&
                !model.extraInfoProperty().get().isEmpty();
    }
}
