package event.tickets.easv.bar.gui.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EventBus {
    private static final EventBus INSTANCE = new EventBus();
    private final HashMap<Class<?>, Set<Consumer<?>>> subscribers = new HashMap<>();

    private EventBus() {}

    private static synchronized EventBus instance() {
        return INSTANCE;
    }

    public static <T> void subscribe(Class<T> eventType, Consumer<T> listener) {
        instance().subscribers.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public static <T> void publish(T event) {
        Set<Consumer<?>> eventSubscribers = instance().subscribers.getOrDefault(event.getClass(), new HashSet<>());
        eventSubscribers.forEach(subscriber -> ((Consumer<T>) subscriber).accept(event));
    }
}
