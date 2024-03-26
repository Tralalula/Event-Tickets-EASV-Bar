package event.tickets.easv.bar.util;

/**
 * Enumerates types of failures that can occur within the application.
 */
public enum FailureType {
    DB_CONNECTION_FAILURE,
    IO_FAILURE,
    DB_DATA_RETRIEVAL_FAILURE,
    BACKGROUND_TASK_FAILURE
}