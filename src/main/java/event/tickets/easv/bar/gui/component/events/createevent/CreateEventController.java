package event.tickets.easv.bar.gui.component.events.createevent;

import event.tickets.easv.bar.be.Event;
import event.tickets.easv.bar.bll.EntityManager;
import event.tickets.easv.bar.gui.common.Action.CreateEvent;
import event.tickets.easv.bar.gui.common.Action.EditEvent;
import event.tickets.easv.bar.gui.common.ActionHandler;
import event.tickets.easv.bar.gui.common.EventModel;
import event.tickets.easv.bar.gui.common.NotificationType;
import event.tickets.easv.bar.gui.common.ViewHandler;
import event.tickets.easv.bar.gui.util.BackgroundExecutor;
import event.tickets.easv.bar.util.AppConfig;
import event.tickets.easv.bar.util.FileManagementService;
import event.tickets.easv.bar.util.Result;
import event.tickets.easv.bar.util.Result.Success;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

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

    void onCreateEvent(Runnable postTaskGuiActions) {
        BackgroundExecutor.performBackgroundTask(
                this::createEvent,
                postTaskGuiActions,
                success -> ActionHandler.handle(new CreateEvent(EventModel.fromEntity(success.get()))),
                failure -> ViewHandler.notify(NotificationType.FAILURE, "Failed to create event")
        );
    }

    void onEditEvent(Runnable postTaskGuiActions, EventModel eventModel) {
        BackgroundExecutor.performBackgroundTask(
                () -> editEvent(eventModel),
                postTaskGuiActions,
                success -> ActionHandler.handle(new EditEvent(eventModel, success.get())),
                System.out::println
        );
    }

    private Result<EventModel> editEvent(EventModel eventModel) {
        String title = model.eventTitleProperty().get();
        String imageName = model.imageNameProperty().get();
        String location = model.locationProperty().get();
        LocalTime startTime = model.startTimeProperty().get();
        LocalTime endTime = model.endTimeProperty().get();
        LocalDate startDate = model.startDateProperty().get();
        LocalDate endDate = model.endDateProperty().get();
        String extraInfo = model.extraInfoProperty().get();
        String locationGuidance = model.locationGuidanceProperty().get();

        if (!model.imagePathProperty().get().isEmpty()) {
            var imageCopied = FileManagementService.copyImageToDir(model.imagePathProperty().get(), AppConfig.EVENT_TEMP_IMAGE_DIR, imageName);
            if (imageCopied.isFailure()) return imageCopied.failAs();
        }

        var editedEvent = new Event(eventModel.id().get(), title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);
        var result = new EntityManager().update(eventModel.toEntity(), editedEvent);

        if (result.isFailure()) return result.failAs();

        // todo: gør så det bliver resized?

        var finalDir = AppConfig.EVENT_IMAGES_DIR2 + "\\" + editedEvent.id();
        if (!model.imagePathProperty().get().isEmpty()) {
            var fileMoved = FileManagementService.moveFile(Paths.get(AppConfig.EVENT_TEMP_IMAGE_DIR, imageName), Paths.get(finalDir, imageName), StandardCopyOption.REPLACE_EXISTING);
            if (fileMoved.isFailure()) return fileMoved.failAs();
        }


        var updated = EventModel.fromEntity(editedEvent);

        return Success.of(updated);
    }

    private Result<Event> createEvent() {
        String title = model.eventTitleProperty().get();
        String imageName = model.imageNameProperty().get();
        String location = model.locationProperty().get();
        LocalTime startTime = model.startTimeProperty().get();
        LocalTime endTime = model.endTimeProperty().get();
        LocalDate startDate = model.startDateProperty().get();
        LocalDate endDate = model.endDateProperty().get();
        String extraInfo = model.extraInfoProperty().get();
        String locationGuidance = model.locationGuidanceProperty().get();

        var imageCopied = FileManagementService.copyImageToDir(model.imagePathProperty().get(), AppConfig.EVENT_TEMP_IMAGE_DIR, imageName);
        if (imageCopied.isFailure()) return imageCopied.failAs();

        var event = new Event(title, imageName, location, startDate, endDate, startTime, endTime, locationGuidance, extraInfo);
        var result = new EntityManager().add(event);

        if (result.isFailure()) return result;

        var addedEvent = result.get();

        // todo: gør så det bliver resized?
        var finalDir = AppConfig.EVENT_IMAGES_DIR2 + "\\" + addedEvent.id();
        var fileMoved = FileManagementService.moveFile(Paths.get(AppConfig.EVENT_TEMP_IMAGE_DIR, imageName), Paths.get(finalDir, imageName), StandardCopyOption.REPLACE_EXISTING);
        if (fileMoved.isFailure()) return fileMoved.failAs();

        return result;
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
