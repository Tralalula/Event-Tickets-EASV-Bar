package event.tickets.easv.bar.util;

import event.tickets.easv.bar.be.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static SessionManager instance;
    private Map<String, User> sessions;
    private String loggedInUserSessionId;

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

    public synchronized void logout() {
        loggedInUserSessionId = null;
    }

    /**
     * Generer et random session ID
     * @return SessionID
     */
    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}