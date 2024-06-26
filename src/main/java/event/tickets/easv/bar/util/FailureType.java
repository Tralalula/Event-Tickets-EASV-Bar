package event.tickets.easv.bar.util;

/**
 * The types of failures that can occur within the application.
 */
public enum FailureType {
    DB_CONNECTION_FAILURE,
    IO_FAILURE,
    DB_DATA_RETRIEVAL_FAILURE,
    DB_INSERTION_FAILURE,
    BACKGROUND_TASK_FAILURE,
    INVALID_ENTITY_TYPE,
    EMAIL_FAILURE,
    LOGIN_FAILURE
}