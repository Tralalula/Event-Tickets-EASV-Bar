package event.tickets.easv.bar.util;

import event.tickets.easv.bar.be.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static SessionManager instance;
    private Map<String, User> sessions;
    private String loggedInUserSessionId;
    private StringProperty loggedInUsernameProperty = new SimpleStringProperty();

    private SessionManager() {
        sessions = new HashMap<>();
    }

    public static synchronized SessionManager getInstance() {
        if (instance == null)
            instance = new SessionManager();

        return instance;
    }

    /**
     * Metode til at logge på, hvis logget på så log ud.
     * @param user
     * @return sessionID
     */
    public synchronized String login(User user) {
        // Log ud hvis logget på
        if (loggedInUserSessionId != null)
            logout();

        //Lav session
        String sessionId = generateSessionId();
        sessions.put(sessionId, user);
        loggedInUserSessionId = sessionId;
        loggedInUsernameProperty.set(user.getUsername());
        return sessionId;
    }

    /**
     * Bruges til at få fat i nuværende bruger data fra session
     * @return User eller null hvis ikke tilgængelig
     */
    public synchronized User getLoggedInUser() {
        if (loggedInUserSessionId != null)
            return sessions.get(loggedInUserSessionId);

        return null;
    }

    /**
     * Får fat i nuværendes bruger session ID
     * @return Brugerens sessionID
     */
    public synchronized String getCurrentSessionID() {
        return loggedInUserSessionId;
    }

    public StringProperty loggedInUsernameProperty() {
        return loggedInUsernameProperty;
    }

    public synchronized boolean logout() {
        loggedInUserSessionId = null;
        return true;
    }

    /**
     * Generer et random session ID
     * @return SessionID
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}